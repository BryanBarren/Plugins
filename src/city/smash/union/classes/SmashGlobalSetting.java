package city.smash.union.classes;

public enum SmashGlobalSetting implements SmashSetting {
	CHAT_GLOBAL("a"),
	CHAT_BROADCAST("b"),
	CHAT_NAME_ALERT("c"),
	CHAT_PRIVATE("f");
	private String value;
	private SmashGlobalSetting(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	@Override
	public String getTable() {
		return "globalstats";
	}
	@Override
	public String getColumn() {
		return "settings";
	}
}
