package engine;

import java.util.ArrayList;
import java.util.Random;

public class BSPLeaf {
	public static final int MIN_LEAF_SIZE = 6;
	public static final int MAX_LEAF_SIZE = 20;
	
	private Rectangle dimensions;
	private Rectangle room;
	private ArrayList<Rectangle> halls;
	
	private BSPLeaf leftChild;
	private BSPLeaf rightChild;
	
	private Random rand;
	
	public BSPLeaf (Rectangle newDimensions, Random newRand) {
		dimensions = newDimensions;
		rand = newRand;
	}
	
	public boolean split() {
		if (leftChild != null || rightChild != null) {
			//This leaf must have already been split
			return false;
		}
		
		int height = dimensions.getHeight();
		int width = dimensions.getWidth();
		
		boolean splitHorizontally = rand.nextBoolean();
		
		if (width > height && width/height >= 1.25) {
			splitHorizontally = false;
		} else if (height > width && height/width >= 1.25) {
			splitHorizontally = true;
		}
		
		int max = (splitHorizontally ? height : width) - MIN_LEAF_SIZE;
		
		if (max <= MIN_LEAF_SIZE) {
			//Area is too small to split
			return false;
		}
		
		int split = getRandomNumberBetween(MIN_LEAF_SIZE, max);
		
		int row = dimensions.getRow();
		int col = dimensions.getCol();
		height = dimensions.getHeight();
		width = dimensions.getWidth();
		
		Rectangle leftDim;
		Rectangle rightDim;
		
		if (splitHorizontally) {
			leftDim = new Rectangle(row, col, split, width);
			rightDim = new Rectangle(row + split, col, height - split, width);
			
		} else {
			leftDim = new Rectangle(row, col, height, split);
			rightDim = new Rectangle(row, col + split, height, width - split);
		}
		
		leftChild = new BSPLeaf(leftDim, rand);
		rightChild = new BSPLeaf(rightDim, rand);
		
		return true;
	}
	
	public void createRooms() {
		if (leftChild != null || rightChild != null) {
			if (leftChild != null)
				leftChild.createRooms();
			
			if (rightChild != null)
				rightChild.createRooms();
			
			if (leftChild != null && rightChild != null)
				createHall(leftChild.getRoom(), rightChild.getRoom());
			
		} else {
			Position roomStartPos;
			Position roomEndPos;
			
			//roomEndPos = new Position(getRandomNumberBetween(3, dimensions.getHeight() - 2), getRandomNumberBetween(3, dimensions.getWidth() - 2));
			//roomStartPos = new Position(getRandomNumberBetween(1, dimensions.getHeight() - roomEndPos.getCol() - 1), getRandomNumberBetween(1, dimensions.getWidth() - roomEndPos.getRow() - 1));
			
			roomEndPos = new Position(getRandomNumberBetween(3, dimensions.getHeight() - 2), getRandomNumberBetween(3, dimensions.getWidth() - 2));
			
			roomStartPos = new Position(dimensions.getRow() + getRandomNumberBetween(1, dimensions.getHeight() - roomEndPos.getRow() - 1), dimensions.getCol() + getRandomNumberBetween(1, dimensions.getWidth() - roomEndPos.getCol() - 1));
			
			room = new Rectangle(roomStartPos.getRow(), roomStartPos.getCol(), roomEndPos.getRow(), roomEndPos.getCol());
		}
		
	}
	
