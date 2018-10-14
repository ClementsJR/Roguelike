package engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import engine.GameEvent.EventType;

public class Engine {
	
	private Dungeon dungeon;
	
	private PlayerCharacter player;
	private Tile playerTile;
	private Position playerPosition;
	
	private LinkedList<GameEvent> eventQueue;
	
	public Engine() {
		dungeon = new Dungeon();
		
		eventQueue = new LinkedList<GameEvent>();
		
		player = new PlayerCharacter();
		playerTile = new Tile();
		playerPosition = new Position(1, 1);
		
		//Puts the player on the map. The location (1,1) is just for testing purposes.
		movePlayerTo(playerPosition);
		
		eventQueue.clear();
	}
	
	public Tile getTileAt(Position position) {
		return dungeon.getCurrentTileAt(position);
	}

	public int getNumRows() {
		return dungeon.getCurrentFloor().getNumRows();
	}

	public int getNumCols() {
		return dungeon.getCurrentFloor().getNumCols();
	}
	
	public Position getPlayerPosition() {
		return playerPosition;
	}
	
	public LinkedList<GameEvent> getEventQueue() {
		return eventQueue;
	}
	
	public void tileClicked(Position clickedPosition) {
		if(isAdjacentMove(clickedPosition)) {
			Tile target = dungeon.getCurrentTileAt(clickedPosition);
			
			if(isOpenTile(target) && !isStairPosition(target)) {
				movePlayerTo(clickedPosition);
			} else if(isEnemyPosition(target)) {
				playerAttacks(clickedPosition);
			} else if(isStairPosition(target)) {
				changeFloor(target);
			} else {
				return;
			}
			
			takeEnemyTurns();
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
		boolean isStair = target.getBaseEntity().getClass() == Stairs.class;
		return isStair;
	}
	
	private void movePlayerTo(Position target) {
		GameEvent moveRecord = new GameEvent(player, playerPosition, EventType.MOVES_TO, target);
		eventQueue.add(moveRecord);
		
		playerTile.removeOccupant(player);
		playerTile = dungeon.getCurrentTileAt(target);
		playerTile.addOccupant(player);
		
		playerPosition = target;
	}
	
	public void takeEnemyTurns() {
		ArrayList <LivingEntity> livingEntities = dungeon.getCurrentFloor().getLivingEntities();
		
		for(int i = 0; i < livingEntities.size(); i++)
		{
			Position source = livingEntities.get(i).getCurrentPosition();
			Position target = playerPosition;
			Position nextStep = dungeon.getCurrentFloor().getPath(source, target);
		}
	}
	
	
	
	private void playerAttacks(Position target) {
		GameEvent attackRecord = new GameEvent(player, playerPosition, EventType.ATTACKS, target);
		eventQueue.add(attackRecord);
	}
	
	private void changeFloor(Tile stairTile) {
		int stairType = ((Stairs) stairTile.getBaseEntity()).getType();
		Position landing;
		
		if(stairType == 0) {
			dungeon.goUpOneFloor();
			landing = dungeon.getCurrentFloor().GetStairDown();
		} else {
			dungeon.goDownOneFloor();
			landing = dungeon.getCurrentFloor().GetStairUp();
		}
		
		GameEvent changeFloorRecord = new GameEvent(player, playerPosition, EventType.CHANGES_FLOOR, landing);
		eventQueue.add(changeFloorRecord);
		
		playerPosition = landing;
		
		playerTile.removeOccupant(player);
		playerTile = dungeon.getCurrentTileAt(landing);
		playerTile.addOccupant(player);
	}
}
