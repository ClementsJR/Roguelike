package engine;

public class Skeleton extends LivingEntity {
	public static final String SPRITE_URL = "/assets/img/skeleton.gif";
	
	private static int INITIAL_HEALTH = 8;
	private static int INITIAL_ATTACK = 4;
	private static int INITIAL_DEFENSE = 1;
	
	public Skeleton() {
		super(INITIAL_HEALTH, INITIAL_ATTACK, INITIAL_DEFENSE);
		setIsEnemy(true);
		setImage(SPRITE_URL);
	}
}
