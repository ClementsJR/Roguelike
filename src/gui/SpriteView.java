package gui;

import engine.Entity;
import engine.Tile;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class SpriteView extends Pane {
	public static final int STANDARD_SPRITE_DIMENSION = 32;
	
	private Tile tile;
	private ImageView img;
	
	public SpriteView(Tile tile) {
		this.setPrefSize(STANDARD_SPRITE_DIMENSION, STANDARD_SPRITE_DIMENSION);
		this.setOnMouseClicked((event) -> handleClick(event));
		
		this.tile = tile;
		
		img = new ImageView();
		setupImageView();
		this.getChildren().add(img);
	}
	
	private void handleClick(MouseEvent event) {
		System.out.println(this);
	}
	
	private void setupImageView() {
		Entity baseEntity = tile.getBaseEntity();
		Image sprite = baseEntity.getSprite();
		img.setImage(sprite);
	}
	
	public void setTile(Tile newTile) {
		tile = newTile;
		setupImageView();
	}
}
