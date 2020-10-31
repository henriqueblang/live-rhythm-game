package observer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import entity.LibraryObserver;
import entity.Notification;
import entity.NotificationsObserver;

public class ObserverTest {

	private Notification notification;

	private LibraryObserver libraryObserver;
	private NotificationsObserver notificationsObserver;

	@Before
	public void setUp() {
		notification = Mockito.spy(Notification.class);

		libraryObserver = Mockito.spy(LibraryObserver.class);
		notificationsObserver = Mockito.spy(NotificationsObserver.class);

		libraryObserver.setSubject(notification);
		notificationsObserver.setSubject(notification);

		Mockito.doNothing().when(libraryObserver).update(Mockito.anyInt());
		Mockito.doNothing().when(notificationsObserver).update(Mockito.anyInt());
	}

	@Test
	public void registerTest() {
		Mockito.verify(notification).register(libraryObserver);
		Mockito.verify(notification).register(notificationsObserver);
	}

	@Test
	public void resetNotificationsTest() {
		notification.resetNotifications();

		Mockito.verify(libraryObserver).update(0);
		Mockito.verify(notificationsObserver).update(0);
	}

	@Test
	public void incrementNotificationsTest() {
		notification.incrementNotifications();

		Mockito.verify(libraryObserver).update(1);
		Mockito.verify(notificationsObserver).update(1);

		notification.incrementNotifications();

		Mockito.verify(libraryObserver).update(2);
		Mockito.verify(notificationsObserver).update(2);
	}

	@Test
	public void incrementAndResetNotificationsTest() {
		notification.incrementNotifications();

		Mockito.verify(libraryObserver).update(1);
		Mockito.verify(notificationsObserver).update(1);
		
		notification.resetNotifications();

		Mockito.verify(libraryObserver).update(0);
		Mockito.verify(notificationsObserver).update(0);
	}
	
	@Test
	public void setNotificationsTest() {
		notification.setNotifications(10);

		Mockito.verify(libraryObserver).update(10);
		Mockito.verify(notificationsObserver).update(10);
	}
}
