package engine;

public class PlayerCharacter extends LivingEntity{
	public static final String SPRITE_URL = "/assets/img/testPlayer.png";
	
	private static final int INITIAL_HEALTH = 20;
	private static final int INITIAL_MIN_ATTACK = 3;
	private static final int INITIAL_MAX_ATTACK = 5;
	private static final Range INITIAL_ATTACK_RANGE = new Range(INITIAL_MIN_ATTACK, INITIAL_MAX_ATTACK);
	private static final int INITIAL_DEFENSE = 0;
	
	private double hungerLevel;
	
	public PlayerCharacter() {
		super(INITIAL_HEALTH, INITIAL_ATTACK_RANGE, INITIAL_DEFENSE);
		setIsEnemy(false);
		setImage(SPRITE_URL);
	}
	
	public enum HungerStage {
		FULL, PECKISH, HUNGRY, STARVING;
	}
	
}
