package ajbc.doodle.calendar.controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.ErrorMessage;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.services.UserService;

@RequestMapping("/users")
@RestController
public class UserController {

	
	@Autowired
	
	UserService service;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addUser(@RequestBody User user) {
		
		try {
			service.addUser(user);
			user = service.getUser(user.getUserId());
			return ResponseEntity.status(HttpStatus.CREATED).body(user);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to add user to db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, path="/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Integer id) {
		
		User user;
		try {
			user = service.getUser(id);
			return ResponseEntity.ok(user);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to get user with id "+id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getUsers(@RequestParam Map<String, String> map) throws DaoException {
		List<User> list;
		Set<String> keys = map.keySet();
		User user;
		
		if (keys.contains("email")) {
			 user = service.getUserByEmail(map.get("email"));
			 if (user == null)
				return ResponseEntity.notFound().build();
			 return ResponseEntity.ok(user);
		}
		
		else if (keys.contains("eventId"))
			list = service.getUsersByEvent(Integer.parseInt(map.get("eventId")));
		
		else
			list = service.getAllUsers();
		
		
		if (list == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(list);
	}
	
	
	
	
}