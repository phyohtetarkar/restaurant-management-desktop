package com.phyohtet.restaurant.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.phyohtet.restaurant.controller.subscene.AllReport;
import com.phyohtet.restaurant.controller.subscene.DetailReport;
import com.phyohtet.restaurant.entity.Category;
import com.phyohtet.restaurant.entity.OrderHead;
import com.phyohtet.restaurant.entity.Type;
import com.phyohtet.restaurant.service.CategoryService;
import com.phyohtet.restaurant.service.OrderHeadService;
import com.phyohtet.restaurant.service.TypeService;
import com.phyohtet.restaurant.util.DateFormatConverter;
import com.phyohtet.restaurant.util.SpringContextManager;
import com.phyohtet.restaurant.view.util.ListNameCell;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

@Controller
@Scope("prototype")
public class Report implements Initializable {
	@FXML
	private StackPane contentView;
	@FXML
	private ComboBox<OrderHead> sOrderHead;
	@FXML
	private ComboBox<Category> sCategory;
	@FXML
	private ComboBox<Type> sType;
	@FXML
	private DatePicker sSaleDate;
	@FXML
	private TextField sName;
	@FXML
	private CheckBox detailMode;

	@Autowired
	private CategoryService catService;
	@Autowired
	private TypeService typeService;
	@Autowired
	private OrderHeadService ohService;

	private AllReport allReport;
	private DetailReport detailReport;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sCategory.setCellFactory(c -> new ListNameCell<>());
		sType.setCellFactory(c -> new ListNameCell<>());
		sOrderHead.setCellFactory(c -> new ListNameCell<>());

		sSaleDate.setConverter(new DateFormatConverter());

		sOrderHead.getItems().addAll(ohService.findAll());
		sCategory.getItems().addAll(catService.findAll());
		sType.getItems().addAll(typeService.findAll());

		sOrderHead.valueProperty().addListener((a, b, c) -> search());
		sCategory.valueProperty().addListener((a, b, c) -> search());
		sType.valueProperty().addListener((a, b, c) -> search());
		sSaleDate.valueProperty().addListener((a, b, c) -> search());
		sName.textProperty().addListener((a, b, c) -> search());
		detailMode.selectedProperty().addListener((a, b, c) -> switchView(c.booleanValue()));

		switchView(false);
	}

	private void switchView(boolean checked) {
		try {
			Parent view = null;
			ConfigurableApplicationContext context = SpringContextManager.getContext();
			if (!checked) {
				FXMLLoader loader = new FXMLLoader(AllReport.class.getResource("ALLREPORT.fxml"));
				loader.setControllerFactory(context::getBean);
				view = loader.load();
				allReport = loader.getController();
			} else {
				FXMLLoader loader = new FXMLLoader(AllReport.class.getResource("DETAILREPORT.fxml"));
				loader.setControllerFactory(context::getBean);
				view = loader.load();
				detailReport = loader.getController();
			}
			clear();
			searchComponentVisibility(!detailMode.isSelected());
			contentView.getChildren().clear();
			contentView.getChildren().add(view);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void search() {
		if (null != allReport || null != detailReport) {
			if (detailMode.isSelected()) {
				detailReport.search(sOrderHead.getValue(), sSaleDate.getValue());
			} else {
				allReport.search(sOrderHead.getValue(), sCategory.getValue(), sType.getValue(), sSaleDate.getValue(),
						sName.getText());
			}
		}
	}

	private void searchComponentVisibility(boolean v) {
		sCategory.setVisible(v);
		sCategory.setManaged(v);

		sType.setVisible(v);
		sType.setManaged(v);

		sName.setVisible(v);
		sName.setManaged(v);
	}

	public void clear() {
		sOrderHead.setValue(null);
		sCategory.setValue(null);
		sType.setValue(null);
		sSaleDate.setValue(null);
		sName.setText(null);
	}
}
