package ajbc.doodle.calendar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.services.UserService;



@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<?> getUsers(){
		try {
			List<User> usersList = userService.getAllUsers();
			return ResponseEntity.ok(usersList);
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e);
		}
	}
	
}
