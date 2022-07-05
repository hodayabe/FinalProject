package ajbc.doodle.calendar.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.daos.EventDao;
import ajbc.doodle.calendar.entities.Event;


@Service
public class EventService {

	@Autowired
	@Qualifier("htEDao")
	EventDao eventDao;

	public void addEvent(Integer userId , Event event) throws DaoException{
		eventDao.addEvent(userId,event);
	}
	
	
	public List<Event> getAllEvents() throws DaoException{
		return eventDao.getAllEvents();
	}
}
