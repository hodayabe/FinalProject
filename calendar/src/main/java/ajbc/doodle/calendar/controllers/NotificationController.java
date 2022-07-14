package ajbc.doodle.calendar.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import ajbc.doodle.calendar.manager.NotificationManager;
import ajbc.doodle.calendar.services.MessagePushService;
import ajbc.doodle.calendar.services.NotificationService;



/**
 * Restful api service that receives http requests about existed Notifications in the calendar.
 * @author Hodaya David
 *
 */
@RequestMapping("/notifications")
@RestController
public class NotificationController {
	
	@Autowired
	private NotificationService notificationServcie;
	
	
	@Autowired
	private MessagePushService messagePushService;
	
	@Autowired(required = false)
	private NotificationManager notificationManager;
	
	
	
	/**
	 * Adds list of new notifications of an user and of event to the database
	 * @param notifications
	 * @param userId
	 * @param eventId
	 * @return
	 */
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
				notificationManager.addToQueue(notification);
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
	public ResponseEntity<?> getNotifications(@RequestParam Map<String, String> map) throws DaoException {
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
	

			@RequestMapping(method = RequestMethod.PUT)
			public ResponseEntity<?> updateListNotifications(@RequestBody List<Notification> notifications) {
				Notification not;
				List<Notification> addedNotifications = new ArrayList<Notification>();
				try {
					for (int i = 0; i < notifications.size(); i++) {
						notificationManager.removeFromQueue(notificationServcie.getNotificationById(notifications.get(i).getNotId()));
						notificationServcie.updateNotification(notifications.get(i));
						not= notificationServcie.getNotificationById(notifications.get(i).getNotId());
						notificationManager.addToQueue(notifications.get(i));
						addedNotifications.add(not);
					}
					return ResponseEntity.status(HttpStatus.OK).body(addedNotifications);
				} catch (DaoException e) {
					ErrorMessage errorMessage = new ErrorMessage();
					errorMessage.setData(e.getMessage());
					errorMessage.setMessage("failed to update user in db");
					return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
				}
			}
			
			
			
			
			
			@DeleteMapping
			public ResponseEntity<List<Notification>> DeleteNotification(@RequestBody List<Integer> notificationIds, @RequestParam Map<String, String> map)
					throws DaoException {
				Set<String> keys = map.keySet();
				List<Notification> notsList=new ArrayList<Notification>();

				if (keys.contains("soft"))
					for (Integer notId : notificationIds) {
						notsList.add(notificationServcie.softDeleteNotification(notId));
					}

				if (keys.contains("hard"))
					for (Integer notId : notificationIds) {
						notsList.add(notificationServcie.hardDeleteNotification(notId));
					}
					
				return ResponseEntity.ok(notsList);
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