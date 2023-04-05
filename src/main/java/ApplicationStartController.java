import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationStartController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private BorderPane root;

    /*
        this method loads and changes to the start serverConnection scene
     */
    public void startServer() throws IOException {

        // load serverConnection from fxml, set style
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ServerRun.fxml"));
        Parent root2 = loader.load();
        root2.getStylesheets().add("/styles/ServerStyle.css");

        // grab the stage, set window title
        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setTitle("Project 4: Chatting Application Server");

        // finally, update the root scene to serverConnection scene
        root.getScene().setRoot(root2);

        // create a ServerController
        ServerController controller = loader.getController();

        //start serverConnection on port
        Server serverConnection = new Server(data -> Platform.runLater(() -> controller.addData(data)));
        serverConnection.startServer();
    }

    /*
        this method loads and changes to the start client scene
     */
    public void startClient() throws IOException {

        // load serverConnection from fxml, set style
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClientStart.fxml"));
        Parent root2 = loader.load();
        root2.getStylesheets().add("/styles/ClientStyle.css");

        // grab the stage, set window title
        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setTitle("Project 4: Chatting Application Client");

        // finally, update the root scene to serverConnection scene
        root.getScene().setRoot(root2);
    }
}
