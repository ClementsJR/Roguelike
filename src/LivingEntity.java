
import java.util.ArrayList;
import java.util.Random;

public abstract class LivingEntity extends Entity {
	public static final int DEFAULT_SIGHT_DISTANCE = 3;
	
	public static final int NOT_SEEN = -10;
	public static final int WAS_SEEN = -5;
	
	public int maxHealth;
	public int currentHealth;
	protected Range damageRange;
	protected int defense;

	protected int sightDistance;
	private int[][] fogOfWarMap;
	
	private boolean isEnemy;

	protected StatusEffect poisonEffect;
	protected StatusEffect paralysisEffect;
	protected StatusEffect burningEffect;
	protected BehaviorState currentBehavior;
	
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
	
	public void setMaxHealth (int maxHealth) {
		this.maxHealth = maxHealth;
		currentHealth = maxHealth;
	}
	
	public void setAttackRange (int minAttack, int maxAttack) {
		damageRange = new Range(minAttack, maxAttack);
	}
	
	public void setDamageRange(Range damageRange) {
		this.damageRange = damageRange;
	}
	
	public void setDefense(int defense) {
		this.defense = defense;
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
	
	public int receiveDamage(int damage) {
		damage -= defense;
		
		if (damage > 0) {
			currentHealth -= damage;
			return damage;
		} else {
			return 0;
		}
	}
	
	public abstract void LevelUp();
		
	public BehaviorState getCurrentBehavior() {
		return currentBehavior;
	}

	public void setCurrentBehavior(BehaviorState currentBehavior) {
		this.currentBehavior = currentBehavior;
	}

	public enum StatusEffect {
		POISONED, PARALYZED, BURNED {{
			damage = 3;
		}};
		int duration, damage;
	}
	
	public void GiveStatusEffect(StatusEffect effect) {
		
		if(effect == StatusEffect.POISONED) { poisonEffect = effect; }
		if(effect == StatusEffect.PARALYZED) { paralysisEffect = effect; }
		if(effect == StatusEffect.BURNED) { burningEffect = effect; }
	}
	
	public enum FOWState {
		
	}
	
	public void UpdateStatus() {
		if (burningEffect != null && burningEffect.duration > 0) {
			currentHealth -= burningEffect.damage;
			burningEffect.duration--;
			System.out.print("burnDamage");
		}
	}	
}
