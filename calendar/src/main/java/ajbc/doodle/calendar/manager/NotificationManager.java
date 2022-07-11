package ajbc.doodle.calendar.manager;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import ajbc.doodle.calendar.entities.Notification;

public class NotificationManager {
	
	
	public static Queue<Notification> notificationsQueue = new PriorityQueue<Notification>(new Comparator<Notification>() {
		@Override
		public int compare(Notification n1, Notification n2) {
			
			if(n1.getStartDateTime().equals(n2.getStartDateTime()))
				return 0;
			
			else if(n1.getStartDateTime().isAfter(n2.getStartDateTime()))
				return -1;
			
			return 1;
		}});
	
	
//	ScheduledExecutorService executorService = Executors.newScheduledThreadPool(NUM_THREADS);
//	executorService.schedule(() -> System.out.println(num++), 5, TimeUnit.SECONDS);
	
	

}
