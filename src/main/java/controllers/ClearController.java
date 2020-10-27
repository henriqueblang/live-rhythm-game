package controllers;

import java.sql.SQLException;
import java.util.Map;

import dao.MusicDAO;
import entity.Music;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import states.GameStates;
import states.UIStates;
import utils.UIUtils;
import utils.EnumUtils.Grades;
import utils.EnumUtils.Scores;
import utils.GameUtils;

public class ClearController implements Controller {
	
	final private double PANE_WIDTH = 391;
	final private double PANE_HEIGHT = 402;
	
	private int scorePaddingInitialAmount;
	private int hitCountPaddingInitialAmount;
	
	@FXML
    private AnchorPane successPane;
	
	@FXML
    private ImageView gradeImageView;
	
	@FXML
    private Text scorePaddingText;

    @FXML
    private Text scoreText;
    
    @FXML
    private Text highscorePaddingText;
    
    @FXML
    private Text highscoreText;
    
    @FXML
    private Text perfectPaddingText;
    
    @FXML
    private Text perfectText;
    
    @FXML
    private Text greatPaddingText;
    
    @FXML
    private Text greatText;
    
    @FXML
    private Text goodPaddingText;
    
    @FXML
    private Text goodText;
    
    @FXML
    private Text badPaddingText;
	
    @FXML
    private Text badText;
	
	@FXML
    private Text missPaddingText;
	
	@FXML
    private Text missText;

    @FXML
    private Text maxComboPaddingText;
    
    @FXML
    private Text maxComboText;

    @FXML
    void okMouseReleased(MouseEvent event) {
    	UIStates.extraPanes--;
		
    	UIUtils.playEnterSound();
    	UIUtils.changeView("MenuScreen.fxml");
    }
    
    @FXML
    void keyReleased(KeyEvent event) {
    	KeyCode code = event.getCode();
		KeyCode okCode = GameStates.userOptions.getShortcuts().get("Confirm");
		
		if(okCode != null && code == okCode)
			okMouseReleased(null);
    }

	public void fillResults(Grades grade, int score, int maxCombo, Map<Scores, Integer> hitGradeCountMap) {
		int mode = GameStates.gameMode;
		Music music = GameStates.gameMusic;
		
		int highscore = music.getHighscore(mode);
		if(score > highscore) {
			highscore = score;
			
			GameUtils.playHighscoreSound();
			GameStates.gameMusic.setHighscore(mode, highscore);
			
			Thread thread = new Thread() {
				@Override
				public void run() {
					MusicDAO dao = new MusicDAO();
					try {
						dao.updateHighscore(music.getId(), mode, score);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			};
			
			thread.start();
		}
		
		Image gradeImage = null;
		switch(grade) {
		case C:
			gradeImage = UIStates.gradeC;
			break;
		case B:
			gradeImage = UIStates.gradeB;
			break;
		case A:
			gradeImage = UIStates.gradeA;
			break;
		case S:
			gradeImage = UIStates.gradeS;
			break;
		case SS:
			gradeImage = UIStates.gradeSS;
			break;
		}
		
		gradeImageView.setImage(gradeImage);
		
		UIUtils.setPaddedText(scoreText, scorePaddingText, scorePaddingInitialAmount, score);
		UIUtils.setPaddedText(highscoreText, highscorePaddingText, scorePaddingInitialAmount, highscore);
		
		UIUtils.setPaddedText(perfectText, perfectPaddingText, hitCountPaddingInitialAmount, hitGradeCountMap.get(Scores.PERFECT));
		UIUtils.setPaddedText(greatText, greatPaddingText, hitCountPaddingInitialAmount, hitGradeCountMap.get(Scores.GREAT));
		UIUtils.setPaddedText(goodText, goodPaddingText, hitCountPaddingInitialAmount, hitGradeCountMap.get(Scores.GOOD));
		UIUtils.setPaddedText(badText, badPaddingText, hitCountPaddingInitialAmount, hitGradeCountMap.get(Scores.BAD));
		UIUtils.setPaddedText(missText, missPaddingText, hitCountPaddingInitialAmount, hitGradeCountMap.get(Scores.MISS));
		
		UIUtils.setPaddedText(maxComboText, maxComboPaddingText, hitCountPaddingInitialAmount, maxCombo);
	}
	
	@Override
	public void init() {
		UIStates.extraPanes++;
		
		successPane.setLayoutX((UIStates.root.getWidth() / 2) - (PANE_WIDTH / 2));
		successPane.setLayoutY((UIStates.root.getHeight() / 2) - (PANE_HEIGHT / 2));
		
		scorePaddingInitialAmount = scorePaddingText.getText().length();
		hitCountPaddingInitialAmount = maxComboPaddingText.getText().length();
		
		UIStates.root.getScene().setOnKeyReleased(e -> keyReleased(e));
	}
}
