package Network;

import java.io.Serializable;

public class ClientTemp implements Serializable{
	//public boolean ClientInputIsDone;
	//public boolean ClientUpdateIsDone;
	public int operation;
	public float mouse_x, mouse_y;
	public boolean sendData;
	
	public ClientTemp() {
		//ClientInputIsDone = true;
		//ClientUpdateIsDone = true;
		operation = 0;
		mouse_x = 0;
		mouse_y = 0;
		sendData = false;
	}
}
