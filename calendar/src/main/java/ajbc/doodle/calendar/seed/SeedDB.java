package ajbc.doodle.calendar.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.services.UserService;
import java.time.LocalDate;
import java.util.List;

@Component
public class SeedDB {

	@Autowired
	private UserService userService;

	@EventListener
	public void seed(ContextRefreshedEvent event) {
		try {
			seedUsersTable();
		} catch (DaoException e) {
			e.printStackTrace();
		}
	}

	private void seedUsersTable() throws DaoException {
		User user = new User();
		user.setFirstName("Hodaya");
		user.setLastName("David");
		user.setEmail("test@test.com");
		user.setBirthdate(LocalDate.of(2000, 11, 12));
		user.setJoinDate(LocalDate.now());
//		userService.addUser(user);
//		userService.getAllUsers().stream().forEach(System.out::println);
	}

}