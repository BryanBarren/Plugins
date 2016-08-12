package city.smash.union.utils;

import org.bukkit.entity.Player;

import city.smash.union.Variables;

public class ManaUtils {
	
	public static void addMana(Player player, float amount) {
		float mana = getMana(player);
		amount = (float)(Math.ceil(amount/5)*5);
		mana += amount;
		
		if (mana > getMaxMana(player)) {
			mana = getMaxMana(player);
		}
		DataUtils.getPlayer(player).setMana(mana);
		
		refreshEXP(player);
	}
	
	public static void refreshEXP(Player player) {
//		if (didStart) {
//			player.setLevel(0);
//			player.setExp(0.999F);
//			return;
//		}
		float mana = DataUtils.getPlayer(player).getMana();
		player.setLevel(0);
		player.setExp(mana/getMaxMana(player));
	}
	
	public static float getMana(Player player) {
		return DataUtils.getPlayer(player).getMana();
	}
	
	public static float getMaxMana(Player player) {
		return 100;
	}
}
