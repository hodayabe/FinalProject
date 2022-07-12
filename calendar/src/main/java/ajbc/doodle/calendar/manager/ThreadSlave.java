package ajbc.doodle.calendar.manager;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import ajbc.doodle.calendar.daos.DaoException;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;
import ajbc.doodle.calendar.entities.webpush.PushMessage;
import ajbc.doodle.calendar.services.MessagePushService;
import ajbc.doodle.calendar.services.UserService;

//@RestController
public class ThreadSlave implements Runnable {
	
	private UserService userService;
	private MessagePushService messagePushService;
	
	private Notification notification;
	
	
	public ThreadSlave(Notification notification, UserService userService, MessagePushService messagePushService ) {
		this.notification = notification;
		this.userService = userService;
		this.messagePushService = messagePushService;
		System.out.println("in ThreadSlave");
	}

	@Override
	public void run() {
		try {

			User user = userService.getUser(notification.getUserId());
			if(user.getLoggedIn() == 0)
				return;
			System.out.println("in run");
			
			messagePushService.sendPushMessage(user,
					messagePushService.encryptMessage(user, new PushMessage("message: ", notification.toString())));
			
			
//			messagePushService.sendPushMessageToSubscribers(user.getKeys(), user.getAuth(), user.getEndPointLog(), new PushMessage("message: ", notification.toString()));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
