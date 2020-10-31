package states;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import entity.Music;
import entity.wrapper.OptionsWrapper;
import utils.GameUtils;

public class GameStates {
	
	private static GameStates instance;

	private int gameMode;
	private Music gameMusic;
	
	private OptionsWrapper userOptions;
	
	private List<Music> library;
	private List<Double> accuracy;
	
	private GameStates() {}
	
	public static GameStates getInstance() {
		if(instance == null)
			instance = new GameStates();
		
		return instance;
	}
	
	public int getGameMode() {
		return gameMode;
	}

	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}

	public Music getGameMusic() {
		return gameMusic;
	}

	public void setGameMusic(Music gameMusic) {
		this.gameMusic = gameMusic;
	}

	public OptionsWrapper getUserOptions() {
		return userOptions;
	}

	public void setUserOptions(OptionsWrapper userOptions) {
		this.userOptions = userOptions;
	}

	public List<Music> getLibrary() {
		return library;
	}

	public void setLibrary(List<Music> library) {
		this.library = library;
	}

	public List<Double> getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(List<Double> accuracy) {
		this.accuracy = accuracy;
	}

	public void sortLibrary() {
		library.sort(Comparator.comparing(Music::getTitle));
		library.sort(Comparator.comparing(Music::isFavorite).reversed());
	}
	
	public int getHighscoreSum() {
		int highscoreSum = 0;
		
    	for(Music music : library)
    		highscoreSum += Collections.max(music.getHighscores());
    	
    	return highscoreSum;
	}
	
	public double getMeanAccuracy() {
		return accuracy.stream().mapToDouble(i -> i).average().orElse(0);
	}
	
	public boolean addAccuracy(double accuracy) {
		boolean clearOldestEntry = false;
		
		if(this.accuracy.size() == GameUtils.ACCURACY_GAME_AMOUNT) {
			clearOldestEntry= true;
			
			this.accuracy.remove(0);
		}
			
		this.accuracy.add(accuracy);
		
		return clearOldestEntry;
	}
}
