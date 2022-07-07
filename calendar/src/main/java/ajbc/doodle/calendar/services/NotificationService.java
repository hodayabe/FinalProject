package ajbc.doodle.calendar.services;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.daos.NotificationDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;


@Service
public class NotificationService {

	@Autowired
	@Qualifier("htNDao")
	NotificationDao notificationDao;
	@Autowired
	@Qualifier("htEDao")
	EventDao eventDao;
	
	public void addNotification(Notification notification) throws DaoException{
		Event event = eventDao.getEvent(notification.getEventId());
		// Check if event is exist in db.
		if(event == null)
			throw new DaoException("Event no exist in DB");
		notification.setEvent(event);
		notificationDao.addNotification(notification);
	}
	
	public void updateNotification(Notification notification) throws DaoException {
		notificationDao.updateNotification(notification);
	}

	public Notification getNotification(Integer notificationId) throws DaoException {
		return notificationDao.getNotification(notificationId);
	}
	
//	public void deleteNotification(Integer eventId) throws DaoException {
//		notificationDao.deleteEvent(eventId);
//	}
	
	public List<Notification> getAllNotification() throws DaoException{
		return notificationDao.getAllNotification();
	}
}