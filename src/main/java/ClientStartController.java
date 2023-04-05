import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientStartController implements Initializable {
    @FXML
    private BorderPane root;

    @FXML
    private TextField usernameTextField;

    private String username;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /*
        the purpose of this method is to get the username entered by the user
     */
    public void getUsername() {
        username = usernameTextField.getText();
    }

    /*
        this method connects the client to the serverConnection using the entered username
     */
    public void connectClient() throws IOException {
        getUsername();
        if (!username.equals("")) {
            // start client
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClientRun.fxml"));
            Parent root2 = loader.load();
            root2.getStylesheets().add("/styles/ClientStyle.css");

            // grab the stage, set window title
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setTitle("Project 4: Chatting Application Client");
            
            // set username
            ClientController ctr = loader.getController();  // get client run controller to set username value
            ctr.setUsername(username);

            // finally, update the root scene to serverConnection scene
            root.getScene().setRoot(root2);

            // create client run controller and client connection
            ClientController controller = loader.getController();
            Client clientConnection = new Client(username, data -> Platform.runLater(() -> controller.addData(data)));
            clientConnection.start();
            controller.setClient(clientConnection);
        } else {
            // TODO: 4/21/22 handle no username entered 
        }
    }
}
