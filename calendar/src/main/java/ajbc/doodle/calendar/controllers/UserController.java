package ajbc.doodle.calendar.controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.ErrorMessage;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.entities.webpush.PushMessage;
import ajbc.doodle.calendar.entities.webpush.Subscription;
import ajbc.doodle.calendar.entities.webpush.SubscriptionEndpoint;
import ajbc.doodle.calendar.services.MessagePushService;
import ajbc.doodle.calendar.services.UserService;


import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


import org.springframework.web.bind.annotation.GetMapping;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ajbc.doodle.calendar.Application;

import ajbc.doodle.calendar.services.CryptoService;

@RequestMapping("/users")
@RestController
public class UserController {

	@Autowired
	UserService service;

	@Autowired
	MessagePushService messagePushService;

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

	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Integer id) {

		User user;
		try {
			user = service.getUser(id);
			return ResponseEntity.ok(user);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to get user with id " + id);
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

		else if (keys.contains("start") && keys.contains("end"))
			list = service.getUsersWithEventInRange(map.get("start"), map.get("end"));

		else if (keys.contains("eventId"))
			list = service.getUsersByEvent(Integer.parseInt(map.get("eventId")));

		else
			list = service.getAllUsers();

		if (list == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(list);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable Integer id) {
		try {
			user.setUserId(id);
			service.updateUser(user);
			user = service.getUser(id);
			return ResponseEntity.status(HttpStatus.OK).body(user);
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<User> DeleteUser(@PathVariable Integer id, @RequestParam Map<String, String> map)
			throws DaoException {
		Set<String> keys = map.keySet();
		User user = null;

		if (keys.contains("soft"))
			user = service.softDeleteUser(id);

		if (keys.contains("hard"))
			user = service.hardDeleteUser(id);

		if (user == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(user);
	}

	// login
	@RequestMapping(method = RequestMethod.POST, path = "/login/{email}")
	public ResponseEntity<?> login(@RequestBody Subscription subscription, @PathVariable(required = false) String email)
			throws DaoException, InvalidKeyException, JsonProcessingException, NoSuchAlgorithmException,
			InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		try {
			User user = service.getUserByEmail(email);
			service.login(user, subscription);
			messagePushService.sendPushMessage(user,
					messagePushService.encryptMessage(user, new PushMessage("message: ", "hello")));

			return ResponseEntity.ok().body("Logged in");
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}

	}

		@RequestMapping(method = RequestMethod.POST, path = "/logout/{email}")
		public ResponseEntity<?> logout(@PathVariable(required = false) String email) throws DaoException {
			try {
				User user = service.getUserByEmail(email);
				service.logout(user);
				return ResponseEntity.ok().body("Logged out");
			} catch (DaoException e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
			}
		}
		
		@PostMapping("/isSubscribed")
		public boolean isSubscribed(@RequestBody SubscriptionEndpoint subscription) throws DaoException {
			List<User> users = service.getAllUsers();
			for(User user : users) {
				if(user.getEndpoint() != null) {
					if(user.getEndpoint().equals(subscription.getEndpoint()))
						return true;
				}
			}
			return false;
		}

	

}