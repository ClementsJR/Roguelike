package engine;

import java.util.ArrayList;
import java.util.Random;

public class Floor {
	private long seed;
	private Random rand;
	private MapGenAlgorithm algorithm;
	
	private int numRows, numCols;
	private Tile[][] map;
	private ArrayList<LivingEntity> livingEntities;
	
	public Floor(long seed, MapGenAlgorithm algorithm) {
		this.seed = seed;
		rand = new Random(seed);
		
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
				map[row][col].setBaseEntity(new Wall());
			}
		}
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumCols() {
		return numCols;
	}
	
	public Tile getTileAt(Position target) {
		int row = target.getRow();
		int col = target.getCol();
		
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
		int maxSize = BSPLeaf.MAX_LEAF_SIZE;
		
		Rectangle dimensions = new Rectangle(1, 1, numRows - 1, numCols - 1);
		BSPLeaf root = new BSPLeaf(dimensions, rand);
		
		ArrayList<BSPLeaf> leaves = new ArrayList<BSPLeaf> ();
		leaves.add(root);
		
		boolean did_split = true;
		while (did_split) {
			did_split = false;
			
			for (int i = 0; i < leaves.size(); i++) {
				BSPLeaf leaf = leaves.get(i);
				
				if (leaf.getLeftChild() == null && leaf.getRightChild() == null) {
					//if this leaf is too big, or 75% chance
					if (leaf.getWidth() > maxSize || leaf.getHeight() > maxSize || rand.nextDouble() > 0.25) {
						if (leaf.split()) {
							//if we split, push the child leafs to the vector
							leaves.add(leaf.getLeftChild());
							leaves.add(leaf.getRightChild());
							did_split = true;
						}
					}
				}
			}
		}
		
		root.createRooms();
		
		for(int i=0; i<leaves.size(); i++) {
			BSPLeaf leaf = leaves.get(i);
			
			Rectangle room = leaf.getRoom();
			
			if(room.getRow() < 0  || room.getCol() < 0)
				continue;
			
			if(room.getRow() + room.getHeight() >= Engine.DEFAULT_NUM_ROWS || room.getCol() + room.getWidth() >= Engine.DEFAULT_NUM_ROWS)
				continue;
			
			for(int row = room.getRow(); row < room.getRow() + room.getHeight(); row++) {
				for(int col = room.getCol();col < room.getCol() + room.getWidth(); col++) {
					map[row][col].setBaseEntity(new Ground());
				}
			}

			ArrayList<Rectangle> halls = leaf.getHalls();
			
			if(halls == null) {
				continue;
			}
			
			for(int j = 0; j < halls.size(); j++) {
				Rectangle hall = halls.get(j);
				
				if(hall.getRow() < 0  || hall.getCol() < 0)
					continue;
				
				if(hall.getRow() + hall.getHeight() >= Engine.DEFAULT_NUM_ROWS || hall.getCol() + hall.getWidth() >= Engine.DEFAULT_NUM_ROWS)
					continue;
				
				for(int row = hall.getRow(); row < hall.getRow() + hall.getHeight(); row++) {
					for(int col = hall.getCol();col < hall.getCol() + hall.getWidth(); col++) {
						map[row][col].setBaseEntity(new Ground());
					}
				}
			}
		}
	}
	
	private void generateCAMap() {
	}
	
	private void generateTestRoom() {
		numRows = 10;
		numCols = 10;
		
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
	
	//TODO: Implement enemy generation here
	
	public enum MapGenAlgorithm {
		BSP, CELLULAR_AUTOMATA, TEST_ROOM;
	}
}
