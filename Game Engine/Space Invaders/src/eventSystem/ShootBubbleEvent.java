package eventSystem;

import java.util.HashMap;
import java.util.UUID;

public class ShootBubbleEvent extends event{

	public ShootBubbleEvent(int delay, int colorcase, float angle, UUID id) {
		super(delay);
		eventType = "ShootBubble";
		eventArguments = new HashMap<>();
		eventArguments.put("colorcase", colorcase);
		eventArguments.put("angle", angle);
		eventArguments.put("id", id);
		priority = 3;
	}

}
