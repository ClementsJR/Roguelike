package engine;

import java.util.ArrayList;

public abstract class LivingEntity extends Entity {
	protected int maxHealth;
	protected int currentHealth;
	protected Position currentPosition;
	
	protected ArrayList<StatusEffect> activeStatusEffects;
	protected BehaviorState currentBehavior;
	
	public LivingEntity() {
		super(true);
	}
		
	public enum StatusEffect {
		POISONED, PARALYZED;
	}
	
	public enum BehaviorState {
		IDLE, ENGAGED;
	}
}
