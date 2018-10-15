package engine;

import engine.LivingEntity.BehaviorState;
import javafx.scene.image.Image;

public class Skeleton extends Enemy {
	public Skeleton() {
		String SPRITE_URL = "/assets/img/skeleton1.png";
		currentBehavior = BehaviorState.IDLE;
		maxHealth = 100;
		currentHealth = maxHealth;
		attackPower = 10;
		defense = 20;
		sprite = new Image(SPRITE_URL);
	}
}
