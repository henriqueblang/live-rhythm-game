package controllers;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.controlsfx.control.RangeSlider;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import states.GameStates;
import states.UIStates;
import utils.AudioUtils;
import utils.UIUtils;

public class AddMusicController implements Controller {

	private static final double BLUR_AMOUNT = 10;
	private static final Effect blurEffect = new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, 5);
	
	private float audioFPS;
	
	private Clip audioClip;
	private String audioTitle;
	
	private InputStream audioRawStream;
	private AudioInputStream audioStream;
	
	private InputStream thumbnailStream;

	private FileChooser musicFileChooser;
	private FileChooser imageFileChooser;

	private RangeSlider slider;

	@FXML
	private ImageView musicIcon;

	@FXML
	private ImageView thumbnailIcon;

	@FXML
	private ImageView previewIcon;

	@FXML
	private Pane backgroundPane;
	
	@FXML
    private Button musicButton;
	
	@FXML
	private Button thumbnailButton;

	@FXML
	private Button backButton;

	@FXML
	private HBox previewHBox;

	@FXML
	private Label musicLabel;

	@FXML
	private Label thumbnailLabel;
	
	private void generateMusicData(File file) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		audioRawStream = new FileInputStream(file);
		
		AudioInputStream ais = AudioUtils.getSoundAudioInputStream(file);
		
		AudioFormat baseFormat = ais.getFormat();
		AudioFormat decodedFormat = new AudioFormat(
			AudioFormat.Encoding.PCM_SIGNED,
			baseFormat.getSampleRate(), 
			16, 
			baseFormat.getChannels(), 
			baseFormat.getChannels() * 2,
			baseFormat.getSampleRate(), 
			false
		);
			
		byte[] audioArray = IOUtils.toByteArray(ais);
		
		ByteArrayInputStream baisClip = new ByteArrayInputStream(audioArray);
		audioClip = AudioSystem.getClip();
		audioClip.open(new AudioInputStream(baisClip, decodedFormat, audioArray.length / decodedFormat.getFrameSize()));
		
		ByteArrayInputStream baisAudioStream = new ByteArrayInputStream(audioArray);
		audioStream = new AudioInputStream(baisAudioStream, decodedFormat, audioArray.length / decodedFormat.getFrameSize());
	}

	@FXML
	void backMouseReleased(MouseEvent event) {
		if(UIStates.getInstance().getExtraPanes() > 0)
			return;
		else if(audioClip != null)
			audioClip.close();
		
		UIUtils.playBackSound();
		UIUtils.changeView("MenuScreen.fxml");
	}

	@FXML
	void musicMouseEntered(MouseEvent event) {
		if(UIStates.getInstance().getExtraPanes() > 0)
			return;
		
		musicIcon.setVisible(true);
		musicButton.setStyle("-fx-background-color: rgba(255, 71, 131, 0.15);");
	}

	@FXML
	void musicMouseExited(MouseEvent event) {
		if(UIStates.getInstance().getExtraPanes() > 0)
			return;
		
		musicIcon.setVisible(false);
		musicButton.setStyle("-fx-background-color: transparent;");
	}

	@FXML
	void musicMouseReleased(MouseEvent event) throws UnsupportedAudioFileException {
		if(UIStates.getInstance().getExtraPanes() > 0)
			return;
		
		File selectedMusic = musicFileChooser.showOpenDialog(UIStates.getInstance().getPrimaryStage());
		
		if(audioClip != null && audioClip.isRunning()) {
			previewIcon.setImage(UIStates.getInstance().getPlayImage());
			
			audioClip.stop();
		}

		if (selectedMusic == null)
			return;
		
		audioTitle = selectedMusic.getName();
		
		try {
			generateMusicData(selectedMusic);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			UIUtils.showError(e.getMessage());
		}
		
		audioFPS = audioClip.getFormat().getFrameRate();

		musicLabel.setText(audioTitle);
		
		if(!previewIcon.isVisible())
			previewIcon.setVisible(true);

		// TODO check if method AudioUtils::getAudioDuration is usable here
		int duration = (int) (audioClip.getFrameLength() / audioFPS);
		if (slider != null) {
			slider.adjustHighValue(duration);

			slider.setLowValue(0);
			slider.setLowValueChanging(true);

			slider.setMax(duration);
			slider.setHighValue(duration);
			slider.setHighValueChanging(true);
		} else {
			Text textLowValue = new Text();
			Text textHighValue = new Text();
			
			textLowValue.setFont(Font.font("Century Gothic"));
			textHighValue.setFont(Font.font("Century Gothic"));

			slider = new RangeSlider(0, duration, 0, duration) {
				@Override
				protected void layoutChildren() {
					super.layoutChildren();

					double yPosi = 0;

					Pane lowThumb = (Pane) lookup(".range-slider .low-thumb");
					Pane highThumb = (Pane) lookup(".range-slider .high-thumb");
					Pane layoutReference = lowThumb == null ? highThumb : lowThumb;

					if (layoutReference != null)
						yPosi = layoutReference.getLayoutY() + 40;

					if (lowThumb != null) {
						textLowValue.setLayoutX(lowThumb.getLayoutX() + lowThumb.getWidth() / 2
								- textLowValue.getLayoutBounds().getWidth() / 2);

						if (textLowValue.getLayoutY() != yPosi)
							textLowValue.setLayoutY(yPosi);
					}

					if (highThumb != null) {
						textHighValue.setLayoutX(highThumb.getLayoutX() + highThumb.getWidth() / 2
								- textHighValue.getLayoutBounds().getWidth() / 2);

						if (textHighValue.getLayoutY() != yPosi)
							textHighValue.setLayoutY(yPosi);
					}
				}
			};
			UIUtils.addStylesheet("rangeslider.css");
			slider.getStyleClass().add("slider");

			StringConverter<Number> stringConverter = new StringConverter<Number>() {
				@Override
				public String toString(Number object) {
					long seconds = object.longValue();
					long minutes = TimeUnit.SECONDS.toMinutes(seconds);
					long remainingseconds = seconds - TimeUnit.MINUTES.toSeconds(minutes);
					return String.format("%02d", minutes) + ":" + String.format("%02d", remainingseconds);
				}

				@Override
				public Double fromString(String string) {
					return null;
				}
			};

			slider.setLabelFormatter(stringConverter);

			textLowValue.setText(stringConverter.toString(0));
			textHighValue.setText(stringConverter.toString(duration));

			slider.lowValueProperty().addListener((observable, oldValue, newValue) ->
				textLowValue.setText(stringConverter.toString(newValue.doubleValue()))
			);
			slider.highValueProperty().addListener((observable, oldValue, newValue) -> 
				textHighValue.setText(stringConverter.toString(newValue.doubleValue()))
			);

			previewHBox.getChildren().addAll(slider, textLowValue, textHighValue);
		}
	}

	@FXML
	void thumbnailMouseEntered(MouseEvent event) {
		if(UIStates.getInstance().getExtraPanes() > 0)
			return;
		
		thumbnailIcon.setVisible(true);
		thumbnailButton.setStyle("-fx-background-color: rgba(255, 71, 131, 0.15);");
	}

	@FXML
	void thumbnailMouseExited(MouseEvent event) {
		if(UIStates.getInstance().getExtraPanes() > 0)
			return;
		
		thumbnailIcon.setVisible(false);
		thumbnailButton.setStyle("-fx-background-color: transparent;");
	}

	@FXML
	void thumbnailMouseReleased(MouseEvent event) {
		if(UIStates.getInstance().getExtraPanes() > 0)
			return;
		
		File selectedThumbnail = imageFileChooser.showOpenDialog(UIStates.getInstance().getPrimaryStage());

		if (selectedThumbnail == null)
			return;
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(selectedThumbnail);
		} catch (IOException e) {
			UIUtils.showError(e.getMessage());
		}
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, FilenameUtils.getExtension(selectedThumbnail.getName()), os);
		} catch (IOException e) {
			UIUtils.showError(e.getMessage());
		}
		thumbnailStream = new ByteArrayInputStream(os.toByteArray());

		thumbnailLabel.setText(selectedThumbnail.getName());
	}

	@FXML
	void previewMouseReleased(MouseEvent event) {
		if(UIStates.getInstance().getExtraPanes() > 0)
			return;
		
		if(audioClip != null && audioClip.isRunning()) {
			previewIcon.setImage(UIStates.getInstance().getPlayImage());
			
			audioClip.stop();
		}
		else {
			previewIcon.setImage(UIStates.getInstance().getStopImage());
			
			double lowValue = slider.getLowValue();
			double highValue = slider.getHighValue();

			int previewStart = (int) (lowValue * audioFPS); 
			int previewEnd = (int) (highValue * audioFPS);
			
			audioClip.setFramePosition(previewStart);
			audioClip.setLoopPoints(previewStart, previewEnd);
			audioClip.loop(Clip.LOOP_CONTINUOUSLY);
		}	
	}
	
	@FXML
	void okMouseReleased(MouseEvent event) {
		if(UIStates.getInstance().getExtraPanes() > 0)
			return;
		
		if(audioStream == null) {
			UIUtils.showError("Select a music file.");
			
			return;
		}
		
		double lowValue = slider.getLowValue();
		double highValue = slider.getHighValue();

		int previewStart = (int) (lowValue * audioFPS); 
		int previewEnd = (int) (highValue * audioFPS);
		
		if(audioClip.isRunning())
			audioClip.stop();
		
		thumbnailStream = new BufferedInputStream(thumbnailStream);
		thumbnailStream.mark(Integer.MAX_VALUE);
		
		audioRawStream = new BufferedInputStream(audioRawStream);
		audioRawStream.mark(Integer.MAX_VALUE);
		
		AudioUtils.saveMusic(audioTitle, audioClip, previewStart, previewEnd, audioRawStream, audioStream, thumbnailStream);
		
		UIUtils.playEnterSound();
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
		
		if(okCode != null && code == okCode)
			okMouseReleased(null);
		else if(backCode != null && code == backCode)
			backMouseReleased(null);
    }

	@Override
	public void init() {
		backgroundPane.setBackground(UIUtils.getRandomBackground());
		backgroundPane.setEffect(blurEffect);
		
		musicFileChooser = new FileChooser();
		FileChooser.ExtensionFilter mp3Filter = new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
		musicFileChooser.getExtensionFilters().add(mp3Filter);

		imageFileChooser = new FileChooser();
		FileChooser.ExtensionFilter imgFilter = new FileChooser.ExtensionFilter("Image files (*.png, *.jpg)", "*.png",
				"*.jpg");
		imageFileChooser.getExtensionFilters().add(imgFilter);
		
		try {
			thumbnailStream = new FileInputStream(UIStates.getInstance().getThumbnailDefaultFile());
		} catch (FileNotFoundException e) {
			UIUtils.showError(e.getMessage());
		}
	}
}