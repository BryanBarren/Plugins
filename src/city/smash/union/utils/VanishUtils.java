package city.smash.union.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import city.smash.union.scheduler.Gametick;

public class VanishUtils {
	public static void vanishRecheck(Player player) {
		boolean isVanish = DataUtils.getPlayer(player).isVanished();
		for (Player target : Bukkit.getOnlinePlayers()) {
			if (target == player) {
				continue;
			}
			if (isVanish || !Gametick.isGameTicking(player)) {
				target.hidePlayer(player);
			} else {
				target.showPlayer(player);
			}
			if (DataUtils.getPlayer(target).isVanished() || !Gametick.isGameTicking(target)) {
				player.hidePlayer(target);
			} else {
				player.showPlayer(target);
			}
		}
	}
	
//	public static void vanishRecheck(Player player) {
//		if (PlayerData.getPlayer(player).isInSpawn) {
//			vanishRecheckSpawn(player);
//		} else {
//			vanishRecheckArena(player);
//		}
//	}
//	public static void vanishRecheckSpawn(Player player) {
//		boolean hideOthers = PlayerData.getPlayer(player).settingsMap.get("hideplayers");
//		boolean isStaff = player.hasPermission("staff");
//		for (playerData data : PlayerData.playerInfo.values()) {
//			if (data.player.equals(player)) {
//				continue;
//			}
//			if (!isStaff) {
//				if (data.isVanish) {		
//					player.hidePlayer(data.player);
//					Bukkit.broadcastMessage("Hiding " + data.player.getDisplayName() + " from " + player.getDisplayName());
//				}
//				if (data.settingsMap.get("hideplayers")) {
//					data.player.hidePlayer(player);
//					Bukkit.broadcastMessage("Hiding " + player.getDisplayName() + " from " + data.player.getDisplayName());
//				} else {
//					data.player.showPlayer(player);
//					Bukkit.broadcastMessage("Showing " + player.getDisplayName() + " to " + data.player.getDisplayName());
//				}
//			} else {
//				data.player.showPlayer(player);
//				Bukkit.broadcastMessage("Showing " + player.getDisplayName() + " to " + data.player.getDisplayName());
//			}
//			
//			if (hideOthers) {
//				if (!data.player.hasPermission("staff")) {
//					player.hidePlayer(data.player);
//					Bukkit.broadcastMessage("Hiding " + data.player.getDisplayName() + " from " + player.getDisplayName());
//				}
//			} else {
//				if (!data.isVanish) {
//					player.showPlayer(data.player);
//					Bukkit.broadcastMessage("Showing " + data.player.getDisplayName() + " to " + player.getDisplayName());
//				}
//			}
//		}
//	}
//	
//	public static void vanishRecheckArena(Player player) {
//		boolean isVanish = PlayerData.getPlayer(player).isVanish;
//		for (playerData data : PlayerData.playerInfo.values()) {
//			if (data.player.equals(player)) {
//				continue;
//			}
//			if (!data.isInSpawn) {
//				if (!data.isVanish) {
//					player.showPlayer(data.player);
//					Bukkit.broadcastMessage("Showing " + data.player.getDisplayName() + " to " + player.getDisplayName());
//				}
//				if (!isVanish) {
//					data.player.showPlayer(player);
//					Bukkit.broadcastMessage("Showing " + player.getDisplayName() + " to " + data.player.getDisplayName());
//				}
//			} else {
//				if (!data.player.hasPermission("staff")) {
//					player.showPlayer(data.player);
//					Bukkit.broadcastMessage("Showing " + data.player.getDisplayName() + " to " + player.getDisplayName());
//				}
//			}
//		}
//	}
}
