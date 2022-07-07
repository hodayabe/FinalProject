package ajbc.doodle.calendar.daos;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import ajbc.doodle.calendar.entities.Notification;




@Transactional(rollbackFor = {DaoException.class}, readOnly = true)
public interface NotificationDao {

	//CRUD operations
	@Transactional(readOnly = false)
	public default void addNotification(Notification notification) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	@Transactional(readOnly = false)
	public default void updateNotification(Notification notification) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default Notification getNotification(Integer notificationId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	@Transactional(readOnly = false)
	public default void deleteNotification(Integer notificationId) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	//Queries
	
	// QUERIES
		public default List<Notification> getAllNotification() throws DaoException {
			throw new DaoException("Method not implemented");
		}

	
	
}