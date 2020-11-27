package controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.collection.Pair;
import entity.ui.Note;
import entity.wrapper.OptionsWrapper;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import states.GameStates;
import utils.GameUtils;
import utils.UIUtils;
import utils.EnumUtils.Types;

public class OptionsController implements Controller {

	private static final double BLUR_AMOUNT = 10;
	private static final Effect blurEffect = new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, 5);

	private static final double NOTE_SPEED_INCREMENT = 100;
	private static final double NOTE_SPEED_INCREASE = 500;

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
		if (UIUtils.isUIBlocked())
			return;

		double newNoteSpeed = optionsWrapper.getNoteSpeed();

		newNoteSpeed -= NOTE_SPEED_INCREMENT;

		if (newNoteSpeed < GameUtils.MIN_NOTE_SPEED)
			newNoteSpeed = GameUtils.MIN_NOTE_SPEED;

		animateNewNote(GameUtils.DEFAULT_NOTE_HEIGHT, newNoteSpeed);
		noteSpeedText.setText(Integer.toString(getFormattedNoteSpeed(GameUtils.MAX_NOTE_SPEED - newNoteSpeed)));

		optionsWrapper.setNoteSpeed(newNoteSpeed);
	}

	@FXML
	void noteSpeedDecrementMouseReleased(MouseEvent event) {
		if (UIUtils.isUIBlocked())
			return;

		double newNoteSpeed = optionsWrapper.getNoteSpeed();

		newNoteSpeed += NOTE_SPEED_INCREMENT;

		if (newNoteSpeed > GameUtils.MAX_NOTE_SPEED)
			newNoteSpeed = GameUtils.MAX_NOTE_SPEED;

		animateNewNote(GameUtils.DEFAULT_NOTE_HEIGHT, newNoteSpeed);
		noteSpeedText.setText(Integer.toString(getFormattedNoteSpeed(GameUtils.MAX_NOTE_SPEED - newNoteSpeed)));

		optionsWrapper.setNoteSpeed(newNoteSpeed);
	}

	@FXML
	void noteSpeedIncreaseMouseReleased(MouseEvent event) {
		if (UIUtils.isUIBlocked())
			return;

		double newNoteSpeed = optionsWrapper.getNoteSpeed();

		newNoteSpeed -= NOTE_SPEED_INCREASE;

		if (newNoteSpeed < GameUtils.MIN_NOTE_SPEED)
			newNoteSpeed = GameUtils.MIN_NOTE_SPEED;

		animateNewNote(GameUtils.DEFAULT_NOTE_HEIGHT, newNoteSpeed);
		noteSpeedText.setText(Integer.toString(getFormattedNoteSpeed(GameUtils.MAX_NOTE_SPEED - newNoteSpeed)));

		optionsWrapper.setNoteSpeed(newNoteSpeed);
	}

	@FXML
	void noteSpeedDecreaseMouseReleased(MouseEvent event) {
		if (UIUtils.isUIBlocked())
			return;

		double newNoteSpeed = optionsWrapper.getNoteSpeed();

		newNoteSpeed += NOTE_SPEED_INCREASE;

		if (newNoteSpeed > GameUtils.MAX_NOTE_SPEED)
			newNoteSpeed = GameUtils.MAX_NOTE_SPEED;

		animateNewNote(GameUtils.DEFAULT_NOTE_HEIGHT, newNoteSpeed);
		noteSpeedText.setText(Integer.toString(getFormattedNoteSpeed(GameUtils.MAX_NOTE_SPEED - newNoteSpeed)));

		optionsWrapper.setNoteSpeed(newNoteSpeed);
	}

	@FXML
	void defaultMouseReleased(MouseEvent event) {
		if (UIUtils.isUIBlocked())
			return;

		optionsWrapper = new OptionsWrapper(true);

		animateNewNote(GameUtils.DEFAULT_NOTE_HEIGHT, optionsWrapper.getNoteSpeed());

		noteSpeedText.setText(Integer.toString(getFormattedNoteSpeed(optionsWrapper.getNoteSpeed())));

		Map<String, KeyCode> defaultShortcuts = optionsWrapper.getShortcuts();
		for (Map.Entry<Button, Pair<String, KeyCode>> entry : shortcuts.entrySet()) {
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
		if (UIUtils.isUIBlocked())
			return;

		GameStates.getInstance().setUserOptions(optionsWrapper);

		UIUtils.playEnterSound();
		try {
			GameUtils.saveUserSettings(GameStates.getInstance().getUserOptions());

			UIUtils.showInformation("Your preferences were updated.");
		} catch (IOException e) {
			UIUtils.showError(e.getMessage());
		}
	}

	@FXML
	void backMouseReleased(MouseEvent event) {
		if (UIUtils.isUIBlocked())
			return;

		UIUtils.playBackSound();
		UIUtils.changeView("MenuScreen.fxml");
	}

	@FXML
	void keyReleased(KeyEvent event) {
		if (UIUtils.isUIBlocked())
			return;

		event.consume();

		KeyCode code = event.getCode();
		KeyCode saveCode = GameStates.getInstance().getUserOptions().getShortcuts().get("Confirm");
		KeyCode backCode = GameStates.getInstance().getUserOptions().getShortcuts().get("Back");

		if (saveCode != null && code == saveCode)
			saveMouseReleased(null);
		else if (backCode != null && code == backCode)
			backMouseReleased(null);
	}

	void shortcutMouseExited(MouseEvent event) {
		if (UIUtils.isUIBlocked())
			return;

		Button shortcutButton = (Button) event.getSource();
		shortcutButton.setStyle("-fx-background-color: transparent;");
	}

	void shortcutMouseEntered(MouseEvent event) {
		if (UIUtils.isUIBlocked())
			return;

		Button shortcutButton = (Button) event.getSource();
		shortcutButton.setStyle("-fx-background-color: rgba(255, 71, 131, 0.15);");
	}

	void shortcutMouseReleased(MouseEvent event) {
		if (UIUtils.isUIBlocked())
			return;

		Button shortcutButton = (Button) event.getSource();
		shortcutButton.setStyle("-fx-background-color: transparent;");

		InputController inputController = (InputController) UIUtils.addView("Input.fxml");

		inputController.setOptionsInputButton(shortcutButton);
		inputController.setInputMessage(shortcuts.get(shortcutButton).getR());
	}

	void setShortchutInput(Button inputButton, KeyCode shortcut) {
		for (Map.Entry<Button, Pair<String, KeyCode>> entry : shortcuts.entrySet()) {
			Button shortcutButton = entry.getKey();
			Pair<String, KeyCode> shortcutInformation = entry.getValue();

			if (shortcutInformation.getR() != shortcut)
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

	void simulateNoteAnimation(Note note, double noteSpeed) {
		if (noteAnimation != null)
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
		return (int) Math.round(noteSpeed / 100);
	}

	private double getProportionalNoteWidth(double noteWidth) {
		return (noteWidth * gamePreviewPane.getWidth()) / 1080;
	}

	private double getProportionalNoteHeight(double noteHeight) {
		return (noteHeight * gamePreviewPane.getHeight()) / 720;
	}

	private void animateNewNote(double noteSize, double noteSpeed) {
		Note note = new Note(1, Types.NORMAL, 81, getProportionalNoteWidth(GameUtils.NOTE_WIDTH),
				getProportionalNoteHeight(noteSize), 133, gamePreviewPane);
		simulateNoteAnimation(note, noteSpeed);
	}

	@Override
	public void init() {
		backgroundPane.setBackground(UIUtils.getRandomBackground());
		backgroundPane.setEffect(blurEffect);

		optionsWrapper.setShortcuts(GameStates.getInstance().getUserOptions().getShortcuts());
		optionsWrapper.setNoteSpeed(GameStates.getInstance().getUserOptions().getNoteSpeed());

		animateNewNote(GameUtils.DEFAULT_NOTE_HEIGHT, optionsWrapper.getNoteSpeed());

		noteSpeedText.setText(
				Integer.toString(getFormattedNoteSpeed(GameUtils.MAX_NOTE_SPEED - optionsWrapper.getNoteSpeed())));

		List<Node> children = shortcutsGridPane.getChildrenUnmodifiable();
		for (Node child : children) {
			Button shortcutButton = new Button();
			shortcutButton.setStyle("-fx-background-color: transparent;");
			shortcutButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

			Integer rowIndex = GridPane.getRowIndex(child);
			if (rowIndex == null || !(child instanceof Label))
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
