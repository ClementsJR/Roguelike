package gui;

import engine.*;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
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
		
		int centerRow = totalRows/2;
		int centerCol = totalCols/2;
		
		int playerRow = game.getPlayerRow();
		int playerCol = game.getPlayerCol();
		
		//int rowOffset = (centerRow - playerRow) * SpriteView.STANDARD_SPRITE_DIMENSION;// + (Main.DEFAULT_WINDOW_HEIGHT / 2);
		//int colOffset = (centerCol - playerCol) * SpriteView.STANDARD_SPRITE_DIMENSION;// + (Main.DEFAULT_WINDOW_WIDTH / 2);
		
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
		drawFloor();
	}
}
