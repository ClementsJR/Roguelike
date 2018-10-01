package engine;

public abstract class Entity {
	protected boolean impassable;
	
	public Entity() {
		impassable = false;
	}
	
	public Entity(boolean impassable) {
		this.impassable = impassable;
	}
	
	public boolean isImpassable() {
		return impassable;
	}
	
	public void setImpassable(boolean impassable) {
		this.impassable = impassable;
	}
}
