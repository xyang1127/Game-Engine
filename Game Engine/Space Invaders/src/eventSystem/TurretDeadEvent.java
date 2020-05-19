package eventSystem;

import java.util.HashMap;
import java.util.UUID;

public class TurretDeadEvent extends event{
	public TurretDeadEvent(int delay, UUID id) {
		super(delay);
		eventType = "TurretDead";
		eventArguments = new HashMap<>();
		eventArguments.put("id", id);
		priority = 3;
	}
}
