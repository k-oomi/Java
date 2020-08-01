import javax.management.NotificationListener;

public class MobileDevice {
	private final String name;
	private final NotificationListener listener;

	public MobileDevice(String name, NotificationListener listener) {
		this.name = name;
		this.listener = listener;
	}

	public NotificationListener getListenere() {return listener;}

	public String getName() { return name;}

}
