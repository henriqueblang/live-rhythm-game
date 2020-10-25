package entity;

public class BooleanWrapper {
	private boolean wrappedBoolean;

	public BooleanWrapper() {
		this(false);
	}

	public BooleanWrapper(boolean wrappedBoolean) {
		super();
		this.wrappedBoolean = wrappedBoolean;
	}

	public boolean getWrappedBoolean() {
		return wrappedBoolean;
	}

	public void setWrappedBoolean(boolean wrappedBoolean) {
		this.wrappedBoolean = wrappedBoolean;
	}
}
