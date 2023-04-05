import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.Serializable;
import java.net.URL;
import java.util.*;

public class ClientController implements Initializable {

    private Client clientConnection;
    private String username;
    private HashSet<String> recipients = new HashSet<>();

    @FXML
    private TextField messageTextField;

    @FXML
    private ListView<String> listItems;

    @FXML
    private ListView<String> listUsers;

    @FXML
    private ListView<String> listRecipients;

    @FXML
    private Button removeRecipientButton;

    @FXML
    private Text connectedAsText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setUsername(String s) {
        username = s;
        connectedAsText.setText("Connected as: " + username);
        recipients.add(username);
    }

    public void setClient(Client clientConnection) {
        this.clientConnection = clientConnection;
    }

    public void addData(Serializable data) {
        Data info = (Data) data;

        listUsers.getItems().clear();
        for (String name : info.clients) {
            if (!name.equals(username)) { listUsers.getItems().add(name); }
        }

        // update recipient options when users leave and remove any recipients who left server
        for (String rec: recipients) {
            if (!info.clients.contains(rec)) {
                recipients.remove(rec);
                listRecipients.getItems().clear();
                for (String r: recipients) {
                    if (!Objects.equals(r, username)) {
                        listRecipients.getItems().add(r);
                    }
                }
            }
        }


        if (info.hasMessage) {
            if (info.recipients.contains(this.username) && info.recipients.size() > 1) {  // user is in DM
                StringBuilder dmList = new StringBuilder();
                for(String user : info.recipients) { dmList.append(user); dmList.append(", "); }
                dmList.delete(dmList.length()-2,dmList.length());  // remove last 2 chars
                listItems.getItems().add("(DM group: " + dmList + ") " + info.sender + ": " + info.inMsg);
            }
            else{
                if (info.recipients.size() == 1){     // message sent to main chat
                    listItems.getItems().add(info.sender + ": " + info.inMsg);
                }
            }
        }


    }

    public void sendMessage() {
        clientConnection.info.sender = username;
        clientConnection.info.outMsg = messageTextField.getText();  // set message to serializable data class
        clientConnection.info.recipients = recipients;
        messageTextField.clear();

        if (clientConnection.info.outMsg != "") {
            clientConnection.info.hasMessage = true;
        }
        else {
            clientConnection.info.hasMessage = false;
        }
        clientConnection.send();                                    // tell client to send data class
    }

    public void addRecipient() {
        String selectedUser = listUsers.getSelectionModel().getSelectedItem();

        if (!listRecipients.getItems().contains(selectedUser)) {  // check if user already in recipients
            listRecipients.getItems().add(selectedUser);
            recipients.add(selectedUser);
        }

        if (listRecipients.getItems().size() > 0) {  // enable remove button if necessary
            removeRecipientButton.setDisable(false);
        }
    }

    public void removeRecipient() {
        String user = listRecipients.getSelectionModel().getSelectedItem();
        listRecipients.getItems().remove(listRecipients.getSelectionModel().getSelectedItem());

        this.recipients.remove(user);

        if (listRecipients.getItems().size() == 0) {  // disable remove button if necessary
            removeRecipientButton.setDisable(true);
        }
    }
}
