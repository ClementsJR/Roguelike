package engine;

import java.util.Random;

public class Range {
	private int lowerBound;
	private int upperBound;
	
	public Range(int lowerBound, int upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	public int getLowerBound() { return lowerBound; }
	public int getUpperBound() { return upperBound; }
	
	public void setLowerBound(int lowerBound) { this.lowerBound = lowerBound; }
	public void setUpperBound(int upperBound) { this.upperBound = upperBound; }
	
	public boolean isInRange(int num) {
		boolean isInRange;
		isInRange = (lowerBound <= num && num <= upperBound);
		return isInRange;
	}
	
	public int getRandomNum() {
		Random rand = new Random();
		
		int difference = upperBound - lowerBound;
		int result = rand.nextInt(difference + 1) + lowerBound;
		return result;
	}
}
