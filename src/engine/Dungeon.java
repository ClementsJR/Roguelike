package engine;

import java.util.ArrayList;
import java.util.Random;

public class Dungeon {
	public static final int DEFAULT_NUM_ROWS = 32;
	public static final int DEFAULT_NUM_COLS = 32;
	
	private static final int MAX_NUM_FLOORS = 30;
	
	private ArrayList<Floor> floors;
	private int currentFloorIndex;
	
	public Dungeon() {
		floors = new ArrayList<Floor>();
		
		Random rand = new Random();
		long seed = rand.nextLong();
		
		generateNewFloor(seed, Floor.MapGenAlgorithm.BSP);
		
		currentFloorIndex = 0;
	}
	
	private void generateNewFloor(long seed, Floor.MapGenAlgorithm algorithm) {
		Floor firstFloor = new Floor(seed, algorithm);
		floors.add(firstFloor);
	}
	
	public Floor getCurrentFloor() {
		return floors.get(currentFloorIndex);
	}
	
	public Tile getCurrentTileAt(Position target) {
		return floors.get(currentFloorIndex).getTileAt(target);
	}
	
	public void goUpOneFloor() {
		
	}
	
	public void goDownOneFloor() {
		
	}
}
