package ajbc.doodle.calendar.seed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.enums.RepeatingOptions;

@Component
public class SeedDB {

	private final RepeatingOptions NONE_REP = RepeatingOptions.NONE;

	@Autowired
	private UserDao userDao;

	@Autowired
	private EventDao eventDao;

	@EventListener
	public void seed(ContextRefreshedEvent event) throws DaoException {
//		seedUsersTable();
		seedEventTable();
	}

	private void seedUsersTable() throws DaoException {
		List<User> users = userDao.getAllUsers();
		if (users == null || users.size() == 0) {
			userDao.addUser(
					new User("Hodaya", "David", "hodaya@gmail.com", LocalDate.of(1996, 05, 02), LocalDate.now(), 0));
			userDao.addUser(
					new User("Noum", "David", "noum@gmail.com", LocalDate.of(1994, 04, 06), LocalDate.now(), 0));

			userDao.addUser(
					new User("Ariel", "David", "ariel@gmail.com", LocalDate.of(2004, 04, 15), LocalDate.now(), 0));
		}
	}

	private void seedEventTable() throws DaoException {
		List<User> guests = new ArrayList<User>();
		guests.add(userDao.getUserById(1000));
		guests.add(userDao.getUserById(1001));

		List<Event> events = eventDao.getAllEvents();
		if (events == null || events.size() == 0) {
			eventDao.addEvent(new Event(1000, "Exam", 0, LocalDateTime.of(2022, 12, 11, 10, 0),
					LocalDateTime.of(2022, 07, 10, 15, 0), "university", "math", NONE_REP, 0,
					new ArrayList<User>()));

			eventDao.addEvent(new Event(1002, "BirthDay", 0, LocalDateTime.of(2022, 07, 10, 10, 0),
					LocalDateTime.of(2022, 07, 10, 15, 0), "Home", "Birthday", NONE_REP, 0, guests));
		}
	}

}