package engine;

import java.util.ArrayList;
import java.util.Random;

public class Floor {
	private static final float CHANCE_TO_START_ALIVE = 0.40f;
	private static final int NUM_CELL_AUTOMATA_ITERATIONS = 5;
	
	private long seed;
	private Random rand;
	private MapGenAlgorithm algorithm;
	public boolean noStairsUp;
	public boolean noStairsDown;
	private Position stairUp;
	private Position stairDown;

	private int numRows, numCols;
	private Tile[][] map;
	private ArrayList<LivingEntity> livingEntities;
	private ArrayList<Position> openPositions;
	
	public Floor(long seed, MapGenAlgorithm algorithm, boolean DontMakeStairsUp, boolean DontMakeStairsDown) {
		this.seed = seed;
		rand = new Random(seed);
		
		this.algorithm = algorithm;
		
		livingEntities = new ArrayList<LivingEntity>();
		noStairsUp = DontMakeStairsUp;
		noStairsDown = DontMakeStairsDown;
		instantiateMapBase();
		generateMap();
		
		openPositions = new ArrayList<Position>();
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				if (!map[row][col].isBlocked())
					openPositions.add(new Position (row, col));
			}
		}
		
		addStairs();
		addEnemies();
	}
	
	private void addStairs() {
		int randPos = rand.nextInt(openPositions.size());
		stairUp = openPositions.remove(randPos);
		if (!noStairsUp) {
			map[stairUp.getRow()][stairUp.getCol()].setBaseEntity(new Stairs(Stairs.StairType.UP));
		}
		if (!noStairsDown) {
			randPos = rand.nextInt(openPositions.size());
			stairDown = openPositions.remove(randPos);
			map[stairDown.getRow()][stairDown.getCol()].setBaseEntity(new Stairs(Stairs.StairType.DOWN));
		}
	}
	
	private void addEnemies() {
		int randPos;
		for (int i = 0; i < 4; i++) {
			Skeleton skelly = new Skeleton();
			randPos = rand.nextInt(openPositions.size());
			skelly.setCurrentPosition(openPositions.remove(randPos));
			livingEntities.add(skelly);
			map[skelly.currentPosition.getRow()][skelly.getCurrentPosition().getCol()].addOccupant(skelly);
		}
	}
	
	private void instantiateMapBase() {
		numRows = Dungeon.DEFAULT_NUM_ROWS;
		numCols = Dungeon.DEFAULT_NUM_COLS;
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
	
	public ArrayList<LivingEntity> getLivingEntities() {
		return livingEntities;
	}
	
	public Position getPath(Position source, Position target) {
		
		Position nextStep = null;
		
		
		return nextStep;
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
			
			//if(!leaf.hasRoom())
			//	continue;
			
			Rectangle room = leaf.getRoom();
			
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
				
				for(int row = hall.getRow(); row < hall.getRow() + hall.getHeight(); row++) {
					for(int col = hall.getCol();col < hall.getCol() + hall.getWidth(); col++) {
						map[row][col].setBaseEntity(new Ground());
					}
				}
			}
		}
		/*int index1 = rand.nextInt(leaves.size());
		int index2 = rand.nextInt(leaves.size());
		while (index1 == index2) {
			index2 = rand.nextInt(leaves.size());
		}
		Rectangle room1 = leaves.get(index1).getRoom();
		Rectangle room2 = leaves.get(index2).getRoom();
		int centerRow = room1.getRow() + (room1.getHeight()/2);
		int centerCol = room1.getCol() + (room1.getWidth()/2);
		if (!noStairsDown) {
			map[centerRow][centerCol].setBaseEntity(new Stairs(Stairs.StairType.DOWN));
			stairDown = new Position(centerRow, centerCol);
		}
		centerRow = room2.getRow() + (room2.getHeight()/2);
		centerCol = room2.getCol() + (room2.getWidth()/2);
		if (!noStairsUp) {
			map[centerRow][centerCol].setBaseEntity(new Stairs(Stairs.StairType.UP));
		}
		stairUp = new Position(centerRow, centerCol);
		*/
	}
	
	private void generateCAMap() {
		boolean[][] cellmap = new boolean[numRows][numCols];
		cellmap = initializeCellMap(cellmap);
		
		for (int i = 0; i < NUM_CELL_AUTOMATA_ITERATIONS; i++) {
			cellmap = doSimulationStep (cellmap);
		}
		
		for (int row = 0; row < numRows; row ++) {
			for (int col = 0; col < numCols; col++) {
				if(row == 0 || row == numRows - 1 || col == 0 || col == numCols - 1) {
					map[row][col].setBaseEntity(new Wall());
				} else if (cellmap[row][col]) {
					map[row][col].setBaseEntity(new Wall());
				} else {
					map[row][col].setBaseEntity(new Ground());
				}
			}
		}
	}
	
	public Position GetStairUp() {
		return stairUp;
	}
	
	public Position GetStairDown() {
		return stairDown;
	}
	
	private boolean[][] initializeCellMap (boolean[][] map){
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				if (rand.nextFloat() < CHANCE_TO_START_ALIVE) {
					map[row][col] = true;
				}
			}
		}
		
		return map;
	}
	
	private boolean[][] doSimulationStep (boolean[][] oldMap){
		boolean[][] newMap = new boolean[numRows][numCols];
		
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				int livingNeighbors = countLivingNeighbors (oldMap, row, col);
				
				if (oldMap[row][col]) {
					newMap[row][col] = !(livingNeighbors < 3);
				} else {
					newMap[row][col] = (livingNeighbors > 4);
				}
			}
		}
		
		return newMap;
	}
	
	private int countLivingNeighbors (boolean[][] map, int row, int col) {
		int count = 0;
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				int neighborRow = row + i;
				int neighborCol = col + j;
				
				if (i == 0 && j == 0) {
					continue;
				} else if (neighborRow < 0 || neighborCol < 0 || neighborRow >= numRows || neighborCol >= numCols) {
					count++;
				} else if (map[neighborRow][neighborCol]) {
					count++;
				}
			}
		}
		
		return count;
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
