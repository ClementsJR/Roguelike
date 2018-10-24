package gui;

import engine.*;
import java.util.LinkedList;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

public class GameScreenController {
	private Engine game;
	private boolean acceptInput;
	
	private static final String DAMAGE_LABEL_STYLE_CLASS = "damageLabel";
	private static final String ZERO_LABEL_STYLE_CLASS = "zeroLabel";
	private static final String NONZERO_LABEL_STYLE_CLASS = "nonzeroLabel";
	
	@FXML
	Pane backgroundPane;
	
	@FXML
	ImageView loadingAnimation;
	
	@FXML
	TilePane mapGrid;
	
	@FXML
	public void initialize() {
		acceptInput = true;
		startLoadingAnimation();
	}
	
	public void setupGame() {
		backgroundPane.getScene().setOnKeyPressed((event) -> handleKeyPress(event));
		
		game = new Engine();
		drawFloor();
		endLoadingAnimation();
	}
	
	public void tileClicked(SpriteView spriteView) {
		if(acceptInput == false) {
			return;
		}
		
		int index = mapGrid.getChildren().indexOf(spriteView);
		
		Position clicked = getPositionOf(index);
		
		game.tileSelected(clicked);
		updateFloor();
	}
	
	private void startLoadingAnimation() {
		mapGrid.setVisible(false);
		loadingAnimation.setVisible(true);
	}
	
	private void endLoadingAnimation() {
		loadingAnimation.setVisible(false);
		mapGrid.setVisible(true);
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
	
	private void drawFloor() {
		mapGrid.getChildren().clear();
		
		PlayerCharacter player = game.getPlayer();
		
		int totalRows = game.getNumRows();
		int totalCols = game.getNumCols();
		
		mapGrid.setPrefColumns(totalCols);
		
		for(int row = 0; row < totalRows; row++) {
			for(int col = 0; col < totalCols; col++) {
				Position position = new Position(row, col);
				
				int fow = player.getFOWAt(position);
				SpriteView spriteView = new SpriteView(game.getTileAt(position), this, fow);
				
				mapGrid.getChildren().add(spriteView);
			}
		}
		
		centerMapGrid();
	}
	
	private void centerMapGrid() {
		int playerRow = game.getPlayerPosition().getRow();
		int playerCol = game.getPlayerPosition().getCol();
		
		int rowOffset = (Main.DEFAULT_WINDOW_HEIGHT / 2) - ((playerRow + 1) * SpriteView.STANDARD_SPRITE_DIMENSION);
		int colOffset = (Main.DEFAULT_WINDOW_WIDTH / 2) - ((playerCol + 1) * SpriteView.STANDARD_SPRITE_DIMENSION);
		
		mapGrid.setTranslateY(rowOffset);
		mapGrid.setTranslateX(colOffset);
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
				updatePosition(source);
				transition = makeMoveAnimation(actor, source, target);
				
				turnAnimations.getChildren().add(transition);
				
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
				updatePosition(source);
				transition = makeDeathAnimation(actor, source);
				turnAnimations.getChildren().add(transition);
				
				break;
			default:
				//do nothing yet.
			}
		}
		
