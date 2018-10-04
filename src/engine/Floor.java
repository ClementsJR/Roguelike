package engine;

import java.util.ArrayList;
import java.util.Random;

public class Floor {
	private long seed;
	private MapGenAlgorithm algorithm;
	
	private int numRows, numCols;
	private Tile[][] map;
	private ArrayList<LivingEntity> livingEntities;
	
	public Floor(long seed, MapGenAlgorithm algorithm) {
		this.seed = seed;
		this.algorithm = algorithm;
		
		livingEntities = new ArrayList<LivingEntity>();
		
		instantiateMapBase();
		generateMap();
	}
	
	private void instantiateMapBase() {
		numRows = Engine.DEFAULT_NUM_ROWS;
		numCols = Engine.DEFAULT_NUM_COLS;
		map = new Tile[numRows][numCols];
		
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				map[row][col] = new Tile();
			}
		}
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumCols() {
		return numCols;
	}
	
	public Tile getTileAt(int row, int col) {
		return map[row][col];
	}
	
	public void generateMap() {
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
		numRows = 32;
		numCols = 32;
		
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				if(row == 0 || row == numRows - 1 || col == 0 || col == numCols - 1) {
					map[row][col].setBaseEntity(new Wall());
				} else {
					map[row][col].setBaseEntity(new Ground());
				}
			}
		}
	}
	
	public enum MapGenAlgorithm {
		BSP, CELLULAR_AUTOMATA, TEST_ROOM;
	}
}
