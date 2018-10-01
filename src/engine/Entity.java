package engine;

public abstract class Entity {
	protected Position position;
	protected boolean impassable;
	
	public Entity() {
		position = new Position();
	}
	
	public Entity(Position p) {
		position = p;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public boolean isImpassable() {
		return impassable;
	}
	
	public void setPosition(Position p) {
		position = p;
	}
	
	public void setImpassable(boolean impassable) {
		this.impassable = impassable;
	}
}
