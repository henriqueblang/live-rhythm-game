package entity;

import java.util.ArrayList;

import javax.sound.sampled.Clip;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MusicTest {
	
	private Music music;
	
	@Before
	public void setUp() {
		Clip audio = Mockito.mock(Clip.class);
		
		music = new Music(
			"",
			audio,
			0,
			0,
			null,
			new ArrayList<>()
		);
	}
	
	@Test
	public void isFavoriteTest() {
		Assert.assertFalse(music.isFavorite());
	}
	
	@Test
	public void isPlayingTest() {
		music.playAudio();
		
		Mockito.verify(music.getAudio()).start();
	}
	
	@Test
	public void isPlayingPreviewTest() {
		music.playPreview();
		
		Mockito.verify(music.getAudio()).loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	@Test
	public void isPlayingFromMicrosecondPositionTest() {
		music.playAudioFromMicrosecondPosition(1_000_000);
		
		Mockito.verify(music.getAudio()).start();
	}
	
	@Test
	public void isNotPlayingTest() {
		Assert.assertFalse(music.isPlaying());
	}
	
	@Test
	public void playAndStopTest() {
		music.playAudio();

		Mockito.verify(music.getAudio()).start();
		Mockito.when(music.getAudio().isRunning()).thenReturn(true);
		
		music.stopAudio();
		
		Mockito.verify(music.getAudio()).stop();
	}
}