	public void createHall(Rectangle left, Rectangle right) {
		halls = new ArrayList<Rectangle> ();
		
		Position leftPos = new Position(getRandomNumberBetween(left.getRow() + 1, left.getRow() + left.getHeight() - 2), getRandomNumberBetween(left.getCol() + 1, left.getCol() + left.getWidth() - 2));
		Position pos2 = new Position(getRandomNumberBetween(right.getRow() + 1, right.getRow() + right.getHeight() - 2), getRandomNumberBetween(right.getCol() + 1, right.getCol() + right.getWidth() - 2));
		
		int w = pos2.getCol() - leftPos.getCol();
		int h = pos2.getRow() - leftPos.getRow();
		
		if (w < 0) {
			if (h < 0) {
				if (rand.nextDouble() < 0.5) {
					halls.add(new Rectangle(leftPos.getRow(), pos2.getCol(), 1, Math.abs(w)));
					halls.add(new Rectangle(pos2.getRow(), pos2.getCol(), Math.abs(h), 1));
				} else {
					halls.add(new Rectangle(leftPos.getRow(), pos2.getCol(), 1, Math.abs(w)));
					halls.add(new Rectangle(pos2.getRow(), leftPos.getCol(), Math.abs(h), 1));
				}
				
			} else if (h > 0) {
				if (rand.nextBoolean()) {
					halls.add(new Rectangle(leftPos.getRow(), pos2.getCol(), 1, Math.abs(w)));
					halls.add(new Rectangle(leftPos.getRow(), pos2.getCol(), Math.abs(h), 1));
				} else {
					halls.add(new Rectangle(pos2.getRow(), pos2.getCol(), 1, Math.abs(w)));
					halls.add(new Rectangle(leftPos.getRow(), leftPos.getCol(), Math.abs(h), 1));
				}
				
			} else {
				halls.add(new Rectangle(pos2.getRow(), pos2.getCol(), 1, Math.abs(w)));
			}
			
		} else if (w > 0) {
			if (h < 0) {
				if (rand.nextBoolean()) {
					halls.add(new Rectangle(pos2.getRow(), leftPos.getCol(), 1, Math.abs(w)));
					halls.add(new Rectangle(pos2.getRow(), leftPos.getCol(), Math.abs(h), 1));
				} else {
					halls.add(new Rectangle(leftPos.getRow(), leftPos.getCol(), 1, Math.abs(w)));
					halls.add(new Rectangle(pos2.getRow(), pos2.getCol(), Math.abs(h), 1));
				}
				
			} else if (h > 0) {
				if (rand.nextBoolean()) {
					halls.add(new Rectangle(leftPos.getRow(), leftPos.getCol(), 1, Math.abs(w)));
					halls.add(new Rectangle(leftPos.getRow(), pos2.getCol(), Math.abs(h), 1));
				} else {
					halls.add(new Rectangle(leftPos.getRow(), leftPos.getCol(), 1, Math.abs(w)));
					halls.add(new Rectangle(leftPos.getRow(), pos2.getCol(), Math.abs(h), 1));
				}
				
			} else {
				halls.add(new Rectangle(leftPos.getRow(), leftPos.getCol(), 1, Math.abs(w)));
			}
			
		} else {
			if (h < 0) {
				halls.add(new Rectangle(pos2.getRow(), pos2.getCol(), Math.abs(h), 1));
			} else if (h > 0) {
				halls.add(new Rectangle(leftPos.getRow(), leftPos.getCol(), Math.abs(h), 1));
			}
		}
		
	}
	
	public Rectangle getRoom() {
		if (room != null) {
			return room;
		} else {
			boolean hasLeftRoom = (leftChild != null);
			boolean hasRightRoom = (rightChild != null);

			Rectangle lRoom = new Rectangle();
			Rectangle rRoom = new Rectangle();
			
			if (hasLeftRoom)
				lRoom = leftChild.getRoom();
			
			if (hasRightRoom)
				rRoom = rightChild.getRoom();
			
			if (!hasLeftRoom && !hasRightRoom) {
				return null;
			} else if (!hasRightRoom) {
				return lRoom;
			} else if (!hasLeftRoom) {
				return rRoom;
			} else if (rand.nextBoolean()) {
				return lRoom;
			} else {
				return rRoom;
			}
		}
	}
	
	public int getHeight() { return dimensions.getHeight(); }
	public int getWidth() { return dimensions.getWidth(); }
	public BSPLeaf getLeftChild() { return leftChild; }
	public BSPLeaf getRightChild() { return rightChild; }
	public ArrayList<Rectangle> getHalls() { return halls; }
	
	private int getRandomNumberBetween(int min, int max) {
		if(max == min) {
			return 0;
		}
		
		if(min > max) {
			return getRandomNumberBetween(max, min);
		}
		
		return rand.nextInt(max - min) + min;
	}
}
