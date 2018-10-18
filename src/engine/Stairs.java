package engine;

import javafx.scene.image.Image;

public class Stairs extends Entity {
	private StairType stairType;
	
	public static final String STAIR_UP_SPRITE_URL = "/assets/img/stair_up.png";
	public static final String STAIR_DOWN_SPRITE_URL = "/assets/img/stair_down.png";
	
	public Stairs(StairType type) {
		impassable = false;
		
		stairType = type;
		
		if(stairType == StairType.UP) {
			sprite = new Image(STAIR_UP_SPRITE_URL);
		} else {
			sprite = new Image(STAIR_DOWN_SPRITE_URL);
		}
	}
	
	public StairType getType() {
		return stairType;
	}
	
	public enum StairType {
		UP, DOWN;
	}
	
}