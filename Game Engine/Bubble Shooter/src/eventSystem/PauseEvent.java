package eventSystem;

public class PauseEvent extends event{

	public PauseEvent(long duration) {
		super(duration);
		eventType = "Pause";
		priority = 3;
	}
	
}
