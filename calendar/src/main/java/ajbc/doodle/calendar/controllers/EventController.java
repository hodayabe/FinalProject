package ajbc.doodle.calendar.controllers;

import java.time.LocalDateTime;
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
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.services.EventService;
import ajbc.doodle.calendar.services.UserService;

@RequestMapping("/events")
@RestController
public class EventController {

	@Autowired
	EventService eventService;

	@Autowired
	UserService userService;

	@RequestMapping(method = RequestMethod.POST, path = "/{id}")
	public ResponseEntity<?> addEvent(@RequestBody Event event, @PathVariable Integer id) {

		try {
			eventService.addEvent(event, id);
			event = eventService.getEvent(event.getEventId());
			return ResponseEntity.status(HttpStatus.CREATED).body(event);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to add event to db");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
		}
	}

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

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getUserssIds(@RequestParam Map<String, String> map) throws DaoException {

		Set<String> keys = map.keySet();
		User user = null;

		// Get upcoming events of a user
		if (keys.contains("userId") && keys.contains("upcoming")) {
			List<Event> eventList = eventService.getUpcomingEvents(Integer.parseInt(map.get("userId")),LocalDateTime.now());
			return ResponseEntity.ok(eventList);
		}

		// Get events of a user in a range between start date and time to end date andtime
		if (keys.contains("userId") && keys.contains("start") && keys.contains("end")) {
			List<Event> usersList = eventService.getEventOfUserInRange(Integer.parseInt(map.get("userId")),
					map.get("start"), map.get("end"));
			return ResponseEntity.ok(usersList);
		}

		// Get all events in a range between start date and time to end date and time.
		if (!keys.contains("userId") && keys.contains("start") && keys.contains("end")) {
			List<Event> usersList = eventService.getEventsInRange(map.get("start"), map.get("end"));
			return ResponseEntity.ok(usersList);
		}

		//Get events of a user the next coming num of minutes and hours.
				if (keys.contains("userId") && keys.contains("minutes ") && keys.contains("hours")) {
					List<Event> usersList = eventService.nextComingNumOfMinutesAndHours(Integer.parseInt(map.get("userId")),Integer.parseInt(map.get("minutes")), map.get("end"));
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

}