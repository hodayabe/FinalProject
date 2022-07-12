package ajbc.doodle.calendar.daos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;
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
		manager.addQueue(notification);
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
	
	
	
	
	

}