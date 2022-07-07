package ajbc.doodle.calendar.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.ErrorMessage;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.services.EventService;

@RequestMapping("/events")
@RestController
public class EventController {
	
	@Autowired
	EventService service;
	
//	@RequestMapping(method = RequestMethod.POST)
//	public ResponseEntity<?> addEvent(@RequestBody Event event) {
//		
//		try {
//			service.addEvent(event);
//			event = service.getEventById(event.getEventId());
//			return ResponseEntity.status(HttpStatus.CREATED).body(event);
//		} catch (DaoException e) {
//			ErrorMessage errorMessage = new ErrorMessage();
//			errorMessage.setData(e.getMessage());
//			errorMessage.setMessage("failed to add event to db");
//			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
//		}
//	}
//	
//
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getEventById(@PathVariable Integer id) {

		Event event;
		try {
			event = service.getEvent(id);
			return ResponseEntity.ok(event);
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("failed to get event with id " + id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Event>> getAllEvents() throws DaoException {
		List<Event> list;
		list = service.getAllEvent();
		if (list == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(list);
	}
	
	


}