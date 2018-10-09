package engine;

import java.util.ArrayList;

public class Tile {
	private boolean blocked;
	private Entity baseEntity;
	private ArrayList<Entity> occupants;
	
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
	
	public void addOccupant(Entity newOccupant) {
		if(!blocked) {
			occupants.add(newOccupant);
			
			if(newOccupant.isImpassable()) {
				blocked = true;
			}
		}
	}
	
	public void removeOccupant(Entity entity) {
		boolean removed = occupants.remove(entity);
		
		if(removed) {
			blocked = false;
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
