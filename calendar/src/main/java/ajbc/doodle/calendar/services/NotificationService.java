package ajbc.doodle.calendar.services;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.daos.NotificationDao;
import ajbc.doodle.calendar.daos.UserDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;

@Component()
public class NotificationService {

	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private EventDao eventDao;

	@Autowired
	private UserDao userDao;

	
	
	public void addNotification(Notification notification) throws DaoException {
		notification.setEvent(eventDao.getEvent(notification.getEventId()));
		notification.setUser(userDao.getUser(notification.getUserId()));
		notificationDao.addNotification(notification);
	}

	public Notification getNotificationById(Integer notId) throws DaoException {
		return notificationDao.getNotification(notId);
	}
	
	
	public List<Notification> getAllNotifications() throws DaoException {
		return notificationDao.getAllNotifications();
	}
	
	public List<Notification> getUntreatedNotifications() throws DaoException{
		return  notificationDao.getUntreatedNotifications();
	}

	
	
	public Notification updateNotification(Notification notification) throws DaoException {
		notification.setEvent(eventDao.getEvent(notification.getEventId()));
		notification.setUser(userDao.getUser(notification.getUserId()));
		notificationDao.updateNotification(notification);
		
		notification = notificationDao.getNotification(notification.getNotId());

		return notification;
	}
	
	
	public Notification softDeleteNotification(Integer id) throws DaoException {
		return notificationDao.softDeleteNotification(id);
	}

	public Notification hardDeleteNotification(Integer userId) throws DaoException {
		return notificationDao.hardDeleteNotification(userId);
	}
	
	
	//TODO
	private boolean userIsInEvent(int eventId, int userId) throws DaoException {
		Event event = eventDao.getEvent(eventId);
		return event.getGuests().stream().map(User::getUserId).anyMatch(id -> id == userId) || event.getOwnerId().equals(userId) ;
	}
	
	
	public List<Notification> getNotificationsByEvent(Integer eventId) throws DaoException {
		Event event = eventDao.getEvent(eventId);
		if(event == null)
			return null;
		Set<Notification> nots = event.getNotifications();
		
		
		return new ArrayList<Notification>(nots) ;
	}
	
	
	
	

}