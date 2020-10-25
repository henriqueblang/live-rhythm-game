package controllers;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import states.GameStates;
import states.UIStates;
import utils.UIUtils;

public class FailController implements Controller {
	
	private final double PANE_WIDTH = 391;
	private final double PANE_HEIGHT = 262;
	
    @FXML
    private AnchorPane failPane;
    
    @FXML
    void okMouseReleased(MouseEvent event) {
    	UIStates.extraPanes--;
		
    	UIUtils.playEnterSound();
    	UIUtils.changeView("MenuScreen.fxml");
    }
    
    @FXML
    void keyReleased(KeyEvent event) {
    	KeyCode code = event.getCode();
    	KeyCode okCode = GameStates.userOptions.getShortcuts().get("Confirm");
    	
		if(okCode != null && code == okCode)
			okMouseReleased(null);
	}

	@Override
	public void init() {
		UIStates.extraPanes++;
		
		failPane.setLayoutX((UIStates.root.getWidth() / 2) - (PANE_WIDTH / 2));
		failPane.setLayoutY((UIStates.root.getHeight() / 2) - (PANE_HEIGHT / 2));
		
		UIStates.root.getScene().setOnKeyReleased(e -> keyReleased(e));
	}

}
