

import javafx.scene.image.Image;

public abstract class Entity {
	private boolean impassable;
	private Image sprite;
	
	private Position currentPosition;
	
	public Entity() {
		
	}
	
	public boolean isImpassable() { return impassable; }
	public Image getSprite() { return sprite; }
	public Position getPosition() { return currentPosition; }
	
	public void setImpassable(boolean impassable) {
		this.impassable = impassable;
	}
	
	public void setImage(String url) {
		sprite = new Image(url);
	}
	
	public void setPosition(Position newPosition) {
		currentPosition = newPosition;
	}
}
