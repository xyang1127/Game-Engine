package timeSystem;

public class LocalTimeline extends Timeline{
	
	private Timeline anchoredtimeline;
	
	private long pauseTemp;
	private long temp;
	private long elapseTics;
	private long duration = 0;
	
	public LocalTimeline(Timeline timeline, float ticsize) {
		anchoredtimeline = timeline;
		localTicSize = ticsize;
	}

	@Override
	public void start() {
		startTic = anchoredtimeline.getCurrentTics();
		temp = startTic;
		elapseTics = 0;
	}

	@Override
	public long getCurrentTics() {
		if(isPaused) {
			cumulativeTics +=  (anchoredtimeline.getCurrentTics()-pauseTemp)/localTicSize;
			pauseTemp = anchoredtimeline.getCurrentTics();
		}
		elapseTics += ((anchoredtimeline.getCurrentTics()-temp)/localTicSize);
		duration = elapseTics - cumulativeTics;
		temp = anchoredtimeline.getCurrentTics();
		return duration;
	}

	@Override
	public void pause() {
		isPaused = true;
		pauseTemp = anchoredtimeline.getCurrentTics();
	}

	@Override
	public void unpause() {
		isPaused = false;
		cumulativeTics += (anchoredtimeline.getCurrentTics()-pauseTemp)/localTicSize;
	}

	@Override
	public void changeTicSize(float tic) {
		if(!isPaused) {
			elapseTics += (anchoredtimeline.getCurrentTics()-temp)/localTicSize;
			temp = anchoredtimeline.getCurrentTics();
		}else { // is paused
			cumulativeTics += (anchoredtimeline.getCurrentTics()-pauseTemp)/localTicSize;
			pauseTemp = anchoredtimeline.getCurrentTics();
		}
		localTicSize = tic;
	}

}
