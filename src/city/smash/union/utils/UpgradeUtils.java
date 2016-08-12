package city.smash.union.utils;

import city.smash.union.Variables;
import city.smash.union.classes.SmashUpgrade;
import city.smash.union.classes.SmashUpgradeType;

public class UpgradeUtils {
	public static String getUpgradeColor(SmashUpgradeType upgrade) {
		switch (upgrade) {
		case STRENGTH:
			return "§4§l";
		case SPEED:
			return "§9§l";
		case RESISTANCE:
			return "§8§l";
		case MONEYGAIN:
			return "§2§l";
		case MAXHEALTH:
			return "§5§l";
		default:
			return "§4§l";
		}
	}
	public static String getUpgradeName(SmashUpgradeType upgrade) {
		switch (upgrade) {
		case STRENGTH:
			return "Strength";
		case SPEED:
			return "Speed";
		case RESISTANCE:
			return "Resistance";
		case MONEYGAIN:
			return "Point Gain";
		case MAXHEALTH:
			return "Max Health";
		default:
			return "Error";
		}
	}
	public static String getUpgradeString(SmashUpgradeType upgrade) {
		return getUpgradeColor(upgrade) + getUpgradeName(upgrade);
	}
	
	public static int getNextCost(SmashUpgrade upgrade) {
		try {
			return Variables.getInt(upgrade.getType().getValue().toLowerCase() + "_upgradeCost_" + upgrade.getLevel());
		} catch (Exception e) {
			return -1;
		}
	}
}
