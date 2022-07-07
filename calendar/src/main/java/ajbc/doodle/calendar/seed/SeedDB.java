package ajbc.doodle.calendar.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.enums.RepeatingOptions;
import ajbc.doodle.calendar.enums.Units;
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.NotificationService;
import ajbc.doodle.calendar.services.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@Component
public class SeedDB {

	
	@Autowired
	private UserService userService;
	@Autowired
	private EventService eventService;
	@Autowired
	private NotificationService notificationService;
	


	@EventListener
	public void seed(ContextRefreshedEvent event) {
//		try {
//			seedUsersTable();
//			seedEventTable();
//			
//		} catch (DaoException e) {
//			e.printStackTrace();
//		}
	}



	private void seedUsersTable() throws DaoException {

		User user1 = new User();
		user1.setFirstName("zelda");
		user1.setLastName("rrrrr");
		user1.setEmail("zzzzz@test.com");
		user1.setBirthDate(LocalDate.of(1999, 10, 31));
		user1.setJoinDate(LocalDate.now());
		user1.setIsActive(0);
		userService.addUser(user1);
		
		User user2 = new User();
		user2.setFirstName("Ayala");
		user2.setLastName("Maskalchi");
		user2.setEmail("ayala@test.com");
		user2.setBirthDate(LocalDate.of(1999, 10, 31));
		user2.setJoinDate(LocalDate.now());
		user2.setIsActive(0);;
		userService.addUser(user2);
	}

	
	private void seedEventTable() throws DaoException {
		
		Notification notification = new Notification();
		notification.setEventId(1000);
		notification.setUserId(1000);
		notification.setEvent(eventService.getEvent(1000));
		notification.setLocalDateTime(LocalDateTime.of(2022, 10, 31,10,50));
		notification.setMessage("Good Morning");
		notification.setQuantity(3);
		notification.setTitle("mornung");
		notification.setUnit(Units.HOURS);

		Event event1 = new Event();
		event1.setOwnerId(1000);
		event1.setTitle("rrrrrrrrrrr");
		event1.setIsAllDay(1);
		event1.setStart(LocalDateTime.of(2022, 10, 31,10,50));
		event1.setEnd(LocalDateTime.of(2022, 10, 31,00,50));
		event1.setAddress("Jerusalem");
		event1.setDescription("gggggggg");
		event1.setDiscontinued(0);
		event1.setRepeating(RepeatingOptions.NONE);
		event1.setGuests(Arrays.asList(userService.getUser(1001)));
		
		Set<Notification> notifications = new HashSet<Notification>();
		notifications.add(notification);
		event1.setNotifications(notifications);
		
		
		eventService.addEvent(event1);
	}
	


}






