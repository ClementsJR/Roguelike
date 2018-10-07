package engine;

import java.util.ArrayList;

public abstract class LivingEntity extends Entity {
	protected int maxHealth;
	protected int currentHealth;
	
	protected ArrayList<StatusEffect> activeStatusEffects;
	
	public LivingEntity() {
		super(true);
	}
	
	public enum StatusEffect {
		POISONED, PARALYZED, CONFUSED, CHARMED, FRIGHTENED, BOUND, RABID;
	}
}
