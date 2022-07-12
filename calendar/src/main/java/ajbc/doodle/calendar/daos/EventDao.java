package ajbc.doodle.calendar.daos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ajbc.doodle.calendar.entities.Event;


@Transactional(rollbackFor = {DaoException.class}, readOnly = true)
public interface EventDao {

	//CRUD operations
	@Transactional(readOnly = false)
	public default void addEvent(Event even) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	@Transactional(readOnly = false)
	public default void updateEvent(Event even) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default Event getEvent(Integer eventId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	
	@Transactional(readOnly = false)
	public default Event softDeleteEvent(Integer eventId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default Event hardDeleteEvent(Integer eventId) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	//Queries
			public default List<Event> getAllEvents() throws DaoException {
			throw new DaoException("Method not implemented");
		}

		public default List<Event> getEventsByUser(Integer userId) throws DaoException {
			throw new DaoException("Method not implemented");
		}

		public default List<Event> getUpcomingEvents(Integer userId, LocalDateTime date) throws DaoException {
			throw new DaoException("Method not implemented");
		}
		
		public default List<Event> getEventOfUserInRange(Integer userId,LocalDateTime start, LocalDateTime end) throws DaoException {
			throw new DaoException("Method not implemented");
		}
	
}