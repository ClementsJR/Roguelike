package engine;

import javafx.scene.image.Image;

public class Wall extends Entity {
	private static final String SPRITE_URL = "/assets/img/brick_gray_0.png";
	
	public Wall() {
		impassable = true;
		sprite = new Image(SPRITE_URL);
	}
	
	public Image getSprite() {
		return sprite;
	}
}
