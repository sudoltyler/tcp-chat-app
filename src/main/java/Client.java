import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashSet;
import java.util.function.Consumer;

public class Client extends Thread{
	String username;
	Socket socketClient;
    Data info;
	ObjectOutputStream out;
	ObjectInputStream in;
	
	private Consumer<Serializable> callback;
	
	Client(String name, Consumer<Serializable> call){
		callback = call;
        info = new Data();
		username = name;
	}
	
	public void run() {
		
		try {
			socketClient = new Socket("127.0.0.1",5555);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);

			//send username to server
			info.sender = username;
			out.writeObject(info);
			out.reset();
			info.newClient = false;

		}
		catch(Exception e) {
			callback.accept("Client failed to connect, no serverConnection found.");
		}
		
		while(true) {
			 
			try {
                info = (Data) in.readObject();
			    callback.accept(info);
			}
			catch(Exception e) {
				callback.accept("Failed to read from input stream... closing socket");
				try {
					socketClient.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				break;
			}
		}
	
    }
	
	public void send() {
		try {
			out.writeObject(info);
            out.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
