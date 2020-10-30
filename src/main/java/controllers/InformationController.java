package controllers;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import states.GameStates;
import states.UIStates;
import utils.UIUtils;

public class InformationController implements Controller {
	
	private final double PANE_WIDTH = 390;
	private final double PANE_HEIGHT = 260;
	
	private boolean resetWhenClosed = false;
	
    @FXML
    private AnchorPane informationPane;
    
    @FXML
    private Text titleText;
    
    @FXML
    private Text informationText;
    
    public void setWindowInformation(String title, String message) {
    	setWindowInformation(title, message, false);
    }
    
    public void setWindowInformation(String title, String message, boolean resetWhenClosed) {
    	titleText.setText(title);
    	informationText.setText(message);
    	
		this.resetWhenClosed = resetWhenClosed;
	}
    
    @FXML
    void okMouseReleased(MouseEvent event) {
    	UIStates.getInstance().decrementExtraPanes();
    	
    	UIUtils.playEnterSound();
    	
    	if(resetWhenClosed)
        	UIUtils.changeView("MenuScreen.fxml");
    	else {
    		UIStates.getInstance().getRoot().getScene().setOnKeyReleased(null);
    		UIStates.getInstance().getRoot().getChildren().remove(informationPane);
    	}
    }
    
    @FXML
    void keyReleased(KeyEvent event) {
    	KeyCode code = event.getCode();
    	KeyCode okCode = GameStates.getInstance().getUserOptions().getShortcuts().get("Confirm");
    	
		if(okCode != null && code == okCode)
			okMouseReleased(null);
	}

	@Override
	public void init() {
		UIStates.getInstance().incrementExtraPanes();
		
		informationPane.setLayoutX((UIStates.getInstance().getRoot().getWidth() / 2) - (PANE_WIDTH / 2));
		informationPane.setLayoutY((UIStates.getInstance().getRoot().getHeight() / 2) - (PANE_HEIGHT / 2));
		
		UIUtils.makeDraggable(informationPane);
		UIStates.getInstance().getRoot().getScene().setOnKeyReleased(e -> keyReleased(e));	
	}
}
