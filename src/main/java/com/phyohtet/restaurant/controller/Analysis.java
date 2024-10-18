package com.phyohtet.restaurant.controller;

import java.net.URL;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.phyohtet.restaurant.app.ApplicationException;
import com.phyohtet.restaurant.entity.Summary;
import com.phyohtet.restaurant.service.CategoryService;
import com.phyohtet.restaurant.service.ItemService;
import com.phyohtet.restaurant.service.OrderHeadService;
import com.phyohtet.restaurant.service.SummaryService;
import com.phyohtet.restaurant.util.BackgroundLoaderService;
import com.phyohtet.restaurant.util.MessageUtil;
import com.phyohtet.restaurant.util.ValidatorUtil;
import com.phyohtet.restaurant.view.util.ListNameCell;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

@Controller
@Scope("prototype")
public class Analysis implements Initializable {

	private enum Type {
		ITEM, CATEGORY, ORDERHEAD, OVERVIEW
	};

	@FXML
	private BarChart<String, Number> chart;
	@FXML
	private ComboBox<Object> obj;
	@FXML
	private ComboBox<Type> choose;
	@FXML
	private TextField year;
	@FXML
	private ComboBox<Month> month;
	@FXML
	private CheckBox monthly;
	@FXML
	private HBox sContents;

	@Autowired
	private SummaryService summaryService;
	@Autowired
	private CategoryService catService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private OrderHeadService ohService;

	private NumberAxis yAxis;
	private CategoryAxis xAxis;

	private BackgroundLoaderService<List<Summary>> loader;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		loader = new BackgroundLoaderService<>(this::call);
		loader.setOnSucceeded(v -> showChart(loader.getValue()));
		
		month.getItems().addAll(Month.values());
		month.valueProperty().addListener((a, b, c) -> {
			if (null != c) {
				monthly.setSelected(false);
			}
		});
		year.setText(Year.now().toString());
		year.textProperty().addListener((a, b, c) -> {
			if (!c.matches("\\d*")) {
				year.setText(c.replaceAll("[^\\d]", ""));
			}
			if (c.length() > 4) {
				year.setText(c.substring(0, 4));
			}
		});
		
		monthly.selectedProperty().addListener((a, b, c) -> {
			if (c) {
				month.setValue(null);
			}
		});

		obj.setCellFactory(c -> new ListNameCell<>());
		choose.getItems().addAll(Type.values());
		choose.valueProperty().addListener((a, b, c) -> {
			if (c != null) {
				obj.getItems().clear();
				if (!sContents.isVisible()) {
					sContents.setVisible(true);
				}

				switch (c) {
				case ITEM:
					obj.getItems().addAll(itemService.findAll());
					break;
				case CATEGORY:
					obj.getItems().addAll(catService.findAll());
					break;
				case ORDERHEAD:
					obj.getItems().addAll(ohService.findAll());
					break;
				case OVERVIEW:
					reset();
					sContents.setVisible(false);
					loader.restart();
					break;

				default:
					break;
				}
				obj.setPromptText(c.name());
			}
		});

		xAxis = (CategoryAxis) chart.getXAxis();
		xAxis.setTickLabelRotation(-45);
		yAxis = (NumberAxis) chart.getYAxis();
		yAxis.setLabel("Number Of Item");

	}

	private synchronized List<Summary> call() {
		return summaryService.getSummaryBy(obj.getValue(), Year.parse(year.getText())
				, month.getValue(), monthly.isSelected());
	}

	private void showChart(List<Summary> summaries) {
		if (null != summaries) {
			Series<String, Number> series = new Series<>();
			series.setName(choose.getValue().equals(Type.OVERVIEW) ? "Category" : "Sale Date");

			summaries.stream().map(s -> s.getData()).forEach(d -> series.getData().add(d));

			chart.getData().clear();
			chart.getData().add(series);
		}
	}

	public void analyze() {
		try {
			validate(obj.getValue(), "Analyze Type");
			loader.restart();
		} catch (ApplicationException e) {
			MessageUtil.handle(e);
		}
	}

	public void validate(Object o, String name) {
		if (ValidatorUtil.isNullObject(o)) {
			throw new ApplicationException(String.format("Please fill %s!", name));
		}
	}

	public void reset() {
		obj.getItems().clear();
		obj.setPromptText(null);
		monthly.setSelected(false);
		choose.setValue(choose.getValue().equals(Type.OVERVIEW) ? Type.OVERVIEW : null);
		chart.getData().clear();
	}
}
