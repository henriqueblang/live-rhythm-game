package controllers;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import states.GameStates;
import states.UIStates;

public class PauseController implements Controller {
	
	final private double PANE_WIDTH = 350;
	final private double PANE_HEIGHT = 128;
	
	private GameController gameController;
	
	@FXML
	private AnchorPane pausePane;

	@FXML
	void quitAction(MouseEvent event) {
		UIStates.root.getScene().setOnKeyReleased(null);
		
		UIStates.extraPanes--;
		UIStates.root.getChildren().remove(pausePane);
		
		gameController.finishGame(false);
	}

	@FXML
	void resumeAction(MouseEvent event) {
		UIStates.root.getScene().setOnKeyReleased(null);
		
		UIStates.extraPanes--;
		UIStates.root.getChildren().remove(pausePane);
		
		gameController.resumeGame();
	}
	
    void keyReleased(KeyEvent event) {
    	KeyCode code = event.getCode();
		KeyCode okCode = GameStates.userOptions.getShortcuts().get("Confirm");
		KeyCode backCode = GameStates.userOptions.getShortcuts().get("Back");
		
		if(okCode != null && code == okCode)
			resumeAction(null);
		else if(backCode != null && code == backCode)
			quitAction(null);
    }
	
	public void setGameController(GameController gameController) {
		this.gameController = gameController;
	}

	@Override
	public void init() {
		UIStates.extraPanes++;
		
		pausePane.setLayoutX((UIStates.root.getWidth() / 2) - (PANE_WIDTH / 2));
		pausePane.setLayoutY((UIStates.root.getHeight() / 2) - (PANE_HEIGHT / 2));
		
		UIStates.root.getScene().setOnKeyReleased(e -> keyReleased(e));
	}
}
