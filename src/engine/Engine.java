package engine;

import java.util.ArrayList;

public class Engine {
	public static final DEFAULT_NUM_ROWS = 150;
	public static final DEFAULT_NUM_COLS = 150;
	
	private static final TOTAL_NUM_FLOORS = 30;
	
	private ArrayList<Floor> floors;
	private Player player;
	
	public Engine() {
		floors = new ArrayList<Floor>();
		player = new Player;
	}
}
