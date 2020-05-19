package Network;

import java.util.UUID;

public class ServerTemp {
	public boolean ServerUpdateIsDone;
	public UUID id;
	public int operation;
	public float mouse_x, mouse_y;
	
	public ServerTemp() {
		ServerUpdateIsDone = false;
		operation = 0;
		mouse_x = 0;
		mouse_y = 0;
	}
}
