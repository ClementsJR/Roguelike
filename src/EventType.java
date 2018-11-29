
public enum EventType {
	MOVES_TO, ATTACKS, DIES, CHANGES_FLOOR, PICKED_UP, FIRE_BOMBED;
	
	int value = 0;
	
	public int getEventValue() { return value; }
	public void setEventValue(int newValue) { value = newValue; }
}
