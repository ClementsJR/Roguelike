package engine;

import java.util.ArrayList;
import java.util.Random;

public class Floor {
	private static final float CHANCE_TO_START_ALIVE = 0.40f;
	private static final int NUM_CELL_AUTOMATA_ITERATIONS = 5;
	
	private long seed;
	private Random rand;
	private MapGenAlgorithm algorithm;
	
	public boolean makeStairsUp;
	public boolean makeStairsDown;
	private Position stairsUpPosition;
	private Position stairsDownPosition;

	private int numRows, numCols;
	private Tile[][] map;
	private ArrayList<LivingEntity> livingEntities;
	private ArrayList<Position> openPositions;
	
	public Floor(long seed, MapGenAlgorithm algorithm, boolean makeStairsUp, boolean makeStairsDown) {
		this.seed = seed;
		rand = new Random(seed);
		
		this.algorithm = algorithm;
		
		this.makeStairsUp = makeStairsUp;
		this.makeStairsDown = makeStairsDown;
		
		instantiateMapBase();
		generateMap();
		
		livingEntities = new ArrayList<LivingEntity>();
		openPositions = new ArrayList<Position>();
		findOpenPositions();
		
		addStairs();
		addEnemies();
	}
	
	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }
	public Position getStairsUpPosition() { return stairsUpPosition; }
	public Position getStairsDownPosition() { return stairsDownPosition; }
	public ArrayList<LivingEntity> getLivingEntities() { return livingEntities; }
	
	public Tile getTileAt(Position target) {
		int row = target.getRow();
		int col = target.getCol();
		
		return map[row][col];
	}
	
	public Position getPath(Position source, Position target) {
		ArrayList<Position> openTiles = new ArrayList<Position>();
		ArrayList<Position> closedTiles = new ArrayList<Position>();
		Position cameFrom[][] = new Position[numRows][numCols];
		int costs[][] = new int[numRows][numCols];
		int movementCosts[][] = new int[numRows][numCols];
		Position nextStep = null;
		
		int targetRow = target.getRow();
		int targetCol = target.getCol();
		
		openTiles.add(source);
		costs[source.getRow()][source.getCol()] = ((Math.abs(targetRow - source.getRow()) + (Math.abs(targetCol - source.getCol()))));
		movementCosts[source.getRow()][source.getCol()] = 0;
		
		int lowestCostIndex = 0;
		int lowestCost = costs[source.getRow()][source.getCol()];
		if (openTiles.size() == 0) {
			return source;
		}
		
		while (!openTiles.get(lowestCostIndex).equals(target))
		{
			lowestCostIndex = 0;
			lowestCost = costs[openTiles.get(0).getRow()][openTiles.get(0).getCol()];
			for (int k = 0; k < openTiles.size(); k++)
			{
				int r = openTiles.get(k).getRow();
				int c = openTiles.get(k).getCol();
				int compCost = costs[r][c];
				if (compCost < lowestCost) {
					lowestCost = compCost;
					lowestCostIndex = k;
				}
			}
			Position current = openTiles.get(lowestCostIndex);
			openTiles.remove(lowestCostIndex);
			closedTiles.add(current);
			
			int centerRow = current.getRow();
			int centerCol = current.getCol();
			
			for(int rowOffset = -1; rowOffset <= 1; rowOffset++) {
				for(int colOffset = -1; colOffset <= 1; colOffset++) {
					int row = centerRow + rowOffset;
					int col = centerCol + colOffset;
					
					if(row == centerRow && col == centerCol) {
						continue;
					} else if(row < 0 || col < 0 || row >= numRows || col >= numCols) {
						continue;
					} else {
						Tile tile = map[row][col];
						if(tile.isBlocked() && !target.equals(new Position(row, col))) {
							continue;
						}
						boolean isPresentOpen = false;
						boolean isPresentClosed = false;
						for (int i = 0; i < openTiles.size(); i++) {
							if (openTiles.get(i).equals(new Position(row, col))) {
								isPresentOpen = true;
							}
						}
						for (int i = 0; i < closedTiles.size(); i++) {
							if (closedTiles.get(i).equals(new Position(row, col))) {
								isPresentClosed = true;
							}
						}					
						if (isPresentOpen == false && isPresentClosed == false) {
							openTiles.add(new Position(row, col));
							costs[row][col] = ((Math.abs(targetRow - row)) + (Math.abs(targetCol - col)) + movementCosts[centerRow][centerCol]);
							cameFrom[row][col] = current;								
						}
					}
				}
			}
			if (!(openTiles.isEmpty())) {
				lowestCostIndex = 0;
				lowestCost = costs[openTiles.get(0).getRow()][openTiles.get(0).getCol()];
				for (int k = 0; k < openTiles.size(); k++) {
					int r = openTiles.get(k).getRow();
					int c = openTiles.get(k).getCol();
					int compCost = costs[r][c];
					if (compCost < lowestCost) {
						lowestCost = compCost;
						lowestCostIndex = k;
					}
				}	
			}
			else {
				return source;
			}
		}
		
		Position crumb = cameFrom[target.getRow()][target.getCol()];
		if (crumb.equals(source)) {
			
		}
		while (crumb != null && cameFrom[crumb.getRow()][crumb.getCol()] != null && !cameFrom[crumb.getRow()][crumb.getCol()].equals(source)) {
			crumb = cameFrom[crumb.getRow()][crumb.getCol()];
		}
		nextStep = crumb;
		return nextStep;
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
	
	private void generateMap() {
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
		
		boolean didSplit = true;
		while (didSplit) {
			didSplit = false;
			
			for (int i = 0; i < leaves.size(); i++) {
				BSPLeaf leaf = leaves.get(i);
				
				if (leaf.getLeftChild() == null && leaf.getRightChild() == null) {
					//if this leaf is too big, or 75% chance
					if (leaf.getWidth() > maxSize || leaf.getHeight() > maxSize || rand.nextDouble() > 0.25) {
						boolean result = leaf.split();
						
						if (result) {
							//if we split, push the child leaves to the vector
							leaves.add(leaf.getLeftChild());
							leaves.add(leaf.getRightChild());
							didSplit = true;
						}
					}
				}
			}
		}
		
		root.createRooms();
		
		for(int i = 0; i < leaves.size(); i++) {
			BSPLeaf leaf = leaves.get(i);
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
	
	private void findOpenPositions() {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				if (!map[row][col].isBlocked()) {
					openPositions.add(new Position (row, col));
				}
			}
		}
	}
	
	private void addStairs() {
		int randPos = rand.nextInt(openPositions.size());
		stairsUpPosition = openPositions.remove(randPos);
		
		if(makeStairsUp) {
			map[stairsUpPosition.getRow()][stairsUpPosition.getCol()].setBaseEntity(new Stairs(Stairs.StairType.UP));
		}
		
		if(makeStairsDown) {
			randPos = rand.nextInt(openPositions.size());
			stairsDownPosition = openPositions.remove(randPos);
			map[stairsDownPosition.getRow()][stairsDownPosition.getCol()].setBaseEntity(new Stairs(Stairs.StairType.DOWN));
		}
	}
	
	private void addEnemies() {
		int randPos;
		
		for(int i = 0; i < 10; i++) {
			randPos = rand.nextInt(openPositions.size());
			
			Skeleton skelly = new Skeleton();
			Position spawnPosition = openPositions.remove(randPos);
			skelly.setPosition(spawnPosition);
			
			livingEntities.add(skelly);
			map[spawnPosition.getRow()][spawnPosition.getCol()].addOccupant(skelly);
		}
	}
	
	public enum MapGenAlgorithm {
		BSP, CELLULAR_AUTOMATA, TEST_ROOM;
	}
}
