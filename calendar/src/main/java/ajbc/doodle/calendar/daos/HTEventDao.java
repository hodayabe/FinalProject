package ajbc.doodle.calendar.daos;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
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
	public void addEvent(Event event) throws DaoException {
		template.persist(event);
	}

	
	@Override
	public void updateEvent(Event event) throws DaoException {
		template.merge(event);
	}
	@Override
	public Event getEvent(Integer eventId) throws DaoException {
		Event ev = template.get(Event.class, eventId);
		if (ev ==null)
			throw new DaoException("No Such Event in DB");
		return ev;
	}
	
	@Override
	public void deleteEvent(Integer eventId) throws DaoException {
		Event ev = getEvent(eventId);
		ev.setIsActive(0);
		updateEvent(ev);
	}

	
	
		@Override
		public List<Event> getAllEvents() throws DaoException {
			DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
			return (List<Event>) template.findByCriteria(criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
		}
		
		
		@Override
		public List<Event> getEventsByUser(Integer userId) throws DaoException {
			DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
			criteria.createAlias("users", "user");
			criteria.add(Restrictions.eq("user.id", userId));
			
			return (List<Event>)template.findByCriteria(criteria);
		}
		
		

}