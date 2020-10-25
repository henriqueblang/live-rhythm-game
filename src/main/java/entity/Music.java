package entity;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;

import entity.collection.Beatmap;
import javafx.scene.image.Image;

public class Music {
	private int id;
	private int previewStart;
	private int previewEnd;
	private boolean favorite;

	private List<Integer> highscores;

	private String title;

	private Clip audio;

	private Image thumbnail;

	List<Beatmap> beatmaps;

	public Music(String title, Clip audio, int previewStart, int previewEnd, Image thumbnail,
			List<Beatmap> beatmaps) {
		this(-1, title, false, audio, previewStart, previewEnd, thumbnail, beatmaps, new ArrayList<>());
	}

	public Music(int id, String title, boolean favorite, Clip audio, int previewStart, int previewEnd, Image thumbnail,
			List<Beatmap> beatmaps, List<Integer> highscores) {
		this.id = id;
		this.title = title;
		this.audio = audio;
		this.previewStart = previewStart;
		this.previewEnd = previewEnd;
		this.thumbnail = thumbnail;
		this.beatmaps = beatmaps;
		this.favorite = favorite;
		this.highscores = highscores;

		if (this.beatmaps.isEmpty()) {
			for (int i = 0; i < 3; i++)
				this.beatmaps.add(null);
		}
		
		if (this.highscores.isEmpty()) {
			for (int i = 0; i < 3; i++)
				this.highscores.add(0);
		}
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public List<Integer> getHighscores() {
		return highscores;
	}

	public int getHighscore(int index) {
		return highscores.get(index);
	}

	public void setHighscore(int index, int highscore) {
		this.highscores.set(index, highscore);
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public String getTitle() {
		return title;
	}

	public Clip getAudio() {
		return audio;
	}

	public Image getThumbnail() {
		return thumbnail;
	}

	public List<Beatmap> getBeatmaps() {
		return beatmaps;
	}
	
	public void setBeatmap(int index, Beatmap beatmap) {
		this.beatmaps.set(index, beatmap);
	}

	public boolean isPlaying() {
		return audio.isRunning();
	}

	public void playPreview() {		
		audio.setFramePosition(previewStart);
		audio.setLoopPoints(previewStart, previewEnd);
		audio.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void playAudioFromMicrosecondPosition(long time) {
		audio.setMicrosecondPosition(time);
		audio.start();
	}

	public void playAudio() {
		playAudioFromMicrosecondPosition(0);
	}
	
	public void stopAudio() {
		if(!isPlaying())
			return;
		
		audio.stop();
	}
	
	public void closeAudio() {
		audio.close();
	}
}
