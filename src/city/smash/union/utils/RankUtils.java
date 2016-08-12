package city.smash.union.utils;

import org.bukkit.entity.Player;

public class RankUtils {
	public static String getColorOfRank(String rank) {
		switch (rank.toLowerCase()) {
		case "hero":
			return "§a§l";
		case "hydra":
			return "§b§l";
		case "titan":
			return "§e§l";
		case "phoenix":
			return "§6§l";
		case "overlord":
			return "§5§l";
		case "admin":
			return "§c§l";
		case "mod":
			return "§9§l";
		case "builder":
			return "§d§l";
		case "helper":
			return "§3§l";
		case "yt":
			return "§6§l";
		}
		return "§7";
	}
	
	public static String getNextRank(Player player) {
		switch (getDonorRank(player)) {
		case "overlord":
			return "overlord";
		case "phoenix":
			return "overlord";
		case "titan":
			return "phoenix";
		case "hydra":
			return "titan";
		case "hero":
			return "hydra";
		default:
			return "hero";
		}
	}
	
	public static String getDonorRank(Player player) {
		if (player.hasPermission("overlord")) {
			return "overlord";
		}
		if (player.hasPermission("phoenix")) {
			return "phoenix";
		}
		if (player.hasPermission("titan")) {
			return "titan";
		}
		if (player.hasPermission("hydra")) {
			return "hydra";
		}
		if (player.hasPermission("hero")) {
			return "hero";
		}
		return "default";
	}
	
	public static String getRank(Player player) {
		if (player.hasPermission("admin")) {
			return "admin";
		}
		if (player.hasPermission("yt")) {
			return "yt";
		}
		if (player.hasPermission("mod")) {
			return "mod";
		}
		if (player.hasPermission("helper")) {
			return "helper";
		}
		if (player.hasPermission("builder")) {
			return "builder";
		}
		if (player.hasPermission("overlord")) {
			return "overlord";
		}
		if (player.hasPermission("phoenix")) {
			return "phoenix";
		}
		if (player.hasPermission("titan")) {
			return "titan";
		}
		if (player.hasPermission("hydra")) {
			return "hydra";
		}
		if (player.hasPermission("hero")) {
			return "hero";
		}
		return "default";
	}
}
