package ajbc.doodle.calendar.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ajbc.doodle.calendar.enums.RepeatingOptions;

import javax.persistence.JoinColumn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor

@Entity
@Table(name = "events")
public class Event {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eventId;
	
	@JsonIgnore
	@Column(insertable = false, updatable = false)
	private Integer ownerId;
	
	@ManyToOne
	@JoinColumn(name = "ownerId")
	private User owner;
	
	private String title;
	private Integer isAllDay;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	
	@Column(name = "eAddress")
	private String address;
	
	@Column(name = "eDescription")
	private String description;
	
	private Integer isActive;
	
	@Enumerated(EnumType.STRING)
	private RepeatingOptions repeating;
	
	@JsonProperty(access = Access.AUTO)
	@ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE,CascadeType.REFRESH})
	@JoinTable(name = "Users_Events", joinColumns = @JoinColumn(name = "eventId"), inverseJoinColumns = @JoinColumn(name = "userId"))
//	@JsonManagedReference("guests")
	private Set<User> guests = new HashSet<User>();
	
//	@JsonProperty(access = Access.AUTO)
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name="eventId")
	private Set<Notification> notifications = new HashSet<Notification>();
	
	
	public Event(User owner, String title, Integer isAllDay, LocalDateTime startDateTime, LocalDateTime endDateTime,
			String address, String description, RepeatingOptions repeating, Integer isActive, Set<User> guests) {
		this.owner = owner;
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
