package com.csdApp.mainApplication;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MessageWindow {
	
	public void display(String title, String message) {
		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		
		Label label = new Label(message);
		
		Button buttonClose = new Button("OK");
		buttonClose.setOnAction( e -> window.close());
		
		VBox vBox = new VBox();
		vBox.getChildren().addAll(label, buttonClose);
		vBox.setPadding(new Insets(10, 10, 10, 10));
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(10);
		vBox.setMinWidth(350);
		
		Scene scene = new Scene(vBox);
		window.setScene(scene);
		window.showAndWait();
	}

}
