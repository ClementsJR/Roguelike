
public class StatusEffect {
	public StatusEffectType type;
	public int duration;
	public int damage;
	
	public StatusEffect(StatusEffectType type, int duration, int damage) {
		this.type = type;
		this.duration = duration;
		this.damage = damage;
	}
}
