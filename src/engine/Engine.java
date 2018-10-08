package engine;

import java.util.ArrayList;
import java.util.LinkedList;

import engine.GameEvent.EventType;

public class Engine {
	public static final int DEFAULT_NUM_ROWS = 64;
	public static final int DEFAULT_NUM_COLS = 64;
	
	private static final int MAX_NUM_FLOORS = 30;
	
	private ArrayList<Floor> floors;
	private int currentFloorIdx;
	
	private PlayerCharacter player;
	private Tile playerTile;
	private Position playerPosition;
	
	private LinkedList<GameEvent> eventQueue;
	
	public Engine() {
		floors = new ArrayList<Floor>();
		
		//The test floor is just for testing purposes. duh.
		Floor testFloor = new Floor(0, Floor.MapGenAlgorithm.BSP);
		floors.add(testFloor);
		currentFloorIdx = 0;
		
		player = new PlayerCharacter();
		playerTile = new Tile();
		playerPosition = new Position(1, 1);
		
		eventQueue = new LinkedList<GameEvent>();
		
		//Puts the player on the map. The location (1,1) is just for testing purposes.
		movePlayerTo(playerPosition);
		
		eventQueue.clear();
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
		if(isValidMove(clickedPosition)) {
			movePlayerTo(clickedPosition);
			
		}
	}
	
	private boolean isValidMove(Position targetPosition) {
		
		//TODO:
		//Test if the location is adjacent to the player and if there is anything in the way.
		
		return true;
	}
	
	private void movePlayerTo(Position target) {
		GameEvent moveRecord = new GameEvent(player, playerPosition, EventType.MOVES_TO, target);
		eventQueue.add(moveRecord);
		
		playerTile.removeOccupant(player);
		playerTile = floors.get(currentFloorIdx).getTileAt(target);
		playerTile.addOccupant(player);
		
		playerPosition = target;
	}
}
