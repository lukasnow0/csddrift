package com.csdApp.mainApplication;



import com.csdApp.model.Model;
import com.csdApp.model.ModelNew;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;

public class Output {
	
	static int allelNum;
	static int genNum;
	static int netSize;
	static double dip;
	static int droneDist;
	static int swarmDist;
	static String projectDir;
	static boolean dispSim;
	static boolean torus;

	static Stage window;
	static Label label1;
	static Label labelStatus;
	static Label label3;
	static ProgressBar labelGen;
	static Button buttonClose;
	
	public Output(int allelNum, int genNum, int netSize, double dip, int droneDist, int swarmDist, String projectDir, boolean dispSim, boolean torus){
		Output.allelNum = allelNum;
		Output.genNum = genNum;
		Output.netSize = netSize;
		Output.dip = dip;
		Output.droneDist = droneDist;
		Output.swarmDist = swarmDist;
		Output.projectDir = projectDir;
		Output.dispSim = dispSim;
		Output.torus = torus;
	}
	
	public void display() {
		window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Simulation");

		label1 = new Label("Status:");
		labelStatus = new Label("");
		labelStatus.setMinWidth(80);
		label3 = new Label("Generation:");
		labelGen = new ProgressBar();
		labelGen.setMinWidth(80);
		buttonClose = new Button("Close");
		
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(10, 10, 10, 10));
		hBox.setSpacing(10);
		hBox.getChildren().addAll(label1, labelStatus, label3, labelGen, buttonClose);
		
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(10, 10, 10, 10));
		vBox.getChildren().add(hBox);
		vBox.setMinWidth(300);
		
		if(dispSim){
			
			//not implemented yet

		} else {
			Task<Void> task = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					
					ModelService service = new ModelService();
					service.messageProperty().addListener((obs, oldMessage, newMessage) -> {
						updateMessage(newMessage);
					});
					service.valueProperty().addListener((obs, oldValue, newValue) -> {
						updateProgress(newValue.longValue(), genNum);
					});
					buttonClose.setOnAction( e -> {
						service.interruptModel();
						service.cancel();
						window.close();
					});
					
					Platform.runLater(service::start);
					return null;
				}
			};
			
			Thread t = new Thread(task);
			t.setDaemon(true);
			t.start();
						
			labelStatus.textProperty().bind(task.messageProperty());
			labelGen.progressProperty().bind(task.progressProperty());
		}	
        
		Scene scene = new Scene(vBox);
		window.setScene(scene);
		window.show();
	}
	
	private static class ModelService extends Service<Integer> {
	//	Model model = new ModelOld(allelNum, genNum, netSize, dip, droneDist, swarmDist, projectDir, torus);
		Model model = new ModelNew(allelNum, genNum, netSize, dip, droneDist, swarmDist, projectDir, torus);
		Thread modelThread = new Thread(model);

		@Override
		protected Task<Integer> createTask() {
			return new Task<Integer>() {

				@Override
				protected Integer call() throws Exception {
					modelThread.setDaemon(true);
					modelThread.start();
					while(modelThread.isAlive()){;
						updateValue(model.getGenerationCount());
						updateMessage("Running");
						if(modelThread.isInterrupted()){
							return model.getGenerationCount();
						}
						try {
		                     Thread.sleep(100);
		                 } catch (InterruptedException interrupted) {
		                     if (isCancelled()) {
		                         break;
		                     }
		                 }
					}
					updateMessage("Finished");
					updateProgress(1, 1);
					return model.getGenerationCount();
				}
			};
		}
		
		public void interruptModel() {
			modelThread.interrupt();
		}
	}
}
