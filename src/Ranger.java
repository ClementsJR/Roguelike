

public class Ranger extends PlayerCharacter{
	public static final String SPRITE_URL = "/assets/img/ranger4.gif";
	
	private static final int INITIAL_HEALTH = (int)((4 * 0) + Math.floor((1/2) * 0) + 150);
	private static final int INITIAL_ATTACK = (int)(Math.floor((1/2) * 0) + Math.floor((1/6) * 0) + 3);
	private static final Range INITIAL_ATTACK_RANGE = new Range(INITIAL_ATTACK, INITIAL_ATTACK);
	private static final int INITIAL_DEFENSE = 0;
	private int PLAYER_REGEN = (int)(Math.floor((1/5) * playerLevel) + Math.floor((1/10) * playerLevel) + 1);
	public int turnCounter = 0;


	public Ranger() {
		super(INITIAL_HEALTH, INITIAL_ATTACK_RANGE, INITIAL_DEFENSE);
		setIsEnemy(false);
		setImage(SPRITE_URL);
		sightDistance = 4;
	}
	
	public enum HungerStage {
		FULL, PECKISH, HUNGRY, STARVING;
	}
	
	public void LevelUp() {
		playerLevel++;
		goalXP = (int)Math.pow(2, (3 + playerLevel));
		currentXP = 0;
		setMaxHealth((int)((4 * playerLevel) + Math.floor((1/2) * playerLevel) + 15));
		int maxAttack = (int)(Math.floor((1/2) * playerLevel) + Math.floor((1/6) * playerLevel) + 3);
		setAttackRange(maxAttack, maxAttack);
		PLAYER_REGEN = (int)(Math.floor((1/5) * playerLevel) + Math.floor((1/10) * playerLevel) + 1);
	}
	
	public void PlayerRegen() {
		if (currentHealth < maxHealth)
			currentHealth = currentHealth + PLAYER_REGEN;
	}
	
	public int UpdateStatus() {
		turnCounter++;
		if (turnCounter >= 5) {
			PlayerRegen();
			turnCounter = 0;
		}
		
		hungerLevel += 0.01;
		
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
