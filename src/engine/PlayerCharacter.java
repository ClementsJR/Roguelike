package engine;

import engine.LivingEntity.BehaviorState;
import javafx.scene.image.Image;

public class PlayerCharacter extends LivingEntity{
	private static final String SPRITE_URL = "/assets/img/testPlayer.png";
	
	private double hungerLevel;
	
	public PlayerCharacter() {
		maxHealth = 100;
		currentHealth = maxHealth;
		attackPower = 100;
		defense = 20;
		
		sprite = new Image(SPRITE_URL);
	}
	
	public enum HungerStage {
		FULL, PECKISH, HUNGRY, STARVING;
	}
	
}
