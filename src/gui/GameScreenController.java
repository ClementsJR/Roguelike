package gui;

import engine.*;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;

public class GameScreenController {
	@FXML
	TilePane mapGrid;
	
	private Engine game;
	private Floor currentFloor;
	
	@FXML
	public void initialize() {
		game = new Engine();
		currentFloor = game.getCurrentFloor();
		drawFloor();
	}
	
	public void drawFloor() {
		int totalRows = currentFloor.getNumRows();
		int totalCols = currentFloor.getNumCols();
		
		mapGrid.setPrefColumns(totalCols);
		mapGrid.setPrefRows(totalRows);
		
		for(int row = 0; row < totalRows; row++) {
			for(int col = 0; col < totalCols; col++) {
				SpriteView spriteView = new SpriteView(currentFloor.getTileAt(row,  col));
				mapGrid.getChildren().add(spriteView);
			}
		}
	}
}
