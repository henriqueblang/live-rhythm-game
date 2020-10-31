package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import entity.collection.Beatmap;

public class AudioUtilsTest {
	
	private Clip audio;
	private InputStream audioStream;
	
	@Before
	public void setUp() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		audio = AudioUtils.getSoundClip(getClass().getResource("/Styx Helix.mp3"));
		audioStream = getClass().getResource("/Styx Helix.mp3").openStream();
	}
	
	@After
	public void cleanUp() throws IOException {
		audio.close();
		audioStream.close();
	}
	
	@Test
	public void audioDurationTest() {
		Assert.assertEquals(AudioUtils.getAudioDuration(audio), 92, 0.5);
	}
	
	@Test
	public void peaksTest() throws FileNotFoundException, Exception {
		List<float[]> peaks = AudioUtils.getAudioBeats(audioStream, audio.getFormat().getSampleRate());
		
		Assert.assertEquals(peaks.size(), 3975);
		Assert.assertEquals(peaks.get(0).length, 3);
	}
	
	@Test
	public void beatmapsTest() throws FileNotFoundException, Exception {
		List<float[]> peaks = AudioUtils.getAudioBeats(audioStream, audio.getFormat().getSampleRate());
		List<Beatmap> beatmaps = AudioUtils.getAudioBeatmaps(peaks);
		
		Assert.assertEquals(beatmaps.size(), 3);
		Assert.assertEquals(beatmaps.get(0).size(), 3975);
		Assert.assertEquals(beatmaps.get(1).size(), 3975);
		Assert.assertEquals(beatmaps.get(2).size(), 3975);
	}
}
