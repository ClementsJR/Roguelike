package engine;

import java.util.ArrayList;

public abstract class LivingEntity extends Entity {
	protected int maxHealth;
	protected int currentHealth;
	protected Position currentPosition;
	
	protected ArrayList<StatusEffect> activeStatusEffects;
	protected ArrayList<CurrentBehavior> entityCurrentBehavior;
	
	public Position getCurrentPosition() {
		return currentPosition;
	}
	
	public void setCurrentPosition(Position newPosition) {
		currentPosition = newPosition;
	}
	
	public LivingEntity() {
		super(true);
	}
	
	public enum StatusEffect {
		POISONED, PARALYZED, CONFUSED, CHARMED, FRIGHTENED, BOUND, RABID;
	}
	
	public enum CurrentBehavior {
		PURSUING, PATROLLING, FLEEING, HOLDINGPOSITION;
	}
}
