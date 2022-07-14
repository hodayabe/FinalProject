package ajbc.doodle.calendar.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class UserTest {

	private User user;
	
	private static final int ID = 100;
	private static final String FIRST_NAME = "Hodaya", LAST_NAME = "David",EMAIL = "hodaya@gmail.com";
	private static final LocalDate BIRTHDATE = LocalDate.of(1998, 11, 06);
	private static final Integer IS_LOGGED_IN = 0, IS_ACTIVE = 1;
	private static final Set<Event> EVENTS = new HashSet<Event>();
	private static final Set<Notification> NOTIFICATIONS = new HashSet<Notification>();

	
	
	public UserTest() {
		user = new User();
		user.setUserId(ID);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setEmail(EMAIL);
		user.setBirthDate(BIRTHDATE);
		user.setLoggedIn(IS_LOGGED_IN);
		user.setIsActive(IS_ACTIVE);
		
		EVENTS.add(new Event());
		user.setEvents(EVENTS);
		NOTIFICATIONS.add(new Notification());
	}
	
	@Test
	void testFieldsValidation() {
		assertEquals(ID, user.getUserId());
		assertEquals(FIRST_NAME, user.getFirstName());
		assertEquals(LAST_NAME, user.getLastName());
		assertEquals(EMAIL, user.getEmail());
		assertEquals(BIRTHDATE, user.getBirthDate());
		assertEquals(IS_LOGGED_IN, user.getLoggedIn());
		assertEquals(IS_ACTIVE, user.getIsActive());
		
		assertArrayEquals(EVENTS.toArray(), user.getEvents().toArray());
	}
	
}
