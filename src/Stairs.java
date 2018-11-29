

public class Stairs extends Entity {
	private StairType stairType;
	
	public static final String STAIR_UP_SPRITE_URL = "/assets/img/stair_up.png";
	public static final String STAIR_DOWN_SPRITE_URL = "/assets/img/stair_down.png";
	
	public Stairs(StairType type) {
		setImpassable(false);
		
		stairType = type;
		
		if(stairType == StairType.UP) {
			setImage(STAIR_UP_SPRITE_URL);
		} else {
			setImage(STAIR_DOWN_SPRITE_URL);
		}
	}
	
	public StairType getType() {
		return stairType;
	}
	
	public enum StairType {
		UP, DOWN;
	}
	
}