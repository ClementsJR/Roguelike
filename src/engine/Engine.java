package engine;

import java.util.ArrayList;

public class Engine {
	public static final int DEFAULT_NUM_ROWS = 150;
	public static final int DEFAULT_NUM_COLS = 150;
	
	private static final int TOTAL_NUM_FLOORS = 30;
	
	private ArrayList<Floor> floors;
	private int currentFloorIdx;
	
	private PlayerCharacter player;
	
	public Engine() {
		floors = new ArrayList<Floor>();
		
		Floor testFloor = new Floor(0, Floor.MapGenAlgorithm.TEST_ROOM);
		floors.add(testFloor);
		currentFloorIdx = 0;
		
		player = new PlayerCharacter();
		testFloor.getTileAt(3, 3).addOccupant(player);
	}
	
	public Tile getTileAt(int row, int col) {
		return floors.get(currentFloorIdx).getTileAt(row, col);
	}

	public int getNumRows() {
		return floors.get(currentFloorIdx).getNumRows();
	}

	public int getNumCols() {
		return floors.get(currentFloorIdx).getNumCols();
	}
	
	public void tileClicked(int row, int col) {
		System.out.println("Row: " + row + "\tCol: " + col);
	}
}
