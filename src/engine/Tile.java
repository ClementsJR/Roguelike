package engine;

import java.util.ArrayList;

public class Tile {
	private ArrayList<Entity> occupants;
	
	public Tile() {
		occupants = new ArrayList<Entity>();
	}
	
	public ArrayList<Entity> getOccupants() {
		return occupants;
	}
	
	public void addOccupant(Entity newOccupant) {
		occupants.add(newOccupant);
	}
}
