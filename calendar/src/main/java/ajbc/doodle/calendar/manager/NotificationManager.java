package ajbc.doodle.calendar.manager;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
	
	
	public static void addNotification(Notification not) {
		
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
		
		for (int i = 2; i <6; i*=2) {
			executorService.schedule(() -> System.out.println(4+"*****"), i, TimeUnit.SECONDS);
		}
	}
	
	

	
	
	
}
