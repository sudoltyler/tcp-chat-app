import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {

    @FXML
    ListView<String> listItems;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void addData(Serializable data) {
        listItems.getItems().add(data.toString());
    }

}
