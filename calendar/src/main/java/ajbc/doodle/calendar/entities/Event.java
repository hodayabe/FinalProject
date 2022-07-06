package ajbc.doodle.calendar.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ajbc.doodle.calendar.enums.RepeatingOptions;
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
	@Column(updatable = false, nullable = false)
	private Integer eventId;
	
	private Integer ownerId;
	private String title;
	private Integer isAllDay;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	@Column(name = "eAddress")
	private String address;
	@Column(name = "eDescription")
	private String description;
	@Enumerated(EnumType.STRING)
	private RepeatingOptions repeating;
	private Integer isActive;
	
	@ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinTable(name = "Users_Events", joinColumns = @JoinColumn(name = "eventId"), inverseJoinColumns = @JoinColumn(name = "userId"))
	private List<User> guests;
	
//	@OneToMany(cascade = {CascadeType.MERGE})
//	@JoinColumn(name = "notificationId")
//	private List<Notification> notifications;
	

	public Event(Integer ownerId, String title, Integer isAllDay, LocalDateTime startDateTime,
			LocalDateTime endDateTime, String address, String description, RepeatingOptions repeating,
			Integer isActive, List<User> guests) {
		this.ownerId = ownerId;
		this.title = title;
		this.isAllDay = isAllDay;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.address = address;
		this.description = description;
		this.repeating = repeating;
		this.isActive = isActive;
		this.guests = guests;
	}
	
	
	
	

}
