package eventSystem;

import java.util.HashMap;

public class ChangeSpeedEvent extends event{
	
	public ChangeSpeedEvent(int delay, int speedmode) {
		super(delay);
		eventType = "ChangeSpeed";
		eventArguments = new HashMap<>();
		eventArguments.put("speedmode", speedmode);
		priority = 3;
	}
}
