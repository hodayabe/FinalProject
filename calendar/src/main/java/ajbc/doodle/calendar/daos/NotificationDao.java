package ajbc.doodle.calendar.daos;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ajbc.doodle.calendar.entities.Notification;

@Transactional(rollbackFor = { DaoException.class }, readOnly = true)
public interface NotificationDao {

	// CRUD

	@Transactional(readOnly = false)
	public default void addNotification(Notification Notification) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	public default Notification getNotification(Integer NotificationId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	
	@Transactional(readOnly = false)
	public default void updateNotification(Notification not) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	

	// Queries

	public default List<Notification> getAllNotifications() throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	
	public default List<Notification> getNotificationByEventId(Integer eventId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	public default List<Notification> getUntreatedNotifications() throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default Notification softDeleteNotification(Integer id) throws DaoException{
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default Notification hardDeleteNotification(Integer id) throws DaoException{
		throw new DaoException("Method not implemented");
	}

	public default List<Notification> getNotificationsByUserId(Integer userId) throws DaoException{
		throw new DaoException("Method not implemented");
	}
	
	
	
	
	
}