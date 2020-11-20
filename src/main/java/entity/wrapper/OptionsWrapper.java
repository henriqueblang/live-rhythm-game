package entity.wrapper;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.input.KeyCode;
import utils.GameUtils;

public class OptionsWrapper {
	private double noteSpeed;
	
	private Map<String, KeyCode> shortcuts;
	
	public OptionsWrapper() {
		this(false);
	}

	public OptionsWrapper(boolean defaultOptions) {
		if(defaultOptions) {
			Map<String, KeyCode> shortcuts = new HashMap<String, KeyCode>();
			
			shortcuts.put("Back", KeyCode.ESCAPE);
			shortcuts.put("Confirm", KeyCode.ENTER);
			
			shortcuts.put("Next", KeyCode.DOWN);
			shortcuts.put("Previous", KeyCode.UP);
			shortcuts.put("Change mode", KeyCode.TAB);
			
			shortcuts.put("Track 1", KeyCode.D);
			shortcuts.put("Track 2", KeyCode.F);
			shortcuts.put("Track 3", KeyCode.J);
			shortcuts.put("Track 4", KeyCode.K);
			
			this.shortcuts = shortcuts;
			this.noteSpeed = (GameUtils.MIN_NOTE_SPEED + GameUtils.MAX_NOTE_SPEED) / 2; 
		}
	}

	public OptionsWrapper(double noteSpeed, Map<String, KeyCode> shortcuts) {
		super();
		
		this.shortcuts = shortcuts;
		this.noteSpeed = noteSpeed;
	}

	public double getNoteSpeed() {
		return noteSpeed;
	}

	public void setNoteSpeed(double noteSpeed) {
		this.noteSpeed = noteSpeed;
	}

	public Map<String, KeyCode> getShortcuts() {
		return shortcuts;
	}

	public void setShortcuts(Map<String, KeyCode> shortcuts) {
		this.shortcuts = shortcuts;
	}
	
}
