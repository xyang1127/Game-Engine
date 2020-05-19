package eventSystem;

import java.util.HashMap;
import java.util.UUID;

public class TurretSpawnEvent extends event{

	public TurretSpawnEvent(long delay, UUID id) {
		super(delay);
		eventType = "TurretSpawn";
		eventArguments = new HashMap<>();
		eventArguments.put("id", id);
		priority = 3;
	}
}
