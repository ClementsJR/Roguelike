package engine;

import javafx.scene.image.Image;

public class Ground extends Entity {
	private static final String SPRITE_URL = "/assets/img/limestone_0.png";
	
	public Ground() {
		impassable = false;
		sprite = new Image(SPRITE_URL);
	}

}
