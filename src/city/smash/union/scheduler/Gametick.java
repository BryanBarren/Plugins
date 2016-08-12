package city.smash.union.scheduler;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import city.smash.union.Main;
import city.smash.union.Variables;
import city.smash.union.utils.ActionbarUtils;
import city.smash.union.utils.DamageUtils;
import city.smash.union.utils.DataUtils;
import city.smash.union.utils.EnvDamageUtils;
import city.smash.union.utils.HealthUtils;
import city.smash.union.utils.InventoryUtils;
import city.smash.union.utils.LocationUtils;
import city.smash.union.utils.ManaUtils;
import city.smash.union.utils.TimeUtils;

public class Gametick {

	private static void doGametick1tick(Player player) {

//		LocationUtils.surfacePlayer(player);
//		checkForFall(player);
//		if (WorldGuardUtils.isInSpawn(player)) {
//			InventoryUtils.refreshHotbar(player, true);
//			CombattagUtils.checkCooldown(player, true);
//		} else {
//			DataUtils.refreshEntityEquipment(player);
//			HealthUtils.regenHP(player);
//			EnvDamageUtils.applyBorderDamage(player);
//			EnvDamageUtils.applyFireDamage(player);
//			Glitch.updateGlitchLocation(player);
//			CompassUtils.updateCompassLocation(player, true);
//			InventoryUtils.refreshHotbar(player, false);
//			CombattagUtils.checkCooldown(player, false);
//		}
	}
	private static void doGametick60tick(Player player) {
//		if (!WorldGuardUtils.isInSpawn(player)) {
//			CompassUtils.refreshCompassTarget(player);
//		}
	}
	private static void doGametickVanished(Player player) {
		ActionbarUtils.sendActionBar(player, "&a&lYou are vanished!");
		InventoryUtils.refreshHotbar(player, true);
	}

	private static void checkForFall(Player player) {
//		if (WorldGuardUtils.isInSpawn(player)) {
//			if (!player.getAllowFlight()) {
//				if (player.getLocation().getY() <= 15) {
//					player.teleport(Main.SpawnLoc);
//				}
//			}
//		} else {
//			if (player.getLocation().getY() <= Variables.getDouble("enviromentdamage_deathbelowy")) {
//				DamageUtils.Death(player);
//			}
//		}
	}
	private static HashMap<UUID, Integer> gametickInstanceMap = new HashMap<UUID, Integer>();

	public static boolean isGameTicking(Player player) {
		return gametickInstanceMap.containsKey(player.getUniqueId());
	}

	public static void startGametick(final Player player) {

		stopGametick(player);

		int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable() {
			int i = 0;
			@Override
			public void run() {
				if (!player.isOnline()) {
					stopGametick(player);
					return;
				}
				if (player.getGameMode() == GameMode.CREATIVE) {
					return;
				}
				
				if (DataUtils.getPlayer(player).isVanished()) {
					doGametickVanished(player);
					return;
				}
				doGametick1tick(player);
				if (i%60 == 0) {
					doGametick60tick(player);
				}

				i++;
			}
		}, 0L, 1L);
		gametickInstanceMap.put(player.getUniqueId(), id);
	}

	private static void stopGametick(final Player player) {
		if (!gametickInstanceMap.containsKey(player.getUniqueId())) {
			return;
		}
		Bukkit.getScheduler().cancelTask(gametickInstanceMap.get(player.getUniqueId()));
		gametickInstanceMap.remove(player.getUniqueId());
		return;
	}
}