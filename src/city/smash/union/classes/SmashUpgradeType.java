package city.smash.union.classes;

public enum SmashUpgradeType {
	STRENGTH("strength"),
	RESISTANCE("resistance"),
	SPEED("speed"),
	MONEYGAIN("moneygain"),
	MAXHEALTH("maxhealth");
	private String value;
	private SmashUpgradeType(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
}
