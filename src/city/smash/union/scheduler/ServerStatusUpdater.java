package city.smash.union.scheduler;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import city.smash.union.Main;
import city.smash.union.Variables;
import redis.clients.jedis.Jedis;

public class ServerStatusUpdater {
	static String storeKey = (Variables.getString("serverdata_serverstorageprefix") + Main.getServerName()).toLowerCase();

	public static void startUpdater() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(Main.instance, new Runnable() {
			@Override
			public void run() {
				updatePing();
			}
		}, 0L, 1L);
	}
	
	public static void updatePing() {
		String serverName = Main.getServerName();
		Jedis jedis = Main.jedisPool.getResource();
		try {
			ConcurrentHashMap<String, String> updateMap = new ConcurrentHashMap<String, String>();
			updateMap.put("ping", Long.toString(System.currentTimeMillis()));
			updateMap.put("onlineplayers", Integer.toString(Bukkit.getOnlinePlayers().size()));
			updateMap.put("maxdefaultslots", Variables.getString("playercap_maxdefaultslots"));
			updateMap.put("maxdonorslots", Variables.getString("playercap_maxdonorslots"));
			updateMap.put("servername", serverName);
			String listString = "";
			for (Player p : Bukkit.getOnlinePlayers())
			{
				listString += p.getName() + ", ";
			}
			if (listString.length() > 2) {
				listString = listString.substring(0, listString.length()-2);
			}
			if (listString.equalsIgnoreCase("")) {
				listString = "(Server Empty)";
			}
			
			updateMap.put("playerlist", listString);
			String status = "0";
			if (Bukkit.hasWhitelist()) {
				status = "1";
			}
			updateMap.put("status", status);
			updateMap.put("status", status);
			if (!jedis.hexists(storeKey, "startping")) {
				updateMap.put("startping", Long.toString(System.currentTimeMillis()));
			}
			jedis.hmset(storeKey, updateMap);
			jedis.expire(storeKey, 1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}
}