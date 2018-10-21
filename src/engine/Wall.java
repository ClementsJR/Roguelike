package engine;

public class Wall extends Entity {
	public static final String SPRITE_URL = "/assets/img/brick_gray_0.png";
	
	public Wall() {
		setImpassable(true);
		setImage(SPRITE_URL);
	}
}
