

public class Skeleton extends LivingEntity {
	public static final String SPRITE_URL = "/assets/img/skeleton.gif";
	
	public static int currentLevel = PlayerCharacter.PLAYER_LEVEL;
	private static final int INITIAL_HEALTH = (int)(8 + (1 * currentLevel) + Math.floor((1/6) * currentLevel));
	private static final int INITIAL_MIN_ATTACK = (int)(4 + Math.floor((1/2) * currentLevel) + Math.floor((1/5) * currentLevel));
	private static final int INITIAL_MAX_ATTACK = (int)(5 + Math.floor((1/2) * currentLevel) + Math.floor((1/5) * currentLevel));
	private static final Range INITIAL_ATTACK_RANGE = new Range(INITIAL_MIN_ATTACK, INITIAL_MAX_ATTACK);
	private static final int INITIAL_DEFENSE = (int)(1 + Math.floor((1/3) * currentLevel) + Math.floor((1/6) * currentLevel));
	
	public Skeleton() {
		super(INITIAL_HEALTH, INITIAL_ATTACK_RANGE, INITIAL_DEFENSE);
		setIsEnemy(true);
		setImage(SPRITE_URL);
	}
	
	public void LevelUp() {
		setMaxHealth((int)(8 + (1 * currentLevel) + Math.floor((1/6) * currentLevel)));
		int minAttack = (int)(4 + Math.floor((1/2) * currentLevel) + Math.floor((1/5) * currentLevel));
		int maxAttack = (int)(5 + Math.floor((1/2) * currentLevel) + Math.floor((1/5) * currentLevel));
		setAttackRange(minAttack, maxAttack);
		setDefense((int)(1 + Math.floor((1/3) * currentLevel) + Math.floor((1/6) * currentLevel)));
	}
	
}