package ajbc.doodle.calendar.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;

//@Component
@Service
public class UserService {

	@Autowired
	@Qualifier("htUDao")
	UserDao userDao;

	@Autowired
	@Qualifier("htEDao")
	EventDao eventDao;

	public void addUser(User user) throws DaoException {
		user.setIsActive(1);
		userDao.addUser(user);
	}

	public User getUser(Integer userId) throws DaoException {
		return userDao.getUser(userId);
	}

	public List<User> getAllUsers() throws DaoException {
		return userDao.getAllUsers();
	}

	public void updateUser(User user) throws DaoException {
		userDao.updateUser(user);
	}

	public List<User> getUsersByEvent(Integer eventId) throws DaoException {
		Event event = eventDao.getEvent(eventId);
		List<User> usres = event.getGuests();
		usres.add(userDao.getUser(event.getOwnerId()));
		return usres;

	}

	public User getUserByEmail(String email) throws DaoException {
		return userDao.getUserByEmail(email);
	}

}