package eventSystem;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import timeSystem.Timeline;

// singletion class for managering events
// can regsiter and dispatch events

public class EventManager {
	
	public static EventManager single_instance = null;
	public static Timeline timeline;
	
	public static EventManager getEventManager() {
		if(single_instance == null)
			single_instance = new EventManager(timeline);
		
		if(timeline == null)
			System.out.println("you need to create a timeline explicitly instead of calling this function");
		
		return single_instance;
	}
	
	// used for registration: eventType -> linkedlist of Interested object
	private Hashtable<String, CopyOnWriteArrayList<handleable>> registrationList;
	
	public PriorityQueue<event> eventQueue;
	
	public void register(String eventType, handleable interestedObject) { // eventType must be a string instead of class, becasue class can't be passed as a parameter
		if(registrationList.containsKey(eventType)) {
			registrationList.get(eventType).add(interestedObject);
		}else {
			registrationList.put(eventType, new CopyOnWriteArrayList<>());
			registrationList.get(eventType).add(interestedObject);
		}
	}
	
	public void deleteRegistration(String e) {
		registrationList.remove(e);
	}
	
	// may be there is a synchronize block here
	public void enqueueEvent(event e) { // enqueue event
		eventQueue.add(e);
	}
	
	public void dispatch() { // dequeue events when necessary
		event temp;
		event e = eventQueue.peek();
		while((e!=null) && (e.deliveryTime<=timeline.getCurrentTics())) {
			temp = eventQueue.remove();
			dispatchEvents(temp);
			e = eventQueue.peek();
		}
	}
	
	private void dispatchEvents(event e) {
		for(handleable event : registrationList.get(e.eventType))
			event.onEvent(e);
	}
	
	public EventManager(Timeline t) {
		registrationList = new Hashtable<>();
		eventQueue = new PriorityQueue<>(deliveryTimeComparator);
		timeline = t;
		single_instance = this;
	}
	
	Comparator<event> deliveryTimeComparator = new Comparator<event>() {
		@Override
		public int compare(event e1, event e2) {
			return (int) (e1.deliveryTime-e2.deliveryTime);
		}
	};
	
}
