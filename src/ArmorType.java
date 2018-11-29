
public enum ArmorType {
	STARTING {{
		displayName = "Makeshift Armor";
		armorValue=0;
	}}, B1 {{
		displayName="Leather Armor";
		armorValue=2;
	}}, B2 {{
		displayName="Splint Mail";
		armorValue=4;
	}}, B3 {{
		displayName="Plate Mail";
		armorValue=6;
	}}, B4 {{
		displayName="Ring Mail";
		armorValue=8;
	}}, B5 {{
		displayName="Scale Mail";
		armorValue=10;
	}}, B6 {{
		displayName="Chain Mail";
		armorValue=12;
	}}, C1 {{
		displayName="Mystic Garb";
		armorValue=2;
		// -1 turn poison duration and paralysis
		//15% chance to cure status when hit with status
	}}, C2 {{
		displayName="Berserker's Gear";
		armorValue=6;
		// +40% dmg dealt
		// +20% dmg taken
	}}, C3 {{
		displayName="Slime Armor";
		armorValue=10;
		//reduce dmg dealt by 3
		// 15% to poison enemies
		// +5% every 5% of your max hp missing
	}};
	
	String displayName;
	int armorValue;
	
	public String getArmorName() {
		return displayName;
	}
	
	public int getArmorValue() {
		return armorValue;
	}
}
