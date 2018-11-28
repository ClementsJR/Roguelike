

import java.util.ArrayList;
import java.util.LinkedList;

public class Engine {

	private LinkedList<GameEvent> eventQueue;
	private Dungeon dungeon;
	private PlayerCharacter player;
	
	public Engine() {
		eventQueue = new LinkedList<GameEvent>();
		dungeon = new Dungeon();
		player = new Warrior();
		
		player.setPosition(dungeon.getCurrentFloor().getStairsUpPosition());
		movePlayerTo(player.getPosition());
		updatePlayerFOW();
		
		eventQueue.clear();
	}
	
	public Tile getTileAt(Position position) { return dungeon.getTileAt(position); }
	public int getNumRows() { return dungeon.getCurrentFloor().getNumRows(); }
	public int getNumCols() { return dungeon.getCurrentFloor().getNumCols(); }
	public LinkedList<GameEvent> getEventQueue() { return eventQueue; }
	public PlayerCharacter getPlayer() { return player; }
	public Position getPlayerPosition() { return player.getPosition(); }

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
			Tile target = getTileAt(clickedPosition);
			
			if(isOpenTile(target) && !isStairTile(target) && !isFoodTile(target)) {
				movePlayerTo(clickedPosition);
			} else if(hasLivingEntity(target)) {
				playerAttacks(clickedPosition);
			} else if(isStairTile(target)) {
				movePlayerTo(clickedPosition);
				changeFloor(target);
			} else if(isFoodTile(target)) {
				pickUpFood(target);
				movePlayerTo(clickedPosition);
			} else if(isArmorTile(target)) {
				movePlayerTo(clickedPosition);
				pickUpArmor(target);
			} else {
				return;
			}
			player.UpdateStatus();
			takeEnemyTurns();
		}
	}
	
	public ArrayList<LivingEntity> getLivingEntities() {
		return dungeon.getCurrentFloor().getLivingEntities();
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
	
	private boolean isFoodTile(Tile target) {
		boolean isFood = false;
		for (Entity entity : target.getOccupants()) {
			if (entity instanceof Food) {
				isFood = true;
				break;
			}
		}
		return isFood;
	}
	
	private boolean isArmorTile(Tile target) {
		boolean isArmor = false;
		for (Entity entity : target.getOccupants()) {
			if (entity instanceof Armor) {
				isArmor = true;
				break;
			}
		}
		return isArmor;
	}
	
	private boolean hasLivingEntity(Tile target) {
		boolean hasLivingEntity = false;
		
		for(Entity entity : target.getOccupants()) {
			if(entity instanceof LivingEntity) {
				hasLivingEntity = true;
				break;
			}
		}
		
		return hasLivingEntity;
	}
	
	private void movePlayerTo(Position target) {
		GameEvent moveRecord = new GameEvent(player, player.getPosition(), EventType.MOVES_TO, target);
		eventQueue.add(moveRecord);
		
		Tile oldPlayerTile = getTileAt(player.getPosition());
		oldPlayerTile.removeOccupant(player);
		
		Tile newPlayerTile = getTileAt(target);
		newPlayerTile.addOccupant(player);
		
		player.setPosition(target);
		
		updatePlayerFOW();
	}
	
	private void pickUpFood(Tile target) {
		if(player.hasFood == false) {
			for (Entity entity : target.getOccupants()) {
				if (entity instanceof Food) {
					target.removeOccupant(entity);
					player.GiveFood((Food)entity);
					break;
				}
			}
		}
	}
		
	private void pickUpArmor(Tile target) {
		for (Entity entity : target.getOccupants()) {
			if (entity instanceof Armor) {
				target.removeOccupant(entity);
				player.GiveArmor(((Armor)entity).type);
				break;
			}
		}
	}
	
	private void playerAttacks(Position target) {
		GameEvent attackRecord = new GameEvent(player, player.getPosition(), EventType.ATTACKS, target);
		eventQueue.add(attackRecord);
		
		Tile targetTile = getTileAt(target);
		for(int i = 0; i < targetTile.getOccupants().size(); i++) {
			Entity entity = targetTile.getOccupants().get(i);
			
			if(entity instanceof LivingEntity) {
				int damage = player.getRandomAttackDamage();
				damage = ((LivingEntity)entity).receiveDamage(damage);
				attackRecord.getEventType().setEventValue(damage);
				if (player.DealsStatusEffect() == true)
					((LivingEntity)entity).GiveStatusEffect(player.getStatusEffect());
				if (player instanceof Mage)
					((LivingEntity)entity).GiveStatusEffect(player.burningEffect);
				if (((LivingEntity)entity).getCurrentHealth() <= 0) {
					GameEvent deathRecord = new GameEvent(entity, entity.getPosition(), EventType.DIES);
					eventQueue.add(deathRecord);
					player.GiveExperience();
					targetTile.removeOccupant(entity);
					dungeon.getCurrentFloor().getLivingEntities().remove(entity);
				}
				
				break;
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
		
		Tile playerTile = dungeon.getTileAt(player.getPosition());
		
		playerTile.removeOccupant(player);
		playerTile = dungeon.getTileAt(landing);
		playerTile.addOccupant(player);
		
		player.setPosition(landing);
		
		player.resetFOW();
		updatePlayerFOW();
	}
	
	private void updatePlayerFOW() {
		player.setAllCurrentlySeenToWasSeen();
		
		Position startPosition = player.getPosition();
		int startDistance = player.getSightDistance();
		
		tryToSee(player, startPosition, startDistance);
	}
	
	private void tryToSee(LivingEntity entity, Position position, int distanceLeft) {
		Tile tile = dungeon.getTileAt(position);
		
		int fogOfWar = entity.getFOWAt(position);
		if(fogOfWar >= distanceLeft) {
			return;
		}
		
		entity.setCurrentlySeen(position, distanceLeft);
		
		if(distanceLeft == 0) {
			return;
		}
		
		int centerRow = position.getRow();
		int centerCol = position.getCol();
		
		for(int rowOffset = -1; rowOffset <= 1; rowOffset++) {
			for(int colOffset = -1; colOffset <= 1; colOffset++) {
				int row = centerRow + rowOffset;
				int col = centerCol + colOffset;
				
				if(row == centerRow && col == centerCol) {
					continue;
				} else if(row < 0 || col < 0 || row >= Dungeon.DEFAULT_NUM_ROWS || col >= Dungeon.DEFAULT_NUM_COLS) {
					continue;
				} else {
					if(!(isOpenTile(tile) || hasLivingEntity(tile))) {
						return;
					}
					tryToSee(entity, new Position(row, col), distanceLeft - 1);
				}
			}
		}
	}
	
	private void takeEnemyTurns() {
		ArrayList <LivingEntity> livingEntities = dungeon.getCurrentFloor().getLivingEntities();
		
		for(int i = 0; i < livingEntities.size(); i++)
		{
			LivingEntity entity = livingEntities.get(i);
			entity.UpdateStatus();
			Position source = entity.getPosition();
			Position target = player.getPosition();
			Position nextStep = dungeon.getCurrentFloor().getPath(source, target);
			
			int rowDifference = Math.abs(source.getRow() - target.getRow());
			int colDifference = Math.abs(source.getCol() - target.getCol());
			
			BehaviorState currentBehavior = livingEntities.get(i).getCurrentBehavior();
			if (currentBehavior == BehaviorState.IDLE) {
				if ((Math.abs(target.getRow() - source.getRow())) <= 4 && (Math.abs(target.getCol() - source.getCol())) <= 4) {
					livingEntities.get(i).setCurrentBehavior(BehaviorState.ENGAGED);
				}
				else {
					continue;
				}
			}
			currentBehavior = livingEntities.get(i).getCurrentBehavior();
			if (currentBehavior == BehaviorState.ENGAGED) {
				if (rowDifference <= 1 && colDifference <= 1) {
					GameEvent attackRecord = new GameEvent(entity, entity.getPosition(), EventType.ATTACKS, target);
					eventQueue.add(attackRecord);
					
					int damage = ((LivingEntity)entity).getRandomAttackDamage();
					if(entity instanceof Slime) {
						if (((Slime)entity).DealsStatusEffect() == true)
							player.GiveStatusEffect(((Slime)entity).getStatusEffect());
					}
					damage = player.receiveDamage(damage);
					attackRecord.getEventType().setEventValue(damage);
					
					if (player.getCurrentHealth() <= 0) {
						GameEvent deathRecord = new GameEvent(player, player.getPosition(), EventType.DIES);
						eventQueue.add(deathRecord);
						
						return;
					}
					continue;
				}
				else if (!nextStep.equals(source) ) {
					Tile sourceTile = getTileAt(source);
					Tile nextTile = getTileAt(nextStep);
					sourceTile.removeOccupant(livingEntities.get(i));
					nextTile.addOccupant(livingEntities.get(i));
					livingEntities.get(i).setPosition(nextStep);
					
					GameEvent moveRecord = new GameEvent(livingEntities.get(i), source, EventType.MOVES_TO, nextStep);
					eventQueue.add(moveRecord);				
				}
				else {
					//This is in case there is no path
					continue;
				}
			}
			
		}
	}
}