import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

public class Server {
	int count = 1;
	int port = 0;
	ArrayList<ClientThread> clients = new ArrayList<>();
	HashSet<ClientThread> clientList = new HashSet<>();
	TheServer serverConnection;
	private Consumer<Serializable> callback;
	
	Server(Consumer<Serializable> call){
		callback = call;
		serverConnection = new TheServer();
	}
	
	public void startServer() {
			serverConnection.start();
	}

	public class TheServer extends Thread {
		public void run() {
			try (ServerSocket mysocket = new ServerSocket(5555)) {

				while (true) {
					ClientThread c = new ClientThread(mysocket.accept(), count);
					clients.add(c);
					clientList.add(c);
					c.start();
					count++;
				}
			} catch (Exception e) {
				callback.accept("Server socket did not launch");
			}
		}
	} // end of TheServer

	class ClientThread extends Thread {
		Socket connection;
		int count;
		String name;
		ObjectInputStream in;
		ObjectOutputStream out;
		Data dataObj;

		ClientThread(Socket s, int count) {
			this.connection = s;
			this.count = count;
			this.dataObj = new Data();
		}

		public void updateClients(Data message) {
			for (int i = 0; i < clients.size(); i++) {
				ClientThread t = clients.get(i);
				try {
					t.out.reset();
					t.out.writeObject(message);
				}
				catch (Exception e) {
				}
			}
		}

		public void run() {
			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
			}
			catch (Exception e) {
				System.out.println("Streams not open");
			}

			while (true) {
				try {
					dataObj = (Data) in.readObject();

					// new client, add to client list & update others
					if (dataObj.newClient) {
						synchronized (serverConnection) {
							this.name = dataObj.sender;
							callback.accept(name + " has connected to server!");

							//update client lists
							for (ClientThread x : clientList){
								dataObj.clients.add(x.name);
							}
							dataObj.newClient = false;
							updateClients(dataObj);
						}
					}
					else {
						synchronized (serverConnection) {
							Data recInfo = dataObj;
							if (dataObj.hasMessage) {
								String data = dataObj.outMsg;
								callback.accept(name + " sent: " + data);
								recInfo = dataObj;
								recInfo.clients = dataObj.clients;
								recInfo.recipients = dataObj.recipients;
								recInfo.inMsg = dataObj.outMsg;

							}
							updateClients(recInfo);
							dataObj.hasMessage = false;
							dataObj.inMsg = "";
						}
					}

				}
				catch (Exception e) {
					synchronized (serverConnection) {
						callback.accept(name + " left the chat!");

						// Clear clients and add from clientList
						dataObj.clients.clear();
						for (ClientThread t : clientList) {
							dataObj.clients.add(t.name);
						}

						// Remove cur instance from clients
						clients.remove(this);
						clientList.remove(this);
						dataObj.clients.remove(this.name);

						updateClients(dataObj);
					}
					break;
				}
			}
		} // end of run
	}  // end of ClientThread
}  // end of serverConnection
