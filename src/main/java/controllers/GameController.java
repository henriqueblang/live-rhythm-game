package controllers;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import animatefx.animation.Pulse;
import entity.Music;
import entity.collection.Beatmap;
import entity.collection.Pair;
import entity.ui.Note;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import states.GameStates;
import states.UIStates;
import utils.EnumUtils.Grades;
import utils.EnumUtils.Scores;
import utils.EnumUtils.Types;
import utils.AudioUtils;
import utils.GameUtils;
import utils.UIUtils;

public class GameController implements Controller {

	final private boolean AUTO_HIT = true;
	final private double AUTO_HIT_CHANCE = 100;
	final double AUTO_HIT_DELAY = ((650 - (GameStates.getInstance().getUserOptions().getNoteHeight() / 2))
			* GameStates.getInstance().getUserOptions().getNoteSpeed()) / 650;

	private double scoreBarTotalWidth;
	private int paddingInitialAmount;

	private int beatmapPointer;
	private int startTime;
	private double interval;
	private long pauseTime;

	private int maxCombo;
	private int comboLength;

	private int totalHits;
	private int removedNotes;

	private int maxPossibleScore;
	private int followedMultiplierHits;

	private double baseHealthLoss;
	private double missMultiplier;
	private double scoreMultiplier;

	private KeyCode track_1Code;
	private KeyCode track_2Code;
	private KeyCode track_3Code;
	private KeyCode track_4Code;

	private KeyCode flick_1Code;
	private KeyCode flick_2Code;
	private KeyCode flick_3Code;
	private KeyCode flick_4Code;

	private KeyCode pauseCode;

	private Music music;
	private Beatmap beatmap;
	private Grades scoreGrade;

	private Note[] hitTracks = new Note[GameUtils.TRACK_AMOUNT];
	private boolean[] flickTracks = new boolean[GameUtils.TRACK_AMOUNT];
	private Scores[] scorePerTrack = { Scores.MISS, Scores.MISS, Scores.MISS, Scores.MISS };

	private Map<Scores, Integer> hitGradeCountMap = new HashMap<>();

	private BitSet currentlyInUseKeys = new BitSet();

	private Timeline audioFinishTimeline;
	private Timeline noteCreationTimeline;
	private Map<Note, SequentialTransition> noteAnimations = new HashMap<>();

	private List<Timeline> autoHitEvents = new ArrayList<>();

	@FXML
	private ProgressBar healthBar;

	@FXML
	private Rectangle scoreBar;

	@FXML
	private Label comboCountLabel;

	@FXML
	private Label comboLabel;

	@FXML
	private Text countdownText;

	@FXML
	private Text healthText;

	@FXML
	private Text gainText;

	@FXML
	private Text scoreText;

	@FXML
	private Text scorePaddingText;

	@FXML
	private Text hitScoreGradeText;

	@FXML
	void pauseMouseReleased(MouseEvent event) {
		if (!music.isPlaying() || UIStates.getInstance().getExtraPanes() > 0)
			return;

		pauseTime = music.getAudio().getMicrosecondPosition();

		music.stopAudio();

		audioFinishTimeline.pause();
		noteCreationTimeline.pause();

		for (Map.Entry<Note, SequentialTransition> entry : noteAnimations.entrySet()) {
			entry.getValue().pause();
		}

		for (Timeline autoHit : autoHitEvents) {
			autoHit.pause();
		}

		UIUtils.addView("Pause.fxml");
	}

