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
		
		player.setPosition(dungeon.getCurrentFloor().getStairsUpPosition());
		movePlayerTo(player.getPosition());
		
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
		return player.getPosition();
	}

	public void UpKeyPressed() {
		tileSelected(new Position (player.getPosition().getRow() - 1, player.getPosition().getCol()));
	}
	
	public void DownKeyPressed() {
		tileSelected(new Position (player.getPosition().getRow() + 1, player.getPosition().getCol()));
	}

	public void LeftKeyPressed() {
		tileSelected(new Position (player.getPosition().getRow(), player.getPosition().getCol() - 1));

	}

	public void RightKeyPressed() {
		tileSelected(new Position (player.getPosition().getRow(), player.getPosition().getCol() + 1));
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
		int rowDifference = player.getPosition().getRow() - targetPosition.getRow();
		rowDifference = Math.abs(rowDifference);
		
		int colDifference = player.getPosition().getCol() - targetPosition.getCol();
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
		GameEvent moveRecord = new GameEvent(player, player.getPosition(), EventType.MOVES_TO, target);
		eventQueue.add(moveRecord);
		
		Tile currentPlayerTile = dungeon.getTileAt(player.getPosition());
		currentPlayerTile.removeOccupant(player);
		currentPlayerTile = dungeon.getTileAt(target);
		currentPlayerTile.addOccupant(player);
		
		player.setPosition(target);
	}
		
	private void playerAttacks(Position target) {
		GameEvent attackRecord = new GameEvent(player, player.getPosition(), EventType.ATTACKS, target);
		eventQueue.add(attackRecord);
		Tile targetTile = getTileAt(target);
		for(Entity entity : targetTile.getOccupants()) {
			if(entity.getClass() == Enemy.class) {
				((LivingEntity)entity).Hit(player.attackPower);
				if (((LivingEntity)entity).currentHealth <= 0)
					targetTile.removeOccupant((LivingEntity)entity);
			}	
		}
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
		
		GameEvent changeFloorRecord = new GameEvent(player, player.getPosition(), EventType.CHANGES_FLOOR, landing);
		eventQueue.add(changeFloorRecord);
		
		//playerPosition = landing;
		
		Tile playerTile = dungeon.getTileAt(player.getPosition());
		
		playerTile.removeOccupant(player);
		playerTile = dungeon.getTileAt(landing);
		playerTile.addOccupant(player);
		
		player.setPosition(landing);
	}

	private void takeEnemyTurns() {
		ArrayList <LivingEntity> livingEntities = dungeon.getCurrentFloor().getLivingEntities();
		
		for(int i = 0; i < livingEntities.size(); i++)
		{
			Position source = livingEntities.get(i).getPosition();
			Position target = player.getPosition();
			Position nextStep = dungeon.getCurrentFloor().getPath(source, target);
		}
	}
	
}
