package engine;

public class Rectangle {

	private int firstRow, firstCol, height, width;
	
	public Rectangle() {
		
	}
	
	public Rectangle(int newFirstRow, int newFirstCol, int newHeight, int newWidth) {
		firstRow = newFirstRow;
		firstCol = newFirstCol;
		height = newHeight;
		width = newWidth;
	}
	
	public int getRow() { return firstRow; }
	public int getCol() { return firstCol; }
	public int getHeight() { return height; }
	public int getWidth() { return width; }
}
