package ajbc.doodle.calendar.daos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;

@SuppressWarnings("unchecked")
@Component
@Repository("htUDao")
public class HTUserDao implements UserDao {


	@Autowired
	private HibernateTemplate template;
	
	
	@Override
	public void addUser(User user) throws DaoException {
		template.persist(user);
	}

	

	@Override
	public void updateUser(User user) throws DaoException {
		template.merge(user);
	}



	@Override
	public User getUser(Integer userId) throws DaoException {
		User user = template.get(User.class, userId);
		if (user ==null)
			throw new DaoException("No Such User in DB");
		return user;
	}


	@Override
	public List<User> getAllUsers() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		return (List<User>)template.findByCriteria(criteria);
	}



	@Override
	public List<User> getUsersByEventId(Integer eventId) throws DaoException {
				return UserDao.super.getUsersByEventId(eventId);
	}

	
	@Override
	public User getUserByEmail(String email) throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq("email", email));
		List<User> users = (List<User>) template.findByCriteria(criteria);
		if (users.get(0) == null)
			throw new DaoException("No Such User in DB");
		return users.get(0);
	}
	
	
	@Override
	public Set<User> getUsersWithEventInRange(String start, String end) throws DaoException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
		LocalDateTime startDateTime = LocalDateTime.parse(start, formatter);
		LocalDateTime endDateTime = LocalDateTime.parse(end, formatter);
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		Criterion criterion = Restrictions.between("start", startDateTime, endDateTime);
		Criterion criterion1 = Restrictions.between("end", startDateTime, endDateTime);
		
		criteria.add(criterion);
		criteria.add(criterion1);
		
		List<Event> events = (List<Event>)template.findByCriteria(criteria);
		Set<User> users=new HashSet<User>();
	
		events.forEach(e->{
			users.add(template.get(User.class, e.getOwnerId()));
			users.addAll(e.getGuests());
		});

		return users;
	}
	
	@Override
	public User softDeleteUser(Integer userId) throws DaoException {
		User us = getUser(userId);
		us.setIsActive(0);
		updateUser(us);
		return getUser(userId);
	}

	@Override
	public User hardDeleteUser(Integer userId) throws DaoException {
		User us = getUser(userId);
		 template.delete(userId);
		 return us;
	}
	
	
}