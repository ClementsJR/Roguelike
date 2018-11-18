package engine;

public abstract class PlayerCharacter extends LivingEntity{
	
	public static int PLAYER_LEVEL = 0;
	
	public int currentXP;
	public boolean hasFood = false;
	private int goalXP = (int)Math.pow(2, (3 + PLAYER_LEVEL));
	private Food playersFood;
	
	private double hungerLevel;

	public PlayerCharacter(int maxHealth, Range initAttackRange, int defense) {
		super(maxHealth, initAttackRange, defense);
		setIsEnemy(false);
	}
	
	public enum HungerStage {
		FULL, PECKISH, HUNGRY, STARVING;
	}
	
	public void GiveExperience() {
		currentXP = currentXP + (int)(Math.pow(2, PLAYER_LEVEL));
		if (currentXP >= goalXP)
			LevelUp();
	}
	
	public void GiveFood(Food newFood) {
		playersFood = newFood;
		hasFood = true;
	}
	
	public void EatFood() {
		if (hasFood) {
			currentHealth = maxHealth;
			hungerLevel = 0;
			playersFood = null;
			hasFood = false;
		}
	}
	
	public abstract void LevelUp();
	
	public abstract void PlayerRegen();
	
	public abstract void UpdateStatus();
}
