package entity;

import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import observer.Observer;

public class NotificationsObserver extends Observer {
	
	private Text text;
	private ImageView image;
	
	public NotificationsObserver(Notification notification, Text text, ImageView image) {
		super(notification);
		
		this.text = text;
		this.image = image;
	}

	@Override
	public void update(int notifications) {
		text.setText(Integer.toString(notifications));

		if (notifications > 0 && !text.isVisible()) {
			text.setVisible(true);
			image.setVisible(true);
		}
	}

}
