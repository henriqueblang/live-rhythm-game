package controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Note;
import entity.OptionsWrapper;
import entity.collection.Pair;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import states.GameStates;
import states.UIStates;
import utils.GameUtils;
import utils.UIUtils;
import utils.EnumUtils.Types;

public class OptionsController implements Controller {
	
	private static final double BLUR_AMOUNT = 10;
	private static final Effect blurEffect = new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, 5);
	
	private static final double NOTE_SPEED_INCREMENT = 100;
	private static final double NOTE_SPEED_INCREASE = 500;
	
	private static final double NOTE_SIZE_INCREMENT = GameUtils.DEFAULT_NOTE_HEIGHT / 100;
	private static final double NOTE_SIZE_INCREASE = NOTE_SIZE_INCREMENT * 10;
	
	private OptionsWrapper optionsWrapper = new OptionsWrapper();
	
	private Timeline noteAnimation;
	
	private Map<Button, Pair<String, KeyCode>> shortcuts = new HashMap<>();
	
	@FXML
    private Pane backgroundPane;

    @FXML
    private Button backButton;

    @FXML
    private GridPane shortcutsGridPane;

    @FXML
    private Text noteSpeedText;

    @FXML
    private Text noteSizeText;

    @FXML
    private AnchorPane gamePreviewPane;

    @FXML
    void noteSpeedIncrementMouseReleased(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
			return;
    	
    	double newNoteSpeed = optionsWrapper.getNoteSpeed();
    			
    	newNoteSpeed += NOTE_SPEED_INCREMENT;
    	
    	if(newNoteSpeed > GameUtils.MAX_NOTE_SPEED)
    		newNoteSpeed = GameUtils.MAX_NOTE_SPEED;
    	
    	animateNewNote(optionsWrapper.getNoteHeight(), newNoteSpeed);
    	noteSpeedText.setText(Integer.toString(getFormattedNoteSpeed(newNoteSpeed)));
    	
    	optionsWrapper.setNoteSpeed(newNoteSpeed);
    }

    @FXML
    void noteSpeedDecrementMouseReleased(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
			return;
		
    	double newNoteSpeed = optionsWrapper.getNoteSpeed();
		    
    	newNoteSpeed -= NOTE_SPEED_INCREMENT;
    	
    	if(newNoteSpeed < GameUtils.MIN_NOTE_SPEED)
    		newNoteSpeed = GameUtils.MIN_NOTE_SPEED;
    	
    	animateNewNote(optionsWrapper.getNoteHeight(), newNoteSpeed);
    	noteSpeedText.setText(Integer.toString(getFormattedNoteSpeed(newNoteSpeed)));
    	
    	optionsWrapper.setNoteSpeed(newNoteSpeed);
    }
    
    @FXML
    void noteSpeedIncreaseMouseReleased(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
			return;

    	double newNoteSpeed = optionsWrapper.getNoteSpeed();
    	
    	newNoteSpeed += NOTE_SPEED_INCREASE;
    	
    	if(newNoteSpeed > GameUtils.MAX_NOTE_SPEED)
    		newNoteSpeed = GameUtils.MAX_NOTE_SPEED;
    	
    	animateNewNote(optionsWrapper.getNoteHeight(), newNoteSpeed);
    	noteSpeedText.setText(Integer.toString(getFormattedNoteSpeed(newNoteSpeed)));
    	
    	optionsWrapper.setNoteSpeed(newNoteSpeed);
    }

    @FXML
    void noteSpeedDecreaseMouseReleased(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
			return;
    	
    	double newNoteSpeed = optionsWrapper.getNoteSpeed();
        
    	newNoteSpeed -= NOTE_SPEED_INCREASE;
    	
    	if(newNoteSpeed < GameUtils.MIN_NOTE_SPEED)
    		newNoteSpeed = GameUtils.MIN_NOTE_SPEED;
    	
    	animateNewNote(optionsWrapper.getNoteHeight(), newNoteSpeed);
    	noteSpeedText.setText(Integer.toString(getFormattedNoteSpeed(newNoteSpeed)));
    	
    	optionsWrapper.setNoteSpeed(newNoteSpeed);
    }
    
    @FXML
    void noteSizeIncrementMouseReleased(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
			return;
    	
    	double newNoteHeight = optionsWrapper.getNoteHeight();
    	
    	newNoteHeight += NOTE_SIZE_INCREMENT;
    	
    	if(newNoteHeight > GameUtils.MAX_NOTE_HEIGHT)
    		newNoteHeight = GameUtils.MAX_NOTE_HEIGHT;
		
    	animateNewNote(newNoteHeight, optionsWrapper.getNoteSpeed());
    	noteSizeText.setText(Integer.toString(getFormattedNoteSize(newNoteHeight)));
    	
    	optionsWrapper.setNoteHeight(newNoteHeight);
    }

    @FXML
    void noteSizeDecrementMouseReleased(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
			return;
		
    	double newNoteHeight = optionsWrapper.getNoteHeight();
    	
    	newNoteHeight -= NOTE_SIZE_INCREMENT;
    	
    	if(newNoteHeight < GameUtils.MIN_NOTE_HEIGHT)
    		newNoteHeight = GameUtils.MIN_NOTE_HEIGHT;
		
    	animateNewNote(newNoteHeight, optionsWrapper.getNoteSpeed());
    	noteSizeText.setText(Integer.toString(getFormattedNoteSize(newNoteHeight)));
    	
    	optionsWrapper.setNoteHeight(newNoteHeight);
    }
    
    @FXML
    void noteSizeIncreaseMouseReleased(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
			return;
		
    	double newNoteHeight = optionsWrapper.getNoteHeight();
    	
    	newNoteHeight += NOTE_SIZE_INCREASE;
    	
    	if(newNoteHeight > GameUtils.MAX_NOTE_HEIGHT)
    		newNoteHeight = GameUtils.MAX_NOTE_HEIGHT;
		
    	animateNewNote(newNoteHeight, optionsWrapper.getNoteSpeed());
    	noteSizeText.setText(Integer.toString(getFormattedNoteSize(newNoteHeight)));
    	
    	optionsWrapper.setNoteHeight(newNoteHeight);
    }

    @FXML
    void noteSizeDecreaseMouseReleased(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
			return;
		
    	double newNoteHeight = optionsWrapper.getNoteHeight();
    	
    	newNoteHeight -= NOTE_SIZE_INCREASE;
    	
    	if(newNoteHeight < GameUtils.MIN_NOTE_HEIGHT)
    		newNoteHeight = GameUtils.MIN_NOTE_HEIGHT;
		
    	animateNewNote(newNoteHeight, optionsWrapper.getNoteSpeed());
    	noteSizeText.setText(Integer.toString(getFormattedNoteSize(newNoteHeight)));
    	
    	optionsWrapper.setNoteHeight(newNoteHeight);
    }
    
    @FXML
    void defaultMouseReleased(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
			return;
		
    	optionsWrapper = new OptionsWrapper(true);
    	
    	animateNewNote(optionsWrapper.getNoteHeight(), optionsWrapper.getNoteSpeed());
    	
    	noteSizeText.setText(Integer.toString(getFormattedNoteSize(optionsWrapper.getNoteHeight())));
		noteSpeedText.setText(Integer.toString(getFormattedNoteSpeed(optionsWrapper.getNoteSpeed())));
		
		Map<String, KeyCode> defaultShortcuts = optionsWrapper.getShortcuts();
		for(Map.Entry<Button, Pair<String, KeyCode>> entry : shortcuts.entrySet()) {
			Button shortcutButton = entry.getKey();
			Pair<String, KeyCode> shortcutInformation = entry.getValue();
			
			KeyCode defaultShortcutKeyCode = defaultShortcuts.get(shortcutInformation.getL());
			
			shortcutButton.setText(defaultShortcutKeyCode == null ? "<not set>" : defaultShortcutKeyCode.getName());
			shortcutInformation.setR(defaultShortcutKeyCode);
		}
		
		UIUtils.showInformation("Default preferences were recovered.");
    }

    @FXML
    void saveMouseReleased(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
			return;
		
    	GameStates.userOptions = optionsWrapper;
    	
    	UIUtils.playEnterSound();
    	try {
			GameUtils.saveUserSettings(GameStates.userOptions);
			
			UIUtils.showInformation("Your preferences were updated.");
		} catch (IOException e) {
			UIUtils.showError(e.getMessage());
		}
    }

    @FXML
    void backMouseReleased(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
			return;
		
    	UIUtils.playBackSound();
		UIUtils.changeView("MenuScreen.fxml");
    }
    
    @FXML
    void keyReleased(KeyEvent event) {
    	if(UIStates.extraPanes > 0)
			return;
		
    	KeyCode code = event.getCode();
		KeyCode saveCode = GameStates.userOptions.getShortcuts().get("Confirm");
		KeyCode backCode = GameStates.userOptions.getShortcuts().get("Back");
		
		if(saveCode != null && code == saveCode)
			saveMouseReleased(null);
		else if(backCode != null && code == backCode)
			backMouseReleased(null);
    }
    
    void shortcutMouseExited(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
			return;
		
    	Button shortcutButton = (Button) event.getSource();
    	shortcutButton.setStyle("-fx-background-color: transparent;");
    }
    
    void shortcutMouseEntered(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
			return;
    	
    	Button shortcutButton = (Button) event.getSource();
		shortcutButton.setStyle("-fx-background-color: rgba(255, 71, 131, 0.15);");
    }
    
    void shortcutMouseReleased(MouseEvent event) {
    	if(UIStates.extraPanes > 0)
			return;
    	
    	Button shortcutButton = (Button) event.getSource();
    	
    	InputController inputController = (InputController) UIUtils.addView("Input.fxml");
    	inputController.setOptionsController(this);
    	
    	inputController.setOptionsInputButton(shortcutButton);
    	inputController.setInputMessage(shortcuts.get(shortcutButton).getR());
    }
    
    void setShortchutInput(Button inputButton, KeyCode shortcut) {
    	for(Map.Entry<Button, Pair<String, KeyCode>> entry : shortcuts.entrySet()) {
    		Button shortcutButton = entry.getKey();
    		Pair<String, KeyCode> shortcutInformation = entry.getValue();
    		
    		if(shortcutInformation.getR() != shortcut)
    			continue;
    		
    		shortcutButton.setText("<not set>");
    		
    		shortcutInformation.setR(null);
    		optionsWrapper.getShortcuts().put(shortcutInformation.getL(), null);
        }
    	
    	inputButton.setText(shortcut.getName());
    	
    	Pair<String, KeyCode> shortcutInformation = shortcuts.get(inputButton);
    	
    	shortcutInformation.setR(shortcut);
    	optionsWrapper.getShortcuts().put(shortcutInformation.getL(), shortcut);
    }
    
    void simulateAnimateNote(Note note, double noteSpeed) {
    	if(noteAnimation != null)
    		noteAnimation.stop();
    	
		final Duration duration = Duration.millis(noteSpeed);
		Timeline animation = new Timeline(new KeyFrame(duration, new KeyValue(note.yProperty(), note.getEndY())));

		animation.setOnFinished(e -> note.setY(0));
		animation.statusProperty().addListener(new ChangeListener<Status>() {
			@Override
			public void changed(ObservableValue<? extends Status> observableValue, Status oldValue, Status newValue) {
				if (newValue == Status.STOPPED)
					gamePreviewPane.getChildren().remove(note);
			}
		});

		animation.setCycleCount(Animation.INDEFINITE);
		animation.play();
		
		noteAnimation = animation;
	}
    
    private int getFormattedNoteSpeed(double noteSpeed) {
    	return (int) (noteSpeed / 100);
    }

    private int getFormattedNoteSize(double noteSize) {
    	return (int) ((noteSize * 100) / GameUtils.DEFAULT_NOTE_HEIGHT);
    }
    
    private double getProportionalNoteWidth(double noteWidth) {
    	return (noteWidth * gamePreviewPane.getWidth()) / 1080;
    }
    
    private double getProportionalNoteHeight(double noteHeight) {
    	return (noteHeight * gamePreviewPane.getHeight()) / 720;
    }
    
    private void animateNewNote(double noteSize, double noteSpeed) {
		Note note = new Note(1, Types.NORMAL, 83.75, getProportionalNoteWidth(GameUtils.NOTE_WIDTH), getProportionalNoteHeight(noteSize), 157.986, gamePreviewPane);
		simulateAnimateNote(note, noteSpeed);
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
	    
	    backgroundPane.setBackground(new Background(background));
    	backgroundPane.setEffect(blurEffect);
    	
    	optionsWrapper.setShortcuts(GameStates.userOptions.getShortcuts());
    	optionsWrapper.setNoteSpeed(GameStates.userOptions.getNoteSpeed());
    	optionsWrapper.setNoteHeight(GameStates.userOptions.getNoteHeight());
    	
    	animateNewNote(optionsWrapper.getNoteHeight(), optionsWrapper.getNoteSpeed());
    	
    	noteSizeText.setText(Integer.toString(getFormattedNoteSize(optionsWrapper.getNoteHeight())));
		noteSpeedText.setText(Integer.toString(getFormattedNoteSpeed(optionsWrapper.getNoteSpeed())));
		
		List<Node> children = shortcutsGridPane.getChildrenUnmodifiable();
		for(Node child : children) {
			Button shortcutButton = new Button();
			shortcutButton.setStyle("-fx-background-color: transparent;");
			shortcutButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			
			Integer rowIndex = GridPane.getRowIndex(child);
			if(rowIndex == null || !(child instanceof Label))
				continue;
			
			shortcutsGridPane.add(shortcutButton, 1, rowIndex.intValue());
			
			GridPane.setFillWidth(shortcutButton, true);
			GridPane.setFillHeight(shortcutButton, true);
			
			Label shortcutDescriptionLabel = (Label) child;
			String identification = shortcutDescriptionLabel.getText();
			
			KeyCode shortcut = optionsWrapper.getShortcuts().get(identification);
			
			shortcutButton.setText(shortcut == null ? "<not set>" : shortcut.getName());
			shortcutButton.setOnMouseExited(e -> shortcutMouseExited(e));
			shortcutButton.setOnMouseEntered(e -> shortcutMouseEntered(e));
			shortcutButton.setOnMouseReleased(e -> shortcutMouseReleased(e));
			
			shortcuts.put(shortcutButton, new Pair<>(identification, shortcut));
		}
	}

}
