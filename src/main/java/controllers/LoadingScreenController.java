package controllers;

import java.io.File;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import dao.AccuracyDAO;
import dao.MusicDAO;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Text;
import javafx.util.Duration;
import states.GameStates;
import states.UIStates;
import utils.AudioUtils;
import utils.GameUtils;
import utils.UIUtils;

public class LoadingScreenController implements Controller {
	
	@FXML
	private Text ellipsisText;
	
	@FXML
	private Text informationText;

	@Override
	public void init() {
		String randomBackground = UIUtils.getRandomBackground();

		BackgroundImage background = new BackgroundImage(
				new Image(randomBackground, UIStates.primaryStage.getWidth(), UIStates.primaryStage.getHeight(), false,
						true),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				BackgroundSize.DEFAULT);
		UIStates.root.setBackground(new Background(background));
		
		Timeline ellipsisAnimation = new Timeline(new KeyFrame(Duration.millis(500), e -> {
			String ellipsisTextString = ellipsisText.getText();
			
			int ellipsisNewSize = ellipsisTextString.length() + 1;
			
			if(ellipsisNewSize > 3)
				ellipsisTextString = "";
			else
				ellipsisTextString += ".";
			
			ellipsisText.setText(ellipsisTextString);
		}));
		ellipsisAnimation.setCycleCount(Animation.INDEFINITE);
		ellipsisAnimation.play();
		
		final Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				informationText.setText("game audio");
				
				// Play empty sound file to load clip system and avoid delay
				Clip emptySound = AudioUtils.getSoundClip(getClass().getResource("/sounds/ui/empty.mp3"));

				FloatControl emptyVolume = (FloatControl) emptySound.getControl(FloatControl.Type.MASTER_GAIN);
				emptyVolume.setValue(0);

				emptySound.start();
				emptySound.addLineListener(new LineListener() {
					public void update(LineEvent myLineEvent) {
						if (myLineEvent.getType() == LineEvent.Type.STOP)
							emptySound.close();
					}
				});
				
				UIUtils.setBackSound(AudioUtils.getSoundClip(getClass().getResource("/sounds/ui/back.mp3")));
				UIUtils.setEnterSound(AudioUtils.getSoundClip(getClass().getResource("/sounds/ui/enter.mp3")));
				UIUtils.setBackgroundMusic(AudioUtils.getSoundClip(getClass().getResource("/sounds/ui/bgm.mp3")));
				GameUtils.setHitSound(AudioUtils.getSoundClip(getClass().getResource("/sounds/game/hit_normal.mp3")));
				GameUtils.setFlickSound(AudioUtils.getSoundClip(getClass().getResource("/sounds/game/hit_flick.mp3")));
				GameUtils.setHighscoreSound(AudioUtils.getSoundClip(getClass().getResource("/sounds/game/highscore.mp3")));
				GameUtils.setSuccessSound(AudioUtils.getSoundClip(getClass().getResource("/sounds/game/success.mp3")));
				GameUtils.setFailSound(AudioUtils.getSoundClip(getClass().getResource("/sounds/game/fail.mp3")));
	
				informationText.setText("game images");
				
				UIStates.thumbnailDefaultFile = new File(getClass().getResource("/assets/ui/placeholder.png").toURI());
						
				UIStates.playImage = new Image(getClass().getResource("/assets/ui/play_2.png").openStream());
				UIStates.stopImage = new Image(getClass().getResource("/assets/ui/stop.png").openStream());
				UIStates.favoriteImage = new Image(getClass().getResource("/assets/ui/favorite.png").openStream());
				UIStates.unfavoriteImage = new Image(getClass().getResource("/assets/ui/unfavorite.png").openStream());
				UIStates.removeImage = new Image(getClass().getResource("/assets/ui/remove.png").openStream());
				UIStates.easySelectedImage = new Image(getClass().getResource("/assets/ui/easy.png").openStream());
				UIStates.easyNotSelectedImage = new Image(getClass().getResource("/assets/ui/easy_off.png").openStream());
				UIStates.normalSelectedImage = new Image(getClass().getResource("/assets/ui/normal.png").openStream());
				UIStates.normalNotSelectedImage = new Image(getClass().getResource("/assets/ui/normal_off.png").openStream());
				UIStates.hardSelectedImage = new Image(getClass().getResource("/assets/ui/hard.png").openStream());
				UIStates.hardNotSelectedImage = new Image(getClass().getResource("/assets/ui/hard_off.png").openStream());
				UIStates.pauseImage = new Image(getClass().getResource("/assets/game/pause.png").openStream());
				UIStates.scoreBarImage = new Image(getClass().getResource("/assets/game/score.png").openStream());
				UIStates.energyBarImage = new Image(getClass().getResource("/assets/game/energy_bar.png").openStream());
				UIStates.gradeA = new Image(getClass().getResource("/assets/ui/grade_A.png").openStream());
				UIStates.gradeB = new Image(getClass().getResource("/assets/ui/grade_B.png").openStream());
				UIStates.gradeC = new Image(getClass().getResource("/assets/ui/grade_C.png").openStream());
				UIStates.gradeS = new Image(getClass().getResource("/assets/ui/grade_S.png").openStream());
				UIStates.gradeSS = new Image(getClass().getResource("/assets/ui/grade_SS.png").openStream());
				
				informationText.setText("user settings");
				
				GameStates.userOptions = GameUtils.loadUserSettings();
				
				double userNoteSpeed= GameStates.userOptions.getNoteSpeed();
				double userNoteHeight = GameStates.userOptions.getNoteHeight();
				
				if(userNoteSpeed < GameUtils.MIN_NOTE_SPEED)
					GameStates.userOptions.setNoteSpeed(GameUtils.MIN_NOTE_SPEED);
				else if(userNoteSpeed > GameUtils.MAX_NOTE_SPEED)
					GameStates.userOptions.setNoteSpeed(GameUtils.MAX_NOTE_SPEED);
				
				if(userNoteHeight < GameUtils.MIN_NOTE_HEIGHT)
					GameStates.userOptions.setNoteHeight(GameUtils.MIN_NOTE_HEIGHT);
				else if(userNoteHeight > GameUtils.MAX_NOTE_HEIGHT)
					GameStates.userOptions.setNoteHeight(GameUtils.MAX_NOTE_HEIGHT);
						
				informationText.setText("music library");
			
				GameStates.library = (new MusicDAO()).loadMusicLibrary();
				GameStates.accuracy = (new AccuracyDAO()).loadAccuracyList();
				
				return null;
			}
		};
		task.setOnSucceeded(e -> UIUtils.changeView("MenuScreen.fxml"));

		final Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}

}
