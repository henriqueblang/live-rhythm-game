package entity;

import javafx.scene.text.Text;
import observer.Observer;
import states.GameStates;

public class LibraryObserver extends Observer {

	private Text text;
	
	public LibraryObserver() {}
	
	public LibraryObserver(Notification notification, Text text) {
		super(notification);
		
		this.text = text;	
	}
	
	@Override
	public void update(int notifications) {
		text.setText(Integer.toString(GameStates.getInstance().getLibrary().size()));
	}

}
