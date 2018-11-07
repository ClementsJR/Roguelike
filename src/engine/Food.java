package engine;

import java.util.Random;

public class Food extends Entity {

	/*private static final String FOOD1_URL="/assets/img/food_1.png";
	private static final String FOOD2_URL="/assets/img/food_2.png";
	private static final String FOOD3_URL="/assets/img/food_3.png";
	private static final String FOOD4_URL="/assets/img/food_4.png";
	private static final String FOOD5_URL="/assets/img/food_5.png";
	private static final String FOOD6_URL="/assets/img/food_6.png";
	private static final String FOOD7_URL="/assets/img/food_7.png";
	private static final String FOOD8_URL="/assets/img/food_8.png";
	private static final String FOOD9_URL="/assets/img/food_9.png";
	private static final String FOOD10_URL="/assets/img/food_10.png";
	private static final String FOOD11_URL="/assets/img/food_11.png";
	private static final String FOOD12_URL="/assets/img/food_12.png";
	private static final String FOOD13_URL="/assets/img/food_13.png";
	private static final String FOOD14_URL="/assets/img/food_14.png";
	private static final String FOOD15_URL="/assets/img/food_15.png";
	private static final String FOOD16_URL="/assets/img/food_16.png";
	private static final String FOOD17_URL="/assets/img/food_17.png";
	private static final String FOOD18_URL="/assets/img/food_18.png";
	private static final String FOOD19_URL="/assets/img/food_19.png";
	private static final String FOOD20_URL="/assets/img/food_20.png";
	private static final String FOOD21_URL="/assets/img/food_21.png";
	private static final String FOOD22_URL="/assets/img/food_22.png";
	private static final String FOOD23_URL="/assets/img/food_23.png";
	private static final String FOOD24_URL="/assets/img/food_24.png";
	private static final String FOOD25_URL="/assets/img/food_25.png";*/
	
	private static final String URL_START = "/assets/img/food_";
	private static final String URL_END = ".png";

	public Food() {
		setImpassable(false);
		Random rand = new Random();
		
		String spriteURL = URL_START + (rand.nextInt(25) + 1) + URL_END;
		setImage(spriteURL);  
		
		/*switch(rand.nextInt(25)) {
		case 0:
			setImage(FOOD1_URL);
			break;
		case 1:
			setImage(FOOD2_URL);
			break;
		case 2:
			setImage(FOOD3_URL);
			break;
		case 3:
			setImage(FOOD4_URL);
			break;
		case 4:
			setImage(FOOD5_URL);
			break;
		case 5:
			setImage(FOOD6_URL);
			break;
		case 6:
			setImage(FOOD7_URL);
			break;
		case 7:
			setImage(FOOD8_URL);
			break;
		case 8:
			setImage(FOOD9_URL);
			break;
		case 9:
			setImage(FOOD10_URL);
			break;
		case 10:
			setImage(FOOD11_URL);
			break;
		case 11:
			setImage(FOOD12_URL);
			break;
		case 12:
			setImage(FOOD13_URL);
			break;
		case 13:
			setImage(FOOD14_URL);
			break;
		case 14:
			setImage(FOOD15_URL);
			break;
		case 15:
			setImage(FOOD16_URL);
			break;
		case 16:
			setImage(FOOD17_URL);
			break;
		case 17:
			setImage(FOOD18_URL);
			break;
		case 18:
			setImage(FOOD19_URL);
			break;
		case 19:
			setImage(FOOD20_URL);
			break;
		case 20:
			setImage(FOOD21_URL);
			break;
		case 21:
			setImage(FOOD22_URL);
			break;
		case 22:
			setImage(FOOD23_URL);
			break;
		case 23:
			setImage(FOOD24_URL);
			break;
		case 24:
			setImage(FOOD25_URL);
			break;
		default:
			setImage(FOOD1_URL);
			break;
		}*/
	}
}
