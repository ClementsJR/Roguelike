

public class Warrior extends PlayerCharacter{
	public static final String SPRITE_URL = "/assets/img/warrior.png";
	
	private static final int INITIAL_HEALTH = ((6 * 0) + 200);
	private static final int INITIAL_MIN_ATTACK = (int)(Math.ceil((1/2) * 0) + Math.floor((1/6) * 0) + 3);
	private static final int INITIAL_MAX_ATTACK = (int)(Math.floor((1/2) * 0) + Math.floor((1/6) * 0) + 5);
	private static final Range INITIAL_ATTACK_RANGE = new Range(INITIAL_MIN_ATTACK, INITIAL_MAX_ATTACK);
	private static final int INITIAL_DEFENSE = 0;
	private int playerRegen = (int)(Math.floor((1/6) * 0) + Math.floor((1/10) * 0) + 1);
	public int turnCounter = 0;


	public Warrior() {
		super(INITIAL_HEALTH, INITIAL_ATTACK_RANGE, INITIAL_DEFENSE);
		setIsEnemy(false);
		setImage(SPRITE_URL);
	}
	
	public enum HungerStage {
		FULL, PECKISH, HUNGRY, STARVING;
	}
	
	public void LevelUp() {
		playerLevel++;
		goalXP = (int)Math.pow(2, (3 + playerLevel));
		currentXP = 0;
		setMaxHealth((6 * playerLevel) + 200);
		int minAttack = (int)(Math.ceil((1/2) * playerLevel) + Math.floor((1/6) * playerLevel) + 3);
		int maxAttack = (int)(Math.floor((1/2) * playerLevel) + Math.floor((1/6) * playerLevel) + 5);
		setAttackRange(minAttack, maxAttack);
		playerRegen = (int)(Math.floor((1/6) * playerLevel) + Math.floor((1/10) * playerLevel) + 1);
	}
	
	public void PlayerRegen() {
		if (currentHealth < maxHealth) {
			currentHealth = currentHealth + playerRegen;
		}
	}
	
	public int UpdateStatus() {
		turnCounter++;
		
		if (turnCounter >= 4) {
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
