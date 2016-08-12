package city.smash.union.utils;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import city.smash.union.classes.SmashUpgrade;

public class SignUtils {
	public static void updateSign(SmashUpgrade smashUpgrade, Block block) {
		if (block.getState() instanceof Sign) {
			Sign signState = (Sign) block.getState();
			
			signState.setLine(0, UpgradeUtils.getUpgradeString(smashUpgrade.getType()));
			
			signState.setLine(1, "§8§lLevel " + RomanUtils.toRoman(smashUpgrade.getLevel()));
			
			if (smashUpgrade.getCost() > -1) {
				signState.setLine(2, "§8§lCost: " + NumberUtils.addSeperators(smashUpgrade.getCost() - smashUpgrade.getCurrentBalance()));
				int filled =  (int) (Math.ceil((double)smashUpgrade.getCurrentBalance()/(double)smashUpgrade.getCost() * 10));
				String signString = "";
				for (int i = 0; i < filled; i++) {
					signString += "█";
				}
				signString += "§7";
				for (int i = 0; i < 10 - filled; i++) {
					signString += "█";
				}
				signState.setLine(3, signString);
			} else {
				signState.setLine(2, "§8§lCost: 0");
				signState.setLine(3, "§8§lUpgrade Maxed");
			}
			
			signState.update();
		}
	}
}
