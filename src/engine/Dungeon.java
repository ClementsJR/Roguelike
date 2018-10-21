package engine;

import java.util.ArrayList;
import java.util.Random;

public class Dungeon {
	public static final int DEFAULT_NUM_ROWS = 32;
	public static final int DEFAULT_NUM_COLS = 32;
	
	private static final int MAX_NUM_FLOORS = 30;
	private static final int FIRST_CAVE_FLOOR = 0;
	private static final int LAST_CAVE_FLOOR = 2;

	private Range cellularAutomataLevels;
	private ArrayList<Floor> floors;
	private int currentFloorIndex;
	
	public Dungeon() {
		cellularAutomataLevels = new Range(FIRST_CAVE_FLOOR, LAST_CAVE_FLOOR);
		
		floors = new ArrayList<Floor>();
		
		Random rand = new Random();
		long seed = rand.nextLong();
		
		boolean makeStairsUp = false;
		boolean makeStairsDown = true;
		generateNewFloor(seed, Floor.MapGenAlgorithm.CELLULAR_AUTOMATA, makeStairsUp, makeStairsDown);
		
		currentFloorIndex = 0;
	}
	
	private void generateNewFloor(long seed, Floor.MapGenAlgorithm algorithm, boolean makeStairsUp, boolean makeStairsDown) {
		Floor newFloor = new Floor(seed, algorithm, makeStairsUp, makeStairsDown);
		floors.add(newFloor);
	}
	
	public Floor getCurrentFloor() {
		return floors.get(currentFloorIndex);
	}
	
	public Tile getTileAt(Position target) {
		return floors.get(currentFloorIndex).getTileAt(target);
	}
	
	public void goUpOneFloor() {
		currentFloorIndex--;
	}
	
	public void goDownOneFloor() {
		currentFloorIndex++;
		
		if (currentFloorIndex == floors.size()) {
			Random rand = new Random();
			long seed = rand.nextLong();
			
			Floor.MapGenAlgorithm algorithm = (cellularAutomataLevels.isInRange(currentFloorIndex) ? Floor.MapGenAlgorithm.CELLULAR_AUTOMATA : Floor.MapGenAlgorithm.BSP);
			
			boolean makeStairsUp = true;
			boolean makeStairsDown = (currentFloorIndex != MAX_NUM_FLOORS);
			generateNewFloor(seed, algorithm, makeStairsUp, makeStairsDown);
		}
	}

	protected class Range {
		private int lowerBound;
		private int upperBound;
		
		public Range(int lowerBound, int upperBound) {
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
		}
		
		public boolean isInRange(int num) {
			boolean isInRange;
			isInRange = (lowerBound <= num && num <= upperBound);
			return isInRange;
		}
	}
}
