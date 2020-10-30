package states;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class UIStates {
	
	private static UIStates instance;
	
	private int extraPanes;
	
	private Pane root;
	private Stage primaryStage;
	
	private File thumbnailDefaultFile;
	
	private Image playImage;
	private Image stopImage;
	
	private Image favoriteImage;
	private Image unfavoriteImage;
	private Image removeImage;
	
	private Image easySelectedImage;
	private Image easyNotSelectedImage;
	private Image normalSelectedImage;
	private Image normalNotSelectedImage;
	private Image hardSelectedImage;
	private Image hardNotSelectedImage;
	
	private Image pauseImage;
	private Image scoreBarImage;
	private Image energyBarImage;
	
	private Image gradeA;
	private Image gradeB;
	private Image gradeC;
	private Image gradeS;
	private Image gradeSS;
	
	private SimpleIntegerProperty newMusicNotifications = new SimpleIntegerProperty(0);
	
	private UIStates() {}
	
	public static UIStates getInstance() {
		if(instance == null)
			instance = new UIStates();
		
		return instance;
	}

	public int getExtraPanes() {
		return extraPanes;
	}

	public void setExtraPanes(int extraPanes) {
		this.extraPanes = extraPanes;
	}
	
	public void incrementExtraPanes() {
		extraPanes++;
	}
	
	public void decrementExtraPanes() {
		extraPanes--;
	}

	public Pane getRoot() {
		return root;
	}

	public void setRoot(Pane root) {
		this.root = root;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public File getThumbnailDefaultFile() {
		return thumbnailDefaultFile;
	}

	public void setThumbnailDefaultFile(File thumbnailDefaultFile) {
		this.thumbnailDefaultFile = thumbnailDefaultFile;
	}

	public Image getPlayImage() {
		return playImage;
	}

	public void setPlayImage(Image playImage) {
		this.playImage = playImage;
	}

	public Image getStopImage() {
		return stopImage;
	}

	public void setStopImage(Image stopImage) {
		this.stopImage = stopImage;
	}

	public Image getFavoriteImage() {
		return favoriteImage;
	}

	public void setFavoriteImage(Image favoriteImage) {
		this.favoriteImage = favoriteImage;
	}

	public Image getUnfavoriteImage() {
		return unfavoriteImage;
	}

	public void setUnfavoriteImage(Image unfavoriteImage) {
		this.unfavoriteImage = unfavoriteImage;
	}

	public Image getRemoveImage() {
		return removeImage;
	}

	public void setRemoveImage(Image removeImage) {
		this.removeImage = removeImage;
	}

	public Image getEasySelectedImage() {
		return easySelectedImage;
	}

	public void setEasySelectedImage(Image easySelectedImage) {
		this.easySelectedImage = easySelectedImage;
	}

	public Image getEasyNotSelectedImage() {
		return easyNotSelectedImage;
	}

	public void setEasyNotSelectedImage(Image easyNotSelectedImage) {
		this.easyNotSelectedImage = easyNotSelectedImage;
	}

	public Image getNormalSelectedImage() {
		return normalSelectedImage;
	}

	public void setNormalSelectedImage(Image normalSelectedImage) {
		this.normalSelectedImage = normalSelectedImage;
	}

	public Image getNormalNotSelectedImage() {
		return normalNotSelectedImage;
	}

	public void setNormalNotSelectedImage(Image normalNotSelectedImage) {
		this.normalNotSelectedImage = normalNotSelectedImage;
	}

	public Image getHardSelectedImage() {
		return hardSelectedImage;
	}

	public void setHardSelectedImage(Image hardSelectedImage) {
		this.hardSelectedImage = hardSelectedImage;
	}

	public Image getHardNotSelectedImage() {
		return hardNotSelectedImage;
	}

	public void setHardNotSelectedImage(Image hardNotSelectedImage) {
		this.hardNotSelectedImage = hardNotSelectedImage;
	}

	public Image getPauseImage() {
		return pauseImage;
	}

	public void setPauseImage(Image pauseImage) {
		this.pauseImage = pauseImage;
	}

	public Image getScoreBarImage() {
		return scoreBarImage;
	}

	public void setScoreBarImage(Image scoreBarImage) {
		this.scoreBarImage = scoreBarImage;
	}

	public Image getEnergyBarImage() {
		return energyBarImage;
	}

	public void setEnergyBarImage(Image energyBarImage) {
		this.energyBarImage = energyBarImage;
	}

	public Image getGradeA() {
		return gradeA;
	}

	public void setGradeA(Image gradeA) {
		this.gradeA = gradeA;
	}

	public Image getGradeB() {
		return gradeB;
	}

	public void setGradeB(Image gradeB) {
		this.gradeB = gradeB;
	}

	public Image getGradeC() {
		return gradeC;
	}

	public void setGradeC(Image gradeC) {
		this.gradeC = gradeC;
	}

	public Image getGradeS() {
		return gradeS;
	}

	public void setGradeS(Image gradeS) {
		this.gradeS = gradeS;
	}

	public Image getGradeSS() {
		return gradeSS;
	}

	public void setGradeSS(Image gradeSS) {
		this.gradeSS = gradeSS;
	}

	public SimpleIntegerProperty getNewMusicNotifications() {
		return newMusicNotifications;
	}

	public void setNewMusicNotifications(SimpleIntegerProperty newMusicNotifications) {
		this.newMusicNotifications = newMusicNotifications;
	}
}
