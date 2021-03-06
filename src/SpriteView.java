

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class SpriteView extends Pane {
	private ImageView view;
	private GameScreenController controller;
	private Entity entity;
	
	public SpriteView(GameScreenController controller, Entity entity) {
		this.setOnMouseClicked((event) -> handleClick(event));
		this.controller = controller;
		this.entity = entity;
		
		Image sprite = entity.getSprite();
		view = new ImageView(sprite);
		
		this.getChildren().add(view);
	}
	
	public boolean isEntity(Entity otherEntity) {
		return (entity == otherEntity);
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	private void handleClick(MouseEvent event) {
		controller.tileClicked(this);
		event.consume();
	}
	
	/*public static final int STANDARD_SPRITE_DIMENSION = 32;
	
	public static final String NOT_SEEN_OVERLAY = "/assets/img/not_seen_overlay_2.png";
	public static final String WAS_SEEN_OVERLAY = "/assets/img/was_seen_overlay.png";
	
	private GameScreenController controller;
	
	private Tile tile;
	private ArrayList<ImageView> images;
	private ImageView livingEntitySprite;
	private int fogOfWar;
	
	public SpriteView(Tile tile, GameScreenController controller, int fogOfWar) {
		this.setPrefSize(STANDARD_SPRITE_DIMENSION, STANDARD_SPRITE_DIMENSION);
		this.setOnMouseClicked((event) -> handleClick(event));
		this.controller = controller;
		
		setTile(tile, fogOfWar);
	}
	
	public void setTile(Tile newTile, int fogOfWar) {
		tile = newTile;
		this.fogOfWar = fogOfWar;
		images = new ArrayList<ImageView>();
		
		setupImageViews();
		drawImageViews();
	}
	
	public void setTile(Tile newTile) {
		tile = newTile;
		images = new ArrayList<ImageView>();
		
		setupImageViews();
		drawImageViews();
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
					ImageView imgView = new ImageView(sprite);
					images.add(imgView);
					
					if(occupant instanceof LivingEntity) {
						livingEntitySprite = imgView;
					}
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
	
	public ImageView getLivingEntitySprite() { return livingEntitySprite; }*/
}
