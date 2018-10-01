package engine;

import java.util.ArrayList;

public class Floor {
	private int seed;
	private MapGenAlgorithm algorithm;
	
	private int numRows, numCols;
	private Tile[][] map;
	private ArrayList<Entity> mobileEntities;
	
	public Floor(int seed, MapGenAlgorithm algorithm) {
		this.seed = seed;
		this.algorithm = algorithm;
		
		mobileEntities = new ArrayList<Entity>();
		
		instantiateMap();
	}
	
	private void instantiateMap() {
		numRows = Engine.DEFAULT_NUM_ROWS;
		numCols = Engine.DEFAULT_NUM_COLS;
		map = new Tile[numRows][numCols];
		
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCol; col++) {
				map[row][col] = new Tile();
			}
		}
	}
	
	public void generateFloorMap() {
		switch(algorith) {
		case BSP:
			generateBSPMap();
			break;
		case CELLULAR_AUTOMATA:
			generateCAMap();
		}
	}
	
	private void generateBSPMap() {
	}
	
	private void generateCAMap() {
	}
	
	public enum MapGenAlgorithm {
		BSP, CELLULAR_AUTOMATA;
	}
}
