package ajbc.doodle.calendar.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@ToString
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	
	private String lastName;
	private String firstName;
	private String email;
	private LocalDate birthDate;
	private LocalDate joinDate;
	private Integer isActive;
	
//	@JsonIgnore
	@JsonBackReference("guests")
//	@JsonManagedReference("events")
	@ManyToMany(mappedBy="guests",cascade = {CascadeType.MERGE,CascadeType.REFRESH})
	List<Event> events;
	
	public User(String fristName, String lastName, String email, LocalDate birthDate, LocalDate joinDate,
			Integer isActive) {
		this.firstName = fristName;
		this.lastName = lastName;
		this.email = email;
		this.birthDate = birthDate;
		this.joinDate = joinDate;
		this.isActive = isActive;
	}
}
