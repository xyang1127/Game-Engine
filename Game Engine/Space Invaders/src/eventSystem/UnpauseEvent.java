package eventSystem;

public class UnpauseEvent extends event{
	
	public UnpauseEvent(long delay) {
		super(delay);
		eventType = "Unpause";
		priority = 3;
	}
	
}
