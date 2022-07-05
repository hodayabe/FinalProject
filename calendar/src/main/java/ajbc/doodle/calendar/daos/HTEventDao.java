package ajbc.doodle.calendar.daos;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import ajbc.doodle.calendar.entities.Event;

@SuppressWarnings("unchecked")
@Repository("htEDao")
public class HTEventDao implements EventDao {

	@Autowired
	private HibernateTemplate template;


	//CRUD operations
	@Override
	public void addEvent(Integer userId ,Event event) throws DaoException {
		template.persist(event);
	}
	@Override
	public void updateEvent(Integer userId ,Event event) throws DaoException {
		template.merge(event);
	}
	@Override
	public Event getEvent(Integer eventId) throws DaoException {
		Event ev = template.get(Event.class, eventId);
		if (ev ==null)
			throw new DaoException("No Such User in DB");
		return ev;
	}
//	@Override
//	public void deleteEvent(Integer userId) throws DaoException {
//		User us = getUser(userId);
////		us.setDiscontinued(1);
//		updateUser(us);
//	}

		@Override
		public List<Event> getAllEvents() throws DaoException {
			DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
			List<Event> eventsList = (List<Event>)template.findByCriteria(criteria);
			if(eventsList==null)
				throw new DaoException("No users found in DB");
			return eventsList;
		}


}
