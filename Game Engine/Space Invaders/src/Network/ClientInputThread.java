package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import GameObjects.GameObject;

public class ClientInputThread implements Runnable{
	
	private ObjectInputStream objectInputStream;
	private Socket socket;
	public ClientTemp clientTemp;
	private HashMap<String, CopyOnWriteArrayList<GameObject>> h;
	
	public void run() {
		try {
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			while(true) {
				h = (HashMap<String, CopyOnWriteArrayList<GameObject>>) objectInputStream.readObject();
				Client.gameWorld = h;
				//clientTemp.ClientInputIsDone = true;
				//System.out.println("has received data from Server");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public ClientInputThread(Socket s, ClientTemp c) {
		socket = s;
		clientTemp = c;
	}
	
}
