package engine;

public class Position {
	private int row, col;
	
	public static final int PLAYER_INVENTORY = -1;
	public static final int OUT_OF_GAME = -2;
	
	public Position() {
		row = OUT_OF_GAME;
		col = OUT_OF_GAME;
	}
	
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public int getRow() { return row; }
	public int getCol() { return col; }
	
<<<<<<< HEAD
	
=======
	public boolean isInInventory() { return row == PLAYER_INVENTORY; }
	public boolean isOutOfGame() { return row == OUT_OF_GAME; }

>>>>>>> 97ad386cd5eeed60aecab04447cffe85ec9eaa89
	public boolean equals(Position p) {
		boolean isEqual = (row == p.getRow() && col == p.getCol());
		return isEqual;
	}
<<<<<<< HEAD
	public boolean isInInventory() { return row == PLAYER_INVENTORY; }
	public boolean isOutOfGame() { return row == OUT_OF_GAME; }
=======
>>>>>>> 97ad386cd5eeed60aecab04447cffe85ec9eaa89
}
