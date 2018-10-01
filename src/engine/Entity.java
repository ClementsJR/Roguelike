package engine;

public abstract class Entity {
	protected boolean impassable;
	
	public Entity() {
	}
	
	public boolean isImpassable() {
		return impassable;
	}
	
	public void setImpassable(boolean impassable) {
		this.impassable = impassable;
	}
}
