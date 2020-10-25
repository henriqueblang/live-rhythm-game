package controllers;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import states.GameStates;
import states.UIStates;

public class FYIController implements Controller {
	
	private final double PANE_WIDTH = 400;
	private final double PANE_HEIGHT = 160;

	@FXML
    private AnchorPane FYIPane;
	
	@FXML
    private Text informationText;
	
	public void setInformationMessage(String message) {
		informationText.setText(message);
	}

    @FXML
    void okMouseReleased(MouseEvent event) {
    	UIStates.root.getScene().setOnKeyReleased(null);
    	
    	UIStates.extraPanes--;
    	UIStates.root.getChildren().remove(FYIPane);
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
		
		FYIPane.setLayoutX((UIStates.root.getWidth() / 2) - (PANE_WIDTH / 2));
		FYIPane.setLayoutY((UIStates.root.getHeight() / 2) - (PANE_HEIGHT / 2));
		
		UIStates.root.getScene().setOnKeyReleased(e -> keyReleased(e));
	}
}
