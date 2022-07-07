package ajbc.doodle.calendar.daos;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

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
	
//	
//	@Override
//	public List<Product> getProductsByPriceRange(Double min, Double max) throws DaoException {
//		DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
//		Criterion criterion = Restrictions.between("unitPrice", min, max);
//		criteria.add(criterion);
//		return (List<Product>)template.findByCriteria(criteria);
//	}
	
	
}