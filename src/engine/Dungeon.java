package engine;

import java.util.ArrayList;
import java.util.Random;

public class Dungeon {
	public static final int DEFAULT_NUM_ROWS = 32;
	public static final int DEFAULT_NUM_COLS = 32;
	
	private static final int MAX_NUM_FLOORS = 30;
	private static final int FIRST_CAVE_FLOOR = -1;
	private static final int LAST_CAVE_FLOOR = 0;

	private Range cellularAutomataLevels;
	private ArrayList<Floor> floors;
	private int currentFloorIndex;
	
	private int b1Spawn; //range 0-4
	private int b2Spawn; //range 5-9
	private int b3Spawn; //range 10-14
	private int b4Spawn; //range 15-19
	private int b5Spawn; //range 20-24
	private int b6Spawn; //range 25-29
	private int c1Spawn; //range 0-9
	private int c2Spawn; //range 10-19
	private int c3Spawn; //range 20-29
	
	public Dungeon() {
		cellularAutomataLevels = new Range(FIRST_CAVE_FLOOR, LAST_CAVE_FLOOR);
		
		floors = new ArrayList<Floor>();
		
		Random rand = new Random();
		long seed = rand.nextLong();
		
		boolean makeStairsUp = false;
		boolean makeStairsDown = true;
		
		currentFloorIndex = 0;
		
		generateNewFloor(seed, Floor.MapGenAlgorithm.BSP, makeStairsUp, makeStairsDown);
		
		b1Spawn=rand.nextInt(5);
		b2Spawn=rand.nextInt(5)+5;
		b3Spawn=rand.nextInt(5)+10;
		b4Spawn=rand.nextInt(5)+15;
		b5Spawn=rand.nextInt(5)+20;
		b6Spawn=rand.nextInt(5)+25;
		c1Spawn=rand.nextInt(10);
		c2Spawn=rand.nextInt(10)+10;
		c3Spawn=rand.nextInt(10)+20;
		
		if(b1Spawn == 0) {
			floors.get(currentFloorIndex).addArmor(new Armor(Armor.ArmorType.B1));
		}
		
		if(c1Spawn == 0) {
			floors.get(currentFloorIndex).addArmor(new Armor(Armor.ArmorType.C1));
		}
	}
	
	private void generateNewFloor(long seed, Floor.MapGenAlgorithm algorithm, boolean makeStairsUp, boolean makeStairsDown) {
		Floor newFloor = new Floor(seed, algorithm, makeStairsUp, makeStairsDown, currentFloorIndex);
		floors.add(newFloor);
	}
	
	public Floor getCurrentFloor() {
		return floors.get(currentFloorIndex);
	}
	
	public Tile getTileAt(Position target) {
		return floors.get(currentFloorIndex).getTileAt(target);
	}
	
	public void goUpOneFloor() {
		currentFloorIndex--;
	}
	
	public void goDownOneFloor() {
		currentFloorIndex++;
		
		if (currentFloorIndex == floors.size()) {
			Random rand = new Random();
			long seed = rand.nextLong();
			
			Floor.MapGenAlgorithm algorithm = (cellularAutomataLevels.isInRange(currentFloorIndex) ? Floor.MapGenAlgorithm.CELLULAR_AUTOMATA : Floor.MapGenAlgorithm.BSP);
			
			boolean makeStairsUp = true;
			boolean makeStairsDown = (currentFloorIndex != MAX_NUM_FLOORS);
			generateNewFloor(seed, algorithm, makeStairsUp, makeStairsDown);
			
			if(b1Spawn == currentFloorIndex) {
				floors.get(currentFloorIndex).addArmor(new Armor(Armor.ArmorType.B1));
			}
			if(b2Spawn == currentFloorIndex) {
					floors.get(currentFloorIndex).addArmor(new Armor(Armor.ArmorType.B2));
			}
			if(b3Spawn == currentFloorIndex) {
					floors.get(currentFloorIndex).addArmor(new Armor(Armor.ArmorType.B3));		
			}
		
			if(b4Spawn == currentFloorIndex) {
				floors.get(currentFloorIndex).addArmor(new Armor(Armor.ArmorType.B4));	
			}
			
			if(b5Spawn == currentFloorIndex) {
				floors.get(currentFloorIndex).addArmor(new Armor(Armor.ArmorType.B5));	
			}
			
			if(b6Spawn == currentFloorIndex) {
				floors.get(currentFloorIndex).addArmor(new Armor(Armor.ArmorType.B6));	
			}
			
			if(c1Spawn == currentFloorIndex) {
				floors.get(currentFloorIndex).addArmor(new Armor(Armor.ArmorType.C1));	
			}
			
			if(c2Spawn == currentFloorIndex) {
				floors.get(currentFloorIndex).addArmor(new Armor(Armor.ArmorType.C2));	
			}
			
			if(c3Spawn == currentFloorIndex) {
				floors.get(currentFloorIndex).addArmor(new Armor(Armor.ArmorType.C3));	
			}
			
		}
	}
}
