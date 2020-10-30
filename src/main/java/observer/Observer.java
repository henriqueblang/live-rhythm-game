package observer;

public abstract class Observer {
	
	public Observer(Subject subject) {
		subject.register(this);
	}
	
	public abstract void update(int notifications);
}
