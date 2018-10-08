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
		
		Rectangle dimensions = new Rectangle(0, 0, numRows, numCols);
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
		
		/*for(int i=0; i<leaves.size(); i++) {
			BSPLeaf leaf = leaves.get(i);
			
			Rectangle room = leaf.getRoom();
			for(int row = room.getRow(); row < room.getHeight(); row++) {
				for(int col = room.getCol();col < room.getWidth(); col++) {
					map[row][col].setBaseEntity(new Ground());
				}
			}
		}*/
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
