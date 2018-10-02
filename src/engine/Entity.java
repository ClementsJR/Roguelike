package engine;

import javafx.scene.image.Image;

public abstract class Entity {
	protected boolean impassable;
	protected Image sprite;
	
	public Entity() {
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
	
	public Image getSprite() {
		return sprite;
	}
}
