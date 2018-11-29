

import java.util.ArrayList;

public class Tile {
	private boolean blocked;
	private Entity baseEntity;
	private ArrayList<Entity> occupants;
	private boolean occupied;
	
	public Tile() {
		occupants = new ArrayList<Entity>();
	}
	
	public ArrayList<Entity> getOccupants() {
		return occupants;
	}
	
	public boolean isBlocked() {
		return blocked;
	}
	
	public Entity getBaseEntity() {
		return baseEntity;
	}
	
	public boolean isOccupied() {
		return occupied;
	}
	
	public boolean isWall() {
		if (blocked == true && occupied == false)	{
			return true;
		}
		else
			return false;
	}
	
	public void addOccupant(Entity newOccupant) {
		if(!blocked) {
			occupants.add(newOccupant);
			
			if(newOccupant.isImpassable()) {
				blocked = true;
				occupied = true;
			}
		}
	}
	
	public void removeOccupant(Entity entity) {
		boolean removed = occupants.remove(entity);
		
		if(removed) {
			blocked = false;
			occupied = false;
		}
	}
	
	public void setBaseEntity(Entity base) {
		baseEntity = base;
		
		if(baseEntity.isImpassable()) {
			blocked = true;
		} else {
			blocked = false;
		}
	}
}
