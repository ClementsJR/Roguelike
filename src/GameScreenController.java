

import engine.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameScreenController {
	public static final int DEFAULT_WINDOW_WIDTH = 1024;
	public static final int DEFAULT_WINDOW_HEIGHT = 576;
	
	private Engine game;
	private boolean acceptInput;
	
	private static final String DAMAGE_LABEL_STYLE_CLASS = "damageLabel";
	private static final String ZERO_LABEL_STYLE_CLASS = "zeroLabel";
	private static final String NONZERO_LABEL_STYLE_CLASS = "nonzeroLabel";
	
	private static final int STANDARD_SPRITE_DIMENSION = 32;
	
	private static final String NOT_SEEN_OVERLAY = "/assets/img/not_seen_overlay.png";
	private static final String WAS_SEEN_OVERLAY = "/assets/img/was_seen_overlay.png";
	
	@FXML
	Pane backgroundPane;
	
	@FXML
	Pane gameWorld;
	
	@FXML
	Pane floorPane;
	
	@FXML
	Pane entityPane;
	
	@FXML
	Pane fogOfWarPane;
	
	@FXML
	ImageView playerView;
	
	@FXML
	ImageView loadingAnimation;
	
	@FXML
	Pane hud;
	
	@FXML
	Canvas healthBar;
	
	@FXML
	Canvas xpBar;
	
	@FXML
	Canvas hungerBar;
	
	@FXML
	ImageView armorIcon;
	
	@FXML
	ImageView foodIcon;
	
	@FXML
	HBox statusEffectDisplay;
	
	@FXML
	public void initialize() {
		acceptInput = true;
		startLoadingAnimation();
	}
	
	public void setupGame() {
		backgroundPane.getScene().setOnKeyPressed((event) -> handleKeyPress(event));
		
		game = new Engine();
		drawNewFloor();
		drawPlayer();
		updateHUD();
		
		endLoadingAnimation();
	}
	
	private void startLoadingAnimation() {
		hud.setVisible(false);
		playerView.setVisible(false);
		gameWorld.setVisible(false);
		loadingAnimation.setVisible(true);
	}
	
	private void endLoadingAnimation() {
		loadingAnimation.setVisible(false);
		hud.setVisible(true);
		playerView.setVisible(true);
		gameWorld.setVisible(true);
	}
	
	private void drawNewFloor() {
		//gameWorld.setLayoutY(0);
		//gameWorld.setLayoutX(0);

		centerGameMap();
		drawFloor();
		drawTileOccupants();
		drawFOW();
	}

	private void drawFloor() {
		floorPane.getChildren().clear();
		
		int numRows = Dungeon.DEFAULT_NUM_ROWS;
		int numCols = Dungeon.DEFAULT_NUM_COLS;
		
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				Tile tile = game.getTileAt(new Position(row, col));
				Entity entity = tile.getBaseEntity();
				Image sprite = entity.getSprite();
				
				ImageView spriteView = new ImageView(sprite);
				
				int yLayout = row * STANDARD_SPRITE_DIMENSION;
				int xLayout = col * STANDARD_SPRITE_DIMENSION;
				
				spriteView.setLayoutY(yLayout);
				spriteView.setLayoutX(xLayout);
				
				floorPane.getChildren().add(spriteView);
			}
		}
	}
	
	private void drawTileOccupants() {
		entityPane.getChildren().clear();
		
		for(int row = 0; row < Dungeon.DEFAULT_NUM_ROWS; row++) {
			for(int col = 0; col < Dungeon.DEFAULT_NUM_COLS; col++) {
				Tile t = game.getTileAt(new Position(row, col));
				ArrayList<Entity> occupants = t.getOccupants();
				
				for(Entity e : occupants) {
					if(e instanceof PlayerCharacter) {
						continue;
					}
					
					SpriteView spriteView = new SpriteView(e);
					
					int yLayout = row * STANDARD_SPRITE_DIMENSION;
					int xLayout = col * STANDARD_SPRITE_DIMENSION;
					
					spriteView.setLayoutY(yLayout);
					spriteView.setLayoutX(xLayout);
					
					entityPane.getChildren().add(spriteView);
				}
			}
		}
	}
	
	private void drawFOW() {
		fogOfWarPane.getChildren().clear();
		
		PlayerCharacter player = game.getPlayer();

		int numRows = Dungeon.DEFAULT_NUM_ROWS;
		int numCols = Dungeon.DEFAULT_NUM_COLS;
		
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				int fow = player.getFOWAt(new Position(row, col));
				
				if(fow == LivingEntity.NOT_SEEN || fow == LivingEntity.WAS_SEEN) {
					String overlayPath = (fow == LivingEntity.NOT_SEEN) ? NOT_SEEN_OVERLAY : WAS_SEEN_OVERLAY;
					
					Image overlay = new Image(overlayPath);
					ImageView overlayView = new ImageView(overlay);
					
					int yLayout = row * STANDARD_SPRITE_DIMENSION;
					int xLayout = col * STANDARD_SPRITE_DIMENSION;
					
					overlayView.setLayoutY(yLayout);
					overlayView.setLayoutX(xLayout);
					
					fogOfWarPane.getChildren().add(overlayView);
				}
			}
		}
	}
	
	private void drawPlayer() {
		PlayerCharacter player = game.getPlayer();
		Image sprite = player.getSprite();
		playerView.setImage(sprite);
		
		int spriteDimension = 32;
		
		int yOffset = (DEFAULT_WINDOW_HEIGHT / 2) - (spriteDimension);
		int xOffset = (DEFAULT_WINDOW_WIDTH / 2) - (spriteDimension);
		
		playerView.setLayoutY(yOffset);
		playerView.setLayoutX(xOffset);
	}
	
	private void updateHUD() {
		updateHealthBar();
		updateXPBar();
		updateHungerBar();
		updatePoisonedEffectIndicator();
		updateParalysedEffectIndicator();
		updateArmorIcon();
		updateFoodIcon();
	}

	private void updateHealthBar() {
		PlayerCharacter player = game.getPlayer();
		
		double currentHealth = player.currentHealth;
		double maxHealth = player.maxHealth;
		
		double healthPercent = currentHealth / maxHealth;
		
		GraphicsContext pen = healthBar.getGraphicsContext2D();
		pen.clearRect(0, 0, healthBar.getWidth(), healthBar.getHeight());
		
		double height = healthBar.getHeight();
		double width = healthBar.getWidth() * healthPercent;
		
		pen.setFill(Color.rgb(200, 30, 30));
		pen.fillRect(0, 0, width, height);
	}
	
	private void updateXPBar() {
		PlayerCharacter player = game.getPlayer();
		
		double currentXP = player.currentXP;
		double goalXP = player.goalXP;
		
		double xpPercent = currentXP / goalXP;
		
		GraphicsContext pen = xpBar.getGraphicsContext2D();
		pen.clearRect(0, 0, xpBar.getWidth(), xpBar.getHeight());
		
		double height = xpBar.getHeight();
		double width = xpBar.getWidth() * xpPercent;
		
		pen.setFill(Color.rgb(200, 200, 30));
		pen.fillRect(0, 0, width, height);
	}
	
	private void updateHungerBar() {
		PlayerCharacter player = game.getPlayer();
		
		double hungerLevel = player.hungerLevel;
		
		GraphicsContext pen = hungerBar.getGraphicsContext2D();
		pen.clearRect(0, 0, hungerBar.getWidth(), hungerBar.getHeight());
		
		double height = hungerBar.getHeight();
		double width = hungerBar.getWidth() * hungerLevel;
		
		pen.setFill(Color.rgb(30, 30, 200));
		pen.fillRect(0, 0, width, height);
	}
	
	private void updatePoisonedEffectIndicator() {
		
	}
	
	private void updateParalysedEffectIndicator() {
		
	}
	
	private void updateArmorIcon() {
		
	}
	
	private void updateFoodIcon() {
		if(game.getPlayer().hasFood) {
			Food f = game.getPlayer().playerFood;
			foodIcon.setImage(f.getSprite());
		} else {
			foodIcon.setImage(new Image(WAS_SEEN_OVERLAY));
		}
	}
	
	private void centerGameMap() {
		printLayoutTest();
		
		int playerRow = game.getPlayerPosition().getRow();
		int playerCol = game.getPlayerPosition().getCol();
		
		int rowOffset = (DEFAULT_WINDOW_HEIGHT / 2) - ((playerRow + 1) * STANDARD_SPRITE_DIMENSION);
		int colOffset = (DEFAULT_WINDOW_WIDTH / 2) - ((playerCol + 1) * STANDARD_SPRITE_DIMENSION);
		
		gameWorld.setLayoutY(rowOffset);
		gameWorld.setLayoutX(colOffset);
		
		/*System.out.println("Player Row: " + playerRow);
		System.out.println("Player Col: " + playerCol);
		
		System.out.println("rowOffset: " + rowOffset);
		System.out.println("colOffset: " + colOffset);
		
		System.out.println();*/
	}
	
	private void printLayoutTest() {
		for(int row = 0; row < 1; row++) {
			for(int col = 0; col < 1; col++) {
				int rowOffset = (DEFAULT_WINDOW_HEIGHT / 2) - ((row + 1) * STANDARD_SPRITE_DIMENSION);
				int colOffset = (DEFAULT_WINDOW_WIDTH / 2) - ((col + 1) * STANDARD_SPRITE_DIMENSION);
				
				System.out.print("(" + row + ","  + col + ")");
				System.out.println("(" + rowOffset + ","  + colOffset + ")");
				System.out.println("(" + backgroundPane.getLayoutY() + ","  + backgroundPane.getLayoutX() + ")");
				
			}
		}
	}
	
 	/*public void tileClicked(SpriteView spriteView) {
		if(acceptInput == false) {
			return;
		}
		
		int index = mapGrid.getChildren().indexOf(spriteView);
		
		Position clicked = getPositionOf(index);
		
		game.tileSelected(clicked);
		updateFloor();
	}*/
	
	private void handleKeyPress(KeyEvent event) {
		if(acceptInput == false) {
			event.consume();
			return;
		}
		
    	switch(event.getCode()) {
    	case UP:
    		game.UpKeyPressed();
    		break;
    	case DOWN:
    		game.DownKeyPressed();
    		break;
    	case LEFT:
    		game.LeftKeyPressed();
    		break;
    	case RIGHT:
    		game.RightKeyPressed();
    		break;
		default:
			break;
    	}
    	
        event.consume();
        updateFloor();
	}
	
	private void updateFloor() {
		acceptInput = false;
		
		LinkedList<GameEvent> eventQueue = game.getEventQueue();
		
		SequentialTransition turnAnimations = new SequentialTransition();
		turnAnimations.setOnFinished((event) -> {acceptInput = true;});
		
		while(!eventQueue.isEmpty()) {
			GameEvent event = eventQueue.remove();

			Entity actor = event.getActor();
			Position source = event.getSource();
			Position target = event.getTarget();
			
			Transition transition;
			
			switch(event.getEventType()) {
			case MOVES_TO:
				int fowDistance = game.getPlayer().getSightDistance();
				int playerRow = game.getPlayerPosition().getRow();
				int playerCol = game.getPlayerPosition().getCol();
				int rowDifference = Math.abs(playerRow - source.getRow());
				int colDifference = Math.abs(playerCol - source.getCol());
				
				if(rowDifference <= fowDistance && colDifference <= fowDistance) {
					transition = makeMoveAnimation(actor, source, target);
					turnAnimations.getChildren().add(transition);
				} else {
					transition = makeMoveAnimation(actor, source, target);
					((TranslateTransition) transition).setDuration(new Duration(0));
					turnAnimations.getChildren().add(transition);
				}
				
				break;
			case CHANGES_FLOOR:
				//startLoadingAnimation();
				drawNewFloor();
				//endLoadingAnimation();
				
				break;
			case ATTACKS:
				int damage = event.getEventType().getEventValue();
				transition = makeDamageAnimation(target, damage);
				turnAnimations.getChildren().add(transition);
				
				break;
			case DIES:
				transition = makeDeathAnimation(actor);
				turnAnimations.getChildren().add(transition);
				if (actor instanceof PlayerCharacter)
					makeGameOverScreen();
				break;
			default:
				//do nothing yet.
			}
		}
		
		updateHUD();
		turnAnimations.play();
	}
	
	public void makeGameOverScreen() {
		 try {
	        	FXMLLoader fxmlLoader = new FXMLLoader();
	            fxmlLoader.setLocation(getClass().getResource("GameOverScreen.fxml"));
	            Scene scene = new Scene(fxmlLoader.load(), DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
	            
	            Stage stage = new Stage();
	            stage.setTitle("Game Over");
				stage.setResizable(false);
	            stage.setScene(scene);
	            stage.show();
	            
	        	//((Node)(event.getSource())).getScene().getWindow().hide();
	            backgroundPane.getScene().getWindow().hide();
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	private Transition makeMoveAnimation(Entity actor, Position source, Position target) {
		int yOffset = (target.getRow() - source.getRow()) * STANDARD_SPRITE_DIMENSION;
		int xOffset = (target.getCol() - source.getCol()) * STANDARD_SPRITE_DIMENSION;
		
		if(actor instanceof PlayerCharacter) {
			TranslateTransition translation = makeTranslation(gameWorld, -yOffset, -xOffset);
			translation.setOnFinished((event) -> updateFogOfWar());
			translation.setDuration(new Duration(100));
			return translation;
		} else {
			Node node = getSpriteViewFor(actor);
			TranslateTransition translation = makeTranslation(node, yOffset, xOffset);
			translation.setDuration(new Duration(200));
			return translation;
		}
	}
	
	private Node getSpriteViewFor(Entity entity)  {
		for(Node view : entityPane.getChildren()) {
			if( ((SpriteView) view).isEntity(entity) ) {
				return view;
			}
		}
		
		return null;
	}

	private Transition makeDamageAnimation(Position target, int damage) {
		Label dmgLabel = new Label(Integer.toString(damage));
		dmgLabel.getStyleClass().add(DAMAGE_LABEL_STYLE_CLASS);
		
		if(damage == 0) {
			dmgLabel.getStyleClass().add(ZERO_LABEL_STYLE_CLASS);
		} else {
			dmgLabel.getStyleClass().add(NONZERO_LABEL_STYLE_CLASS);
		}
		
		gameWorld.getChildren().add(dmgLabel);
		//backgroundPane.getChildren().add(dmgLabel);
		//hud.getChildren().add(dmgLabel);
		
		int yLayout = /*(int)((gameWorld.getTranslateY())/2) +*/ target.getRow() * STANDARD_SPRITE_DIMENSION;
		int xLayout = /*(int)((gameWorld.getTranslateX())/2) +*/ target.getCol() * STANDARD_SPRITE_DIMENSION;
		
		dmgLabel.setLayoutY(yLayout);
		dmgLabel.setLayoutX(xLayout);
		
		TranslateTransition translation = makeTranslation(dmgLabel, -32, 0);
		FadeTransition fade = makeFade(dmgLabel);
		
		ParallelTransition para = new ParallelTransition();
		para.getChildren().addAll(translation, fade);
		
		return para;
	}
	

	private Transition makeDeathAnimation(Entity actor) {
		Node node = getSpriteViewFor(actor);
		
		FadeTransition fade = makeFade(node);
		fade.setOnFinished((moveEvent) -> cleanUpDeathAnimation(node));
		
		return fade;
	}
	
	private void cleanUpDeathAnimation(Node node) {
		entityPane.getChildren().remove(node);
	}
	
	private TranslateTransition makeTranslation(Node node, double yOffset, double xOffset) {
		TranslateTransition translate = new TranslateTransition();
		
		translate.setNode(node);
		translate.setByY(yOffset);
		translate.setByX(xOffset);
		
		return translate;
	}
	
	private FadeTransition makeFade(Node node) {
		FadeTransition fade = new FadeTransition();
		
		fade.setNode(node);
		fade.setFromValue(1.0);
		fade.setToValue(0.0);
		
		return fade;
	}
	
	private void updateFogOfWar() {
		drawFOW();
	}
	
	@FXML
	public void armorIconHover() {
		
	}
	
	@FXML
	public void armorIconClick() {
		
	}
	
	@FXML
	public void foodIconClick() {
		game.getPlayer().EatFood();
		updateFoodIcon();
		updateHungerBar();
	}
}
