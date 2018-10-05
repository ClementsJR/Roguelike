package engine;

import javafx.scene.image.Image;

public class Ground extends Entity {
	public static final String SPRITE_URL = "/assets/img/limestone_1.png";
	
	public Ground() {
		impassable = false;
		sprite = new Image(SPRITE_URL);
	}

}
