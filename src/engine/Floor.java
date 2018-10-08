package engine;

import java.util.ArrayList;
import java.util.Random;

public class Floor {
	private long seed;
	private Random randnum;
	private MapGenAlgorithm algorithm;
	
	private int numRows, numCols;
	private Tile[][] map;
	private ArrayList<LivingEntity> livingEntities;
	
	public Floor(long seed, MapGenAlgorithm algorithm) {
		this.seed = seed;
		randnum = new Random(seed);
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
		final int MAX_LEAF_SIZE = 20;
		ArrayList <BSPLeaf> _leafs = new ArrayList <BSPLeaf> ();
		BSPLeaf root = new BSPLeaf(0, 0, numCols, numRows);
		_leafs.add(root);
		
		boolean did_split = true;
		while (did_split) {
			did_split = false;
			for (BSPLeaf I : _leafs) {
				if (I.leftChild == null && I.rightChild == null) {
					//if this leaf is too big, or 75% chance
					if (I.width > MAX_LEAF_SIZE || I.height > MAX_LEAF_SIZE || randnum.nextDouble() > 0.25) {
						if (I.split()) {
							//if we split, push the child leafs to the vector
							_leafs.add(I.leftChild);
							_leafs.add(I.rightChild);
							did_split = true;
						}
					}
				}
			}
		}
		root.createRooms();
	}
	
	private void generateCAMap() {
	}
	private int getrandbetween(int min, int max) {
		return randnum.nextInt(max - min)+min;
	}
	
	protected class BSPLeaf{
		public int x, y, width, height;
		
		private final int MIN_LEAF_SIZE = 6;
		public BSPLeaf leftChild;
		public BSPLeaf rightChild;
		public Rectangle room;
		public ArrayList <Rectangle> halls;
		
		public BSPLeaf (int newx, int newy, int newwidth, int newheight){
			x = newx;
			y = newy;
			width = newwidth;
			height = newheight;
		}
		
		public boolean split() {
			if (leftChild != null || rightChild != null)
				return false; //already split
			boolean splitH = randnum.nextBoolean();
			if (width > height && width/height >= 1.25)
				splitH = false;
			else if (height > width && height/width >= 1.25)
				splitH = true;
			int max = (splitH ? height : width) - MIN_LEAF_SIZE;
			if (max <= MIN_LEAF_SIZE)
				return false;	//area is too small to split
			int split = randnum.nextInt(max - MIN_LEAF_SIZE) + MIN_LEAF_SIZE;
			if (splitH)
			{
				leftChild = new BSPLeaf(x, y, width, split);
				rightChild = new BSPLeaf(x, y + split, width, height - split);
			}
			else
			{
				leftChild = new BSPLeaf(x, y, split, height);
				rightChild = new BSPLeaf(x + split, y, width - split, height);
			}
			return true; //split successful
		}
		public void createRooms() {
			if (leftChild != null || rightChild != null) {
				if (leftChild != null)
					leftChild.createRooms();
				if (rightChild != null)
					rightChild.createRooms();
			}
			else {
				Position roomSize;
				Position roomPos;
				roomSize = new Position(getrandbetween(3, height - 2), getrandbetween(3, width - 2));
				roomPos = new Position(getrandbetween(1, height - roomSize.getCol() - 1), getrandbetween(1, width - roomSize.getRow() - 1));
				room = new Rectangle();
				room.x = x + roomPos.getCol();
				room.y = y + roomPos.getRow();
				room.width = roomSize.getCol();
				room.height = roomSize.getRow();
				
				for(int row = room.y; row < room.height; row++) {
					for(int col = room.x; col < room.width; col++) {
						map[row][col].setBaseEntity(new Ground());
					}
				}
			}
		}
	}
	
	public class Rectangle{
		int width, height, x, y;
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
