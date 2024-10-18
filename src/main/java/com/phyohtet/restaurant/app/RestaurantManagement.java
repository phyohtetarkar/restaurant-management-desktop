package com.phyohtet.restaurant.app;

import java.io.IOException;

import com.phyohtet.restaurant.controller.ParentView;
import com.phyohtet.restaurant.util.SpringContextManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RestaurantManagement extends Application {

	@Override
	public void init() throws Exception {
		super.init();
		SpringContextManager.getContext();
	}

	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(ParentView.class.getResource("PARENTVIEW.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Restaurant Management");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		SpringContextManager.close();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
