package controllers;

import java.text.NumberFormat;
import java.util.Locale;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import states.GameStates;
import states.UIStates;
import utils.UIUtils;

public class MenuScreenController implements Controller {
	
	@FXML
    private Group playGroup;
	
	@FXML
    private Group addGroup;
	
	@FXML
    private Group optionsGroup;
	
	@FXML
    private Group quitGroup;
	
    @FXML
    private Text libraryText;
    
    @FXML
    private Text accuracyText;
    
    @FXML
    private Text highscoreText;

    @FXML
    private Text notificationsText;
    
    @FXML
    private ImageView notificationsImage;
    
    @FXML
    void playMouseReleased(MouseEvent event) {
    	if(UIStates.getInstance().getExtraPanes() > 0)
    		return;
    	
    	UIUtils.playEnterSound();
    	UIUtils.stopBackgroundMusic();
    	
    	UIUtils.changeView("SelectMusic.fxml");
    	UIStates.getInstance().getNewMusicNotifications().set(0);
    }

    @FXML
    void addMouseReleased(MouseEvent event) {
    	if(UIStates.getInstance().getExtraPanes() > 0)
    		return;
    	
    	UIUtils.playEnterSound();
    	UIUtils.stopBackgroundMusic();
		UIUtils.changeView("AddMusic.fxml");
    }

    @FXML
    void optionsMouseReleased(MouseEvent event) {
    	if(UIStates.getInstance().getExtraPanes() > 0)
    		return;
    	
    	UIUtils.playEnterSound();
    	UIUtils.changeView("Options.fxml");
    }

    @FXML
    void quitMouseReleased(MouseEvent event) {
    	if(UIStates.getInstance().getExtraPanes() > 0)
    		return;
    	
    	Platform.exit();
    }
    
    @FXML
    void keyReleased(KeyEvent event) {
    	KeyCode code = event.getCode();
		KeyCode quitCode = GameStates.getInstance().getUserOptions().getShortcuts().get("Back");
		
		if(quitCode != null && code == quitCode)
			quitMouseReleased(null);
    }
    
	@Override
	public void init() {
	    UIStates.getInstance().getRoot().setBackground(UIUtils.getRandomBackground());
    	
    	UIUtils.playBackgroundMusic();
    	
    	libraryText.setText(Integer.toString(GameStates.getInstance().getLibrary().size()));
    	accuracyText.setText(String.format("%.2f", GameStates.getInstance().getMeanAccuracy() * 100));
    	highscoreText.setText(NumberFormat.getNumberInstance(Locale.US).format(GameStates.getInstance().getHighscoreSum()));
    	
    	notificationsText.textProperty().bind(UIStates.getInstance().getNewMusicNotifications().asString());
    	if(UIStates.getInstance().getNewMusicNotifications().get() > 0) {
    		notificationsText.setVisible(true);
    		notificationsImage.setVisible(true);
    	}
    	
    	notificationsText.textProperty().addListener((observable, oldValue, newValue) -> {
    		libraryText.setText(Integer.toString(GameStates.getInstance().getLibrary().size()));
    		
			if(newValue != "0" && !notificationsText.isVisible()) {
				notificationsText.setVisible(true);
				notificationsImage.setVisible(true);
			}
    	});
	} 
	
}
