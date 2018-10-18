package gui;

import java.util.LinkedList;

import engine.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

public class GameScreenController {
	private Engine game;
	
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
		
		while(!eventQueue.isEmpty()) {
			GameEvent event = eventQueue.remove();
			
			switch(event.getEventType()) {
			case MOVES_TO:
				/*Position source = event.getSource();
				Position target = event.getTarget();
				
				int sourceSpriteViewIndex = getIndexOf(source);
				int targetSpriteViewIndex = getIndexOf(target);
				
				((SpriteView) mapGrid.getChildren().get(sourceSpriteViewIndex)).setTile(game.getTileAt(source));
				((SpriteView) mapGrid.getChildren().get(targetSpriteViewIndex)).setTile(game.getTileAt(target));*/
				
				drawFloor();
				
				break;
			case CHANGES_FLOOR:
				startLoadingAnimation();
				drawFloor();
				endLoadingAnimation();
			default:
				//do nothing yet.
			}
		}
		
		centerMapGrid();
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
