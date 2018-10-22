package gui;

import java.util.LinkedList;

import engine.*;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;

public class GameScreenController {
	private Engine game;
	
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
		startLoadingAnimation();
	}
	
	public void setupGame() {
		backgroundPane.getScene().setOnKeyPressed((event) -> handleKeyPress(event));
		
		game = new Engine();
		drawFloor();
		endLoadingAnimation();
	}
	
	public void tileClicked(SpriteView spriteView) {
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
	
	private void updateFloor() {
		LinkedList<GameEvent> eventQueue = game.getEventQueue();
		
		SequentialTransition turnAnimations = new SequentialTransition();
		turnAnimations.setOnFinished((event) -> centerMapGrid());
		
		while(!eventQueue.isEmpty()) {
			GameEvent event = eventQueue.remove();

			Position source = event.getSource();
			Position target = event.getTarget();
			
			Transition transition;
			
			switch(event.getEventType()) {
			case MOVES_TO:
				Entity actor = event.getActor();
				
				transition = makeMoveAnimation(source, target);
				
				if(actor instanceof PlayerCharacter) {
					transition.setOnFinished((moveEvent) -> updatePlayerPosition());
				} else {
					transition.setOnFinished((moveEvent) -> updateNonPlayerPosition(source, target));
				}
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
				//transition = makeDeathAnimation(source);
				//transition.setOnFinished((moveEvent) -> updateSinglePosition(source));
				//turnAnimations.getChildren().add(transition);
				updateSinglePosition(source);
				
				break;
			default:
				//do nothing yet.
			}
		}
		
		turnAnimations.play();
		centerMapGrid();
	}
	
	private Transition makeMoveAnimation(Position source, Position target) {
		TranslateTransition translation = new TranslateTransition();
		
		return translation;
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
		
		TranslateTransition translation = new TranslateTransition();
		translation.setDuration(Duration.millis(1000));
		translation.setNode(dmgLabel);
		translation.setByY(-32);
		
		FadeTransition fade = new FadeTransition();
		fade.setDuration(Duration.millis(1000));
		fade.setNode(dmgLabel);
		fade.setFromValue(1.0);
		fade.setToValue(0.0);
		
		ParallelTransition para = new ParallelTransition();
		para.getChildren().addAll(translation, fade);
		
		return para;
	}

	private Transition makeDeathAnimation(Position source) {
		FadeTransition animation = new FadeTransition();
		
		return animation;
	}
	
	private void updatePlayerPosition() {
		PlayerCharacter player = game.getPlayer();
		
		int fowDistance = player.getSightDistance() + 1;
		int playerRow = player.getPosition().getRow();
		int playerCol = player.getPosition().getCol();
		
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
	
	private void updateNonPlayerPosition(Position source, Position target) {
		updateSinglePosition(source);
		updateSinglePosition(target);
	}
	
	private void updateSinglePosition(Position position) {
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
	
	private void centerMapGrid() {
		int playerRow = game.getPlayerPosition().getRow();
		int playerCol = game.getPlayerPosition().getCol();
		
		int rowOffset = (Main.DEFAULT_WINDOW_HEIGHT / 2) - ((playerRow + 1) * SpriteView.STANDARD_SPRITE_DIMENSION);
		int colOffset = (Main.DEFAULT_WINDOW_WIDTH / 2) - ((playerCol + 1) * SpriteView.STANDARD_SPRITE_DIMENSION);
		
		mapGrid.setTranslateY(rowOffset);
		mapGrid.setTranslateX(colOffset);
	}
}
