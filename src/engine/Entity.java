package engine;

import javafx.scene.image.Image;

public abstract class Entity {
	protected boolean impassable;
	protected Image sprite;
	
	protected Position currentPosition;
	
	public Entity() {
	}
	
	public boolean isImpassable() {
		return impassable;
	}
	
	public Image getSprite() {
		return sprite;
	}

	public Position getPosition() {
		return currentPosition;
	}
	
	public void setPosition(Position newPosition) {
		currentPosition = newPosition;
	}
	
	public void setImpassable(boolean impassable) {
		this.impassable = impassable;
	}
}
