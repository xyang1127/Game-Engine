package Network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ServerOutputThread implements Runnable{
	
	private Socket socket;
	private ObjectOutputStream objectOutputStream;
	private ServerTemp serverTemp;
	
	public void run() {
		try {
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			while(true) {
				try {
					synchronized (Server.server) {
						if(serverTemp.ServerUpdateIsDone) {
							objectOutputStream.reset();
							objectOutputStream.writeObject(Server.gameWorld);
							//serverTemp.ServerInputIsDone = false;
							serverTemp.ServerUpdateIsDone = false;
							//System.out.println("has sent data to the client");
						}
					}
				} catch (SocketException e) {}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ServerOutputThread(Socket s, ServerTemp st) {
		socket = s;
		serverTemp = st;
	}
}
