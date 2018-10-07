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
		occupants.add(newOccupant);
	}
	
	public void removeOccupant(Entity entity) {
		occupants.remove(entity);
	}
	
	public void setBaseEntity(Entity base) {
		baseEntity = base;
	}
}
