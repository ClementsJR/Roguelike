package gui;

import java.util.ArrayList;

import engine.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class SpriteView extends Pane {
	public static final int STANDARD_SPRITE_DIMENSION = 32;
	
	public static final String NOT_SEEN_OVERLAY = "/assets/img/not_seen_overlay.png";
	public static final String WAS_SEEN_OVERLAY = "/assets/img/was_seen_overlay.png";
	
	private GameScreenController controller;
	
	private Tile tile;
	private ArrayList<ImageView> images;
	private int fogOfWar;
	
	public SpriteView(Tile tile, GameScreenController controller, int fogOfWar) {
		this.setPrefSize(STANDARD_SPRITE_DIMENSION, STANDARD_SPRITE_DIMENSION);
		this.setOnMouseClicked((event) -> handleClick(event));
		this.controller = controller;
		
		this.fogOfWar = fogOfWar;
		
		setTile(tile);
	}
	
	public void setTile(Tile newTile) {
		tile = newTile;
		images = new ArrayList<ImageView>();
		
		setupImageViews();
		drawImageViews();
	}
	
	private void handleClick(MouseEvent event) {
		controller.tileClicked(this);
		event.consume();
	}
	
	private void setupImageViews() {
		images.clear();
		
		if(fogOfWar == LivingEntity.NOT_SEEN) {
			Image sprite = new Image(NOT_SEEN_OVERLAY);
			images.add(new ImageView(sprite));
		} else {
			Entity baseEntity = tile.getBaseEntity();
			Image sprite = baseEntity.getSprite();
			images.add(new ImageView(sprite));
			
			if(fogOfWar == LivingEntity.WAS_SEEN) {
				sprite = new Image(WAS_SEEN_OVERLAY);
				images.add(new ImageView(sprite));
			} else {
				ArrayList<Entity> occupants = tile.getOccupants();
				for(Entity occupant : occupants) {
					sprite = occupant.getSprite();
					images.add(new ImageView(sprite));
				}
			}
		}
	}
	
	private void drawImageViews() {
		this.getChildren().clear();
		
		for(ImageView img : images) {
			this.getChildren().add(img);
		}
	}
}
