package ajbc.doodle.calendar.manager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.services.MessagePushService;
import ajbc.doodle.calendar.services.NotificationService;
import ajbc.doodle.calendar.services.UserService;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Component
public class NotificationManager {

	@Autowired
	UserService userService;
	@Autowired
	NotificationService notificationService;
	@Autowired
	MessagePushService messagePushService;

	private final int NUM_THREAD_M = 1;
	private LocalDateTime dateTime = null;
	private ScheduledExecutorService executorManager = Executors.newScheduledThreadPool(NUM_THREAD_M);
	private Queue<Notification> notificationsQueue = new PriorityQueue<Notification>(new Comparator<Notification>() {

		@Override
		public int compare(Notification n1, Notification n2) {

			if(n1.getStartDateTime().equals(n2.getStartDateTime()))
				return 0;

			else if(n1.getStartDateTime().isAfter(n2.getStartDateTime()))
				return 1;

			return -1;
		}});

	
	@EventListener
	public void start(ContextRefreshedEvent event) {
		try {
			
			List<Notification> nots = notificationService.getUntreatedNotifications();
			notificationsQueue.addAll(nots);
			
			if(!notificationsQueue.isEmpty()) {
				createThread(notificationsQueue.peek().getStartDateTime());
			}
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	
	private void createThread(LocalDateTime dateTime) {

		this.dateTime = dateTime;

		long secondsToSleep = timeToSleep(dateTime);

		if (executorManager != null) {
			executorManager.shutdownNow();
		}

		executorManager = Executors.newScheduledThreadPool(NUM_THREAD_M);
		executorManager.schedule(queueManager, secondsToSleep, TimeUnit.SECONDS);
	}
	

	public long timeToSleep(LocalDateTime dateTime) {
		long secondsToSleep = LocalDateTime.now().until(dateTime, ChronoUnit.SECONDS);
		return secondsToSleep < 0 ? 0 : secondsToSleep;
//		long sleep = Duration.between(LocalDateTime.now(),dateTime).getSeconds();
//		return sleep < 0 ? 0 : sleep;
		
		
	}
	
	public void addToQueue(Notification notification) {
		notificationsQueue.add(notification);
		if (dateTime == null || notification.getStartDateTime().isBefore(dateTime)) {
			createThread(notification.getStartDateTime());
		}
	}

	public void removeFromQueue(Notification notification) {
		notificationsQueue.remove(notification);
	}
	
	
	

	public Runnable queueManager = () -> {
		List<Notification> list = new ArrayList<Notification>();
		ExecutorService executor = Executors.newCachedThreadPool();
		while(!notificationsQueue.isEmpty()) {

			if(notificationsQueue.peek().getStartDateTime().equals(dateTime) || notificationsQueue.peek().getStartDateTime().isBefore(dateTime)) { 
				list.add(notificationsQueue.poll());
			}
			else
				break;
		}

		System.out.println("list: "+list);

		for (int i = 0; i < list.size(); i++) {
			executor.execute(new Thread(list.get(i), userService, messagePushService,notificationService));
		}
		
		if(!notificationsQueue.isEmpty())
			createThread(notificationsQueue.peek().getStartDateTime());
		else
			dateTime = null;


	};






}