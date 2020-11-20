package controllers;

import java.io.File;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import dao.DAOFactory;
import dao.impl.AccuracyDAO;
import dao.impl.MusicDAO;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
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
		UIStates.getInstance().getRoot().setBackground(UIUtils.getRandomBackground());
		
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
				
				UIStates.getInstance().setThumbnailDefaultFile(new File(getClass().getResource("/assets/ui/placeholder.png").toURI()));
						
				UIStates.getInstance().setPlayImage(new Image(getClass().getResource("/assets/ui/play_2.png").openStream()));
				UIStates.getInstance().setStopImage(new Image(getClass().getResource("/assets/ui/stop.png").openStream()));
				
				UIStates.getInstance().setFavoriteImage(new Image(getClass().getResource("/assets/ui/favorite.png").openStream()));
				UIStates.getInstance().setUnfavoriteImage(new Image(getClass().getResource("/assets/ui/unfavorite.png").openStream()));
				
				UIStates.getInstance().setRemoveImage(new Image(getClass().getResource("/assets/ui/remove.png").openStream()));
				
				UIStates.getInstance().setEasySelectedImage(new Image(getClass().getResource("/assets/ui/easy.png").openStream()));
				UIStates.getInstance().setEasyNotSelectedImage(new Image(getClass().getResource("/assets/ui/easy_off.png").openStream()));
				UIStates.getInstance().setNormalSelectedImage(new Image(getClass().getResource("/assets/ui/normal.png").openStream()));
				UIStates.getInstance().setNormalNotSelectedImage(new Image(getClass().getResource("/assets/ui/normal_off.png").openStream()));
				UIStates.getInstance().setHardSelectedImage(new Image(getClass().getResource("/assets/ui/hard.png").openStream()));
				UIStates.getInstance().setHardNotSelectedImage(new Image(getClass().getResource("/assets/ui/hard_off.png").openStream()));
				
				UIStates.getInstance().setPauseImage(new Image(getClass().getResource("/assets/game/pause.png").openStream()));
				
				UIStates.getInstance().setScoreBarImage(new Image(getClass().getResource("/assets/game/score.png").openStream()));
				UIStates.getInstance().setEnergyBarImage(new Image(getClass().getResource("/assets/game/energy_bar.png").openStream()));
				
				UIStates.getInstance().setGradeA(new Image(getClass().getResource("/assets/ui/grade_A.png").openStream()));
				UIStates.getInstance().setGradeB(new Image(getClass().getResource("/assets/ui/grade_B.png").openStream()));
				UIStates.getInstance().setGradeC(new Image(getClass().getResource("/assets/ui/grade_C.png").openStream()));
				UIStates.getInstance().setGradeS(new Image(getClass().getResource("/assets/ui/grade_S.png").openStream()));
				UIStates.getInstance().setGradeSS(new Image(getClass().getResource("/assets/ui/grade_SS.png").openStream()));
				
				informationText.setText("user settings");
				
				GameStates.getInstance().setUserOptions(GameUtils.loadUserSettings());
				
				double userNoteSpeed= GameStates.getInstance().getUserOptions().getNoteSpeed();
				
				if(userNoteSpeed < GameUtils.MIN_NOTE_SPEED)
					GameStates.getInstance().getUserOptions().setNoteSpeed(GameUtils.MIN_NOTE_SPEED);
				else if(userNoteSpeed > GameUtils.MAX_NOTE_SPEED)
					GameStates.getInstance().getUserOptions().setNoteSpeed(GameUtils.MAX_NOTE_SPEED);
						
				informationText.setText("music library");
			
				MusicDAO musicDao = DAOFactory.createMusicDAO();
				AccuracyDAO accuracyDao = DAOFactory.createAccuracyDAO();
				
				GameStates.getInstance().setLibrary(musicDao.loadMusicLibrary());
				GameStates.getInstance().setAccuracy(accuracyDao.loadAccuracyList());
				
				musicDao.closeConnection();
				accuracyDao.closeConnection();
				
				return null;
			}
		};
		task.setOnSucceeded(e -> UIUtils.changeView("MenuScreen.fxml"));

		final Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}

}
