package entity;

import javafx.beans.property.SimpleIntegerProperty;
import observer.Observer;
import observer.Subject;

public class Notification extends Subject {
	
	private SimpleIntegerProperty notifications = new SimpleIntegerProperty(0);
	
	@Override
	public void inform() {
		for(Observer observer : observers) {
			observer.update(notifications.get());
		}
	}
	
	public void setNotifications(int notifications) {
		this.notifications.set(notifications);
		
		inform();
	}
	
	public void incrementNotifications() {
		setNotifications(notifications.get() + 1);
	}
	
	public void resetNotifications() {
		setNotifications(0);
	}
	
}
