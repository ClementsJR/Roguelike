package engine;

import java.util.ArrayList;
import java.util.Random;

public class Floor {
	private long seed;
	private MapGenAlgorithm algorithm;
	
	private int numRows, numCols;
	private Tile[][] map;
	private ArrayList<MobileEntity> mobileEntities;
	
	public Floor(long seed, MapGenAlgorithm algorithm) {
		this.seed = seed;
		this.algorithm = algorithm;
		
		mobileEntities = new ArrayList<MobileEntity>();
		
		instantiateMap();
	}
	
	private void instantiateMap() {
		numRows = Engine.DEFAULT_NUM_ROWS;
		numCols = Engine.DEFAULT_NUM_COLS;
		map = new Tile[numRows][numCols];
		
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				map[row][col] = new Tile();
			}
		}
	}
	
	public void generateFloorMap() {
		switch(algorithm) {
		case BSP:
			generateBSPMap();
			break;
		case CELLULAR_AUTOMATA:
			generateCAMap();
			break;
		case TEST_ROOM:
			generateTestRoom();
		}
	}
	
	private void generateBSPMap() {
	}
	
	private void generateCAMap() {
	}
	
	private void generateTestRoom() {
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				map[row][col].setBaseEntity(new Wall());
			}
		}
	}
	
	public enum MapGenAlgorithm {
		BSP, CELLULAR_AUTOMATA, TEST_ROOM;
	}
}