		turnAnimations.play();
	}
	
	private Transition makeMoveAnimation(Entity actor, Position source, Position target) {
		ImageView sourceSprite = new ImageView(actor.getSprite());
		backgroundPane.getChildren().add(sourceSprite);
		
		sourceSprite.setLayoutY(0);
		sourceSprite.setLayoutX(0);
		sourceSprite.setTranslateY(mapGrid.getLayoutY() + mapGrid.getTranslateY() + source.getRow() * 32);
		sourceSprite.setTranslateX(mapGrid.getLayoutX() + mapGrid.getTranslateX() + source.getCol() * 32);
		
		int yOffset = (target.getRow() - source.getRow()) * SpriteView.STANDARD_SPRITE_DIMENSION;
		int xOffset = (target.getCol() - source.getCol()) * SpriteView.STANDARD_SPRITE_DIMENSION;
		
		if(actor instanceof PlayerCharacter) {
			TranslateTransition translation = makeTranslation(mapGrid, -yOffset, -xOffset);
			translation.setOnFinished((moveEvent) -> updatePlayerPosition(target));
			
			return translation;
		} else {
			TranslateTransition translation = makeTranslation(sourceSprite, yOffset, xOffset);
			translation.setOnFinished((moveEvent) -> cleanUpMoveAnimation(sourceSprite, actor, target));
			
			return translation;
		}
	}
	
	private void cleanUpMoveAnimation(Node node, Entity actor, Position target) {
		backgroundPane.getChildren().remove(node);
		
		if(actor instanceof PlayerCharacter) {
			updatePlayerPosition(target);
		} else {
			updatePosition(target);
		}
	}

	private Transition makeDamageAnimation(Position target, int damage) {
		int targetIndex = getIndexOf(target);
		SpriteView targetSpriteView = ((SpriteView) mapGrid.getChildren().get(targetIndex));
		
		Label dmgLabel = new Label(Integer.toString(damage));
		dmgLabel.getStyleClass().add(DAMAGE_LABEL_STYLE_CLASS);
		
		if(damage == 0) {
			dmgLabel.getStyleClass().add(ZERO_LABEL_STYLE_CLASS);
		} else {
			dmgLabel.getStyleClass().add(NONZERO_LABEL_STYLE_CLASS);
		}
		
		targetSpriteView.getChildren().add(dmgLabel);
		
		TranslateTransition translation = makeTranslation(dmgLabel, -32, 0);
		FadeTransition fade = makeFade(dmgLabel);
		
		ParallelTransition para = new ParallelTransition();
		para.getChildren().addAll(translation, fade);
		
		return para;
	}

	private Transition makeDeathAnimation(Entity actor, Position source) {
		ImageView sourceSprite = new ImageView(actor.getSprite());
		backgroundPane.getChildren().add(sourceSprite);
		
		sourceSprite.setLayoutY(0);
		sourceSprite.setLayoutX(0);
		sourceSprite.setTranslateY(mapGrid.getLayoutY() + mapGrid.getTranslateY() + source.getRow() * 32);
		sourceSprite.setTranslateX(mapGrid.getLayoutX() + mapGrid.getTranslateX() + source.getCol() * 32);
		
		FadeTransition fade = makeFade(sourceSprite);
		fade.setOnFinished((moveEvent) -> cleanUpDeathAnimation(sourceSprite));
		
		return fade;
	}
	
	private void cleanUpDeathAnimation(Node node) {
		backgroundPane.getChildren().remove(node);
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
	
	private void updatePlayerPosition(Position target) {
		PlayerCharacter player = game.getPlayer();
		
		int fowDistance = player.getSightDistance() + 1;
		int playerRow = target.getRow();
		int playerCol = target.getCol();
		
		for(int row = playerRow - fowDistance; row <= playerRow + fowDistance; row++) {
			for(int col = playerCol - fowDistance; col <= playerCol + fowDistance; col++) {
				if(row < 0 || col < 0 || row >= Dungeon.DEFAULT_NUM_ROWS || col >= Dungeon.DEFAULT_NUM_COLS) {
					continue;
				}
				
				Position position = new Position(row, col);
				int positionSpriteViewIndex = getIndexOf(position);
				((SpriteView) mapGrid.getChildren().get(positionSpriteViewIndex)).setTile(game.getTileAt(position), player.getFOWAt(position));
			}
		}
	}
	
	private void updatePosition(Position position) {
		
		int spriteViewIndex = getIndexOf(position);
		((SpriteView) mapGrid.getChildren().get(spriteViewIndex)).setTile(game.getTileAt(position));
	}
	
	private int getIndexOf(Position position) {
		return (position.getRow() * game.getNumRows()) + position.getCol();
	}
	
	private Position getPositionOf(int index) {
		int numCols = game.getNumCols();
		
		int row = index / numCols;
		int col = index % numCols;
		
		return new Position(row, col);
	}
}
