package engine;

import java.util.ArrayList;

public abstract class LivingEntity extends Entity {
	public static final int DEFAULT_SIGHT_DISTANCE = 3;
	
	public static final int NOT_SEEN = -10;
	public static final int WAS_SEEN = -5;
	
	private int maxHealth;
	private int currentHealth;
	private Range damageRange;
	private int defense;

	private int sightDistance;
	private int[][] fogOfWarMap;
	
	private boolean isEnemy;

	private ArrayList<StatusEffect> activeStatusEffects;
	private BehaviorState currentBehavior;
	
	public LivingEntity() {
		setImpassable(true);
		
		sightDistance = DEFAULT_SIGHT_DISTANCE;
		
		int numRows = Dungeon.DEFAULT_NUM_ROWS;
		int numCols = Dungeon.DEFAULT_NUM_COLS;
		fogOfWarMap = new int[numRows][numCols];
		
		resetFOW();
	}
	
	public LivingEntity(int maxHealth, Range damageRange, int defense) {
		this();
		
		this.maxHealth =  maxHealth;
		this.currentHealth = maxHealth;
		this.damageRange = damageRange;
		this.defense = defense;
	}
	
	public int getSightDistance() { return sightDistance; }
	public int getCurrentHealth() { return currentHealth; }
	public boolean isEnemy() { return isEnemy; }
	public int getFOWAt(Position position) { return fogOfWarMap[position.getRow()][position.getCol()]; }
	public int getRandomAttackDamage() { return damageRange.getRandomNum(); }
	
	public void setDamageRange(Range damageRange) {
		this.damageRange = damageRange;
	}
	
	public void setIsEnemy(boolean isEnemy) {
		this.isEnemy = isEnemy;
	}
	
	public void resetFOW() {
		int numRows = Dungeon.DEFAULT_NUM_ROWS;
		int numCols = Dungeon.DEFAULT_NUM_COLS;
		
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				fogOfWarMap[row][col] = NOT_SEEN;
			}
		}
	}
	
	public void setAllCurrentlySeenToWasSeen() {
		int numRows = Dungeon.DEFAULT_NUM_ROWS;
		int numCols = Dungeon.DEFAULT_NUM_COLS;
		
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				int fow = fogOfWarMap[row][col];
				
				if(fow != NOT_SEEN && fow != WAS_SEEN) {
					fogOfWarMap[row][col] = WAS_SEEN;
				}
			}
		}
	}
	
	public void setCurrentlySeen(Position position, int distance) {
		int fow = distance;
		
		fogOfWarMap[position.getRow()][position.getCol()] = fow;
	}
	
	public void receiveDamage(int damage) {
		damage -= defense;
		
		if (damage > 0) {
			currentHealth -= damage;
		}
	}
		
	public enum StatusEffect {
		POISONED, PARALYZED;
	}
	
	public enum BehaviorState {
		IDLE, ENGAGED;
	}
	
	public enum FOWState {
		
	}
	
}
