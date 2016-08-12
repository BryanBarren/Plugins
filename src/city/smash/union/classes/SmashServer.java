package city.smash.union.classes;

public class SmashServer {
	private String name;
	private int number;
	private int onlineCount;
	public SmashServer(String name) {
		this.name = name;
		this.number = Integer.parseInt(name.split("-")[1]);
	}
	public void setOnline(int online) {
		this.onlineCount = online;
	}
	public int getOnlinePlayers() {
		return this.onlineCount;
	}
	public boolean isOnine() {
		if (this.onlineCount != 0) {
			return true;
		}
		//get from redis
		return true;
	}
	public String getName() {
		return name;
	}
	public int getNumber() {
		return number;
	}
	public int getOnlineCount() {
		return onlineCount;
	}
}