package gui;

import engine.*;

import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;

public class GameScreenController {
	@FXML
	TilePane mapGrid;
	
	private Engine game;
	
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
	}
	
	public void tileClicked(SpriteView spriteView) {
		int index = mapGrid.getChildren().indexOf(spriteView);
		int numCols = game.getNumCols();
		
		int row = index / numCols;
		int col = index % numCols;
		
		game.tileClicked(row, col);
		drawFloor();
	}
}
