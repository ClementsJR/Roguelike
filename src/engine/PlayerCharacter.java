package engine;

public class PlayerCharacter extends LivingEntity{
	public static final String SPRITE_URL = "/assets/img/testPlayer.png";
	
	private static int INITIAL_HEALTH = 20;
	private static int INITIAL_ATTACK = 4;
	private static int INITIAL_DEFENSE = 0;
	
	private double hungerLevel;
	
	public PlayerCharacter() {
		super(INITIAL_HEALTH, INITIAL_ATTACK, INITIAL_DEFENSE);
		setIsEnemy(false);
		setImage(SPRITE_URL);
	}
	
	public enum HungerStage {
		FULL, PECKISH, HUNGRY, STARVING;
	}
	
}
