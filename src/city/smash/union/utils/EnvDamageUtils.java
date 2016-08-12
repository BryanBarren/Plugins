package city.smash.union.utils;

import org.bukkit.entity.Player;

public class EnvDamageUtils {
	public static void applyFireDamage(Player player) {
		if (player.getFireTicks() > 0 && System.currentTimeMillis() - DataUtils.getPlayer(player).getLastFireDamage() > 1000) {
			DataUtils.getPlayer(player).setLastFireDamage(System.currentTimeMillis());
//			DamageUtils.Damage(null, player, 25D, true);
		}
	}
}