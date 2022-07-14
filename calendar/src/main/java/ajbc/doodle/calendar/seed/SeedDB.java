package ajbc.doodle.calendar.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.NotificationDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.enums.RepeatingOptions;
import ajbc.doodle.calendar.enums.Units;
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Component
public class SeedDB {

	
	@Autowired
	private UserService userService;
	@Autowired
	private EventService eventService;
	@Autowired
	private NotificationDao notificationDao;
	


	@EventListener
	public void seed(ContextRefreshedEvent event) throws DaoException{
//		
//			seedUsersTable();
//			seedEventTable();
//			seedNotificationTable();
	}


	private void seedUsersTable() throws DaoException {
		List<User> users = userService.getAllUsers();
		if (users == null || users.size() == 0) {
			userService.addUser(
					new User("Hodaya", "David", "hodaya@gmail.com", LocalDate.of(1998, 11, 06),LocalDate.now()));
			
			userService.addUser(
					new User("Noam", "David", "noam@gmail.com", LocalDate.of(1996, 12, 05), LocalDate.now()));
			
			userService.addUser(
					new User("shir", "levy", "shir@gmail.com", LocalDate.of(2000, 01, 01),LocalDate.now()));
		}
	}
	
	
	
	private void seedEventTable() throws DaoException {
		
		User owner1 = userService.getUser(1000);
		Set<User> guests1 = new HashSet<User>();
		guests1.add(userService.getUser(1001));
//		guests1.add(owner1);
		
		User owner2 = userService.getUser(1001);
		Set<User> guests2 = new HashSet<User>();
		guests2.add(userService.getUser(1000));
		guests2.add(userService.getUser(1002));
//		guests2.add(owner2);
		
		List<Event> events = eventService.getAllEvent();
		if (events == null || events.size() == 0) {
			eventService.addEvent(new Event(owner1, "Exam", 0, LocalDateTime.of(2022, 07, 10, 8, 0),
					LocalDateTime.of(2022, 07, 10, 12, 0), "Azrieli", "math", RepeatingOptions.NONE, 1, guests1),owner1.getUserId());
			
			eventService.addEvent(new Event(owner2, "Wedding", 0, LocalDateTime.of(2022, 10, 12, 20, 30),
					LocalDateTime.of(2022,10, 12, 23, 0), "label", "Wedding", RepeatingOptions.NONE, 1, guests2),owner2.getUserId());
		}
		
//		eventService.addEvent(new Event(owner2, "lobaa", 0, LocalDateTime.of(2022, 07, 12, 9, 30),
//				LocalDateTime.of(2022,07, 12, 23, 0), "ego", "ego", RepeatingOptions.NONE, 1, guests2),owner2.getUserId());
	}
	
	
	private void seedNotificationTable() throws DaoException {
		User user1 = userService.getUser(1000);
		User user2 = userService.getUser(1001);
		User user3 = userService.getUser(1002);

		Event event1 = eventService.getEvent(1000);
		Event event2 = eventService.getEvent(1001);

		List<Notification> notifications = notificationDao.getAllNotifications();
		if (notifications == null || notifications.size() == 0) {
			notificationDao.addNotification(new Notification(event1, user1,event1.getTitle(), "Study", Units.HOURS, 10));
			notificationDao.addNotification(new Notification(event1, user1,event1.getTitle(), "Study", Units.HOURS, 5));
			notificationDao.addNotification(new Notification(event2, user1,event2.getTitle(), "Buy gift", Units.HOURS, 10));
		}
	}
	
	
	

}






