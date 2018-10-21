package engine;

public class Ground extends Entity {
	public static final String SPRITE_URL = "/assets/img/limestone_1.png";
	
	public Ground() {
		setImpassable(false);
		setImage(SPRITE_URL);
	}

}
