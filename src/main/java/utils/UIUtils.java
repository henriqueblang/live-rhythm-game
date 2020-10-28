package utils;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import javax.sound.sampled.Clip;

import controllers.ClearController;
import controllers.Controller;
import controllers.InformationController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import states.UIStates;
import utils.EnumUtils.Grades;
import utils.EnumUtils.Scores;

public class UIUtils {
	private static Clip backSound;
	private static Clip enterSound;
	private static Clip backgroundMusic;

	private static final String[] BACKGROUNDS = { "bg_1.png", "bg_2.png", "bg_3.png", "bg_4.png", "bg_5.png",
			"bg_6.png", "bg_7.png", "bg_8.png", "bg_9.png", "bg_10.png" };

	private static final String[] CHIBIS = { "chibi_1.png", "chibi_2.png", "chibi_3.png" };

	public static String getRandomBackground() {
		return "/assets/backgrounds/" + BACKGROUNDS[new Random().nextInt(BACKGROUNDS.length)];
	}

	public static String getRandomChibi() {
		return "/assets/chibis/" + CHIBIS[new Random().nextInt(CHIBIS.length)];
	}

	public static void setBackSound(Clip backSound) {
		UIUtils.backSound = backSound;
	}

	public static void setEnterSound(Clip enterSound) {
		UIUtils.enterSound = enterSound;
	}

	public static void setBackgroundMusic(Clip backgroundMusic) {
		UIUtils.backgroundMusic = backgroundMusic;
	}

	public static void playBackSound() {
		AudioUtils.playAudio(backSound);
	}

	public static void playEnterSound() {
		AudioUtils.playAudio(enterSound);
	}

