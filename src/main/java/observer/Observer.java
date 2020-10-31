package observer;

public abstract class Observer {
	
	private Subject subject;
	
	public Observer() {}
	
	public Observer(Subject subject) {
		this.subject = subject;
		
		subject.register(this);
	}
	
	public Subject getSubject() {
		return subject;
	}
	
	public void setSubject(Subject subject) {
		this.subject = subject;
		
		subject.register(this);
	}
	
	public abstract void update(int notifications);
}
