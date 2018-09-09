package com.csdapp.mainapplication;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Main extends Application {

	Stage window;
	Scene scene;
	GridPane layout;
	Label labelAlleleNum;
	Label labelGenNum;
	Label labelNetSize;
	Label labelDip;
	Label labelDroneDist;
	Label labelSwarmDist;
	Label labelNetType;
	Label labelProjectPath;
	Label labelShowSim;
	Label labelDispSim;
	TextField textAlleleNum;
	TextField textGenNum;
	TextField textNetSize;
	TextField textDip;
	TextField textDroneDist;
	TextField textSwarmDist;
	TextField textProjectDir;
	ChoiceBox<String> boxNetType;
	CheckBox boxDispSim;
	Button buttonOpen;
	Button buttonGenerate;
	Button buttonClose;
	int allelNum;
	int genNum;
	int netSize;
	double dip;
	int droneDist;
	int swarmDist;
	String projectDir;
	boolean dispSim;
	boolean torus;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		window = primaryStage;
		window.setTitle("CSD Allele Drift");
		
		layout = new GridPane();
		layout.setPadding(new Insets (10, 10, 10, 10));
		layout.setVgap(8);
		layout.setHgap(8);
		prepareLayout();
		
		System.out.println(projectDir);
		buttonOpen.setOnAction( e -> selectProjectPath());
		buttonGenerate.setOnAction( e -> runSimulationWindow());
		buttonClose.setOnAction( e -> window.close());
		
		scene = new Scene(layout, 390, 335);
		window.setScene(scene);
		window.show();
	}
	
	@SuppressWarnings("static-access")
	private void prepareLayout(){
		
		labelAlleleNum = new Label("Allele number:");
		layout.setConstraints(labelAlleleNum, 0, 0);
		
		textAlleleNum = new TextField("315");
		layout.setConstraints(textAlleleNum, 1, 0);
		
		labelGenNum = new Label("Generation number:");
		layout.setConstraints(labelGenNum, 0, 1);
		
		textGenNum = new TextField("20");
		layout.setConstraints(textGenNum, 1, 1);
		
		labelNetSize = new Label("Net side length:");
		layout.setConstraints(labelNetSize, 0, 2);
		
		textNetSize = new TextField("20");
		layout.setConstraints(textNetSize, 1, 2);
		
		labelDip = new Label("DIP:");
		layout.setConstraints(labelDip, 0, 3);
		
		textDip = new TextField("0.1");
		layout.setConstraints(textDip, 1, 3);
		
		labelDroneDist = new Label("Drone distance:");
		layout.setConstraints(labelDroneDist, 0, 4);
		
		textDroneDist = new TextField("5");
		layout.setConstraints(textDroneDist, 1, 4);
		
		labelSwarmDist = new Label("Swarming distance:");
		layout.setConstraints(labelSwarmDist, 0, 5);
		
		textSwarmDist = new TextField("5");
		layout.setConstraints(textSwarmDist, 1, 5);
		
		labelNetType  = new Label("Net type:");
		layout.setConstraints(labelNetType, 0, 6);
		
		boxNetType = new ChoiceBox<>();
		boxNetType.getItems().addAll("Regular net", "Torus net");
		boxNetType.setValue("Regular net");
		layout.setConstraints(boxNetType, 1, 6);
		
		labelDispSim = new Label("Display simulation: ");
		layout.setConstraints(labelDispSim, 0, 7);
		
		boxDispSim = new CheckBox();
		boxDispSim.setDisable(true);
		layout.setConstraints(boxDispSim, 1, 7);
		
		labelProjectPath = new Label("Project folder:");
		layout.setConstraints(labelProjectPath, 0, 8);
		
		textProjectDir = new TextField("C:\\CSDAllelSimulation\\Output");
		layout.setConstraints(textProjectDir, 1, 8);
		
		buttonOpen = new Button("Open directory");
		layout.setConstraints(buttonOpen, 2, 8);
		
		buttonGenerate = new Button("Generate");
		HBox hBoxbtnGen = new HBox();
		hBoxbtnGen.setAlignment(Pos.CENTER_RIGHT);
		hBoxbtnGen.getChildren().add(buttonGenerate);
		layout.setConstraints(hBoxbtnGen, 1, 9);
		
		buttonClose = new Button("Close");
		HBox hBoxbtnCls = new HBox();
		hBoxbtnCls.setAlignment(Pos.CENTER_RIGHT);
		hBoxbtnCls.getChildren().add(buttonClose);
		layout.setConstraints(hBoxbtnCls, 2, 9);
		
		layout.getChildren().addAll(labelAlleleNum, textAlleleNum, labelGenNum, textGenNum, labelNetSize, textNetSize, labelDip,
				textDip, labelDroneDist, textDroneDist, labelSwarmDist, textSwarmDist, labelDispSim, boxDispSim, labelNetType, boxNetType, labelProjectPath, textProjectDir,
				buttonOpen, hBoxbtnGen, hBoxbtnCls);
		
	}
	
	private void selectProjectPath() {
		try {
			DirectoryChooser dirChooser = new DirectoryChooser();
			File file = dirChooser.showDialog(window);
			textProjectDir.setText(file.getAbsolutePath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}
	
	private void runSimulationWindow() {
		//To do
		boolean flag = checkValues();
		Output output = new Output(allelNum, genNum, netSize, dip, droneDist, swarmDist, projectDir, dispSim, torus);
		if(flag){
			output.display();			
		}
	}
	
	private boolean checkValues() {
		
		if(! textAlleleNum.getText().isEmpty() || ! textGenNum.getText().isEmpty() || ! textNetSize.getText().isEmpty() || ! textDip.getText().isEmpty() || 
				! textDroneDist.getText().isEmpty() || ! textSwarmDist.getText().isEmpty() || ! textProjectDir.getText().isEmpty()) {
			
			try {
				allelNum = Integer.parseInt(textAlleleNum.getText());
				genNum = Integer.parseInt(textGenNum.getText());
				netSize = Integer.parseInt(textNetSize.getText());
				dip = Double.parseDouble(textDip.getText());
				droneDist = Integer.parseInt(textDroneDist.getText());
				swarmDist = Integer.parseInt(textSwarmDist.getText());
				projectDir = textProjectDir.getText();
				
				if (!projectDir.endsWith("Output")){
					projectDir = projectDir.concat("//Output");
				}
				
				dispSim = false;
				if(boxDispSim.isSelected()){
					dispSim = true;
				}
				
				torus = false;
				if(boxNetType.getValue() == "Torus net") {
					torus = true;
				}

			} catch (NumberFormatException e) {
				String message = "Fields allele number, generation number, net side size,\r\ndrone distance and swarming distance\r\nneed an integer input.\r\nField DIP needs a double floating point input.";
				MessageWindow mw = new MessageWindow();
				mw.display("Input type error", message);
				return false;
			}
		}
		
		if(allelNum > 1 && allelNum < 1001 && genNum > 0 && genNum < 100001 && netSize > 19 && netSize < 1025
				&& dip >= 0 && dip <= 0.8 && droneDist > 0 && droneDist < 11 && swarmDist > 0 && swarmDist < 11) {
			return true;			
		} else {
			String message = "The value is out of range!\r\nThe ranges for variables are listed below:\r\nAllele number: 2 - 1000, Generation Number: 2 - 100 000, Net side size: 20 - 1024,\r\nDIP: 0.0 - 0.8, Drone distance: 1 - 10, Swarming distance: 1 - 10.";
			MessageWindow mw = new MessageWindow();
			mw.display("Input data out of range", message);
			return false;
		}
	}
}
