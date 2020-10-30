package observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
	
	protected List<Observer> observers = new ArrayList<>();
	
	public void clear() {
		observers.clear();
	}
	
	public void remove(Observer observer) {
		observers.remove(observer);
	}

	public void register(Observer observer) {
		observers.add(observer);
	}
	
	public abstract void inform();
}
