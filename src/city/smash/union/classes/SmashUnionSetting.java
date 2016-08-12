package city.smash.union.classes;

public enum SmashUnionSetting implements SmashSetting {
	LOW_HEALTH_INDICATOR("a");
	private String value;
	private SmashUnionSetting(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	@Override
	public String getTable() {
		return "unionstats";
	}
	@Override
	public String getColumn() {
		return "settings";
	}
}
