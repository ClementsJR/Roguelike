

import java.util.ArrayList;
import java.util.Random;

public abstract class PlayerCharacter extends LivingEntity{
	
	public static int PLAYER_LEVEL = 0;
	
	public int currentXP;
	public boolean hasFood = false;
	public int goalXP = (int)Math.pow(2, (3 + PLAYER_LEVEL));
	public Food playerFood;
	public ArrayList <ArmorType> armorList;
	public ArmorType equippedArmorType;
	public double hungerLevel;
	private StatusEffect dealtStatusEffect;

	public PlayerCharacter(int maxHealth, Range initAttackRange, int defense) {
		super(maxHealth, initAttackRange, defense);
		setIsEnemy(false);
		armorList = new ArrayList <ArmorType>();
		armorList.add(ArmorType.STARTING);
		equippedArmorType = armorList.get(0);
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
	
	public void EquipArmor(ArmorType t) { equippedArmorType = t; }
	
	public int receiveDamage(int damage) {
		if(equippedArmorType == ArmorType.C2)
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
		
		if(equippedArmorType == ArmorType.C2)
			dmg += dmg * 0.4;
		if(equippedArmorType == ArmorType.C3)
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
		
		if(equippedArmorType == ArmorType.C1) {
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
		double addedChance = 1 - (currentHealth / maxHealth);
		if (addedChance <= 0.85)
			chanceToHit += addedChance;
		
		if(equippedArmorType == ArmorType.C3) {
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
