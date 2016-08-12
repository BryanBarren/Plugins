package city.smash.union.utils;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import city.smash.union.classes.SmashPlayer;

public class DataUtils {

	public static ConcurrentHashMap<UUID, SmashPlayer> playerInfo = new ConcurrentHashMap<UUID, SmashPlayer>();

	//General playerData functions
	public static void instantiatePlayerData(Player player) {
		if (Bukkit.isPrimaryThread()) {
			throw new RuntimeException("MySQL on Blocking Thread");
		}
		SettingsUtils.instantiateSettings(player);
	}

	public static SmashPlayer getPlayer(Player player) {
		if (playerInfo.containsKey(player.getUniqueId())) {
			return playerInfo.get(player.getUniqueId());
		}
		return null;
	}

	public static void removePlayer(Player player) {
		if (playerInfo.containsKey(player.getUniqueId()) && !player.isOnline()) {
			playerInfo.remove(player.getUniqueId());
		}
	}
}