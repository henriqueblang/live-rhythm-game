package controllers;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import states.GameStates;
import states.UIStates;
import utils.UIUtils;

public class PauseController implements Controller {

	final private double PANE_WIDTH = 350;
	final private double PANE_HEIGHT = 128;

	@FXML
	private AnchorPane pausePane;

	@FXML
	void quitAction(MouseEvent event) {
		UIStates.getInstance().getRoot().getScene().setOnKeyReleased(null);

		UIStates.getInstance().decrementExtraPanes();
		UIStates.getInstance().getRoot().getChildren().remove(pausePane);

		((GameController) UIStates.getInstance().getMainController()).finishGame(false);
	}

	@FXML
	void resumeAction(MouseEvent event) {
		UIStates.getInstance().getRoot().getScene().setOnKeyReleased(null);

		UIStates.getInstance().decrementExtraPanes();
		UIStates.getInstance().getRoot().getChildren().remove(pausePane);

		((GameController) UIStates.getInstance().getMainController()).resumeGame();
	}

	void keyReleased(KeyEvent event) {
		KeyCode code = event.getCode();
		KeyCode okCode = GameStates.getInstance().getUserOptions().getShortcuts().get("Confirm");
		KeyCode backCode = GameStates.getInstance().getUserOptions().getShortcuts().get("Back");

		if (okCode != null && code == okCode)
			resumeAction(null);
		else if (backCode != null && code == backCode)
			quitAction(null);
	}

	@Override
	public void init() {
		UIStates.getInstance().incrementExtraPanes();

		pausePane.setLayoutX((UIStates.getInstance().getRoot().getWidth() / 2) - (PANE_WIDTH / 2));
		pausePane.setLayoutY((UIStates.getInstance().getRoot().getHeight() / 2) - (PANE_HEIGHT / 2));

		UIUtils.makeDraggable(pausePane);
		UIStates.getInstance().getRoot().getScene().setOnKeyReleased(e -> keyReleased(e));
	}
}
