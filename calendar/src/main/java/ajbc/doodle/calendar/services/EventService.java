package ajbc.doodle.calendar.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.daos.NotificationDao;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;


@Service
public class EventService {

	@Autowired
	@Qualifier("htEDao")
	EventDao eventDao;
	@Autowired
	@Qualifier("htUDao")
	UserDao userDao;
	@Autowired
	@Qualifier("htNDao")
	NotificationDao notificationDao;
	
	public void addEvent(Event event) throws DaoException{
//		User user = userDao.getUser(event.getOwnerId());
//		// Check if user owner is exist in db.
//		if(user == null)
//			throw new DaoException("User no exist in DB");
		Set<Notification> notifications = event.getNotifications();
		
		if(!notifications.isEmpty()) {
			List<Notification> listNot = new ArrayList<Notification>(notifications);
			for (int i = 0; i < listNot.size(); i++) {
				notificationDao.addNotification(listNot.get(i));
			}
		}	
		eventDao.addEvent(event);
	}
	
	public void updateEvent(Event event) throws DaoException {
		eventDao.updateEvent(event);
	}

	public Event getEvent(Integer userId) throws DaoException {
		return eventDao.getEvent(userId);
	}
	
	public void deleteEvent(Integer eventId) throws DaoException {
		eventDao.deleteEvent(eventId);
	}
	
	public List<Event> getAllEvent() throws DaoException{
		return eventDao.getAllEvent();
	}
}