package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.io.FilenameUtils;

import dao.MusicDAO;
import entity.Music;
import entity.collection.Beatmap;
import entity.collection.Pair;
import entity.decoder.MP3Decoder;
import entity.transform.FFT;
import javafx.scene.image.Image;
import states.GameStates;
import states.UIStates;
import utils.EnumUtils.Types;

public class AudioUtils {

	private static final int THRESHOLD_WINDOW_SIZE = 10;
	private static final float[] MULTIPLIERS = {2.5f, 2.0f, 1.5f};
	
	public static void playAudio(Clip audio) {
		if(audio.isRunning())
			audio.stop();
		
		audio.setMicrosecondPosition(0);
		audio.start();
	}
	
	public static double getAudioDuration(Clip audio) {
		return audio.getFrameLength() / audio.getFormat().getFrameRate();
	}
	
	public static AudioInputStream getSoundAudioInputStream(URL file) throws UnsupportedAudioFileException, IOException {
		AudioInputStream ais = AudioSystem.getAudioInputStream(file);
		
		AudioInputStream din = null;
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
		
		din = AudioSystem.getAudioInputStream(decodedFormat, ais);
		ais = din;
		
		return ais;
	}
	
	public static AudioInputStream getSoundAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
		return getSoundAudioInputStream(file.toURI().toURL());
	}
	
	public static Clip getSoundClip(URL file) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		AudioInputStream ais = getSoundAudioInputStream(file);
		
		Clip clip = AudioSystem.getClip();
		clip.open(ais);
		
		return clip;
	}
	
	public static void saveMusic(String title, Clip clip, int previewStart, int previewEnd, InputStream audioRawStream,
			AudioInputStream audioDecodedStream, InputStream thumbnailStream) {
		final String cleanTitle = FilenameUtils.removeExtension(title);
	
		Thread processing = new Thread() {
			@Override
			public void run() {
				List<float[]> beats = null;
				try {
					beats = getAudioBeats(audioRawStream, audioDecodedStream.getFormat().getSampleRate());
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}

				List<Beatmap> beatmaps = getAudioBeatmaps(beats);

				Music music = new Music(cleanTitle, clip, previewStart, previewEnd, new Image(thumbnailStream), beatmaps);
				GameStates.getInstance().getLibrary().add(music);
				
				UIStates.getInstance().getNotification().incrementNotifications();
				
				try {
					audioRawStream.reset();
					thumbnailStream.reset();
					
					MusicDAO dao = new MusicDAO();
					int musicId = dao.insertMusic(cleanTitle, previewStart, previewEnd, audioRawStream, thumbnailStream);
					
					for(int mode = 0; mode < beatmaps.size(); mode++) {
						dao.insertBeatmap(musicId, mode, beatmaps.get(mode));
						dao.insertHighscore(musicId, mode);
					}
					
					music.setId(musicId);
				} catch (SQLException | IOException e) {
					System.err.println(e.getMessage());
				}
				
			}
		};

		processing.start();
	}

	public static List<Beatmap> getAudioBeatmaps(List<float[]> beats) {
		List<Beatmap> beatmaps = new ArrayList<>();

		Random random = new Random();
		
		float[] lowestSensibilityBeats = beats.get(0);
		float[] highestBeats = new float[lowestSensibilityBeats.length];
		
		for(int i = 0; i < lowestSensibilityBeats.length; i++)
			beatmaps.add(new Beatmap());
		
		for(int i = 0; i < beats.size(); i++) {
			float[] beat = beats.get(i);
			
			for(int k = 0; k < highestBeats.length; k++) {
				if(beat[k] <= highestBeats[k])
					continue;
				
				highestBeats[k] = beat[k];
			}
		}
		
		for(int i = 0; i < beats.size(); i++) {
			float[] beat = beats.get(i);
			
			for(int k = 0; k < beat.length; k++) {
				Beatmap beatmap = beatmaps.get(k);
				
				if (beat[k] == 0)
					beatmap.add(null);
				else {
					List<Pair<Integer, Types>> noteData = new ArrayList<>();

					int noteAmount = beat[k] <= highestBeats[k] * 0.75 ? 1 : 2;
					Types noteType = Types.NORMAL;					
					
					List<Integer> consumedTracks = new ArrayList<>();
					while(consumedTracks.size() < noteAmount) {
						int track = random.nextInt(4);

						while (consumedTracks.contains(track))
							track = random.nextInt(4);

						consumedTracks.add(track);
						noteData.add(new Pair<>(track, noteType));
					}

					beatmap.add(noteData);
				}
			}
		}
		
		return beatmaps;
	}

	public static List<Beatmap> getAudioBeatmaps(AudioInputStream audio) throws FileNotFoundException, Exception  {
		return getAudioBeatmaps(getAudioBeats(audio, audio.getFormat().getSampleRate()));
	}

	private static List<float[]> getPeaks(InputStream audioRawStream, float sampleRate)
			throws FileNotFoundException, Exception {
		MP3Decoder decoder = new MP3Decoder(audioRawStream);

		FFT fft = new FFT(1024, sampleRate);
		fft.window(FFT.HAMMING);
		
		float[] samples = new float[1024];
		float[] spectrum = new float[1024 / 2 + 1];
		float[] lastSpectrum = new float[1024 / 2 + 1];
		
		List<Float> spectralFlux = new ArrayList<Float>();
		
		while (decoder.readSamples(samples) > 0) {
			fft.forward(samples);
			System.arraycopy(spectrum, 0, lastSpectrum, 0, spectrum.length);
			System.arraycopy(fft.getSpectrum(), 0, spectrum, 0, spectrum.length);

			float flux = 0;
			for (int i = 0; i < spectrum.length; i++) {
				float value = (spectrum[i] - lastSpectrum[i]);
				flux += value < 0 ? 0 : value;
			}
			spectralFlux.add(flux);
		}

		List<float[]> thresholds = new ArrayList<float[]>();
		
		for (int i = 0; i < spectralFlux.size(); i++) {
			int start = Math.max(0, i - THRESHOLD_WINDOW_SIZE);
			int end = Math.min(spectralFlux.size() - 1, i + THRESHOLD_WINDOW_SIZE);
			float mean = 0;
			
			for (int j = start; j <= end; j++)
				mean += spectralFlux.get(j);
			
			mean /= (end - start);
			
			float[] localThreholds = new float[MULTIPLIERS.length];
			for(int k = 0; k < localThreholds.length; k++)
				localThreholds[k] = mean * MULTIPLIERS[k];
			
			thresholds.add(localThreholds);
		}

		List<float[]> prunnedSpectralFluxes = new ArrayList<float[]>();
		
		for (int i = 0; i < thresholds.size(); i++) {
			float[] threshold = thresholds.get(i);
			float[] localPrunnedSpectralFluxes = new float[threshold.length];
			
			for(int k = 0; k < localPrunnedSpectralFluxes.length; k++) {
				if (threshold[k] <= spectralFlux.get(i))
					localPrunnedSpectralFluxes[k] = spectralFlux.get(i) - threshold[k];
				else
					localPrunnedSpectralFluxes[k] = 0f;
			}
			
			prunnedSpectralFluxes.add(localPrunnedSpectralFluxes);
		}

		List<float[]> peaks = new ArrayList<float[]>();
		
		for (int i = 1; i < prunnedSpectralFluxes.size() - 1; i++) {
			float[] prunnedSpectralFlux = prunnedSpectralFluxes.get(i);
			float[] prunnedSpectralFluxPrevious = prunnedSpectralFluxes.get(i - 1);
			float[] prunnedSpectralFluxFollowing = prunnedSpectralFluxes.get(i + 1);
			
			float[] localPeaks = new float[prunnedSpectralFlux.length];
			
			for(int k = 0; k < localPeaks.length; k++) {
				// < 20 Hz is practically inaudible, so let's prune it
				if (prunnedSpectralFlux[k] > prunnedSpectralFluxFollowing[k]
						&& prunnedSpectralFlux[k] > prunnedSpectralFluxPrevious[k]
						&& prunnedSpectralFlux[k] >= 20)
					localPeaks[k] = prunnedSpectralFlux[k];
				else
					localPeaks[k] = 0f;
			}
			
			peaks.add(localPeaks);
		}
		
		return peaks; 
	}
	
	public static List<float[]> getAudioBeats(InputStream audioRawStream, float sampleRate)
			throws FileNotFoundException, Exception {
		List<float[]> beats = getPeaks(audioRawStream, sampleRate);

		return beats;
	}
}
