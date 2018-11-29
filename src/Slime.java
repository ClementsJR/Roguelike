

import java.util.Random;

public class Slime extends LivingEntity {
	public static final String SPRITE_URL = "/assets/img/slime.gif";
	
	private int statusInflictChance;
	private StatusEffect dealtStatusEffect;
	
	public Slime(int level, StatusEffect dealtStatusEffect) {
		super(getInitialHealth(level), getInitialAttackRange(level), getInitialDefense(level));
		setIsEnemy(true);
		setImage(SPRITE_URL);
		
		statusInflictChance = (int)((30 + (1 * level) + Math.floor((1/3) * level))/100);
		this.dealtStatusEffect = dealtStatusEffect;
	}
	
	private static int getInitialHealth(int level) {
		return (int)(15 + (2 * level) + Math.floor((1/6) * level));
	}
	
	private static Range getInitialAttackRange(int level) {
		int minAttack = (int)(1 + Math.floor((1/3) * level));
		int maxAttack = (int)(3 + Math.floor((1/3) * level));
		
		return new Range(minAttack, maxAttack);
	}
	
	private static int getInitialDefense(int level) {
		return 0;
	}
	
	public boolean DealsStatusEffect() {
		Random rand = new Random();
		double randNum = rand.nextDouble();
		if (randNum <= statusInflictChance)
			return true;
		else
			return false;
	}
	
	public StatusEffect getStatusEffect() {
		Random rand = new Random();
		int statusDuration = rand.nextInt(3) + 3;
		StatusEffect effect = dealtStatusEffect;
		
		effect.duration = statusDuration;
		effect.damage = 1;
		
		return effect;
	}
}