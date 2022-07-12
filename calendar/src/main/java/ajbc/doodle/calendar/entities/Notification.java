package ajbc.doodle.calendar.entities;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ajbc.doodle.calendar.enums.Units;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@ToString

@Entity
@Table(name = "Notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer notId;
	
	@Column(insertable=false, updatable=false)
	private Integer eventId;
	
	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name="eventId")
	private Event event;
	
//	@JsonIgnore
	@Column(insertable=false, updatable=false)
	private Integer userId;
	
	
	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name="userId")
//	@JsonBackReference
	private User user;
	
	private LocalDateTime startDateTime;
	
	private String title;
	
	@Column(name = "nMessage")
	private String message;
		
	@Enumerated(EnumType.STRING)
	private Units units;
	
	private int quantity;
	
	
	public Notification(Event event, User user, String title,String message, Units units, Integer qiantity) {
		this.eventId=event.getEventId();
		this.event = event;
		this.userId=user.getUserId();
		this.user = user;
		this.title = title;
		this.message = message;
		this.units = units;
		this.quantity = qiantity;
		if (units.equals(Units.HOURS))
			this.startDateTime = event.getStartDateTime().minusHours(quantity);
		else
			this.startDateTime = event.getStartDateTime().minusMinutes(quantity);
		
	}


	@Override
	public String toString() {
		return "Notification [notId=" + notId + ", eventId=" + eventId + ", userId=" + userId + ", startDateTime="
				+ startDateTime + ", title=" + title + ", message=" + message + ", units=" + units + ", quantity="
				+ quantity + "]";
	}
	
	

}