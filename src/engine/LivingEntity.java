package engine;

import java.util.ArrayList;

public abstract class LivingEntity extends Entity {
	protected int maxHealth;
	protected int currentHealth;
	protected int attackPower;
	protected int defense;
	
	protected ArrayList<StatusEffect> activeStatusEffects;
	protected BehaviorState currentBehavior;
	
	public LivingEntity() {
		impassable = true;
	}
		
	public enum StatusEffect {
		POISONED, PARALYZED;
	}
	
	public enum BehaviorState {
		IDLE, ENGAGED;
	}
}
