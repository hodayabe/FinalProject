package ajbc.doodle.calendar.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.entities.webpush.Subscription;

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

	public Set<User> getUsersWithEventInRange(String start, String end ) throws DaoException {
		return userDao.getUsersWithEventInRange(start,end);
	}
	
	public void updateUser(User user) throws DaoException {
		userDao.updateUser(user);
	}

	public Set<User> getUsersByEvent(Integer eventId) throws DaoException {
		Event event = eventDao.getEvent(eventId);
		if(event == null)
			return null;
		Set<User> usres = event.getGuests();
			usres.add(userDao.getUser(event.getOwnerId()));
		return usres;
	}

	public synchronized User getUserByEmail(String email) throws DaoException {
		return userDao.getUserByEmail(email);
	}
	
	
	public User softDeleteUser(Integer userId) throws DaoException {
		return userDao.softDeleteUser(userId);
	}

	public User hardDeleteUser(Integer userId) throws DaoException {
		return userDao.hardDeleteUser(userId);
	}

	
	//log in 	
		public void login(User user, Subscription subscription) throws DaoException {
			user.loggIn(true);
			
			user.setEndpoint(subscription.getEndpoint());
			user.setP256dh(subscription.getKeys().getP256dh());
			user.setAuth(subscription.getKeys().getAuth());
			
			userDao.updateUser(user);
		}
		
		public void logout(User user) throws DaoException {
			user.loggIn(false);
			
			user.setEndpoint(null);
			user.setP256dh(null);
			user.setAuth(null);
			userDao.updateUser(user);
		}
}