	public static void playBackgroundMusic() {
		if(backgroundMusic.isRunning())
			return;
		
		backgroundMusic.setMicrosecondPosition(0);
		backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public static void stopBackgroundMusic() {
		backgroundMusic.stop();
	}

	public static void changeView(String viewPath) {
		FXMLLoader fxmlLoader = new FXMLLoader();

		try {
			UIStates.root = fxmlLoader.load(UIUtils.class.getResource("/view/" + viewPath).openStream());
		} catch (IOException e) {
			UIUtils.showError(e.getMessage());
		}

		UIStates.parent = UIStates.root;

		Scene scene = new Scene(UIStates.root);
		UIStates.primaryStage.setScene(scene);

		if (!UIStates.primaryStage.isShowing()) {
			UIStates.primaryStage.sizeToScene();
			UIStates.primaryStage.setResizable(false);
			UIStates.primaryStage.show();
		}

		Controller controller = (Controller) fxmlLoader.getController();
		controller.init();
	}

	public static Controller addView(String viewPath) {
		FXMLLoader fxmlLoader = new FXMLLoader();

		try {
			UIStates.root.getChildren()
					.add(fxmlLoader.load(UIUtils.class.getResource("/view/" + viewPath).openStream()));
		} catch (IOException e) {
			UIUtils.showError(e.getMessage());
		}

		Controller controller = (Controller) fxmlLoader.getController();
		controller.init();
		
		return controller;
	}
	
	public static void showClear(Grades grade, int score, int maxCombo, 
			Map<Scores, Integer> hitGradeCountMap) {
		ClearController controller = (ClearController) UIUtils.addView("Clear.fxml");
		
		controller.fillResults(
			grade, 
			score, 
			maxCombo, 
			hitGradeCountMap
		);
	}

	public static void showFail() {
		InformationController ic = (InformationController) UIUtils.addView("Information.fxml");
		
		ic.setWindowInformation(
			"Fail",
			"You couldn't clear this song. Keep practising. If you are uncomfortable, adjust your game settings.",
			true
		);
	}
	
	public static void showError(String message) {
		InformationController ic = (InformationController) UIUtils.addView("Information.fxml");
		
		ic.setWindowInformation("Error", message);
	}
	
	public static void showInformation(String message) {
		InformationController ic = (InformationController) UIUtils.addView("Information.fxml");
		
		ic.setWindowInformation("Information", message);
	}

	public static void addStylesheet(String css) {
		UIStates.root.getScene().getStylesheets().add(UIUtils.class.getResource("/view/css/" + css).toExternalForm());
	}
	
	public static void setPaddedText(Text text, Text paddingText, int initialPadding, int value) {
		
		String oldValueString = text.getText();
		int oldValueDigits = oldValueString.length();
		
		String newValueString = Integer.toString(value);
		int newValueDigits = newValueString.length();
		
		if(newValueDigits != oldValueDigits) {
			String zeroes = "";
			
			int zeroesAmount = (initialPadding + 1) - newValueDigits;
			for(int i = 0; i < zeroesAmount; i++)
				zeroes += "0";
			
			paddingText.setText(zeroes);
		}
	
		text.setText(newValueString);
	}
	
	public static void showAnimatedText(Text text, String message, double duration) {
		final String initialMessage = message;
		
		if(!text.isVisible())
			text.setVisible(true);
		
		text.setScaleX(1);
		text.setScaleY(1);
		text.setOpacity(1);
		
		text.setText(message);
		
		Timeline animationText = new Timeline(
				new KeyFrame(Duration.millis(duration), new KeyValue(text.opacityProperty(), 0)));
		animationText.play();

		ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(duration), text);
		scaleTransition.setFromX(1);
		scaleTransition.setFromY(1);
		scaleTransition.setByX(0.5);
		scaleTransition.setByY(0.5);
		scaleTransition.play();

		Timeline hideText = new Timeline(new KeyFrame(Duration.millis(duration), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				animationText.stop();
				
				if(initialMessage != text.getText())
					return;

				text.setScaleX(1);
				text.setScaleY(1);
				text.setOpacity(1);
				text.setVisible(false);
			}
		}));
		hideText.play();
	}
	
	public static void showRipple(double x, double y, Paint color) {
		Circle ripple_1 = new Circle(x, y, 0, null);
		Circle ripple_2 = new Circle(x, y, 0, null);

		ripple_1.setStroke(color);
		ripple_2.setStroke(color);

		UIStates.root.getChildren().add(ripple_1);
		UIStates.root.getChildren().add(ripple_2);

		Timeline animationRipple_1 = new Timeline(
				new KeyFrame(Duration.ZERO, new KeyValue(ripple_1.radiusProperty(), 0)),
				new KeyFrame(Duration.seconds(0.3), new KeyValue(ripple_1.radiusProperty(), 100)),
				new KeyFrame(Duration.seconds(0.3), new KeyValue(ripple_1.opacityProperty(), 0)));
		animationRipple_1.play();

		Timeline animationRipple_2 = new Timeline(
				new KeyFrame(Duration.seconds(0.1), new KeyValue(ripple_2.radiusProperty(), 0)),
				new KeyFrame(Duration.seconds(0.3), new KeyValue(ripple_2.radiusProperty(), 80)),
				new KeyFrame(Duration.seconds(0.3), new KeyValue(ripple_2.opacityProperty(), 0)));
		animationRipple_2.play();

		Timeline removeRipple = new Timeline(new KeyFrame(Duration.seconds(0.3), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				UIStates.root.getChildren().remove(ripple_1);
				animationRipple_1.stop();

				UIStates.root.getChildren().remove(ripple_2);
				animationRipple_2.stop();
			}
		}));
		removeRipple.play();
	}

    public static void makeDraggable(Node node) {
        final Delta dragDelta = new Delta();

        node.setOnMousePressed(me -> {
            dragDelta.x = me.getX();
            dragDelta.y = me.getY();
        });
        
        node.setOnMouseDragged(me -> {
            node.setLayoutX(node.getLayoutX() + me.getX() - dragDelta.x);
            node.setLayoutY(node.getLayoutY() + me.getY() - dragDelta.y);
        });
    }
    
    private static class Delta {
        public double x;
        public double y;
    }
}
