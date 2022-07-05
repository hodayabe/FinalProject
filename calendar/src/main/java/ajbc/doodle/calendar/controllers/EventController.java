package ajbc.doodle.calendar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.services.EventService;



@RestController
@RequestMapping("events")
public class EventController {

	@Autowired
	private EventService eventService;
	
	@GetMapping
	public ResponseEntity<?> getEvents(){
		try {
			List<Event> eventsList = eventService.getAllEvents();
			return ResponseEntity.ok(eventsList);
		} catch (DaoException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e);
		}
	}
	
}