	@FXML
	void keyPressed(KeyEvent event) {
		if (!music.isPlaying())
			return;

		KeyCode code = event.getCode();
		int codeOrdinal = code.ordinal();

		Note note = null;

		if ((track_1Code != null && code == track_1Code) || (flickTracks[0] && code == flick_1Code))
			note = hitTracks[0];
		else if ((track_2Code != null && code == track_2Code) || (flickTracks[1] && code == flick_2Code))
			note = hitTracks[1];
		else if ((track_3Code != null && code == track_3Code) || (flickTracks[2] && code == flick_3Code))
			note = hitTracks[2];
		else if ((track_4Code != null && code == track_4Code) || (flickTracks[3] && code == flick_4Code))
			note = hitTracks[3];

		if (note == null)
			return;

		Types noteType = note.getType();
		if (noteType == Types.LONG_MIDDLE) {
			if (currentlyInUseKeys.get(codeOrdinal))
				scorePerTrack[note.getTrack()] = Scores.NEUTRAL;

			return;
		}

		if (currentlyInUseKeys.get(codeOrdinal))
			return;

		currentlyInUseKeys.set(codeOrdinal, true);

		if (noteType == Types.NORMAL)
			hitNote(note, true);
		else if (noteType == Types.LONG_START)
			hitNote(note, false);
		else if (noteType == Types.FLICK) {
			int noteTrack = note.getTrack();

			if (flickTracks[noteTrack]) {
				hitNote(note, true);
			}

			flickTracks[noteTrack] = !flickTracks[noteTrack];
		}
	}

	@FXML
	void keyReleased(KeyEvent event) {
		if (!music.isPlaying())
			return;

		event.consume();

		KeyCode code = event.getCode();
		int codeOrdinal = event.getCode().ordinal();

		currentlyInUseKeys.set(codeOrdinal, false);

		Note note = null;

		if (pauseCode != null && code == pauseCode)
			pauseMouseReleased(null);
		else if (track_1Code != null && code == track_1Code) {
			if (flickTracks[0])
				flickTracks[0] = false;

			note = hitTracks[0];
		} else if (track_2Code != null && code == track_2Code) {
			if (flickTracks[1])
				flickTracks[1] = false;

			note = hitTracks[1];
		} else if (track_3Code != null && code == track_3Code) {
			if (flickTracks[2])
				flickTracks[2] = false;

			note = hitTracks[2];
		} else if (track_4Code != null && code == track_4Code) {
			if (flickTracks[3])
				flickTracks[3] = false;

			note = hitTracks[3];
		}

		if (note == null || note.getType() != Types.LONG_END)
			return;

		hitNote(note, true);
	}

	private void makeNotes() {
		if (beatmapPointer == beatmap.size())
			return;

		List<Pair<Integer, Types>> notesData = beatmap.get(beatmapPointer);
		beatmapPointer++;

		if (notesData == null)
			return;

		for (Pair<Integer, Types> noteData : notesData) {
			Note note = new Note(noteData.getL(), noteData.getR());

			note.setCache(true);
			note.setCacheHint(CacheHint.SPEED);

			animateNote(note);

			if (AUTO_HIT) {
				Random random = new Random();

				if ((random.nextInt((100 - 1) + 1) + 1) <= AUTO_HIT_CHANCE) {
					Timeline autoHit = new Timeline(
							new KeyFrame(Duration.millis(AUTO_HIT_DELAY), e -> hitNote(note, true)));
					autoHit.statusProperty().addListener(new ChangeListener<Status>() {
						@Override
						public void changed(ObservableValue<? extends Status> observableValue, Status oldValue,
								Status newValue) {
							if (newValue == Status.STOPPED)
								autoHitEvents.remove(autoHit);
						}
					});

					autoHit.setCycleCount(1);
					autoHit.play();

					autoHitEvents.add(autoHit);
				}
			}
		}
	}

