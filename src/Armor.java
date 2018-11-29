

public class Armor extends Entity {
	public ArmorType type;
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
}
