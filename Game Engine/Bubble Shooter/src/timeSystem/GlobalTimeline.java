package timeSystem;

// this is global time line, also called game time line
// this is based on real time

public class GlobalTimeline extends Timeline{
	
	private long pauseTemp;
	private long temp;
	private long elapseTics;
	private long duration = 0;
	
	public GlobalTimeline(float l) {
		localTicSize = l;
	}

	@Override
	public void pause() {
		isPaused = true;
		pauseTemp = System.currentTimeMillis();
	}
	
	@Override
	public void unpause() {
		isPaused = false;
		cumulativeTics += (System.currentTimeMillis()-pauseTemp)/localTicSize;
	}
	
	@Override
	public void start() {
		startTic = System.currentTimeMillis();
		temp = startTic;
		elapseTics = 0;
	}


	@Override
	public long getCurrentTics() {
		if(isPaused) {
			cumulativeTics +=  (System.currentTimeMillis()-pauseTemp)/localTicSize;
			pauseTemp = System.currentTimeMillis();
		}
		elapseTics += ((System.currentTimeMillis()-temp)/localTicSize);
		duration = elapseTics - cumulativeTics;
		temp = System.currentTimeMillis();
		return duration;
	}
	
	@Override
	public void changeTicSize(float tic) {
		if(!isPaused) {
			elapseTics += (System.currentTimeMillis()-temp)/localTicSize;
			temp = System.currentTimeMillis();
		}else { // is paused
			cumulativeTics += (System.currentTimeMillis()-pauseTemp)/localTicSize;
			pauseTemp = System.currentTimeMillis();
		}
		localTicSize = tic;
	}
}
