package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controllers.ClearController;

import entity.Music;
import entity.OptionsWrapper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import states.GameStates;
import utils.EnumUtils.Grades;
import utils.EnumUtils.Scores;

public class GameUtils {

	public final static int ACCURACY_GAME_AMOUNT = 5;

	public final static int TRACK_AMOUNT = 4;
	public final static int GAME_START_COUNTDOWN = 5;
	public final static double NOTE_WIDTH = 135;

	public final static double MIN_NOTE_SPEED = 100;
	public final static double MAX_NOTE_SPEED = 2500;

	public final static double DEFAULT_NOTE_HEIGHT = 40;
	// 1% de DEFAULT_NOTE_HEIGHT
	public final static double MIN_NOTE_HEIGHT = DEFAULT_NOTE_HEIGHT / 100;
	// 200% de DEFAULT_NOTE_HEIGHT
	public final static double MAX_NOTE_HEIGHT = (DEFAULT_NOTE_HEIGHT * 200) / 100;

	private final static String OPTIONS_FILE_NAME = "settings.json";

	private static Clip hitSound;
	private static Clip flickSound;

	private static Clip highscoreSound;

	private static Clip successSound;
	private static Clip failSound;

	public static OptionsWrapper getDefaultOptions() {
		return new OptionsWrapper(true);
	}

	public static OptionsWrapper loadUserSettings() throws IOException {
		OptionsWrapper optionsWrapper = new OptionsWrapper();
		
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(OPTIONS_FILE_NAME));
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();

			optionsWrapper = gson.fromJson(bufferedReader, OptionsWrapper.class);
		} catch (FileNotFoundException e) {
			optionsWrapper = new OptionsWrapper(true);
			
			saveUserSettings(optionsWrapper);
		}

		return optionsWrapper;
	}

	public static void saveUserSettings(OptionsWrapper optionsWrapper) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(OPTIONS_FILE_NAME));
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		
		bufferedWriter.write(gson.toJson(optionsWrapper));
		bufferedWriter.close();
	}

	public static void setHitSound(Clip hitSound) {
		GameUtils.hitSound = hitSound;

		FloatControl hitVolume = (FloatControl) GameUtils.hitSound.getControl(FloatControl.Type.MASTER_GAIN);
		float range = hitVolume.getMaximum() - hitVolume.getMinimum();
		float gain = (float) ((range * 0.75) + hitVolume.getMinimum());
		hitVolume.setValue(gain);
	}

	public static void setFlickSound(Clip flickSound) {
		GameUtils.flickSound = flickSound;

		FloatControl flickVolume = (FloatControl) GameUtils.flickSound.getControl(FloatControl.Type.MASTER_GAIN);
		float rangeFlick = flickVolume.getMaximum() - flickVolume.getMinimum();
		float gainFlick = (float) ((rangeFlick * 0.85) + flickVolume.getMinimum());
		flickVolume.setValue(gainFlick);
	}

	public static void setHighscoreSound(Clip highscoreSound) {
		GameUtils.highscoreSound = highscoreSound;
	}

	public static void setSuccessSound(Clip successSound) {
		GameUtils.successSound = successSound;
	}

	public static void setFailSound(Clip failSound) {
		GameUtils.failSound = failSound;
	}

	public static void playHitSound() {
		Thread thread = new Thread() {
			public void run() {
				hitSound.stop();

				hitSound.setMicrosecondPosition(0);
				hitSound.start();
			}
		};

		thread.start();
	}

	public static void playFlickSound() {
		Thread thread = new Thread() {
			public void run() {
				flickSound.stop();

				flickSound.setMicrosecondPosition(0);
				flickSound.start();
			}
		};

		thread.start();
	}

	public static void playHighscoreSound() {
		highscoreSound.setMicrosecondPosition(0);
		highscoreSound.start();
	}

	public static void playSuccessSound() {
		successSound.setMicrosecondPosition(0);
		successSound.start();
	}

	public static void playFailSound() {
		failSound.setMicrosecondPosition(0);
		failSound.start();
	}

	public static void startGame(Music music, int mode) {
		GameStates.gameMode = mode;
		GameStates.gameMusic = music;

		UIUtils.changeView("Game.fxml");
	}

	public static void showResultView(boolean clear, Grades grade, int score, int maxCombo,
			Map<Scores, Integer> hitGradeCountMap) {
		if (!clear) {
			playFailSound();

			double audioDuration = 1000 * AudioUtils.getAudioDuration(failSound);
			Timeline audioFinishTimeline = new Timeline(
					new KeyFrame(Duration.millis(audioDuration), e -> UIUtils.addView("Fail.fxml")));
			audioFinishTimeline.play();
		} else {
			playSuccessSound();

			double audioDuration = 1000 * AudioUtils.getAudioDuration(failSound);
			Timeline audioFinishTimeline = new Timeline(new KeyFrame(Duration.millis(audioDuration), e -> {
				ClearController controller = (ClearController) UIUtils.addView("Clear.fxml");
				controller.fillResults(grade, score, maxCombo, hitGradeCountMap);
			}));
			audioFinishTimeline.play();
		}
	}
}
