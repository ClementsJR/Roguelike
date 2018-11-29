

public class Rectangle {

	private int firstRow, firstCol, height, width;
	
	public Rectangle() {
		
	}
	
	public Rectangle(int firstRow, int firstCol, int height, int width) {
		this.firstRow = firstRow;
		this.firstCol = firstCol;
		this.height = height;
		this.width = width;
	}
	
	public int getRow() { return firstRow; }
	public int getCol() { return firstCol; }
	public int getHeight() { return height; }
	public int getWidth() { return width; }
}
