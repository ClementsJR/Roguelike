package gui;

import java.util.LinkedList;

import engine.*;

import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;

public class GameScreenController {
	private Engine game;
	
	@FXML
	TilePane mapGrid;
	
	@FXML
	public void initialize() {
		game = new Engine();
		drawFloor();
	}
	
	public void drawFloor() {
		mapGrid.getChildren().clear();
		
		int totalRows = game.getNumRows();
		int totalCols = game.getNumCols();
		
		mapGrid.setPrefColumns(totalCols);
		mapGrid.setPrefRows(totalRows);
		
		for(int row = 0; row < totalRows; row++) {
			for(int col = 0; col < totalCols; col++) {
				SpriteView spriteView = new SpriteView(game.getTileAt(row,  col), this);
				
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
				int sourceRow = event.getSourceRow();
				int sourceCol = event.getSourceCol();

				int targetRow = event.getTargetRow();
				int targetCol = event.getTargetCol();
				
				int sourceSpriteViewIndex = getIndex(sourceRow, sourceCol);
				int targetSpriteViewIndex = getIndex(targetRow, targetCol);
				
				((SpriteView) mapGrid.getChildren().get(sourceSpriteViewIndex)).setTile(game.getTileAt(sourceRow, sourceCol));
				((SpriteView) mapGrid.getChildren().get(targetSpriteViewIndex)).setTile(game.getTileAt(targetRow, targetCol));
				
				break;
			default:
				//do nothing yet.
			}
		}
	}
	
	private int getIndex(int row, int col) {
		return (row * game.getNumRows()) + col;
	}
	
	private void centerMapGrid() {
		int playerRow = game.getPlayerRow();
		int playerCol = game.getPlayerCol();
		
		int rowOffset = (Main.DEFAULT_WINDOW_HEIGHT / 2) - ((playerRow + 1) * SpriteView.STANDARD_SPRITE_DIMENSION);
		int colOffset = (Main.DEFAULT_WINDOW_WIDTH / 2) - ((playerCol + 1) * SpriteView.STANDARD_SPRITE_DIMENSION);
		
		mapGrid.setTranslateY(rowOffset);
		mapGrid.setTranslateX(colOffset);
	}
	
	public void tileClicked(SpriteView spriteView) {
		int index = mapGrid.getChildren().indexOf(spriteView);
		int numCols = game.getNumCols();
		
		int row = index / numCols;
		int col = index % numCols;
		
		game.tileClicked(row, col);
		updateFloor();
	}
}
