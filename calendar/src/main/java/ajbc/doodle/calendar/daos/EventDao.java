package ajbc.doodle.calendar.daos;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ajbc.doodle.calendar.entities.Event;



@Transactional(rollbackFor = {DaoException.class}, readOnly = true)
public interface EventDao {

	//CRUD operations
	@Transactional(readOnly = false)
	public default void addEvent(Integer userId ,Event event) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	@Transactional(readOnly = false)
	public default void updateEvent(Integer userId ,Event event) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default Event getEvent(Integer eventId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	@Transactional(readOnly = false)
	public default void deleteEvent(Integer eventId) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	//Queries
	
	// QUERIES
		public default List<Event> getAllEvents() throws DaoException {
			throw new DaoException("Method not implemented");
		}


		public default long count() throws DaoException {
			throw new DaoException("Method not implemented");
		}
	
	
}