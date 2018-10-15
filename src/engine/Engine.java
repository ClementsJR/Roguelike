package engine;

import java.util.ArrayList;
import java.util.LinkedList;

import engine.GameEvent.EventType;

public class Engine {

	private LinkedList<GameEvent> eventQueue;
	private Dungeon dungeon;
	private PlayerCharacter player;
	
	public Engine() {
		eventQueue = new LinkedList<GameEvent>();
		dungeon = new Dungeon();
		player = new PlayerCharacter();
		
		player.setCurrentPosition(dungeon.getCurrentFloor().getStairsUpPosition());
		movePlayerTo(player.getCurrentPosition());
		
		eventQueue.clear();
	}
	
	public Tile getTileAt(Position position) {
		return dungeon.getTileAt(position);
	}

	public int getNumRows() {
		return dungeon.getCurrentFloor().getNumRows();
	}

	public int getNumCols() {
		return dungeon.getCurrentFloor().getNumCols();
	}
	
	public LinkedList<GameEvent> getEventQueue() {
		return eventQueue;
	}
	
	public Position getPlayerPosition() {
		return player.getCurrentPosition();
	}

	public void UpKeyPressed() {
		tileSelected(new Position (player.getCurrentPosition().getRow() - 1, player.getCurrentPosition().getCol()));
	}
	
	public void DownKeyPressed() {
		tileSelected(new Position (player.getCurrentPosition().getRow() + 1, player.getCurrentPosition().getCol()));
	}

	public void LeftKeyPressed() {
		tileSelected(new Position (player.getCurrentPosition().getRow(), player.getCurrentPosition().getCol() - 1));

	}

	public void RightKeyPressed() {
		tileSelected(new Position (player.getCurrentPosition().getRow(), player.getCurrentPosition().getCol() + 1));
	}
	
	public void tileSelected(Position clickedPosition) {
		if(isAdjacentMove(clickedPosition)) {
			Tile target = dungeon.getTileAt(clickedPosition);
			
			if(isOpenTile(target) && !isStairTile(target)) {
				movePlayerTo(clickedPosition);
			} else if(hasEnemy(target)) {
				playerAttacks(clickedPosition);
			} else if(isStairTile(target)) {
				movePlayerTo(clickedPosition);
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
		
		return isAdjacent;
	}
	
	private boolean isOpenTile(Tile target) {
		boolean isOpenTile;
		isOpenTile = !target.isBlocked();
		return isOpenTile;
	}
	
	private boolean isStairTile(Tile target) {
		boolean isStair;
		isStair = (target.getBaseEntity().getClass() == Stairs.class);
		return isStair;
	}
	
	private boolean hasEnemy(Tile target) {
		boolean hasEnemy = false;
		
		for(Entity entity : target.getOccupants()) {
			if(entity.getClass() == Enemy.class) {
				hasEnemy = true;
				break;
			}
		}
		
		return hasEnemy;
	}
	
	private void movePlayerTo(Position target) {
		GameEvent moveRecord = new GameEvent(player, player.getCurrentPosition(), EventType.MOVES_TO, target);
		eventQueue.add(moveRecord);
		
		Tile currentPlayerTile = dungeon.getTileAt(player.getCurrentPosition());
		currentPlayerTile.removeOccupant(player);
		currentPlayerTile = dungeon.getTileAt(target);
		currentPlayerTile.addOccupant(player);
		
		player.setCurrentPosition(target);
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
			landing = dungeon.getCurrentFloor().getStairsDownPosition();
		} else {
			dungeon.goDownOneFloor();
			landing = dungeon.getCurrentFloor().getStairsUpPosition();
		}
		
		GameEvent changeFloorRecord = new GameEvent(player, player.getCurrentPosition(), EventType.CHANGES_FLOOR, landing);
		eventQueue.add(changeFloorRecord);
		
		//playerPosition = landing;
		
		Tile playerTile = dungeon.getTileAt(player.getCurrentPosition());
		
		playerTile.removeOccupant(player);
		playerTile = dungeon.getTileAt(landing);
		playerTile.addOccupant(player);
		
		player.setCurrentPosition(landing);
	}

	private void takeEnemyTurns() {
		ArrayList <LivingEntity> livingEntities = dungeon.getCurrentFloor().getLivingEntities();
		
		for(int i = 0; i < livingEntities.size(); i++)
		{
			Position source = livingEntities.get(i).getCurrentPosition();
			Position target = player.getCurrentPosition();
			Position nextStep = dungeon.getCurrentFloor().getPath(source, target);
		}
	}
	
}
