package engine;

import javafx.scene.image.Image;

public class Skeleton extends Enemy {
	public static final String SPRITE_URL = "/assets/img/skeleton.gif";
	
	public Skeleton() {
		currentBehavior = BehaviorState.IDLE;
		maxHealth = 100;
		currentHealth = maxHealth;
		attackPower = 10;
		defense = 20;
		
		sprite = new Image(SPRITE_URL);
	}
}
