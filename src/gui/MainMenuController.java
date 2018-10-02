package gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainMenuController {
	@FXML
	Button newGameBtn;
	@FXML
	Button loadGameBtn;
	
	@FXML
	public void initialize() {
		newGameBtn.setOnMouseClicked((event) -> newGame(event));
		loadGameBtn.setOnMouseClicked((event) -> loadGame(event));
	}
	
	@FXML
	public void newGame(MouseEvent event) {
        try {
        	FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("GameScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), Main.DEFAULT_WINDOW_WIDTH, Main.DEFAULT_WINDOW_HEIGHT);
            
            Stage stage = new Stage();
            stage.setTitle("Roguelike");
			stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void loadGame(MouseEvent event) {
	}
}
