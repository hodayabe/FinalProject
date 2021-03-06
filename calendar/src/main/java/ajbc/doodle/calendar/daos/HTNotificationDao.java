package ajbc.doodle.calendar.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.manager.NotificationManager;

@SuppressWarnings("unchecked")
@Component(value = "htNDao")
public class HTNotificationDao implements NotificationDao {

	@Autowired
	private HibernateTemplate template;

	@Autowired
	private NotificationManager manager;

	// CRUD

	@Override
	public void addNotification(Notification notification) throws DaoException {
		template.persist(notification);
		manager.addToQueue(notification);
	}

	@Override
	public Notification getNotification(Integer NotificationId) throws DaoException {
		Notification not = template.get(Notification.class, NotificationId);
		if (not == null)
			throw new DaoException("No Such Notification in DB");
		return not;
	}
	
	
	public void updateNotification(Notification not) throws DaoException {
		template.merge(not);
	}
	
	
	

	// Queries

	@Override
	public List<Notification> getAllNotifications() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
		return (List<Notification>) template
				.findByCriteria(criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
	}

	
	@Override
	public List<Notification> getNotificationByEventId(Integer eventId) throws DaoException {
				return NotificationDao.super.getNotificationByEventId(eventId);
	}
	
	
	
	@Override
	public List<Notification> getUntreatedNotifications() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
		criteria.add(Restrictions.eq("isTreated", 0));
		
		return (List<Notification>)template.findByCriteria(criteria);
	}
	
	
	@Override
	public List<Notification> getNotificationsByUserId(Integer userId) throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
		Criterion criterion1 = Restrictions.eq("userId", userId);
		criteria.add(criterion1);
		
		List<Notification> notifications = (List<Notification>)template.findByCriteria(criteria);
		
		return notifications;
	}
	
	@Override
	public Notification softDeleteNotification(Integer id) throws DaoException {
		Notification not = getNotification(id);
		not.setIsActive(0);
		updateNotification(not);
		return getNotification(id);
	}

	@Override
	public Notification hardDeleteNotification(Integer id) throws DaoException {
		Notification not = getNotification(id);
		 template.delete(not);
		 return not;
	}
	

}