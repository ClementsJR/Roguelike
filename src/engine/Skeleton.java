package engine;

public class Skeleton extends LivingEntity {
	public static final String SPRITE_URL = "/assets/img/skeleton.gif";
	
	private static final int INITIAL_HEALTH = 8;
	private static final int INITIAL_MIN_ATTACK = 4;
	private static final int INITIAL_MAX_ATTACK = 5;
	private static final Range INITIAL_ATTACK_RANGE = new Range(INITIAL_MIN_ATTACK, INITIAL_MAX_ATTACK);
	private static final int INITIAL_DEFENSE = 1;
	
	public Skeleton() {
		super(INITIAL_HEALTH, INITIAL_ATTACK_RANGE, INITIAL_DEFENSE);
		setIsEnemy(true);
		setImage(SPRITE_URL);
	}
	
	public void LevelUp() {
	}
	
}