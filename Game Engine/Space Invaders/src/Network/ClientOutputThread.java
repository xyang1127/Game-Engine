package Network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientOutputThread implements Runnable{
	
	private ObjectOutputStream objectOutputStream;
	private Socket socket;
	public ClientTemp clientTemp;
	
	public void run() {
		try {
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			while(true) {
				synchronized (Client.clientTemp) {
					if(clientTemp.sendData) {
						objectOutputStream.reset();
						objectOutputStream.writeObject(clientTemp);
						clientTemp.sendData = false;
						clientTemp.operation = 0;
					}	
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ClientOutputThread(Socket s, ClientTemp c) {
		socket = s;
		clientTemp = c;
	}
	
}
