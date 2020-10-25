package main;

import javafx.application.Application;
import javafx.stage.Stage;
import states.UIStates;
import utils.UIUtils;

public class Runnable extends Application {
	
	private void initOverview() {
		UIUtils.changeView("LoadingScreen.fxml");
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		UIStates.primaryStage = primaryStage;
		UIStates.primaryStage.setTitle("live!");
		
		initOverview();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}