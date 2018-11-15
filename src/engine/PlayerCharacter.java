package engine;

public abstract class PlayerCharacter extends LivingEntity{
	
	public static int PLAYER_LEVEL = 0;
	
	public int currentXP;
	private int goalXP = (int)Math.pow(2, (3 + PLAYER_LEVEL));
	
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
	
	public abstract void LevelUp();
	
	public abstract void PlayerRegen();
	
	public abstract void UpdateStatus();
}
