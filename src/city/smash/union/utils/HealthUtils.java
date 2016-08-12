package city.smash.union.utils;

import org.bukkit.entity.Player;

public class HealthUtils {
	public static boolean addHealth(Player player, Double amount) {
		boolean result = true;
		if (player.getHealth() + amount >= player.getMaxHealth()) {
			player.setHealth(player.getMaxHealth());
		} else if (player.getHealth() + amount <= 0) {
			DamageUtils.Death(player);
			result = false;
		} else {
			player.setHealth(player.getHealth()+amount);
		}
		ScoreboardUtils.refreshHPIndicator(player);
		return result;
	}
}