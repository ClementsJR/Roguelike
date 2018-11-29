

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
	private static final String NO_FOOD_OVERLAY = "/assets/img/no_food_overlay.png";
	
	private static final String FLAME_PARTICLE = "/assets/img/burning_particles.gif";
	private static final String PARALYSIS_PARTICLE = "/assets/img/paralysis_icon.png";
	private static final String POISONED_PARTICLE = "/assets/img/poison_icon.png";
	
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
	HBox armorList;
	
	@FXML
	ImageView foodIcon;
	
	@FXML
	ImageView paralyzedDisplay;
	
	@FXML
	ImageView poisonedDisplay;
	
	public void initialize() {
		acceptInput = true;
		startLoadingAnimation();
	}
	
	public void setupGame(Class playerClass) {
		backgroundPane.getScene().setOnKeyPressed((event) -> handleKeyPress(event));
		
		game = new Engine(playerClass);
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
		gameWorld.setLayoutY(0);
		gameWorld.setLayoutX(0);
		gameWorld.setTranslateY(0);
		gameWorld.setTranslateX(0);
		
		centerGameMap();
		drawFloor();
		drawTileOccupants();
		drawFOW();
	}
	
	private void centerGameMap() {
		int playerRow = game.getPlayerPosition().getRow();
		int playerCol = game.getPlayerPosition().getCol();
		
		int rowOffset = (DEFAULT_WINDOW_HEIGHT / 2) - ((playerRow + 1) * STANDARD_SPRITE_DIMENSION);
		int colOffset = (DEFAULT_WINDOW_WIDTH / 2) - ((playerCol + 1) * STANDARD_SPRITE_DIMENSION);
		
		gameWorld.setLayoutY(rowOffset);
		gameWorld.setLayoutX(colOffset);
	}

	private void drawFloor() {
		floorPane.getChildren().clear();
		
		int numRows = Dungeon.DEFAULT_NUM_ROWS;
		int numCols = Dungeon.DEFAULT_NUM_COLS;
		
		PlayerCharacter player = game.getPlayer();
		
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				int fow = player.getFOWAt(new Position(row, col));
				
				int yLayout = row * STANDARD_SPRITE_DIMENSION;
				int xLayout = col * STANDARD_SPRITE_DIMENSION;

				Tile tile = game.getTileAt(new Position(row, col));
				Entity entity = tile.getBaseEntity();
				
				SpriteView tileView = new SpriteView(this, entity);
				tileView.setLayoutY(yLayout);
				tileView.setLayoutX(xLayout);
				
				floorPane.getChildren().add(tileView);
				
				if(fow == LivingEntity.NOT_SEEN) {
					tileView.setVisible(false);
				}
			}
		}
	}
	
	private void drawTileOccupants() {
		for(int row = 0; row < Dungeon.DEFAULT_NUM_ROWS; row++) {
			for(int col = 0; col < Dungeon.DEFAULT_NUM_COLS; col++) {
				
				int fow = game.getPlayer().getFOWAt(new Position(row, col));
				
				Tile t = game.getTileAt(new Position(row, col));
				ArrayList<Entity> occupants = t.getOccupants();
				
				for(Entity e : occupants) {
					if(e instanceof PlayerCharacter) {
						continue;
					}
					
					SpriteView spriteView = new SpriteView(this, e);
					
					int yLayout = row * STANDARD_SPRITE_DIMENSION;
					int xLayout = col * STANDARD_SPRITE_DIMENSION;
					
					spriteView.setLayoutY(yLayout);
					spriteView.setLayoutX(xLayout);
					
					floorPane.getChildren().add(spriteView);
					
					if(fow == LivingEntity.NOT_SEEN || fow == LivingEntity.WAS_SEEN) {
						spriteView.setVisible(false);
					}
				}
			}
		}
	}
	
	private void drawFOW() {
		PlayerCharacter player = game.getPlayer();

		int numRows = Dungeon.DEFAULT_NUM_ROWS;
		int numCols = Dungeon.DEFAULT_NUM_COLS;
		
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				int fow = player.getFOWAt(new Position(row, col));
				
				ImageView overlayView = new ImageView(new Image(WAS_SEEN_OVERLAY));
				
				int yLayout = row * STANDARD_SPRITE_DIMENSION;
				int xLayout = col * STANDARD_SPRITE_DIMENSION;
				
				overlayView.setLayoutY(yLayout);
				overlayView.setLayoutX(xLayout);
				
				floorPane.getChildren().add(overlayView);
				
				if(fow == LivingEntity.NOT_SEEN || fow >= 0) {
					overlayView.setVisible(false);
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
		PlayerCharacter player = game.getPlayer();
		
		ArmorType armorType = player.equippedArmorType;
		Armor currentArmor = new Armor(armorType);
		
		armorIcon.setImage(currentArmor.getSprite());
	}
	
	private void updateFoodIcon() {
		if(game.getPlayer().hasFood) {
			Food f = game.getPlayer().playerFood;
			foodIcon.setImage(f.getSprite());
		} else {
			foodIcon.setImage(new Image(NO_FOOD_OVERLAY));
		}
	}
	
	public void tileClicked(SpriteView spriteView) {
		if(acceptInput == false) {
			return;
		}
		
		int row = (int)(spriteView.getLayoutY() + spriteView.getTranslateY()) / 32;
		int col = (int)(spriteView.getLayoutX() + spriteView.getTranslateX()) / 32;
		
		game.tileSelected(new Position(row, col));
		updateFloor();
	}
	
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
			case PICKED_UP:
				Node spriteView = getSpriteViewFor(actor);
				floorPane.getChildren().remove(spriteView);
				
				break;
			case FIRE_BOMBED:
				ImageView image = new ImageView(new Image(FLAME_PARTICLE));
				
				int yLayout = target.getRow() * STANDARD_SPRITE_DIMENSION;
				int xLayout = target.getCol() * STANDARD_SPRITE_DIMENSION;
				
				image.setLayoutY(yLayout);
				image.setLayoutX(xLayout);
				
				gameWorld.getChildren().add(image);
				
				transition = makeFade(image);
				((FadeTransition) transition).setDuration(new Duration(50));
				transition.setOnFinished((e) -> gameWorld.getChildren().remove(image));
				turnAnimations.getChildren().add(transition);
				
				break;
			default:
				//do nothing yet.
			}
		}
		
		updateStatusEffects();
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
			
			int sourceFOW = game.getPlayer().getFOWAt(source);
			int targetFOW = game.getPlayer().getFOWAt(target);
			
			if(sourceFOW < 0 && targetFOW >= 0) {
				node.setVisible(true);
			} else if(sourceFOW >= 0 && targetFOW < 0) {
				translation.setOnFinished((event) -> node.setVisible(false));
			}
			
			return translation;
		}
	}
	
	private Node getSpriteViewFor(Entity entity)  {
		for(Node view : floorPane.getChildren()) {
			if( (view instanceof SpriteView) && ((SpriteView) view).isEntity(entity) ) {
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
		
		para.setOnFinished((event) -> gameWorld.getChildren().remove(dmgLabel));
		
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
		for(Node node : floorPane.getChildren()) {
			int row = (int)(node.getLayoutY() + node.getTranslateY()) / 32;
			int col = (int)(node.getLayoutX() + node.getTranslateX()) / 32;
			
			int fow = game.getPlayer().getFOWAt(new Position(row, col));
			
			if(fow >= 0) {
				node.setVisible(true);
				
				if(node instanceof ImageView) {
					node.setVisible(false);
				}
			} else if(fow == LivingEntity.WAS_SEEN) {
				if(node instanceof ImageView) {
					node.setVisible(true);
				} else {
					SpriteView sprite = (SpriteView)node;
					
					if(sprite.getEntity() instanceof LivingEntity) {
						node.setVisible(false);
					}
				}
			}
		}
	}
	
	@FXML
	public void armorIconHover() {
		ArrayList<ArmorType> inventory = game.getPlayer().armorList;
		int offset = inventory.size() * 40;
		
		armorList.setTranslateX(-offset);
		
		for(ArmorType t : inventory) {
			Armor a = new Armor(t);
			ImageView v = new ImageView(a.getSprite());
			v.setOnMouseClicked((event) -> equipArmor(t));
			armorList.getChildren().add(v);
		}
		
		armorList.setVisible(true);
	}
	
	private void equipArmor(ArmorType t) {
		game.getPlayer().EquipArmor(t);
		updateArmorIcon();
	}
	
	@FXML
	public void armorIconUnhover() {
		armorList.setVisible(false);
		
		armorList.getChildren().clear();
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
	
	private void updateStatusEffects() {
		updatePlayerStatusEffects();
		updateEnemyStatusEffects();
	}
	
	private void updatePlayerStatusEffects() {
		PlayerCharacter player = game.getPlayer();
		
		boolean isParalyzed = player.paralysisEffect.duration > 0;
		boolean isPoisoned = player.poisonEffect.duration > 0;
		
		if(isParalyzed) {
			paralyzedDisplay.setVisible(true);
		} else {
			paralyzedDisplay.setVisible(false);
		}
		
		if(isPoisoned) {
			poisonedDisplay.setVisible(true);
		} else {
			poisonedDisplay.setVisible(false);
		}
	}
	
	private void updateEnemyStatusEffects() {
		for(LivingEntity entity : game.getLivingEntities()) {
			boolean isBurned = entity.burningEffect.duration > 0;
			boolean isPoisoned = entity.poisonEffect.duration > 0;
			
			if(isBurned) {
				ImageView view = new ImageView(new Image(FLAME_PARTICLE));
				gameWorld.getChildren().add(view);
				view.setLayoutY(entity.getPosition().getRow() * 32);
				view.setLayoutX(entity.getPosition().getRow() * 32);
				
				FadeTransition fade = makeFade(view);
				fade.setDuration(new Duration(200));
				fade.setOnFinished((event) -> gameWorld.getChildren().remove(view));
				
				fade.play();
			}
			
			if(isPoisoned) {
				ImageView view = new ImageView(new Image(POISONED_PARTICLE));
				gameWorld.getChildren().add(view);
				view.setLayoutY(entity.getPosition().getRow() * 32);
				view.setLayoutX(entity.getPosition().getRow() * 32);
				
				FadeTransition fade = makeFade(view);
				fade.setDuration(new Duration(200));
				fade.setOnFinished((event) -> gameWorld.getChildren().remove(view));
				
				fade.play();
			}
		}
	}
}
