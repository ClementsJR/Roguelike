package engine;

import java.util.ArrayList;

public class Engine {
	public static final int DEFAULT_NUM_ROWS = 150;
	public static final int DEFAULT_NUM_COLS = 150;
	
	private static final int TOTAL_NUM_FLOORS = 30;
	
	private ArrayList<Floor> floors;
	private PlayerCharacter player;
	
	public Engine() {
		floors = new ArrayList<Floor>();
		//player = new Player;
	}
}
