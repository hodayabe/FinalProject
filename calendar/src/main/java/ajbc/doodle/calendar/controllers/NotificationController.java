package ajbc.doodle.calendar.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.ErrorMessage;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.manager.NotificationManager;
import ajbc.doodle.calendar.services.MessagePushService;
import ajbc.doodle.calendar.services.NotificationService;

@RequestMapping("/notifications")
@RestController
public class NotificationController {
	
	@Autowired
	private NotificationService notificationServcie;
	
	
	@Autowired
	private MessagePushService messagePushService;
	
	@Autowired(required = false)
	private NotificationManager notificationManager;
	
	
	
//	@RequestMapping(method = RequestMethod.POST)
//	public ResponseEntity<?> addNotification(@RequestBody Notification notification, @RequestParam Integer userId ,@RequestParam Integer eventId) {
//		try {
//			notificationServcie.addNotification(userId, eventId, notification);
//			return ResponseEntity.status(HttpStatus.CREATED).build();
//		} catch (DaoException e) {
//			return ResponseEntity.status(HttpStatus.valueOf(500)).body(e.getMessage());
//		}
//	}
	
	
	@PostMapping("/{userId}/{eventId}")
	public ResponseEntity<?> addNotification(@RequestBody List<Notification> notifications,
			@PathVariable Integer userId, @PathVariable Integer eventId) {
		try {
			List<Notification> addedNotifications = new ArrayList<Notification>();

			for (int i = 0; i < notifications.size(); i++) {
				notifications.get(i).setEventId(eventId);
				notifications.get(i).setUserId(userId);
				notificationServcie.addNotification(notifications.get(i));

				Notification notification = notificationServcie.getNotificationById(notifications.get(i).getNotId());
				addedNotifications.add(notification);
				notificationManager.addQueue(notification);
			}

			return ResponseEntity.status(HttpStatus.CREATED).body(addedNotifications);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to add notification to db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}
	
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getNotificationById(@PathVariable Integer id) {

		Notification notification;
		try {
			notification = notificationServcie.getNotificationById(id);
			return ResponseEntity.ok(notification);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to get notification with id " + id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getUsers(@RequestParam Map<String, String> map) throws DaoException {
		List<Notification> notList=null;

		Set<String> keys = map.keySet();

		if (keys.contains("eventId")) 
			notList = notificationServcie.getNotificationsByEvent(Integer.parseInt(map.get("eventId")));		
		
		else
			notList = notificationServcie.getAllNotifications();
		
		
		if (notList == null )
			return ResponseEntity.notFound().build();

		
		return ResponseEntity.ok(notList);
	}
	
	
	// Update Notification
			@RequestMapping(method = RequestMethod.PUT, path="/{id}")
			public ResponseEntity<?> updateProduct(@RequestBody Notification notification, @PathVariable Integer id) {

				try {
					notification.setNotId(id);
					notificationServcie.updateNotification(notification);
					notification = notificationServcie.getNotificationById(notification.getNotId());
					return ResponseEntity.status(HttpStatus.OK).body(notification);
				} catch (DaoException e) {
					ErrorMessage errorMessage = new ErrorMessage();
					errorMessage.setData(e.getMessage());
					errorMessage.setMessage("failed to update Notification in db");
					return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
				}
			}

	
	
	
	//push controller methods
		@GetMapping(path = "/publicSigningKey", produces = "application/octet-stream")
		public byte[] publicSigningKey() {
			return messagePushService.getServerKeys().getPublicKeyUncompressed();
		}

		@GetMapping(path = "/publicSigningKeyBase64")
		public String publicSigningKeyBase64() {
			return messagePushService.getServerKeys().getPublicKeyBase64();
		}

	
}