	private void animateNote(Note note) {
		SequentialTransition animation = new SequentialTransition();

		double upToPixel = note.getEndY() - GameStates.getInstance().getUserOptions().getNoteHeight();
		double upToTime = (upToPixel * GameStates.getInstance().getUserOptions().getNoteSpeed()) / note.getEndY();

		final Duration SEC_1 = Duration.millis(upToTime);
		Timeline timeline = new Timeline();

		KeyFrame end = new KeyFrame(SEC_1, new KeyValue(note.yProperty(), upToPixel));
		timeline.getKeyFrames().add(end);
		timeline.setOnFinished(e -> hitTracks[note.getTrack()] = note);

		final Duration SEC_2 = Duration.millis(GameStates.getInstance().getUserOptions().getNoteSpeed() - upToTime);
		Timeline timeline_2 = new Timeline();

		KeyFrame end_2 = new KeyFrame(SEC_2, new KeyValue(note.yProperty(), note.getEndY()));
		timeline_2.getKeyFrames().add(end_2);

		timeline_2.statusProperty().addListener(new ChangeListener<Status>() {
			@Override
			public void changed(ObservableValue<? extends Status> observableValue, Status oldValue, Status newValue) {
				if (newValue == Status.STOPPED)
					removeNote(note);
			}
		});

		animation.getChildren().add(timeline);
		animation.getChildren().add(timeline_2);
		animation.play();

		noteAnimations.put(note, animation);
	}

	private void removeNote(Note note) {
		int track = note.getTrack();
		Scores gainScoreType = scorePerTrack[track];

		UIStates.getInstance().getRoot().getChildren().remove(note);

		if (gainScoreType == Scores.MISS || gainScoreType == Scores.BAD) {
			double baseHealthLost = baseHealthLoss;

			scoreMultiplier = 1f;
			followedMultiplierHits = 0;

			updateCombo(followedMultiplierHits);

			switch (gainScoreType) {
			case MISS:
				missMultiplier += 0.10;
				break;
			case BAD:
				baseHealthLost /= 2;
				break;
			default:
				break;
			}

			double healthLost = baseHealthLost * missMultiplier;
			updateHealthBar(healthLost);
		} else {
			int score = Integer.parseInt(scoreText.getText());

			missMultiplier = 1;
			followedMultiplierHits++;

			if (followedMultiplierHits % 15 == 0)
				scoreMultiplier += 0.05f;

			updateCombo(followedMultiplierHits);
			updateScoreBar(score, maxPossibleScore);
		}

		removedNotes++;
		hitGradeCountMap.put(gainScoreType, hitGradeCountMap.get(gainScoreType) + 1);

		if (gainScoreType == Scores.MISS)
			UIUtils.showAnimatedText(hitScoreGradeText, "MISS!", 400);

		hitTracks[track] = null;
		flickTracks[track] = false;
		scorePerTrack[track] = Scores.MISS;
	}

	private void hitNote(Note note, boolean endAnimation) {
		Scores hitScoreType = getNodeHitScore(note);

		if (hitScoreType == null)
			return;

		int hitScoreValue = (int) (hitScoreType.getValue() * scoreMultiplier);

		String gain = "+ " + Integer.toString(hitScoreValue);
		String hitGradeString = "";

		switch (hitScoreType) {
		case PERFECT:
			hitGradeString = "PERFECT!";
			break;
		case GREAT:
			hitGradeString = "GREAT!";
			break;
		case GOOD:
			hitGradeString = "GOOD!";
			break;
		case BAD:
			hitGradeString = "BAD!";
			break;
		default:
			break;
		}

		totalHits++;
		scorePerTrack[note.getTrack()] = hitScoreType;

		switch (note.getType()) {
		case NORMAL:
		case LONG_START:
		case LONG_END:
			GameUtils.playHitSound();

			break;
		case FLICK:
			GameUtils.playFlickSound();

			break;
		default:
			break;
		}

		updateScore(hitScoreValue);
		UIUtils.showAnimatedText(gainText, gain, 200);

		UIUtils.showAnimatedText(hitScoreGradeText, hitGradeString, 400);
		UIUtils.showRipple(note.getX() + (GameUtils.NOTE_WIDTH / 2), note.getEndY(), note.getFill());

		if (endAnimation) {
			SequentialTransition noteAnimation = noteAnimations.get(note);

			if (noteAnimation == null)
				return;

			noteAnimation.stop();
			noteAnimations.remove(note);
		}
	}

