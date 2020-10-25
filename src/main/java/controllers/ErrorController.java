package controllers;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import states.GameStates;
import states.UIStates;

public class ErrorController implements Controller {
	
	private final double PANE_WIDTH = 400;
	private final double PANE_HEIGHT = 160;

	@FXML
    private AnchorPane errorPane;
	
	@FXML
    private Text errorText;
	
	public void setErrorMessage(String message) {
		errorText.setText(message);
	}

    @FXML
    void okMouseReleased(MouseEvent event) {
    	UIStates.root.getScene().setOnKeyReleased(null);
    	
    	UIStates.extraPanes--;
    	UIStates.root.getChildren().remove(errorPane);
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
		
		errorPane.setLayoutX((UIStates.root.getWidth() / 2) - (PANE_WIDTH / 2));
		errorPane.setLayoutY((UIStates.root.getHeight() / 2) - (PANE_HEIGHT / 2));
		
		UIStates.root.getScene().setOnKeyReleased(e -> keyReleased(e));
	}
}
