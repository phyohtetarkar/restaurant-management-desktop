package com.phyohtet.restaurant.util;

import java.util.Optional;

import com.phyohtet.restaurant.app.ApplicationException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.StageStyle;

public class MessageUtil {

	private static Label label;

	public static void init(Label label) {
		MessageUtil.label = label;
	}

	public static void handle(ApplicationException e) {
		if (e.isNeedToAlert()) {
			showDialog(e.getMessage(), AlertType.ERROR);
		} else {
			label.setText(e.getMessage());
		}
	}

	public static void showDialog(String message, AlertType alertType) {
		buildAlert(message, alertType).show();
	}

	public static boolean showConfirmDialog(String message) {
		Optional<ButtonType> type = buildAlert(message, AlertType.CONFIRMATION).showAndWait();

		if (type.get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}

	private static Alert buildAlert(String message, AlertType alertType) {
		
		Alert alert = new Alert(alertType);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.getDialogPane().getStylesheets()
			.add(MessageUtil.class.getResource("dialog.css").toExternalForm());
		alert.initStyle(StageStyle.UNDECORATED);
		alert.setOnShowing(evt -> Navigator.lowerBrightness());
		alert.setOnHidden(evt -> Navigator.resetBrightness());
		
		return alert;
	}

	public static void clear() {
		label.setText("");
	}
}