	private Scores getNodeHitScore(Note note) {
		Scores score = null;

		double distancePerGrade = GameStates.getInstance().getUserOptions().getNoteHeight() / 8;
		double noteMiddle = note.getY() + (GameStates.getInstance().getUserOptions().getNoteHeight() / 2);

		double hitDistance = Math.abs(note.getEndY() - noteMiddle);
		if (hitDistance <= distancePerGrade)
			score = Scores.PERFECT;
		else if (hitDistance <= distancePerGrade * 2)
			score = Scores.GREAT;
		else if (hitDistance <= distancePerGrade * 3)
			score = Scores.GOOD;
		else if (hitDistance <= distancePerGrade * 4)
			score = Scores.BAD;

		return score;
	}

	private void updateScore(int gain) {
		String oldScoreString = scoreText.getText();

		int score = Integer.parseInt(oldScoreString);
		int newScore = score + gain;

		UIUtils.setPaddedText(scoreText, scorePaddingText, paddingInitialAmount, newScore);
	}

	private void updateScoreBar(int score, int total) {
		if (score <= total * 0.30) {
			if (scoreGrade != Grades.C) {
				scoreBar.setFill(Color.rgb(159, 197, 248));

				scoreGrade = Grades.C;
			}
		} else if (score <= total * 0.60) {
			if (scoreGrade != Grades.B) {
				scoreBar.setFill(Color.rgb(139, 177, 228));

				scoreGrade = Grades.B;
			}
		} else if (score <= total * 0.80) {
			if (scoreGrade != Grades.A) {
				scoreBar.setFill(Color.rgb(119, 157, 208));

				scoreGrade = Grades.A;
			}
		} else if (score <= total * 0.90) {
			if (scoreGrade != Grades.S) {
				scoreBar.setFill(Color.rgb(99, 137, 188));

				scoreGrade = Grades.S;
			}
		} else {
			if (scoreGrade != Grades.SS) {
				scoreBar.setFill(Color.rgb(79, 117, 168));

				scoreGrade = Grades.SS;
			}
		}

		scoreBar.setWidth(scoreBarTotalWidth * (((double) score) / total));
	}

	private void updateCombo(int combo) {
		if (combo <= 0) {
			comboLabel.setVisible(false);
			comboCountLabel.setVisible(false);
		} else if (!comboCountLabel.isVisible() || !comboLabel.isVisible()) {
			comboLabel.setVisible(true);
			comboCountLabel.setVisible(true);
		}

		comboLength = combo;
		if (comboLength > maxCombo)
			maxCombo = comboLength;

		comboCountLabel.setText(Integer.toString(combo));
		new Pulse(comboCountLabel).play();
	}

	private void updateHealthBar(double value) {
		double health = healthBar.getProgress();

		health += value;

		if (health <= 0) {
			health = 0;

			finishGame(false);
		} else if (health <= 0.25) {
			healthText.setFill(Color.RED);
			healthBar.setStyle("-fx-accent: red;");
		} else if (health <= 0.65) {
			healthText.setFill(Color.rgb(234, 234, 0));
			healthBar.setStyle("-fx-accent: yellow;");
		} else {
			healthText.setFill(Color.rgb(0, 126, 0));
			healthBar.setStyle("-fx-accent: green;");
		}

		healthText.setText(Integer.toString((int) (health * 100)));
		healthBar.setProgress(health);
	}

