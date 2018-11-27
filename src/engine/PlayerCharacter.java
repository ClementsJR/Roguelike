package engine;

import java.util.ArrayList;

import engine.Armor.ArmorType;

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
	
	public void EquipArmor() {
		equipedArmorType = armorList.get(armorSelect);
		if (equipedArmorType == ArmorType.C2) {
			// current dmg = dmg + (dmg * .4)
		}
	}
	
	public int getRandomAttackDamage() {
		int dmg = damageRange.getRandomNum();
		if(equipedArmorType == ArmorType.C2)
		dmg += dmg * 0.4;
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
	
	public abstract void LevelUp();
	
	public abstract void PlayerRegen();
	
	public abstract void UpdateStatus();
}
