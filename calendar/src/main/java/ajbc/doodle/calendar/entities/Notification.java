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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import ajbc.doodle.calendar.enums.Units;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString


@Entity
@Table(name = "Notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer notId;
	private Integer userId;
	private LocalDateTime localDateTime;
	private String title;
	@Column(name = "nMessage")
	private String message;
	@Enumerated(EnumType.STRING)
	private Units unit;
	private int quantity;
	
	@JsonIgnore
	@Column(insertable=false, updatable=false)
	private Integer eventId;
	
	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name="eventId")
	@JsonIgnore
	private Event event;

}