	void resumeGame() {
		startTime = GameUtils.GAME_START_COUNTDOWN;

		final Timeline startTimeline = new Timeline(new KeyFrame(Duration.ZERO, event -> {
			String text = startTime == 0 ? "START!" : Integer.toString(startTime);
			startTime--;

			UIUtils.showAnimatedText(countdownText, text, 1000);
		}), new KeyFrame(Duration.seconds(1)));
		startTimeline.setOnFinished(event -> {
			if (pauseTime > 0) {
				music.playAudioFromMicrosecondPosition(pauseTime);

				audioFinishTimeline.play();
				noteCreationTimeline.play();

				for (Map.Entry<Note, SequentialTransition> entry : noteAnimations.entrySet()) {
					entry.getValue().play();
				}

				for (Timeline autoHit : autoHitEvents) {
					autoHit.play();
				}
			} else {
				double delay = ((650 - (GameStates.getInstance().getUserOptions().getNoteHeight() / 2))
						* GameStates.getInstance().getUserOptions().getNoteSpeed()) / 650;

				noteCreationTimeline = new Timeline(new KeyFrame(Duration.ZERO, e -> makeNotes()),
						new KeyFrame(Duration.seconds(interval)));
				noteCreationTimeline.setCycleCount(beatmap.size());
				noteCreationTimeline.play();

				Timeline audioDelayTimeline = new Timeline(
						new KeyFrame(Duration.millis(delay), e -> music.playAudio()));
				audioDelayTimeline.play();

				double audioDuration = 1000 * AudioUtils.getAudioDuration(music.getAudio());
				audioFinishTimeline = new Timeline(
						new KeyFrame(Duration.millis(delay + audioDuration), e -> finishGame(true)));
				audioFinishTimeline.play();
			}

		});
		startTimeline.setCycleCount(startTime + 1);
		startTimeline.play();
	}

	void finishGame(boolean clear) {
		int score = Integer.parseInt(scoreText.getText());

		if (scoreGrade == null)
			scoreGrade = Grades.C;

		music.stopAudio();

		audioFinishTimeline.stop();
		noteCreationTimeline.stop();

		for (Map.Entry<Note, SequentialTransition> entry : noteAnimations.entrySet())
			entry.getValue().stop();

		if (removedNotes > 0)
			GameUtils.registerNewAccuracy((double) totalHits / removedNotes);

		GameUtils.showResultView(clear, scoreGrade, score, maxCombo, hitGradeCountMap);
	}

	@Override
	public void init() {
		resumeGame();

		music = GameStates.getInstance().getGameMusic();
		beatmap = music.getBeatmaps().get(GameStates.getInstance().getGameMode());
		interval = 1024 / music.getAudio().getFormat().getSampleRate();

		track_1Code = GameStates.getInstance().getUserOptions().getShortcuts().get("Track 1");
		track_2Code = GameStates.getInstance().getUserOptions().getShortcuts().get("Track 2");
		track_3Code = GameStates.getInstance().getUserOptions().getShortcuts().get("Track 3");
		track_4Code = GameStates.getInstance().getUserOptions().getShortcuts().get("Track 4");

		flick_1Code = GameStates.getInstance().getUserOptions().getShortcuts().get("Flick 1");
		flick_2Code = GameStates.getInstance().getUserOptions().getShortcuts().get("Flick 2");
		flick_3Code = GameStates.getInstance().getUserOptions().getShortcuts().get("Flick 3");
		flick_4Code = GameStates.getInstance().getUserOptions().getShortcuts().get("Flick 4");

		pauseCode = GameStates.getInstance().getUserOptions().getShortcuts().get("Back");

		scoreBar.setFill(Color.TRANSPARENT);
		scoreBarTotalWidth = scoreBar.getWidth();
		paddingInitialAmount = scorePaddingText.getText().length();

		missMultiplier = 1;
		scoreMultiplier = 1;

		int totalNotes = beatmap.getData().stream().filter(Objects::nonNull).mapToInt(List::size).sum();

		double maxPossibleScoreMultiplier = 1;
		for (int i = 1; i <= totalNotes / 15; i++) {
			maxPossibleScore += 15 * Scores.PERFECT.getValue() * maxPossibleScoreMultiplier;
			maxPossibleScoreMultiplier += 0.05;
		}
		maxPossibleScore += (totalNotes - (15 * (totalNotes / 15))) * Scores.PERFECT.getValue()
				* maxPossibleScoreMultiplier;
		
		baseHealthLoss = -(2.0 / totalNotes);

		hitGradeCountMap.put(Scores.PERFECT, 0);
		hitGradeCountMap.put(Scores.GREAT, 0);
		hitGradeCountMap.put(Scores.GOOD, 0);
		hitGradeCountMap.put(Scores.BAD, 0);
		hitGradeCountMap.put(Scores.MISS, 0);
	}
}
