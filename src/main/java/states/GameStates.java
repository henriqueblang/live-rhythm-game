package states;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dao.AccuracyDAO;
import entity.BooleanWrapper;
import entity.Music;
import entity.OptionsWrapper;
import utils.GameUtils;

public class GameStates {
	
	public static List<Music> library;
	
	public static int gameMode;
	public static Music gameMusic;
	
	public static OptionsWrapper userOptions;
	
	public static List<Double> accuracy;
	
	public static void sortLibrary() {
		library.sort(Comparator.comparing(Music::getTitle));
		library.sort(Comparator.comparing(Music::isFavorite).reversed());
	}
	
	public static int getHighscoreSum() {
		int highscoreSum = 0;
		
    	for(Music music : library)
    		highscoreSum += Collections.max(music.getHighscores());
    	
    	return highscoreSum;
	}
	
	public static double getMeanAccuracy() {
		return accuracy.stream().mapToDouble(i -> i).average().orElse(0);
	}
	
	public static void addAccuracy(double beatmapAccuracy) {
		final BooleanWrapper clearOldestEntry = new BooleanWrapper();
		
		if(accuracy.size() == GameUtils.ACCURACY_GAME_AMOUNT) {
			clearOldestEntry.setWrappedBoolean(true);
			
			accuracy.remove(0);
		}
			
		accuracy.add(beatmapAccuracy);
		
		Thread thread = new Thread() {
			@Override
			public void run() {
				AccuracyDAO dao = new AccuracyDAO();
				try {
					dao.insertAccuracy(beatmapAccuracy, clearOldestEntry);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};
		
		thread.start();
	}
}
