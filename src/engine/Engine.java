package engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import engine.GameEvent.EventType;

public class Engine {
	
	private Dungeon dungeon;
	
	private PlayerCharacter player;
	
	private LinkedList<GameEvent> eventQueue;
	
	public Engine() {
		dungeon = new Dungeon();
		
		eventQueue = new LinkedList<GameEvent>();
		
		player = new PlayerCharacter();
		
		//The location (1,1) is just for testing purposes.
		player.setCurrentPosition(dungeon.getCurrentFloor().GetStairUp());
		movePlayerTo(player.getCurrentPosition());
		
		eventQueue.clear();
	}
	
	public void UpKeyPressed() {
		tileClicked(new Position (player.getCurrentPosition().getRow() - 1, player.getCurrentPosition().getCol()));
	}
	
	public void DownKeyPressed() {
		tileClicked(new Position (player.getCurrentPosition().getRow() + 1, player.getCurrentPosition().getCol()));
	}

	public void LeftKeyPressed() {
		tileClicked(new Position (player.getCurrentPosition().getRow(), player.getCurrentPosition().getCol() - 1));

	}

	public void RightKeyPressed() {
		tileClicked(new Position (player.getCurrentPosition().getRow(), player.getCurrentPosition().getCol() + 1));
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
		return player.getCurrentPosition();
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
		int rowDifference = player.getCurrentPosition().getRow() - targetPosition.getRow();
		rowDifference = Math.abs(rowDifference);
		
		int colDifference = player.getCurrentPosition().getCol() - targetPosition.getCol();
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
		GameEvent moveRecord = new GameEvent(player, player.getCurrentPosition(), EventType.MOVES_TO, target);
		eventQueue.add(moveRecord);
		
		Tile currentPlayerTile = dungeon.getCurrentTileAt(player.getCurrentPosition());
		currentPlayerTile.removeOccupant(player);
		currentPlayerTile = dungeon.getCurrentTileAt(target);
		currentPlayerTile.addOccupant(player);
		
		player.setCurrentPosition(target);
	}
	
	public void takeEnemyTurns() {
		ArrayList <LivingEntity> livingEntities = dungeon.getCurrentFloor().getLivingEntities();
		
		for(int i = 0; i < livingEntities.size(); i++)
		{
			Position source = livingEntities.get(i).getCurrentPosition();
			Position target = player.getCurrentPosition();
			Position nextStep = dungeon.getCurrentFloor().getPath(source, target);
		}
	}
		
	private void playerAttacks(Position target) {
		GameEvent attackRecord = new GameEvent(player, player.getCurrentPosition(), EventType.ATTACKS, target);
		eventQueue.add(attackRecord);
	}
	
	private void changeFloor(Tile stairTile) {
		Stairs.StairType stairType = ((Stairs) stairTile.getBaseEntity()).getType();
		Position landing;
		
		if(stairType == Stairs.StairType.UP) {
			dungeon.goUpOneFloor();
			landing = dungeon.getCurrentFloor().GetStairDown();
		} else {
			dungeon.goDownOneFloor();
			landing = dungeon.getCurrentFloor().GetStairUp();
		}
		
		GameEvent changeFloorRecord = new GameEvent(player, player.getCurrentPosition(), EventType.CHANGES_FLOOR, landing);
		eventQueue.add(changeFloorRecord);
		
		//playerPosition = landing;
		
		Tile playerTile = dungeon.getCurrentTileAt(player.getCurrentPosition());
		
		playerTile.removeOccupant(player);
		playerTile = dungeon.getCurrentTileAt(landing);
		playerTile.addOccupant(player);
		
		player.setCurrentPosition(landing);
	}
}
