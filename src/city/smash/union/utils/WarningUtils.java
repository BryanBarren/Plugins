package city.smash.union.utils;

import org.bukkit.entity.Player;

public class WarningUtils {
	public static void noRankWarn(Player player, String rank) {
		String rankString = RankUtils.getColorOfRank(rank) + rank.toUpperCase();
		if (!rank.equalsIgnoreCase("overlord")) {
			rankString += RankUtils.getColorOfRank(rank) + "+";
		}
		
		ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
		ChatUtils.sendMessage(player, "&c&lSorry, but you must be "+rankString+" &c&lto do this.");
		ChatUtils.sendMessage(player, "&7Purchase "+RankUtils.getColorOfRank(rank) + rank.toUpperCase()+"&7 at &astore.smash.city &7to bypass this.");
		ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
	}

	public static boolean shouldWarn (Player player, String target) {
		return shouldWarn(player, target, 1000L);
	}
	
	public static boolean shouldWarn (Player player, String target, Long duration) {
		target = target.toLowerCase();
		if (DataUtils.getPlayer(player).getWarningCooldown().containsKey(target)) {
			if (System.currentTimeMillis() - DataUtils.getPlayer(player).getWarningCooldown().get(target) < duration) {
				return false;
			}
			DataUtils.getPlayer(player).getWarningCooldown().put(target, System.currentTimeMillis());
		} else {
			DataUtils.getPlayer(player).getWarningCooldown().put(target, System.currentTimeMillis());
		}
		return true;
	}
	
	public static void removeWarning(Player player, String target) {
		target = target.toLowerCase();
		DataUtils.getPlayer(player).getWarningCooldown().remove(target);
	}
}
