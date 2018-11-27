package engine;

public class Armor extends Entity {
	private ArmorType type;
	private static final String STARTING_URL="/assets/img/armor_a_1.png";
	private static final String B1_URL="/assets/img/armor_b_1.png";
	private static final String B2_URL="/assets/img.armor_b_2.png";
	private static final String B3_URL="/assets/img/armor_b_3.png";
	private static final String B4_URL="/assets/img/armor_b_4.png";
	private static final String B5_URL="/assets/img/armor_b_5.png";
	private static final String B6_URL="/assets/img/armor_b_6.png";
	private static final String C1_URL="/assets/img/armor_c_1.png";
	private static final String C2_URL="/assets/img/armor_c_2.png";
	private static final String C3_URL="/assets/img/armor_c_3.png";
	
	public Armor(ArmorType type) {
		setImpassable(false);
		
		this.type = type;
		
		switch(type) {
		case STARTING:
			setImage(STARTING_URL);
			break;
		case B1:
			setImage(B1_URL);
			break;
		case B2:
			setImage(B2_URL);
			break;
		case B3:
			setImage(B3_URL);
			break;
		case B4:
			setImage(B4_URL);
			break;
		case B5:
			setImage(B5_URL);
			break;
		case B6:
			setImage(B6_URL);
			break;
		case C1:
			setImage(C1_URL);
			break;
		case C2: 
			setImage(C2_URL);
			break;
		case C3:
			setImage(C3_URL);
			break;
		}
		
	}
	
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
}
