package gui;

import engine.*;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

public class GameScreenController {
	@FXML
	TilePane mapGrid;
	
	Engine game;
	
	@FXML
	public void initialize() {
		game = new Engine();
		
		Floor floor = game.getCurrentFloor();
		drawFloor(floor);
	}
	
	public void drawFloor(Floor floor) {
		int totalRows = floor.getNumRows();
		int totalCols = floor.getNumCols();
		
		mapGrid.setPrefColumns(totalCols);
		mapGrid.setPrefRows(totalRows);
		
		for(int row = 0; row < totalRows; row++) {
			for(int col = 0; col < totalCols; col++) {
				Image sprite = floor.getTileAt(row, col).getBaseEntity().getSprite();
				mapGrid.getChildren().add(new ImageView(sprite));
			}
		}
	}
}
