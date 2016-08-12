package city.smash.union.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class HitdetectionUtils {
	public static List<Player> getPlayersAroundPoint(Player player, Location loc, double distance) {
		return getPlayersAroundPoint(player, loc, new Vector(distance, distance, distance), new HashSet<UUID>());
	}
	public static List<Player> getPlayersAroundPoint(Player player, Location loc, Vector offset) {
		return getPlayersAroundPoint(player, loc, offset, new HashSet<UUID>());
	}
	public static List<Player> getPlayersAroundPoint(Player player, Location loc, double distance, HashSet<UUID> damageMap) {
		return getPlayersAroundPoint(player, loc, new Vector(distance, distance, distance), damageMap);
	}
	
	public static List<Player> getPlayersAroundPoint(Player player, Location loc, Vector offset, HashSet<UUID> damageMap) {
		List<Player> result = new ArrayList<Player>();
		for (Entity e : loc.getWorld().getNearbyEntities(loc, offset.getX()+1, offset.getY()+1, offset.getZ()+1)) {
			if (!(e instanceof Player)) {
				continue;
			}
			Player target = (Player)e;
//			if (!isInit) {
//				continue;
//			}
			if (target.equals(player)) {
				continue;
			}
			if (DataUtils.getPlayer(target).isVanished()) {
				continue;
			}
			if (damageMap.contains(target.getUniqueId())) {
				continue;
			}
			if (target.getLocation().toVector().setY(0).distanceSquared(loc.toVector().setY(0)) > offset.setY(0).lengthSquared()) {
				continue;
			}
			
			result.add(target);
		}
		return result;
	}
}
