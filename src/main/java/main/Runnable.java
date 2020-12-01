package main;

import javafx.application.Application;
import javafx.stage.Stage;
import states.UIStates;
import utils.UIUtils;

public class Runnable extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		UIStates.getInstance().setPrimaryStage(primaryStage);
		UIStates.getInstance().getPrimaryStage().setTitle("live!");
		
		UIUtils.changeView("LoadingScreen.fxml");
	}
	
}