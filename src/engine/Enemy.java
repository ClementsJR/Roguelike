package engine;

import javafx.scene.image.Image;

public class Enemy extends LivingEntity{
	private static final String SPRITE_URL = "/assets/img/testEnemy.png";
	
	public Enemy() {
		super();
		sprite = new Image(SPRITE_URL);
	}
}
