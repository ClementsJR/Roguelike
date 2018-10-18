package engine;

import java.util.ArrayList;

public abstract class LivingEntity extends Entity {
	public static final int DEFAULT_SIGHT_DISTANCE = 3;
	
	public static final int NOT_SEEN = -10;
	public static final int WAS_SEEN = -5;
	
	protected int maxHealth;
	protected int currentHealth;
	protected int attackPower;
	protected int defense;
	
	protected ArrayList<StatusEffect> activeStatusEffects;
	protected BehaviorState currentBehavior;

	protected int sightDistance;
	protected int[][] fogOfWarMap;
	
	public LivingEntity() {
		impassable = true;
		
		sightDistance = DEFAULT_SIGHT_DISTANCE;
		
		int numRows = Dungeon.DEFAULT_NUM_ROWS;
		int numCols = Dungeon.DEFAULT_NUM_COLS;
		fogOfWarMap = new int[numRows][numCols];
		
		resetFOW();
	}
	
	public int getSightDistance() {
		return sightDistance;
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
	
	public int getFOWAt(Position position) {
		return fogOfWarMap[position.getRow()][position.getCol()];
	}
	
	public void setCurrentlySeen(Position position, int distance) {
		int fow = distance;
		
		fogOfWarMap[position.getRow()][position.getCol()] = fow;
	}
		
	public enum StatusEffect {
		POISONED, PARALYZED;
	}
	
	public enum BehaviorState {
		IDLE, ENGAGED;
	}
	
	public void Hit(int opponentAttackPower) {
		opponentAttackPower = opponentAttackPower - defense;
		if (opponentAttackPower > 0)
			currentHealth = currentHealth - opponentAttackPower;
	}
	
}
