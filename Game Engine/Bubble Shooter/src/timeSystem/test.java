package timeSystem;

public class test {

	public static void main(String[] args) {

		GlobalTimeline globalTimeline = new GlobalTimeline(1);
		
		globalTimeline.start();
		
		while(true) {
			System.out.println(globalTimeline.getCurrentTics());
			if(globalTimeline.getCurrentTics() == 3000)
				globalTimeline.pause();
		}
		
	}

}
