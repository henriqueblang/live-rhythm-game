package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import states.UIStates;

public class InputController implements Controller {
	
	private final double PANE_WIDTH = 400;
	private final double PANE_HEIGHT = 160;
	
	private KeyCode inputKeyCode;
	
	private Button optionsInputButton;
	private OptionsController optionsController;
	
	@FXML
    private AnchorPane inputPane;
	
	@FXML
    private Text inputText;
	
	public void setInputMessage(KeyCode code) {
		inputText.setText(code == null ? "<not set>" : code.getName());
	}

    @FXML
    void okMouseReleased(MouseEvent event) {
    	UIStates.root.getScene().setOnKeyReleased(null);
    	
    	UIStates.extraPanes--;
    	UIStates.root.getChildren().remove(inputPane);
    	
    	optionsController.setShortchutInput(optionsInputButton, inputKeyCode);
    }
    
    @FXML
    void cancelMouseReleased(MouseEvent event) {
    	UIStates.root.getScene().setOnKeyReleased(null);
    	
    	UIStates.extraPanes--;
    	UIStates.root.getChildren().remove(inputPane);
    }
    
    @FXML
    void keyReleased(KeyEvent event) {
    	KeyCode code = event.getCode();
		
    	inputKeyCode = code;
    	setInputMessage(inputKeyCode);
    }
    
    public void setOptionsInputButton(Button optionsInputButton) {
		this.optionsInputButton = optionsInputButton;
	}
	
    public void setOptionsController(OptionsController optionsController) {
		this.optionsController = optionsController;
	}
    
	@Override
	public void init() {
		UIStates.extraPanes++;
		
		inputPane.setLayoutX((UIStates.root.getWidth() / 2) - (PANE_WIDTH / 2));
		inputPane.setLayoutY((UIStates.root.getHeight() / 2) - (PANE_HEIGHT / 2));
		
		UIStates.root.getScene().setOnKeyReleased(e -> keyReleased(e));
	}
}
