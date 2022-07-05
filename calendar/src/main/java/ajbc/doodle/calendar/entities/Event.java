package ajbc.doodle.calendar.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@ToString
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eventId;
	private Integer ownerId;
	private String title;
	private boolean isAllDay;
	private LocalDateTime start;
	private LocalDateTime end;
	private String address;
	private String description;
	@Enumerated(EnumType.STRING)
	private RepeatingOptions repeating;
	private Integer discontinued;
	
	
//	@JsonIgnore
//	@Column(insertable = false, updatable = false)
//	private List<User> guests;
	
//	@JsonIgnore
//	@Column(insertable = false, updatable = false)
//	private List<Notification> notifications;
	

	
	
	
	
	

}
