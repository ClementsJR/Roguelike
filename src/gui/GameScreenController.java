package gui;

import engine.*;

import java.util.ArrayList;
import java.util.LinkedList;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;

public class GameScreenController {
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
	public void initialize() {
		acceptInput = true;
		startLoadingAnimation();
	}
	
	public void setupGame() {
		backgroundPane.getScene().setOnKeyPressed((event) -> handleKeyPress(event));
		
		game = new Engine();
		drawNewFloor();
		drawPlayer();
		
		endLoadingAnimation();
	}
	
	private void drawNewFloor() {
		drawFloor();
		drawLivingEntities();
		drawFOW();
		//drawPlayer();
		centerGameMap();
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
	
	private void drawLivingEntities() {
		entityPane.getChildren().clear();
		
		ArrayList<LivingEntity> livingEntities = game.getLivingEntities();
		
		for(Entity entity : livingEntities) {
			SpriteView spriteView = new SpriteView(entity);
			
			int row = entity.getPosition().getRow();
			int col = entity.getPosition().getCol();
			
			int yLayout = row * STANDARD_SPRITE_DIMENSION;
			int xLayout = col * STANDARD_SPRITE_DIMENSION;
			
			spriteView.setLayoutY(yLayout);
			spriteView.setLayoutX(xLayout);
			
			entityPane.getChildren().add(spriteView);
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
		
		int yOffset = (Main.DEFAULT_WINDOW_HEIGHT / 2) - (spriteDimension);
		int xOffset = (Main.DEFAULT_WINDOW_WIDTH / 2) - (spriteDimension);
		
		playerView.setLayoutY(yOffset);
		playerView.setLayoutX(xOffset);
	}
	
	private void centerGameMap() {
		int playerRow = game.getPlayerPosition().getRow();
		int playerCol = game.getPlayerPosition().getCol();
		
		int rowOffset = (Main.DEFAULT_WINDOW_HEIGHT / 2) - ((playerRow + 1) * STANDARD_SPRITE_DIMENSION);
		int colOffset = (Main.DEFAULT_WINDOW_WIDTH / 2) - ((playerCol + 1) * STANDARD_SPRITE_DIMENSION);
		
		gameWorld.setLayoutY(rowOffset);
		gameWorld.setLayoutX(colOffset);
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
	
	private void startLoadingAnimation() {
		playerView.setVisible(false);
		gameWorld.setVisible(false);
		loadingAnimation.setVisible(true);
	}
	
	private void endLoadingAnimation() {
		loadingAnimation.setVisible(false);
		playerView.setVisible(true);
		gameWorld.setVisible(true);
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
				startLoadingAnimation();
				drawFloor();
				endLoadingAnimation();
				
				break;
			case ATTACKS:
				int damage = event.getEventType().getEventValue();
				transition = makeDamageAnimation(target, damage);
				turnAnimations.getChildren().add(transition);
				
				break;
			case DIES:
				transition = makeDeathAnimation(actor);
				turnAnimations.getChildren().add(transition);
				
				break;
			default:
				//do nothing yet.
			}
		}
		
		turnAnimations.play();
	}
	
	private Transition makeMoveAnimation(Entity actor, Position source, Position target) {
		int yOffset = (target.getRow() - source.getRow()) * STANDARD_SPRITE_DIMENSION;
		int xOffset = (target.getCol() - source.getCol()) * STANDARD_SPRITE_DIMENSION;
		
		if(actor instanceof PlayerCharacter) {
			TranslateTransition translation = makeTranslation(gameWorld, -yOffset, -xOffset);
			translation.setOnFinished((event) -> updateFogOfWar());
			return translation;
		} else {
			Node node = getSpriteViewFor(actor);
			TranslateTransition translation = makeTranslation(node, yOffset, xOffset);
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
		
		int yLayout = target.getRow() * STANDARD_SPRITE_DIMENSION;
		int xLayout = target.getCol() * STANDARD_SPRITE_DIMENSION;
		
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
}
