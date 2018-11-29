

public class Skeleton extends LivingEntity {
	public static final String SPRITE_URL = "/assets/img/skeleton.gif";
	
	public Skeleton(int level) {
		super(getInitialHealth(level), getInitialAttackRange(level), getInitialDefense(level));
		setIsEnemy(true);
		setImage(SPRITE_URL);
	}
	
	private static int getInitialHealth(int level) {
		return (int)(8 + (1 * level) + Math.floor((1/6) * level));
	}
	
	private static Range getInitialAttackRange(int level) {
		int minAttack = (int)(4 + Math.floor((1/2) * level) + Math.floor((1/5) * level));
		int maxAttack = (int)(5 + Math.floor((1/2) * level) + Math.floor((1/5) * level));
		
		return new Range(minAttack, maxAttack);
	}
	
	private static int getInitialDefense(int level) {
		return (int)(1 + Math.floor((1/3) * level) + Math.floor((1/6) * level));
	}
}