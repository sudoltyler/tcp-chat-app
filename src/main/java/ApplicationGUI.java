import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationGUI extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/ApplicationStart.fxml"));
			primaryStage.setTitle("Project 4: Chatting Application");
			Scene scene = new Scene(root, 600, 500);
			scene.getStylesheets().add("/styles/StartMenuStyle.css");

			primaryStage.setOnCloseRequest(t -> {
				Platform.exit();
				System.exit(0);
			});

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
