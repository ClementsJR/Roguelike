package engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import engine.GameEvent.EventType;

public class Engine {
	public static final int DEFAULT_NUM_ROWS = 32;
	public static final int DEFAULT_NUM_COLS = 32;
	
	private static final int MAX_NUM_FLOORS = 30;
	
	private ArrayList<Floor> floors;
	private int currentFloorIdx;
	
	private PlayerCharacter player;
	private Tile playerTile;
	private Position playerPosition;
	
	private LinkedList<GameEvent> eventQueue;
	
	public Engine() {
		floors = new ArrayList<Floor>();
		
		Random rand = new Random();
		long floorSeed = rand.nextLong();
		
		generateFloor(floorSeed, Floor.MapGenAlgorithm.CELLULAR_AUTOMATA);
		currentFloorIdx = 0;
		
		eventQueue = new LinkedList<GameEvent>();
		
		player = new PlayerCharacter();
		playerTile = new Tile();
		playerPosition = new Position(1, 1);
		
		//Puts the player on the map. The location (1,1) is just for testing purposes.
		movePlayerTo(playerPosition);
		
		eventQueue.clear();
	}
	
	private void generateFloor(long seed, Floor.MapGenAlgorithm algorithm) {
		Floor newFloor = new Floor(seed, algorithm);
		floors.add(newFloor);
	}
	
	public Tile getTileAt( Position position ) {
		return floors.get(currentFloorIdx).getTileAt(position);
	}

	public int getNumRows() {
		return floors.get(currentFloorIdx).getNumRows();
	}

	public int getNumCols() {
		return floors.get(currentFloorIdx).getNumCols();
	}
	
	public Position getPlayerPosition() {
		return playerPosition;
	}
	
	public LinkedList<GameEvent> getEventQueue() {
		return eventQueue;
	}
	
	public void tileClicked(Position clickedPosition) {
		if(isAdjacentMove(clickedPosition)) {
			Floor currentFloor = floors.get(currentFloorIdx);
			Tile target = currentFloor.getTileAt(clickedPosition);
			
			if(isOpenTile(target)) {
				movePlayerTo(clickedPosition);
			} else if(isEnemyPosition(target)) {
				playerAttacks(clickedPosition);
			} else if(isStairPosition(target)) {
				changeFloor(target);
			} else {
				return;
			}
		}
	}
	
	private boolean isAdjacentMove(Position targetPosition) {
		int rowDifference = playerPosition.getRow() - targetPosition.getRow();
		rowDifference = Math.abs(rowDifference);
		
		int colDifference = playerPosition.getCol() - targetPosition.getCol();
		colDifference = Math.abs(colDifference);
		
		boolean isAdjacent = (rowDifference <= 1 && colDifference <= 1);
		
		//return isAdjacent;
		return true;
	}
	
	private boolean isOpenTile(Tile target) {
		boolean validMove = true;
		
		if(target.isBlocked()) {
			validMove = false;
		}
		
		return validMove;
	}
	
	private boolean isEnemyPosition(Tile target) {
		boolean hasEnemy = false;
		
		for(Entity entity : target.getOccupants()) {
			if(entity.getClass() == Enemy.class) {
				hasEnemy = true;
				break;
			}
		}
		
		return hasEnemy;
	}
	
	private boolean isStairPosition(Tile target) {
		return false;
	}
	
	private void movePlayerTo(Position target) {
		GameEvent moveRecord = new GameEvent(player, playerPosition, EventType.MOVES_TO, target);
		eventQueue.add(moveRecord);
		
		playerTile.removeOccupant(player);
		playerTile = floors.get(currentFloorIdx).getTileAt(target);
		playerTile.addOccupant(player);
		
		playerPosition = target;
	}
	
	private void playerAttacks(Position target) {
		GameEvent attackRecord = new GameEvent(player, playerPosition, EventType.ATTACKS, target);
		eventQueue.add(attackRecord);
	}
	
	private void changeFloor(Tile stairTile) {
		//GameEvent changeFloorRecord = new GameEvent(player, playerPosition, EventType.CHANGES_FLOOR, );
		//eventQueue.add(changeFloorRecord);
	}
}
