package engine;

public class PlayerCharacter extends LivingEntity{
	private double hungerLevel;
	
	public PlayerCharacter() {
	}
	
	public enum HungerStage {
		FULL, PECKISH, HUNGRY, STARVING;
	}
}
