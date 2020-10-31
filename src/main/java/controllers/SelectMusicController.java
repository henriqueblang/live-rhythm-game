package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import dao.MusicDAO;
import entity.Music;
import entity.ui.Marquee;
import entity.ui.MusicButton;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import states.GameStates;
import states.UIStates;
import utils.GameUtils;
import utils.UIUtils;

public class SelectMusicController implements Controller {

	private final double BLUR_AMOUNT = 10;
	private final Effect blurEffect = new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, 5);

	private final double TITLE_WIDTH_PERCENTAGE = 0.80;
	
	private double THUMBNAIL_WIDTH;
	private double THUMBNAIL_HEIGHT;

	private Button selectedModeButton;	
	private MusicButton selectedMusicButton;
	
	@FXML
	private Pane backgroundPane;
	
	@FXML
    private AnchorPane selectedPane;

    @FXML
    private AnchorPane notSelectedPane;

	@FXML
	private ScrollPane scrollPane;
	
	@FXML
	private VBox musicsVBox;

	@FXML
    private Button easyButton;
	
	@FXML
    private Button normalButton;

	@FXML
    private Button hardButton;
	
	@FXML
    private ImageView thumbnailImageView;

	@FXML
	private Text scoreText;

	@FXML
	void modeMouseReleased(MouseEvent event) {
		Button button = (Button) event.getSource();
		
		if(button == selectedModeButton || UIStates.getInstance().getExtraPanes() > 0)
			return;
		
		changeMode(button);
	}
	
	@FXML
	void confirmMouseReleased(MouseEvent event) {
		if(selectedMusicButton == null || UIStates.getInstance().getExtraPanes() > 0)
			return;
		
		Music music = selectedMusicButton.getMusic();
		
		music.stopAudio();
		
		int modeIndex = -1;
		if(selectedModeButton == easyButton)
			modeIndex = 0;
		else if(selectedModeButton == normalButton)
			modeIndex = 1;
		else if(selectedModeButton == hardButton)
			modeIndex = 2;
		
		UIUtils.playEnterSound();
		GameUtils.startGame(music, modeIndex);
	}

	@FXML
	void backMouseReleased(MouseEvent event) {
		if(UIStates.getInstance().getExtraPanes() > 0)
			return;
		
		if(selectedMusicButton != null) {
			Music music = selectedMusicButton.getMusic();
			
			music.stopAudio();
		}
		
		UIUtils.playBackSound();
		UIUtils.changeView("MenuScreen.fxml");
	}
	
	@FXML
    void keyReleased(KeyEvent event) {
		if(UIStates.getInstance().getExtraPanes() > 0)
			return;
		
		event.consume();
		
		KeyCode code = event.getCode();
		
		KeyCode okCode = GameStates.getInstance().getUserOptions().getShortcuts().get("Confirm");
		KeyCode backCode = GameStates.getInstance().getUserOptions().getShortcuts().get("Back");
		
		KeyCode modeCode = GameStates.getInstance().getUserOptions().getShortcuts().get("Change mode");
		KeyCode nextUpCode = GameStates.getInstance().getUserOptions().getShortcuts().get("Previous");
		KeyCode nextDownCode = GameStates.getInstance().getUserOptions().getShortcuts().get("Next");
		
		if(okCode != null && code == okCode)
			confirmMouseReleased(null);
		else if(backCode != null && code == backCode)
			backMouseReleased(null);
		else if(modeCode != null && code == modeCode) {
			Button nextModeButton = null;
			if(selectedModeButton == easyButton)
				nextModeButton = normalButton;
			else if(selectedModeButton == normalButton)
				nextModeButton = hardButton;
			else if(selectedModeButton == hardButton)
				nextModeButton = easyButton;
			
			changeMode(nextModeButton);
		}
		else if((nextUpCode != null && code == nextUpCode) || (nextDownCode != null && code == nextDownCode)) {
			MusicButton nextMusicButton;
			
			List<Node> musicButtons = musicsVBox.getChildrenUnmodifiable();
			
			if(musicButtons.isEmpty())
				return;
			
			int nextMusicIndex; 
			int nextIndexDirection = code == nextDownCode ? 1 : -1;
			int selectedMusicIndex = selectedMusicButton == null ? -1 : musicButtons.indexOf(selectedMusicButton);
				
			nextMusicIndex = selectedMusicIndex + nextIndexDirection;
			if(nextMusicIndex < 0)
				nextMusicIndex = musicButtons.size() - 1;
			else if(nextMusicIndex == musicButtons.size())
				nextMusicIndex = 0;
			
			nextMusicButton = (MusicButton) musicButtons.get(nextMusicIndex);
			
			if(selectedMusicButton == nextMusicButton)
				return;
			
			selectMusic(nextMusicButton);
		}
	}

	private void favoriteMouseReleased(MouseEvent event) {
		if(UIStates.getInstance().getExtraPanes() > 0)
			return;
		
		Button favoriteButton = (Button) event.getSource();
		HBox musicHBox = (HBox) favoriteButton.getParent();
		MusicButton button = (MusicButton) musicHBox.getParent();
		Music music = button.getMusic();
		
		if(selectedMusicButton != button) {
			// 1st element is default button style.
			// Being a list, new elements will be
			// pushed to the last index.
			// I guess this is kind of safe?
			button.getStyleClass().remove(1);
			
			button.getStyleClass().add(music.isFavorite() ? "normal" : "favorite");
		}
		
		ImageView favoriteStatusImageView = (ImageView) favoriteButton.getGraphic();
		favoriteStatusImageView.setImage(music.isFavorite() ? UIStates.getInstance().getUnfavoriteImage() : UIStates.getInstance().getFavoriteImage());
		
		music.setFavorite(!music.isFavorite());
		
		GameStates.getInstance().sortLibrary();
		
		int musicNewIndex = GameStates.getInstance().getLibrary().indexOf(music);
		musicsVBox.getChildren().remove(button);
		musicsVBox.getChildren().add(musicNewIndex, button);
		
		Thread processing = new Thread() {
			@Override
			public void run() {
				MusicDAO dao = new MusicDAO();
				try {
					dao.updateFavorite(music.getId(), music.isFavorite());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};

		processing.start();
	}

	private void removeMouseReleased(MouseEvent event) {
		if(UIStates.getInstance().getExtraPanes() > 0)
			return;
		
		Button removeButton = (Button) event.getSource();
		HBox musicHBox = (HBox) removeButton.getParent();
		MusicButton button = (MusicButton) musicHBox.getParent();
		Music music = button.getMusic();
		
		if(selectedMusicButton == button) {
			selectedMusicButton = null;
			
			selectedPane.setVisible(false);
			notSelectedPane.setVisible(true);
			
			music.stopAudio();
			
			UIUtils.playBackSound();
		}

		musicsVBox.getChildren().remove(button);
		GameStates.getInstance().getLibrary().remove(music);
		
		Thread processing = new Thread() {
			@Override
			public void run() {
				MusicDAO dao = new MusicDAO();
				try {
					dao.deleteMusic(music.getId());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};

		processing.start();
	}

	private void musicMouseReleased(MouseEvent event) {
		MusicButton button = (MusicButton) event.getSource();
		
		selectMusic(button);
	}

	private void musicMouseEntered(MouseEvent event) {
		MusicButton button = (MusicButton) event.getSource();
		
		runMarquee(button);
	}

	private void musicMouseExited(MouseEvent event) {
		MusicButton button = (MusicButton) event.getSource();
		
		stopMarquee(button);
	}
	
	private void changeMode(Button modeButton) {
		if(selectedMusicButton == null || modeButton == null || UIStates.getInstance().getExtraPanes() > 0)
			return;
		
		Music music = selectedMusicButton.getMusic();
		
		int modeIndex = -1;
		Image newModeOnImage = null;
		if(modeButton == easyButton) {
			modeIndex = 0;
			newModeOnImage = UIStates.getInstance().getEasySelectedImage();
		}
		else if(modeButton == normalButton) {
			modeIndex = 1;
			newModeOnImage = UIStates.getInstance().getNormalSelectedImage();
		}
		else if(modeButton == hardButton) {
			modeIndex = 2;
			newModeOnImage = UIStates.getInstance().getHardSelectedImage();
		}
		
		if(newModeOnImage == null)
			return;
		
		Image oldModeOffImage = null;
		if(selectedModeButton == easyButton)
			oldModeOffImage = UIStates.getInstance().getEasyNotSelectedImage();
		else if(selectedModeButton == normalButton)
			oldModeOffImage = UIStates.getInstance().getNormalNotSelectedImage();
		else if(selectedModeButton == hardButton)
			oldModeOffImage = UIStates.getInstance().getHardNotSelectedImage();
		
		ImageView newModeImageView = (ImageView) modeButton.getGraphic();
		ImageView oldModeImageView = (ImageView) selectedModeButton.getGraphic();
		
		newModeImageView.setImage(newModeOnImage);
		oldModeImageView.setImage(oldModeOffImage);
		
		selectedModeButton = modeButton;
		scoreText.setText(NumberFormat.getNumberInstance(Locale.US).format(music.getHighscore(modeIndex)));
	}
	
	private void selectMusic(MusicButton musicButton) {
		if(musicButton == null || UIStates.getInstance().getExtraPanes() > 0)
			return;
		
		Music music = musicButton.getMusic();
		
		String css = musicButton != selectedMusicButton ? "selected" : (music.isFavorite() ? "favorite" : "normal");
		// 1st element is default button style.
		// Being a list, new elements will be
		// pushed to the last index.
		// I guess this is kind of safe?
		musicButton.getStyleClass().remove(1);
		musicButton.getStyleClass().add(css);

		if (musicButton == selectedMusicButton) {
			selectedMusicButton = null;
			
			selectedPane.setVisible(false);
			notSelectedPane.setVisible(true);
			
			music.stopAudio();
			
			UIUtils.playBackSound();
		} else {
			if (selectedMusicButton != null) {
				// 1st element is default button style.
				// Being a list, new elements will be
				// pushed to the last index.
				// I guess this is kind of safe?
				selectedMusicButton.getStyleClass().remove(1);
				selectedMusicButton.getStyleClass().add(selectedMusicButton.getMusic().isFavorite() ? "favorite" : "normal");
				
				selectedMusicButton.getMusic().stopAudio();
			}

			selectedMusicButton = musicButton;
			
			selectedPane.setVisible(true);
			notSelectedPane.setVisible(false);
			
			int modeIndex = -1;
			if(selectedModeButton == easyButton)
				modeIndex = 0;
			else if(selectedModeButton == normalButton)
				modeIndex = 1;
			else if(selectedModeButton == hardButton)
				modeIndex = 2;
	
			thumbnailImageView.setImage(music.getThumbnail());
			thumbnailImageView.setFitWidth(THUMBNAIL_WIDTH);
			thumbnailImageView.setFitHeight(THUMBNAIL_HEIGHT);
			
			scoreText.setText(NumberFormat.getNumberInstance(Locale.US).format(music.getHighscore(modeIndex)));
			
			music.playPreview();
			UIUtils.playEnterSound();
		}
	}
	
	private void runMarquee(MusicButton musicButton) {
		if(musicButton == null)
			return;
		
		Marquee marquee = musicButton.getMarquee();

		if (marquee.getBoundsPane().getAlignment() == Pos.CENTER)
			return;

		marquee.run();
	}
	
	private void stopMarquee(MusicButton musicButton) {
		if(musicButton == null)
			return;
		
		Marquee marquee = musicButton.getMarquee();
		
		if (marquee.getBoundsPane().getAlignment() == Pos.CENTER)
			return;

		marquee.stop();
	}

	private void fillMusicScrollPane() throws IOException {

		HBox musicHBox = null;

		StackPane titlePane = null;
		Marquee musicTitle = null;

		MusicButton musicButton = null;
		Button favoriteButton = null;
		Button removeButton = null;

		ImageView favoriteImageView = null;
		ImageView removeImageView = null;

		GameStates.getInstance().sortLibrary();

		for (Music music : GameStates.getInstance().getLibrary()) {
			titlePane = new StackPane();
			musicHBox = new HBox();
			musicTitle = new Marquee(music.getTitle());
			favoriteButton = new Button();
			removeButton = new Button();
			favoriteImageView = new ImageView(music.isFavorite() ? UIStates.getInstance().getFavoriteImage() : UIStates.getInstance().getUnfavoriteImage());
			removeImageView = new ImageView(UIStates.getInstance().getRemoveImage());

			musicButton = new MusicButton(music, musicTitle);

			double buttonWidth = scrollPane.getWidth() - scrollPane.getPadding().getLeft()
					- scrollPane.getPadding().getRight();
			double titleWidth = buttonWidth * TITLE_WIDTH_PERCENTAGE;

			musicButton.setPrefWidth(buttonWidth);

			String css = music.isFavorite() ? "favorite" : "normal";
			musicButton.getStyleClass().add(css);

			musicHBox.setAlignment(Pos.CENTER_LEFT);

			titlePane.setAlignment(Pos.CENTER);
			titlePane.setMinWidth(titleWidth);
			titlePane.setPrefWidth(titleWidth);
			titlePane.setMaxWidth(titleWidth);

			musicTitle.setFont(Font.font("Century Gothic", 25));
			musicTitle.setFill(Color.WHITE);
			musicTitle.setBoundsFrom(titlePane);
			musicTitle.setScrollDuration(15);

			titlePane.getChildren().add(musicTitle);
			titlePane.setClip(new Rectangle(titleWidth, 100));

			favoriteButton.setStyle("-fx-background-color: transparent");
			favoriteButton.setMinWidth(UIStates.getInstance().getFavoriteImage().getWidth());
			favoriteButton.setMinHeight(UIStates.getInstance().getFavoriteImage().getHeight());
			favoriteButton.setPrefWidth(UIStates.getInstance().getFavoriteImage().getWidth());
			favoriteButton.setPrefHeight(UIStates.getInstance().getFavoriteImage().getHeight());
			favoriteButton.setGraphic(favoriteImageView);
			favoriteButton.setOnMouseReleased(e -> favoriteMouseReleased(e));

			removeButton.setStyle("-fx-background-color: transparent");
			removeButton.setMinWidth(UIStates.getInstance().getRemoveImage().getWidth());
			removeButton.setMinHeight(UIStates.getInstance().getRemoveImage().getHeight());
			removeButton.setPrefWidth(UIStates.getInstance().getRemoveImage().getWidth());
			removeButton.setPrefHeight(UIStates.getInstance().getRemoveImage().getHeight());
			removeButton.setGraphic(removeImageView);
			removeButton.setOnMouseReleased(e -> removeMouseReleased(e));

			musicHBox.getChildren().addAll(titlePane, favoriteButton, removeButton);

			musicButton.setGraphic(musicHBox);
			musicButton.setOnMouseEntered(e -> musicMouseEntered(e));
			musicButton.setOnMouseExited(e -> musicMouseExited(e));
			musicButton.setOnMouseReleased(e -> musicMouseReleased(e));

			musicsVBox.getChildren().add(musicButton);

			if (musicTitle.getLayoutBounds().getWidth() > titleWidth)
				titlePane.setAlignment(Pos.CENTER_LEFT);
		}
	}
	
	@Override
	public void init() {
		backgroundPane.setBackground(UIUtils.getRandomBackground());
		backgroundPane.setEffect(blurEffect);

		scrollPane.setFitToWidth(true);

		try {
			fillMusicScrollPane();
		} catch (IOException e) {
			UIUtils.showError(e.getMessage());
		}
		
		selectedModeButton = easyButton;
		
		THUMBNAIL_WIDTH = thumbnailImageView.getFitWidth();
		THUMBNAIL_HEIGHT = thumbnailImageView.getFitHeight();
	}

}