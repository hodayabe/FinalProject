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

@Component()
public class NotificationService {

	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private EventDao eventDao;

	@Autowired
	private UserDao userDao;

	public void addNotification(int userId, int eventId, Notification notification) throws DaoException {
		if (!userIsParticipant(eventId, userId))
			throw new DaoException("This user is not participates in this event");
		notification.setEvent(eventDao.getEvent(eventId));
		notification.setUser(userDao.getUser(userId));
		notificationDao.addNotification(notification);
	}

	public List<Notification> getAllNotifications() throws DaoException {
		return notificationDao.getAllNotifications();
	}

	private boolean userIsParticipant(int eventId, int userId) throws DaoException {
		Event event = eventDao.getEvent(eventId);
		return event.getGuests().stream().map(User::getUserId).anyMatch(id -> id == userId);
	}

}