package engine;

import java.util.ArrayList;

public class Floor {
	private int seed;
	private MapGenerationAlgorithm algorithm;
	
	private int length, width;
	private Tile[][] map;
	private ArrayList<Entity> mobileEntities;
	
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
	
	public enum MapGenerationAlgorithm {
		BSP, CELLULAR_AUTOMATA;
	}
}
