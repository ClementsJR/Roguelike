package gui;

import java.util.ArrayList;

import engine.Entity;
import engine.Tile;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class SpriteView extends Pane {
	public static final int STANDARD_SPRITE_DIMENSION = 32;
	
	private Tile tile;
	private ArrayList<ImageView> images;
	
	public SpriteView(Tile tile) {
		this.setPrefSize(STANDARD_SPRITE_DIMENSION, STANDARD_SPRITE_DIMENSION);
		this.setOnMouseClicked((event) -> handleClick(event));
		
		setTile(tile);
	}
	
	public void setTile(Tile newTile) {
		tile = newTile;
		
		images = new ArrayList<ImageView>();
		setupImageViews();
		drawImageViews();
	}
	
	private void handleClick(MouseEvent event) {
		System.out.println(this);
	}
	
	private void setupImageViews() {
		images.clear();
		
		Entity baseEntity = tile.getBaseEntity();
		Image sprite = baseEntity.getSprite();
		images.add(new ImageView(sprite));
		
		ArrayList<Entity> occupants = tile.getOccupants();
		for(Entity occupant : occupants) {
			sprite = occupant.getSprite();
			images.add(new ImageView(sprite));
		}
	}
	
	private void drawImageViews() {
		this.getChildren().clear();
		
		for(ImageView img : images) {
			this.getChildren().add(img);
		}
	}
}
