package states;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class UIStates {
	public static int extraPanes;
	
	public static Stage primaryStage;
	public static Parent parent;
	public static Pane root;
	
	public static File thumbnailDefaultFile;
	
	public static Image playImage;
	public static Image stopImage;
	
	public static Image favoriteImage;
	public static Image unfavoriteImage;
	public static Image removeImage;
	
	public static Image easySelectedImage;
	public static Image easyNotSelectedImage;
	public static Image normalSelectedImage;
	public static Image normalNotSelectedImage;
	public static Image hardSelectedImage;
	public static Image hardNotSelectedImage;
	
	public static Image pauseImage;
	public static Image scoreBarImage;
	public static Image energyBarImage;
	
	public static Image gradeA;
	public static Image gradeB;
	public static Image gradeC;
	public static Image gradeS;
	public static Image gradeSS;
	
	public static SimpleIntegerProperty newMusicNotifications = new SimpleIntegerProperty(0);
}
