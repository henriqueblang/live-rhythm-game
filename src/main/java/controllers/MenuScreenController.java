package controllers;

import java.text.NumberFormat;
import java.util.Locale;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
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
    	if(UIStates.extraPanes > 0)
    		return;
    	
    	UIUtils.playEnterSound();
    	UIUtils.stopBackgroundMusic();
    	
    	UIUtils.changeView("SelectMusic.fxml");
    	UIStates.newMusicNotifications.set(0);
    }

    @FXML
    void addMouseReleased(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
    		return;
    	
    	UIUtils.playEnterSound();
    	UIUtils.stopBackgroundMusic();
		UIUtils.changeView("AddMusic.fxml");
    }

    @FXML
    void optionsMouseReleased(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
    		return;
    	
    	UIUtils.playEnterSound();
    	UIUtils.changeView("Options.fxml");
    }

    @FXML
    void quitMouseReleased(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
    		return;
    	
    	Platform.exit();
    }
    
    @FXML
    void keyReleased(KeyEvent event) {
    	KeyCode code = event.getCode();
		KeyCode quitCode = GameStates.userOptions.getShortcuts().get("Back");
		
		if(quitCode != null && code == quitCode)
			quitMouseReleased(null);
    }
    
	@Override
	public void init() {
	    String randomBackground = UIUtils.getRandomBackground();
	    
	    BackgroundImage background = new BackgroundImage(
			new Image(
					randomBackground, 
					UIStates.primaryStage.getWidth(), 
					UIStates.primaryStage.getHeight(), 
					false, 
					true
			), 
			BackgroundRepeat.NO_REPEAT, 
			BackgroundRepeat.NO_REPEAT,
			BackgroundPosition.CENTER, 
			BackgroundSize.DEFAULT
		);
	    
    	UIStates.root.setBackground(new Background(background));
    	
    	UIUtils.playBackgroundMusic();
    	
    	libraryText.setText(Integer.toString(GameStates.library.size()));
    	accuracyText.setText(String.format("%.2f", GameStates.getMeanAccuracy() * 100));
    	highscoreText.setText(NumberFormat.getNumberInstance(Locale.US).format(GameStates.getHighscoreSum()));
    	
    	notificationsText.textProperty().bind(UIStates.newMusicNotifications.asString());
    	if(UIStates.newMusicNotifications.get() > 0) {
    		notificationsText.setVisible(true);
    		notificationsImage.setVisible(true);
    	}
    	
    	notificationsText.textProperty().addListener((observable, oldValue, newValue) -> {
    		libraryText.setText(Integer.toString(GameStates.library.size()));
    		
			if(newValue != "0" && !notificationsText.isVisible()) {
				notificationsText.setVisible(true);
				notificationsImage.setVisible(true);
			}
    	});
	} 
	
}
