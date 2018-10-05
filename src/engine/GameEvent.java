package engine;

public class GameEvent {
	private Entity actor;
	
	private int sourceRow, sourceCol;
	private int targetRow, targetCol;
	
	private EventType type;
	
	public GameEvent(Entity actor, EventType type) {
		this.actor = actor;
		this.type = type;
		
		setSource(0,0);
		setTarget(0,0);
	}
	
	public void setSource(int row, int col) {
		sourceRow = row;
		sourceCol = col;
	}
	
	public void setTarget(int row, int col) {
		targetRow = row;
		targetCol = col;
	}
	
	public int getSourceRow() { return sourceRow; }
	public int getSourceCol() { return sourceCol; }
	public int getTargetRow() { return targetRow; }
	public int getTargetCol() { return targetCol; }
	public EventType getEventType() { return type; }
	
	public enum EventType {
		MOVES_TO, ATTACKS, DIES;
	}
}
