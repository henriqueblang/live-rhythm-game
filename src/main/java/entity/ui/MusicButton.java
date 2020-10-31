package entity.ui;

import entity.Music;
import javafx.scene.control.Button;

public class MusicButton extends Button {
	private Music music;
	private Marquee marquee;
	
	public MusicButton(Music music) {
		this.music = music;
	}
	
	public MusicButton(Music music, Marquee marquee) {
		this.music = music;
		this.marquee = marquee;
	}
	
	public Music getMusic() {
		return this.music;
	}
	
	public Marquee getMarquee() {
		return this.marquee;
	}
}
