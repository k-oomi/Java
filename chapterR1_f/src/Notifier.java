import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Notifier {
	private static final Notifier INSTANCE = new Notifier();

	private final Object lock = new Object();
	//利用者ごとに携帯端末を管理
	private final Map<String, List<MobileDevice>> userMobileDevices = new HashMap<>();

	//携帯端末ごとに通知メッセージを保持
	private final Map<MobileDevice, List<String>> messagesToDeliver = new HashMap<>();

	private volatile boolean active = true;

	public static Notifier getInstance() { return INSTANCE;}

	private Notifier() {}

	public void register(String user, MobileDevice device) {
		synchronized (lock) {
			List<MobileDevice> devices = userMobileDevices.get(user);
			if (devices == null) {
				devices = new ArrayList<>();
				userMobileDevices.put(user, devices);
			}
			devices.add(device);
		}
	}

	public void send(String user, String message) {
		List<MobileDevice> devices = new ArrayList<>();
		synchronized(lock) {
			if(userMobileDevices.containsKey(user)) {
				for(MobileDevice device : userMobileDevices.get(user)) {
					List<String> messageList = messagesToDeliver.get(devices);
					if(messageList == null) {
						messageList = new ArrayList<>();
						messagesToDeliver.put(device, messageList);
					}
					messageList.add(message);
					devices.add(device);

				}
			}
		}
		for (MobileDevice device : devices) {
			synchronized(device) {
				//通知メッセージがあることを待ち受け状態のスレッドに通知
				device.notifyAll();

			}
		}

	}

	public void loopForMessages(MobileDevice device) {

	}


}

