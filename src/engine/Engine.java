package engine;

import java.util.ArrayList;
import java.util.LinkedList;

import engine.GameEvent.EventType;

public class Engine {
	public static final int DEFAULT_NUM_ROWS = 150;
	public static final int DEFAULT_NUM_COLS = 150;
	
	private static final int MAX_NUM_FLOORS = 30;
	
	private ArrayList<Floor> floors;
	private int currentFloorIdx;
	
	private PlayerCharacter player;
	private Tile playerTile;
	private int playerRow = 0, playerCol = 0;
	
	private LinkedList<GameEvent> eventQueue;
	
	public Engine() {
		floors = new ArrayList<Floor>();
		
		//The test floor is just for testing purposes. duh.
		Floor testFloor = new Floor(0, Floor.MapGenAlgorithm.TEST_ROOM);
		floors.add(testFloor);
		currentFloorIdx = 0;
		
		player = new PlayerCharacter();
		playerTile = new Tile();
		
		eventQueue = new LinkedList<GameEvent>();
		
		//Puts the player on the map. The location (4,4) is just for testing purposes.
		movePlayerTo(1,1);
		
		eventQueue.clear();
	}
	
	public Tile getTileAt(int row, int col) {
		return floors.get(currentFloorIdx).getTileAt(row, col);
	}

	public int getNumRows() {
		return floors.get(currentFloorIdx).getNumRows();
	}

	public int getNumCols() {
		return floors.get(currentFloorIdx).getNumCols();
	}
	
	public int getPlayerRow() {
		return playerRow;
	}
	
	public int getPlayerCol() {
		return playerCol;
	}
	
	public LinkedList<GameEvent> getEventQueue() {
		return eventQueue;
	}
	
	public void tileClicked(int row, int col) {
		if(isValidMove(row, col)) {
			movePlayerTo(row, col);
		}
	}
	
	private boolean isValidMove(int row, int col) {
		
		//TODO:
		//Test if the location is adjacent to the player and if there is anything in the way.
		
		return true;
	}
	
	private void movePlayerTo(int row, int col) {
		GameEvent moveRecord = new GameEvent(player, EventType.MOVES_TO);
		moveRecord.setSource(playerRow, playerCol);
		moveRecord.setTarget(row, col);
		eventQueue.add(moveRecord);
		
		playerTile.getOccupants().remove(player);
		playerTile = floors.get(currentFloorIdx).getTileAt(row, col);
		playerTile.addOccupant(player);
		
		playerRow = row;
		playerCol = col;
	}
}
