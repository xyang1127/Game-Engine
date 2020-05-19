package eventSystem;

public class StartReplayEvent extends event{
	
	public StartReplayEvent(int delay) {
		super(delay);
		eventType = "StartReplay";
		priority = 3;
	}

}
