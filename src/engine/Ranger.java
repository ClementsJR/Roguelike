package engine;

public class Ranger extends PlayerCharacter{
	public static final String SPRITE_URL = "/assets/img/warrior3.png";
	
	private static final int INITIAL_HEALTH = (int)((4 * PLAYER_LEVEL) + Math.floor((1/2) * PLAYER_LEVEL) + 15);
	//private static final int INITIAL_MIN_ATTACK = (int)(Math.ceil((1/2) * PLAYER_LEVEL) + Math.floor((1/6) * PLAYER_LEVEL) + 3);
	private static final int INITIAL_MAX_ATTACK = (int)(Math.floor((1/2) * PLAYER_LEVEL) + Math.floor((1/6) * PLAYER_LEVEL) + 3);
	private static final Range INITIAL_ATTACK_RANGE = new Range(INITIAL_MAX_ATTACK, INITIAL_MAX_ATTACK);
	private static final int INITIAL_DEFENSE = 0;
	private int PLAYER_REGEN = (int)(Math.floor((1/5) * PLAYER_LEVEL) + Math.floor((1/10) * PLAYER_LEVEL) + 1);
	public int count = 0;


	public Ranger() {
		super(INITIAL_HEALTH, INITIAL_ATTACK_RANGE, INITIAL_DEFENSE);
		//setIsEnemy(false);
		setImage(SPRITE_URL);
	}
	
	public enum HungerStage {
		FULL, PECKISH, HUNGRY, STARVING;
	}
	
	public void LevelUp() {
		PLAYER_LEVEL++;
		currentXP = 0;
		setMaxHealth((6 * PLAYER_LEVEL) + 20);
		int maxAttack = (int)(Math.floor((1/2) * PLAYER_LEVEL) + Math.floor((1/6) * PLAYER_LEVEL) + 3);
		setAttackRange(maxAttack, maxAttack);
		PLAYER_REGEN = (int)(Math.floor((1/5) * PLAYER_LEVEL) + Math.floor((1/10) * PLAYER_LEVEL) + 1);
	}
	
	public void PlayerRegen() {
		if (currentHealth < maxHealth)
			currentHealth = currentHealth + PLAYER_REGEN;
	}
	
	public void UpdateStatus() {
		count++;
		if (count >= 5) {
			PlayerRegen();
			count = 0;
		}
	}
	
}