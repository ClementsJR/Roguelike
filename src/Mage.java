
import java.util.Random;

public class Mage extends PlayerCharacter{
	public static final String SPRITE_URL = "/assets/img/mage4.gif";
	
	private static final int INITIAL_HEALTH = (int)((3 * PLAYER_LEVEL) + Math.floor((3/5) * PLAYER_LEVEL) + 12);
	//private static final int INITIAL_MIN_ATTACK = (int)(Math.ceil((1/2) * PLAYER_LEVEL) + Math.floor((1/6) * PLAYER_LEVEL) + 3);
	//private static final int INITIAL_MAX_ATTACK = (int)(Math.floor((2/5) * PLAYER_LEVEL) + 3);
	private static final Range INITIAL_ATTACK_RANGE = new Range(0, 0);
	private static final int INITIAL_DEFENSE = 0;
	private int PLAYER_REGEN = (int)(Math.floor((1/6) * PLAYER_LEVEL) + 1);
	public int count = 0;
	private StatusEffect dealtStatusEffect;
	private HungerStage currentHungerStage;

	public Mage() {
		super(INITIAL_HEALTH, INITIAL_ATTACK_RANGE, INITIAL_DEFENSE);
		//setIsEnemy(false);
		setImage(SPRITE_URL);
		
		burningEffect = StatusEffect.BURNED;
		burningEffect.damage = (int)(Math.floor((2/5) * PLAYER_LEVEL) + 3);
		burningEffect.duration = 3;
		
		currentHungerStage = HungerStage.FULL;
	}
	
	public enum HungerStage {
		FULL, PECKISH, HUNGRY, STARVING;
	}
	
	public void LevelUp() {
		PLAYER_LEVEL++;
		goalXP = (int)Math.pow(2, (3 + PLAYER_LEVEL));
		currentXP = 0;
		setMaxHealth((int)((3 * PLAYER_LEVEL) + Math.floor((3/5) * PLAYER_LEVEL) + 12));
		setAttackRange(0, 0);
		PLAYER_REGEN = (int)(Math.floor((1/6) * PLAYER_LEVEL) + 1);
	}
	
	public void PlayerRegen() {
		if (currentHealth < maxHealth)
			currentHealth = currentHealth + PLAYER_REGEN;
	}
	
	public StatusEffect getStatusEffect() {
		int statusDuration = 3;
		dealtStatusEffect.duration = statusDuration;
		dealtStatusEffect.damage = (int)(Math.floor((2/5) * PLAYER_LEVEL) + 3);
		return dealtStatusEffect;
	}
	
	public void UpdateStatus() {
		count++;
		if (count >= 5) {
			PlayerRegen();
			count = 0;
		}
		
		hungerLevel += 0.01;
		if (hungerLevel >= 0.0 && hungerLevel < 0.25)
			currentHungerStage = HungerStage.FULL;
			
		if (hungerLevel >= 0.25 && hungerLevel < 0.5)
			currentHungerStage = HungerStage.PECKISH;
		
		if (hungerLevel >= 0.5 && hungerLevel < 0.75)
			currentHungerStage = HungerStage.HUNGRY;
		
		if (hungerLevel >= 0.75) {
			currentHungerStage = HungerStage.STARVING;
			if (currentHealth > 1)
				currentHealth--;
		}
	}
	
}
