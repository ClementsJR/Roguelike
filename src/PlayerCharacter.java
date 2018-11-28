

import java.util.ArrayList;
import java.util.Random;

public abstract class PlayerCharacter extends LivingEntity{
	
	public static int PLAYER_LEVEL = 0;
	
	public int currentXP;
	public boolean hasFood = false;
	public int goalXP = (int)Math.pow(2, (3 + PLAYER_LEVEL));
	public Food playerFood;
	private ArrayList <ArmorType> armorList;
	private int armorSelect = 0;
	private ArmorType equipedArmorType;
	public double hungerLevel;
	private StatusEffect dealtStatusEffect;

	public PlayerCharacter(int maxHealth, Range initAttackRange, int defense) {
		super(maxHealth, initAttackRange, defense);
		setIsEnemy(false);
		armorList = new ArrayList <ArmorType>();
		hungerLevel = 0.0;
	}
	
	public enum HungerStage {
		FULL, PECKISH, HUNGRY, STARVING;
	}
	
	public void GiveExperience() {
		currentXP = currentXP + (int)(Math.pow(2, PLAYER_LEVEL));
		if (currentXP >= goalXP)
			LevelUp();
	}
	
	public void GiveFood(Food newFood) {
		playerFood = newFood;
		hasFood = true;
	}
	
	public void EquipArmor() { equipedArmorType = armorList.get(armorSelect); }
	
	public int receiveDamage(int damage) {
		if(equipedArmorType == ArmorType.C2)
			damage += damage * 0.2;
		damage -= defense;
		
		if (damage > 0) {
			currentHealth -= damage;
			return damage;
		} else {
			return 0;
		}
	}
	
	public int getRandomAttackDamage() {
		int dmg = damageRange.getRandomNum();
		
		if(equipedArmorType == ArmorType.C2)
			dmg += dmg * 0.4;
		if(equipedArmorType == ArmorType.C3)
			dmg -= 3;
		
		return dmg;
	}
	
	public void EatFood() {
		if (hasFood) {
			currentHealth = maxHealth;
			hungerLevel = 0;
			playerFood = null;
			hasFood = false;
		}
	}
	
	public void GiveArmor(ArmorType newArmor) {
		armorList.add(newArmor);
	}
	
	public void GiveStatusEffect(StatusEffect effect) {
		Random rand = new Random();
		double randNum = rand.nextDouble();
		
		if(equipedArmorType == ArmorType.C1) {
			if(randNum <= 0.15)
				return;
			
			effect.duration--;
		}
		
		
		if(effect == StatusEffect.POISONED) { poisonEffect = effect; }
		if(effect == StatusEffect.PARALYZED) { paralysisEffect = effect; }
		if(effect == StatusEffect.BURNED) { burningEffect = effect; }
	}
	
	public boolean DealsStatusEffect() {
		double chanceToHit = 0.15;
		Random rand = new Random();
		double randNum = rand.nextDouble();
		double addedChance = currentHealth / maxHealth;
		if (addedChance < 1)
			chanceToHit += addedChance;
		
		if(equipedArmorType == ArmorType.C3) {
			if (randNum <= chanceToHit)
				return true; 
		}
		return false;
	}
	
	public StatusEffect getStatusEffect() {
		Random rand = new Random();
		int statusDuration = rand.nextInt(3) + 3;
		dealtStatusEffect.duration = statusDuration;
		return dealtStatusEffect;
	}
	
	public abstract void LevelUp();
	
	public abstract void PlayerRegen();
	
	public abstract void UpdateStatus();
}
