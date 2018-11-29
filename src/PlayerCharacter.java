

import java.util.ArrayList;
import java.util.Random;

public abstract class PlayerCharacter extends LivingEntity{
	
	public int playerLevel = 0;
	public int currentXP;
	public boolean hasFood = false;
	public int goalXP = (int)Math.pow(2, (3 + playerLevel));
	public Food playerFood;
	public ArrayList <ArmorType> armorList;
	public ArmorType equippedArmorType;
	public double hungerLevel;
	public int playerRegen;
	
	public PlayerCharacter(int maxHealth, Range initAttackRange, int defense, int regen) {
		super(maxHealth, initAttackRange, defense);
		playerRegen = regen;
		
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
		currentXP = currentXP + (int)(Math.pow(2, playerLevel));
		if (currentXP >= goalXP)
			LevelUp();
	}
	
	public void GiveFood(Food newFood) {
		playerFood = newFood;
		hasFood = true;
	}
	
	public void EquipArmor(ArmorType t) {
		equippedArmorType = t;
	}
	
	public int receiveDamage(int damage) {
		if(equippedArmorType == ArmorType.C2)
			damage += damage * 0.2;
		damage -= equippedArmorType.armorValue;
		
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
		System.out.println("yep");
		armorList.add(newArmor);
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
		
		int duration = rand.nextInt(3) + 3;
		int damage = 1;
		
		StatusEffect poison = new StatusEffect(StatusEffectType.POISONED, duration, damage);
		
		return poison;
	}
	
	public abstract void LevelUp();
	
	public abstract void PlayerRegen();
}
