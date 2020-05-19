package eventSystem;

public class StartRecordingEvent extends event{
	
	public StartRecordingEvent(int delay) {
		super(delay);
		eventType = "StartRecording";
		priority = 3;
	}
}
