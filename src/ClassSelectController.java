import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ClassSelectController {
	public static final int DEFAULT_WINDOW_WIDTH = 1024;
	public static final int DEFAULT_WINDOW_HEIGHT = 576;
	
	@FXML
	Button warriorBtn;

	@FXML
	Button rangerBtn;

	@FXML
	Button mageBtn;
	
	@FXML
	public void initialize() {
		warriorBtn.setOnMouseClicked((event) -> warriorSelected(event));
		rangerBtn.setOnMouseClicked((event) -> rangerSelected(event));
		mageBtn.setOnMouseClicked((event) -> mageSelected(event));
	}
	
	public void warriorSelected(MouseEvent event) {
		GameScreenController controller = setUpGameScreen(event);
        Platform.runLater(() -> controller.setupGame(Warrior.class));
	}
	
	public void rangerSelected(MouseEvent event) {
		GameScreenController controller = setUpGameScreen(event);
        Platform.runLater(() -> controller.setupGame(Ranger.class));
	}
	
	public void mageSelected(MouseEvent event) {
		GameScreenController controller = setUpGameScreen(event);
        Platform.runLater(() -> controller.setupGame(Mage.class));
	}
	
	private GameScreenController setUpGameScreen(MouseEvent event) {
        try {
        	FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("GameScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
            
            Stage stage = new Stage();
            stage.setTitle("Roguelike");
			stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            
        	((Node)(event.getSource())).getScene().getWindow().hide();
        	
        	return (GameScreenController) fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
	}
}
