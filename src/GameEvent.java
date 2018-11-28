

public class GameEvent {
	private Entity actor;
	
	private EventType type;
	
	private Position source;
	private Position target;
	
	public GameEvent(Entity actor, Position source, EventType type) {
		this.actor = actor;
		this.type = type;
		this.source = source;
		this.target = new Position();
	}
	
	public GameEvent(Entity actor, Position source, EventType type, Position target) {
		this.actor = actor;
		this.type = type;
		this.source = source;
		this.target = target;
	}
	
	public void setSource(int row, int col) {
		source = new Position(row, col);
	}
	
	public void setTarget(int row, int col) {
		target = new Position(row, col);
	}
	
	public Entity getActor() { return actor; }
	public Position getSource() { return source; }
	public Position getTarget() { return target; }
	public EventType getEventType() { return type; }
}
