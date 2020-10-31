package states;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import entity.Music;
import entity.collection.Beatmap;

@RunWith(MockitoJUnitRunner.class)
public class GameStatesTest {
	
	private static GameStates gameStates;
	
	private static Music music1;
	private static Music music2;
	private static Music music3;
	
	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUp() {
		gameStates = GameStates.getInstance();
		
		List<Beatmap> dummyBeatmapList = new ArrayList<>();
		dummyBeatmapList.add(new Beatmap());
		dummyBeatmapList.add(new Beatmap());
		dummyBeatmapList.add(new Beatmap());
		
		music1 = new Music(
			-1,
			"A",
			false,
			null,
			0,
			0,
			null,
			dummyBeatmapList
		);
		music2 = new Music(
			-1,
			"C",
			true,
			null,
			0,
			0,
			null,
			dummyBeatmapList
		);
		music3 = new Music(
			-1,
			"B",
			true,
			null,
			0,
			0,
			null,
			dummyBeatmapList
		);
		
		List<Music> library = new ArrayList<>();
		List<Double> accuracy = Mockito.spy(ArrayList.class);
		
		gameStates.setLibrary(library);
		gameStates.setAccuracy(accuracy);
		
		music1.setHighscore(0, 10);
		music1.setHighscore(1, 15);
		music1.setHighscore(2, 5);
		
		music2.setHighscore(0, 20);
		music2.setHighscore(1, 5);
		music2.setHighscore(2, 0);
		
		music3.setHighscore(0, 15);
		music3.setHighscore(1, 0);
		music3.setHighscore(2, 20);
		
		library.add(music1);
		library.add(music2);
		library.add(music3);
		
		gameStates.addAccuracy(5.0);
		gameStates.addAccuracy(8.0);
		gameStates.addAccuracy(1.0);
		gameStates.addAccuracy(2.0);
		gameStates.addAccuracy(7.0);
		gameStates.addAccuracy(1.0);
	}
	
	@Test
	public void sortLibraryTest() {
		gameStates.sortLibrary();
		
		Assert.assertEquals(music3, gameStates.getLibrary().get(0));
	}
	
	@Test
	public void getHighscoreSumTest() {
		Assert.assertEquals(55, gameStates.getHighscoreSum());
	}
	
	@Test
	public void getMeanAccuracyTest() {
		Assert.assertEquals(3.8, gameStates.getMeanAccuracy(), 0);
	}
	
	@Test
	public void accuracyRemovedTest() {
		Mockito.verify(gameStates.getAccuracy()).remove(0);
		Mockito.verify(gameStates.getAccuracy()).remove(Mockito.anyInt());
	}
}
