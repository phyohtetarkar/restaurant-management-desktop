package com.phyohtet.restaurant.controller.subscene;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.phyohtet.restaurant.app.ApplicationException;
import com.phyohtet.restaurant.entity.Category;
import com.phyohtet.restaurant.entity.Item;
import com.phyohtet.restaurant.entity.OrderDetail;
import com.phyohtet.restaurant.entity.OrderHead;
import com.phyohtet.restaurant.entity.OrderItem;
import com.phyohtet.restaurant.entity.Type;
import com.phyohtet.restaurant.service.CategoryService;
import com.phyohtet.restaurant.service.ItemService;
import com.phyohtet.restaurant.service.OrderHeadService;
import com.phyohtet.restaurant.service.TypeService;
import com.phyohtet.restaurant.util.BackgroundLoaderService;
import com.phyohtet.restaurant.util.MessageUtil;
import com.phyohtet.restaurant.util.Navigator;
import com.phyohtet.restaurant.util.SpringContextManager;
import com.phyohtet.restaurant.util.ValidatorUtil;
import com.phyohtet.restaurant.view.custom.ItemView;
import com.phyohtet.restaurant.view.util.DeleteCell;
import com.phyohtet.restaurant.view.util.ListNameCell;
import com.phyohtet.restaurant.view.util.QtyCell;
import com.phyohtet.restaurant.view.util.ResponsiveFlowPane;
import com.phyohtet.restaurant.view.util.TableCompoundNameCell;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Controller
@Scope("prototype")
public class NewOrder implements Initializable {

	@FXML
	private ResponsiveFlowPane pane;
	@FXML
	private TableView<OrderItem> orderTable;
	@FXML
	private TableColumn<OrderItem, Item> nameColumn;
	@FXML
	private TableColumn<OrderItem, String> delColumn;
	@FXML
	private TableColumn<OrderItem, Integer> qtyColumn;
	@FXML
	private ComboBox<OrderHead> orderHead;
	@FXML
	private ComboBox<Category> sCategory;
	@FXML
	private ComboBox<Type> sType;
	@FXML
	private TextField sName;

	@Autowired
	private ItemService itemService;
	@Autowired
	private CategoryService catService;
	@Autowired
	private TypeService typeService;
	@Autowired
	private OrderHeadService ohService;

	private BackgroundLoaderService<List<Item>> loader;
	private Consumer<OrderDetail> orderLoader;

	private OrderDetail orderDetail;

	public static void show(Consumer<OrderDetail> orderLoader) {
		try {
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Create New Order");
			

			ConfigurableApplicationContext context = SpringContextManager.getContext();
			FXMLLoader fxml = new FXMLLoader(NewOrder.class.getResource("NEWORDER.fxml"));
			fxml.setControllerFactory(context::getBean);
			
			Parent root = fxml.load();
			NewOrder controller = fxml.getController();
			controller.orderLoader = orderLoader;

			stage.setOnShown(evt -> Navigator.setCurrentOwner(root));
			stage.setOnHidden(evt -> Navigator.setCurrentOwner(null));
			stage.setScene(new Scene(root));
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
			throw new ApplicationException("Please contact to developer!", e);
		}
	}

	public static void show(OrderDetail orderDetail, Consumer<OrderDetail> orderLoader) {
		try {
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Edit Order");

			ConfigurableApplicationContext context = SpringContextManager.getContext();
			FXMLLoader fxml = new FXMLLoader(NewOrder.class.getResource("NEWORDER.fxml"));
			fxml.setControllerFactory(context::getBean);
			
			Parent root = fxml.load();
			NewOrder controller = fxml.getController();

			controller.orderLoader = orderLoader;
			controller.orderTable.getItems().addAll(orderDetail.getOrderItems());
			controller.orderDetail = orderDetail;
			controller.orderHead.setValue(orderDetail.getOrderHead());

			stage.setOnShown(evt -> Navigator.setCurrentOwner(root));
			stage.setOnHidden(evt -> Navigator.setCurrentOwner(null));
			stage.setScene(new Scene(root));
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
			throw new ApplicationException("Please contact to developer!", e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loader = new BackgroundLoaderService<>(this::call);
		loader.setOnSucceeded(v -> showData(loader.getValue()));

		initTableColumn();

		sCategory.setCellFactory(c -> new ListNameCell<>());
		sType.setCellFactory(c -> new ListNameCell<>());
		orderHead.setCellFactory(c -> new ListNameCell<>());

		sCategory.getItems().addAll(catService.findAll());
		sType.getItems().addAll(typeService.findAll());
		orderHead.getItems().addAll(ohService.findAll());

		sCategory.valueProperty().addListener((a, b, c) -> search());
		sType.valueProperty().addListener((a, b, c) -> search());
		sName.textProperty().addListener((a, b, c) -> search());

		loader.start();
	}

	private void initTableColumn() {
		nameColumn.setCellFactory(c -> new TableCompoundNameCell<>(i -> {
			OrderItem oi = orderTable.getItems().get(i);
			return oi.getName();
		}));

		qtyColumn.setCellFactory(c -> new QtyCell((co, i) -> {
			co.accept(orderTable.getItems().get(i));
			orderTable.refresh();
		}));

		delColumn.setCellFactory(d -> new DeleteCell<>(i -> {
			orderTable.getItems().remove(orderTable.getItems().get(i));
		}));
	}

	private synchronized List<Item> call() {
		return itemService.findBy(sCategory.getValue(), sType.getValue(), sName.getText());
	}

	private void showData(List<Item> items) {
		pane.getChildren().clear();
		pane.getChildren().addAll(items.stream()
					.map(i -> new ItemView(i, this::addOrder))
					.collect(Collectors.toList()));
		pane.refreshChild(pane.getWidth());
	}

	private void search() {
		loader.restart();
	}

	private void addOrder(Item item) {
		OrderItem orderItem = orderTable.getItems().stream()
				.filter(oi -> oi.getItem().getId() == item.getId())
				.findAny()
				.orElseGet(() -> {
					OrderItem oi = new OrderItem();
					oi.setItem(item);
					orderTable.getItems().add(oi);
					return oi;
				});
		orderItem.setQuantity(orderItem.getQuantity() + 1);
		orderItem.setTotalPrice(orderItem.getQuantity() * item.getPrice());
		orderTable.refresh();
	}

	public void save() {
		try {
			if (null != orderDetail) {
				populateOrderDetail(orderDetail);
				orderLoader.accept(orderDetail);
			} else {
				OrderDetail orderDetail = new OrderDetail();
				populateOrderDetail(orderDetail);
				orderLoader.accept(orderDetail);
			}
			close();
		} catch (ApplicationException e) {
			MessageUtil.handle(e);
		}
	}

	private void populateOrderDetail(OrderDetail orderDetail) {
		if (ValidatorUtil.isEmptyList(orderTable.getItems())) {
			throw new ApplicationException("OrderItem must not be empty!");
		}
		if (ValidatorUtil.isNullObject(orderHead.getValue())) {
			throw new ApplicationException("Please select Order Head!");
		}
		orderDetail.setOrderHead(orderHead.getValue());
		orderDetail.setOrderItems(new HashSet<>(orderTable.getItems()));
		orderDetail.setTotalItem(orderTable.getItems().stream().mapToInt(oi -> oi.getQuantity()).sum());
		orderDetail.setTotalPrice(orderTable.getItems().stream().mapToInt(oi -> oi.getTotalPrice()).sum());
	}

	public void clear() {
		sCategory.setValue(null);
		sType.setValue(null);
		sName.setText(null);
	}

	public void close() {
		pane.getScene().getWindow().hide();
	}
}
