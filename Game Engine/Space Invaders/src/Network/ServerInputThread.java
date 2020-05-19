package Network;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

public class ServerInputThread implements Runnable{
	
	private Socket socket;
	private ObjectInputStream objectInputStream;
	private ClientTemp ct;
	private ServerTemp serverTemp;
	
	public void run() {
		try {
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			while(true) {
				try {
					ct = (ClientTemp) objectInputStream.readObject();
					synchronized (Server.server) {
						serverTemp.operation = ct.operation;
						serverTemp.mouse_x = ct.mouse_x;
						serverTemp.mouse_y = ct.mouse_y;
					}
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ServerInputThread(Socket s, ServerTemp st) {
		socket = s;
		serverTemp = st;
	}
}
