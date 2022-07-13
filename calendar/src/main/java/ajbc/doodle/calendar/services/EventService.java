package ajbc.doodle.calendar.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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
		Set<Notification> notifications = event.getNotifications();
		event.setOwner(user);
		event.setOwnerId(id);
		Set <User> usersListList = event.getGuests();
		usersListList.add(user);
		event.setGuests(usersListList);
		event.setNotifications(new HashSet<Notification>());
		
		eventDao.addEvent(event);
		
		
		Notification curNotification;

		if(!notifications.isEmpty()) {
			List<Notification> listNot = new ArrayList<Notification>(notifications);
			for (int i = 0; i < listNot.size(); i++) {
				curNotification = listNot.get(i);
				notificationDao.addNotification(new Notification(event,user,event.getTitle(), curNotification.getTitle() , curNotification.getUnits(), curNotification.getQuantity() ));
			}
		}	
		notificationDao.addNotification( new Notification(event, user,event.getTitle(),event.getTitle(), Units.HOURS, 0));
	}


	public void updateEvent(Event event) throws DaoException {
		eventDao.updateEvent(event);
	}

	public Event getEvent(Integer eventId) throws DaoException {
		return eventDao.getEvent(eventId);
	}

	public Event softDeleteEvent(Integer eventId) throws DaoException {
		return eventDao.softDeleteEvent(eventId);
	}

	public Event hardDeleteEvent(Integer eventId) throws DaoException {
		return eventDao.hardDeleteEvent(eventId);
	}

	public List<Event> getAllEvent() throws DaoException{
		return eventDao.getAllEvents();
	}

	public List<Event> getUpcomingEvents(Integer userId, LocalDateTime date) throws DaoException {
		return eventDao.getUpcomingEvents(userId, date);
	}

	public List<Event>getEventOfUserInRange(Integer userId,LocalDateTime start, LocalDateTime end) throws DaoException {
		return eventDao.getEventOfUserInRange(userId, start,end);
	}


	public List<Event>getEventsInRange(LocalDateTime start, LocalDateTime end) throws DaoException {
		return eventDao.getEventOfUserInRange(0, start,end);
	}


}