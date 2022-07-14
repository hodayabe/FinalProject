package ajbc.doodle.calendar.daos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;

@SuppressWarnings("unchecked")
@Repository("htEDao")
public class HTEventDao implements EventDao {

	@Autowired
	private HibernateTemplate template;

	// CRUD operations
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
		if (ev == null)
			throw new DaoException("No Such Event in DB");
		return ev;
	}

	@Override
	public Event softDeleteEvent(Integer eventId) throws DaoException {
		Event ev = getEvent(eventId);
		ev.setIsActive(0);
		updateEvent(ev);
		return getEvent(eventId);
	}

	@Override
	public Event hardDeleteEvent(Integer eventId) throws DaoException {
		Event ev = getEvent(eventId);
		 template.delete(ev);
		 return ev;
	}

	@Override
	public List<Event> getAllEvents() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		return (List<Event>) template.findByCriteria(criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
	}

	@Override
	public List<Event> getEventsByUser(Integer userId) throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		Criterion criterion1 = Restrictions.eq("ownerId", userId);
		criteria.add(criterion1);
		
		return (List<Event>)template.findByCriteria(criteria);
	}
	
	

	@Override
	public List<Event> getUpcomingEvents(Integer userId, LocalDateTime date) throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		Criterion criterion1 = Restrictions.gt("startDateTime", date);
		criteria.add(criterion1);

		List<Event> events = (List<Event>) template.findByCriteria(criteria);
		
		events.forEach(e->{
			if(!isUserInEvent(e,userId))
				events.remove(e);
		});
		
		Set<Event> returnEvents =new HashSet<Event>(events);
		return new ArrayList<Event>(returnEvents);
	}

	@Override
	public List<Event> getEventOfUserInRange(Integer userId, LocalDateTime start, LocalDateTime end) throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		Criterion criterion = Restrictions.between("startDateTime", start, end);
		Criterion criterion1 = Restrictions.between("endDateTime", start, end);

		criteria.add(criterion);
		criteria.add(criterion1);

		List<Event> events = (List<Event>) template.findByCriteria(criteria);
		
		if (!userId.equals(0)) {
		events.forEach(e->{
			if(!isUserInEvent(e,userId))
				events.remove(e);});
		}
		
		Set<Event> returnEvents = new HashSet<Event>(events);

		return new ArrayList<Event>(returnEvents);
	}

	
	

	// check if the user is in the events
	private boolean isUserInEvent(Event event, Integer userId) {
		
		if(event == null)
			return false;
		
			if (event.getOwnerId().equals(userId)) 
				return true;

			List <User> users = new ArrayList<User>(event.getGuests());
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).getUserId().equals(userId)) 
					return true;	
			}
		
		return false;
	}

}