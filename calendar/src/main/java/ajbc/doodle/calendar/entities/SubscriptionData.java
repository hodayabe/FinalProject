package ajbc.doodle.calendar.entities;


import javax.persistence.Entity;
import javax.persistence.Table;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


//@ToString
//@NoArgsConstructor
//@Getter
//@Setter
//
//@Entity
//@Table(name = "SubscriptionData")
public class SubscriptionData {
	
	private  String endpoint;
	private  String p256dh;
	private  String auth;
	

}
