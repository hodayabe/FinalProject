package ajbc.doodle.calendar.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.ErrorMessage;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.UserService;


/**
 * Restful api service that receives http requests about existed events in the calendar.
 * @author Hodaya David
 *
 */
@RequestMapping("/events")
@RestController
public class EventController {

	@Autowired
	EventService eventService;

	@Autowired
	UserService userService;

	
	/**
	 * add list of events to db
	 * @param events
	 * @param id
	 * @return
	 */
	@PostMapping("/{id}")
	public ResponseEntity<?> addEvent(@RequestBody List<Event> events, @PathVariable Integer id) {

		List<Event> eventsList=new ArrayList<Event>();
		try {
			for (Event event2 : events) {
				eventService.addEvent(event2, id);
				eventsList.add(eventService.getEvent(event2.getEventId()));
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(eventsList);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to add event to db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}
	
	

	/**
	 * get Event By Id
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getEventById(@PathVariable Integer id) {

		Event event;
		try {
			event = eventService.getEvent(id);
			return ResponseEntity.ok(event);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to get event with id " + id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
		}
	}


	/**
	 * update Event
	 * @param event
	 * @param eventId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, path = "/{eventId}")
	public ResponseEntity<?> updateEvent(@RequestBody Event event, @PathVariable Integer eventId) {
		try {
			event.setEventId(eventId);
			eventService.updateEvent(event);
			event = eventService.getEvent(eventId);
			return ResponseEntity.status(HttpStatus.OK).body(event);
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(e.getMessage());
		}
	}
	

	/**
	 * get Users by params:
	 * userId-upcoming: get Upcoming Events
	 * start-end:get Events In Range
	 * no param: get all Users
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getUsers(@RequestParam Map<String, String> map) throws DaoException {

		Set<String> keys = map.keySet();
		User user = null;

		// Get upcoming events of a user
		if (keys.contains("userId") && keys.contains("upcoming")) {
			List<Event> eventList = eventService.getUpcomingEvents(Integer.parseInt(map.get("userId")),LocalDateTime.now());
			return ResponseEntity.ok(eventList);
		}

		
		if (keys.contains("start") && keys.contains("end")) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime startDateTime = LocalDateTime.parse(map.get("start"), formatter);
			LocalDateTime endDateTime = LocalDateTime.parse( map.get("end"), formatter);
			
			// Get all events in a range between start date and time to end date and time.
			if(!keys.contains("userId")) {
				List<Event> usersList = eventService.getEventsInRange(startDateTime, endDateTime);
				return ResponseEntity.ok(usersList);
			}
			
			// Get events of a user in a range between start date and time to end date andtime
			List<Event> usersList = eventService.getEventOfUserInRange(Integer.parseInt(map.get("userId")),startDateTime,endDateTime);
			return ResponseEntity.ok(usersList);
		}


		//Get events of a user the next coming num of minutes and hours.
				if (keys.contains("userId") && keys.contains("minutes") && keys.contains("hours")) {
					LocalDateTime dateTime =LocalDateTime.now().plusHours(Integer.parseInt(map.get("hours"))).plusMinutes(Integer.parseInt(map.get("minutes")));
					
					List<Event> usersList = eventService.getEventOfUserInRange(Integer.parseInt(map.get("userId")),LocalDateTime.now(),dateTime);
					return ResponseEntity.ok(usersList);
				}
		
		// Get all events of a user
		if (keys.contains("userId")) {

			user = userService.getUser(Integer.parseInt(map.get("userId")));

			if (user == null) 
				return ResponseEntity.notFound().build();
			
			Set<Event> eventList = user.getEvents();
			return ResponseEntity.ok(eventList);
		}


		// Get all events
		List<Event> list;
		list = eventService.getAllEvent();
		if (list == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(list);
	}

	
	/**
	 * Delete Events by params:
	 * soft: update isActiv to false
	 * hard: delete from db
	 * @param eventsIds
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	@DeleteMapping
	public ResponseEntity<List<Event>> DeleteEvent(@RequestBody List<Integer> eventsIds, @RequestParam Map<String, String> map)
			throws DaoException {
		Set<String> keys = map.keySet();
		List<Event> eventsList=new ArrayList<Event>();

		if (keys.contains("soft"))
			for (Integer evId : eventsIds) {
				eventsList.add(eventService.softDeleteEvent(evId));
			}
		if (keys.contains("hard"))
			for (Integer evId : eventsIds) {
				eventsList.add(eventService.hardDeleteEvent(evId));
			}
			

		return ResponseEntity.ok(eventsList);
	}

	
	
}