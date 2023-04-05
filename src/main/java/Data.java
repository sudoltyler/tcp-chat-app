import java.io.Serializable;
import java.util.HashSet;

public class Data implements Serializable {
    String sender;
    HashSet<String> clients;
    HashSet<String> recipients;
    String inMsg;
    String outMsg;
    Boolean newClient;
    Boolean hasMessage;

    Data() {
        newClient = true;
        hasMessage = false;
        sender = "";
        clients = new HashSet<>();
        recipients = new HashSet<>();
        inMsg = "";
        outMsg = "";
    }

}
