
import java.util.Random;

public class Mage extends PlayerCharacter{
	public static final String SPRITE_URL = "/assets/img/mage.png";
	
	private static final int INITIAL_HEALTH = (int)((3 * 0) + Math.floor((3/5) * 0) + 12);
	private static final Range INITIAL_ATTACK_RANGE = new Range(0, 0);
	private static final int INITIAL_DEFENSE = 0;
	private HungerStage currentHungerStage;
	private int PLAYER_REGEN = (int)(Math.floor((1/6) * playerLevel) + 1);
	public int turnCounter = 0;

	public Mage() {
		super(INITIAL_HEALTH, INITIAL_ATTACK_RANGE, INITIAL_DEFENSE);
		setIsEnemy(false);
		setImage(SPRITE_URL);
		
		currentHungerStage = HungerStage.FULL;
	}
	
	public enum HungerStage {
		FULL, PECKISH, HUNGRY, STARVING;
	}
	
	public void LevelUp() {
		playerLevel++;
		goalXP = (int)Math.pow(2, (3 + playerLevel));
		currentXP = 0;
		setMaxHealth((int)((3 * playerLevel) + Math.floor((3/5) * playerLevel) + 12));
		int attack = (int)(Math.floor((2/5) * playerLevel) + 3);
		setAttackRange(0, 0);
		PLAYER_REGEN = (int)(Math.floor((1/6) * playerLevel) + 1);
	}
	
	public void PlayerRegen() {
		if (currentHealth < maxHealth)
			currentHealth = currentHealth + PLAYER_REGEN;
	}
	
	public boolean DealsStatusEffect() {
		if(equippedArmorType == ArmorType.C3) {
			double chanceToHit = 0.15;
			Random rand = new Random();
			double randNum = rand.nextDouble();
			double addedChance = 1 - (currentHealth / maxHealth);
			if (addedChance <= 0.85)
				chanceToHit += addedChance;
			
			if (randNum <= chanceToHit)
				return true; 
		}
		
		return true;
	}
	
	public StatusEffect getStatusEffect() {
		if(equippedArmorType == ArmorType.C3) {
			Random rand = new Random();
			
			int duration = rand.nextInt(3) + 3;
			int damage = 1;
			
			StatusEffect poison = StatusEffect.POISONED;
			poison.duration = duration;
			poison.damage = damage;
			
			return poison;
		} else {
			int duration = 3;
			int damage = (int)Math.floor((2/5) * playerLevel) + 3;
			
			StatusEffect burning = StatusEffect.BURNED;
			burning.duration = duration;
			burning.damage = damage;
			
			return burning;
		}
	}
	
	public int UpdateStatus() {
		turnCounter++;
		if (turnCounter >= 5) {
			PlayerRegen();
			turnCounter = 0;
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
		
		int totalDamage = 0;
		
		if (poisonEffect != null && poisonEffect.duration > 0) {
			currentHealth -= poisonEffect.damage;
			totalDamage += poisonEffect.damage;
			poisonEffect.duration--;
		}
		
		if (paralysisEffect != null && paralysisEffect.duration > 0) {
			paralysisEffect.duration--;
		}
		
		if (burningEffect != null && burningEffect.duration > 0) {
			currentHealth -= burningEffect.damage;
			totalDamage += burningEffect.damage;
			burningEffect.duration--;
		}
		
		return totalDamage;
	}
	
}
