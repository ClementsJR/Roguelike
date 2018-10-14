package engine;

import javafx.scene.image.Image;

public class Stairs extends Entity {
	private int stairType;
	
	public static final String STAIR_UP_SPRITE_URL = "/assets/img/stair_up.png";
	public static final String STAIR_DOWN_SPRITE_URL = "/assets/img/stair_down.png";
	
	public Stairs(int type) {
		super(false);
		
		stairType = type;
		
		if(stairType == 0) {
			sprite = new Image(STAIR_UP_SPRITE_URL);
		} else {
			sprite = new Image(STAIR_DOWN_SPRITE_URL);
		}
	}
	
	public int getType() {
		return stairType;
	}
	
}