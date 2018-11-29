
public enum EventType {
	MOVES_TO, ATTACKS, DIES, CHANGES_FLOOR;
	
	int value = 0;
	
	public int getEventValue() { return value; }
	public void setEventValue(int newValue) { value = newValue; }
}
