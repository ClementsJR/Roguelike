

import java.io.IOException;
import java.net.URL;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class MainMenuController {
	public static final int DEFAULT_WINDOW_WIDTH = 1024;
	public static final int DEFAULT_WINDOW_HEIGHT = 576;
	
	@FXML
	Button newGameBtn;
	//@FXML
	//Button loadGameBtn;
	
	private final URL BLIP_URL = getClass().getResource("/assets/sound/mmBlip.mp3");
	private final AudioClip mmBlip = new AudioClip(BLIP_URL.toString());
	
	@FXML
	public void initialize() {
		mmBlip.setVolume(0.5);
		
		setupNewGameButton();
		//setupLoadGameButton();
	}
	
	private void setupNewGameButton() {
		newGameBtn.setOnMouseClicked((event) -> newGame(event));
		newGameBtn.setOnMouseEntered((event) -> playBlip());
	}
	
	/*private void setupLoadGameButton() {
		loadGameBtn.setOnMouseClicked((event) -> loadGame(event));
	}*/
	
	private void playBlip() {
		mmBlip.play();
	}
	
	public void newGame(MouseEvent event) {
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

        	//((GameScreenController) fxmlLoader.getController()).setupGame();
        	Platform.runLater(() -> ((GameScreenController) fxmlLoader.getController()).setupGame());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/*public void loadGame(MouseEvent event) {
	}*/
}
