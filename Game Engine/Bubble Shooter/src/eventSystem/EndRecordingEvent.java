package eventSystem;

public class EndRecordingEvent extends event{
	
	public EndRecordingEvent(int delay) {
		super(delay);
		eventType = "EndRecording";
		priority = 3;
	}
	
}
