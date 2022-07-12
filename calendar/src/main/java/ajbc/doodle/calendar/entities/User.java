package ajbc.doodle.calendar.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	
	private String firstName;
	private String lastName;
	
	@Column(unique = true)
	private String email;
	
	private LocalDate birthDate;
	private LocalDate joinDate;
	private Integer isActive;
	private Integer loggedIn;
	
	
	@Column(name = "uEndpoint")
	private  String endpoint;
	private  String p256dh;
	private  String auth;
	
//	@JsonProperty(access = Access.READ_ONLY)
	@Fetch(FetchMode.JOIN)
	@ManyToMany(mappedBy="guests",cascade = {CascadeType.MERGE,CascadeType.REFRESH})
	@JsonIgnore
	private Set<Event> events = new HashSet<Event>();
	
	
	
	public void loggIn(boolean bool) {
		if(bool)
			setLoggedIn(1);
		
		else
			setLoggedIn(0);
	}
	
	
	public User(String fristName, String lastName, String email, LocalDate birthDate, LocalDate joinDate) {
		this.firstName = fristName;
		this.lastName = lastName;
		this.email = email;
		this.birthDate = birthDate;
		this.joinDate = joinDate;
		this.isActive = 1;
		this.loggedIn = 0;
		
	}
	
}
