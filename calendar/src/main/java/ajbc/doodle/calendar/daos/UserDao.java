package ajbc.doodle.calendar.daos;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ajbc.doodle.calendar.entities.User;

@Transactional(rollbackFor = {DaoException.class}, readOnly = true)
public interface UserDao {

	@Transactional(readOnly = false)
	public default void addUser(User user) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	public default User getUser(Integer userId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default void updateUser(User user) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default User softDeleteUser(Integer userId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default User hardDeleteUser(Integer userId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	
	public default List<User> getAllUsers() throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	public default User getUserByEmail(String email) throws DaoException {
		
		throw new DaoException("Method not implemented");
	}
	
	public default List<User> getUsersWithEventInRange(String start, String end) throws DaoException {
		
		throw new DaoException("Method not implemented");
	}
	
	public default List<User> getUsersByEventId(Integer eventId) throws DaoException {
		
		throw new DaoException("Method not implemented");
	}
	
	
	

}