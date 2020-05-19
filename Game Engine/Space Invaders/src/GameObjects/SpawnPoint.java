package GameObjects;

import java.util.UUID;

public class SpawnPoint extends GameObject{
	public float x, y;
	
	public SpawnPoint(float x, float y) {
		id = UUID.randomUUID();
		this.x = x;
		this.y = y;
	}
}
