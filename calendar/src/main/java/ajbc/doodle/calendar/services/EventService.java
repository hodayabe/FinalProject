package ajbc.doodle.calendar.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.daos.NotificationDao;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.enums.Units;


@Component
public class EventService {

	@Autowired
	EventDao eventDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	NotificationDao notificationDao;
	
	
	public void addEvent(Event event , Integer id) throws DaoException{
		User user = userDao.getUser(id);
		event.setOwner(user);
		eventDao.addEvent(event);
		
		notificationDao.addNotification(new Notification(event, user,event.getTitle(),event.getTitle(), Units.HOURS, 0));
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
		return eventDao.getAllEvents();
	}
}