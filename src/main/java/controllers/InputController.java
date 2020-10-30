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

	@FXML
	private AnchorPane inputPane;

	@FXML
	private Text inputText;

	public void setOptionsInputButton(Button optionsInputButton) {
		this.optionsInputButton = optionsInputButton;
	}

	public void setInputMessage(KeyCode code) {
		inputText.setText(code == null ? "<not set>" : code.getName());
	}

	@FXML
	void okMouseReleased(MouseEvent event) {
		if (inputKeyCode == null)
			cancelMouseReleased(event);
		else {
			UIStates.getInstance().getRoot().getScene().setOnKeyReleased(null);

			UIStates.getInstance().decrementExtraPanes();
			UIStates.getInstance().getRoot().getChildren().remove(inputPane);

			((OptionsController) UIStates.getInstance().getMainController()).setShortchutInput(optionsInputButton, inputKeyCode);
		}
	}

	@FXML
	void cancelMouseReleased(MouseEvent event) {
		UIStates.getInstance().getRoot().getScene().setOnKeyReleased(null);

		UIStates.getInstance().decrementExtraPanes();
		UIStates.getInstance().getRoot().getChildren().remove(inputPane);
	}

	@FXML
	void keyReleased(KeyEvent event) {
		KeyCode code = event.getCode();

		inputKeyCode = code;
		setInputMessage(inputKeyCode);
	}

	@Override
	public void init() {
		UIStates.getInstance().incrementExtraPanes();

		inputPane.setLayoutX((UIStates.getInstance().getRoot().getWidth() / 2) - (PANE_WIDTH / 2));
		inputPane.setLayoutY((UIStates.getInstance().getRoot().getHeight() / 2) - (PANE_HEIGHT / 2));

		UIStates.getInstance().getRoot().getScene().setOnKeyReleased(e -> keyReleased(e));
	}
}
