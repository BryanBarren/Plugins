package city.smash.union.utils;

import org.bukkit.Bukkit;

import city.smash.union.Main;
import net.md_5.bungee.api.ChatColor;
import redis.clients.jedis.Jedis;

public class JedisUtils {
	public static void publishCommand(String channel, String message) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {
			@Override
			public void run() {
				Jedis jedis = null;
				try {
					jedis = (Jedis)Main.jedisPool.getResource();
					jedis.publish(channel.toLowerCase(), message);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(ChatColor.RED + "Jedis failure");
				} finally {
					if (jedis != null) {
						jedis.close();
					}
				}
			}
		});
	}
	public static boolean isOnline(String player) {
		Jedis jedis = null;
		try {
			jedis = (Jedis)Main.jedisPool.getResource();
			return jedis.exists(("playerdata-"+player).toLowerCase());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(ChatColor.RED + "Jedis failure");
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return false;
	}

	public static String getServer(String player) {
		Jedis jedis = null;
		try {
			jedis = (Jedis)Main.jedisPool.getResource();
			return jedis.hget(("playerdata-"+player).toLowerCase(), "online");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(ChatColor.RED + "Jedis failure");
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	public static String getValue(String key) {
		Jedis jedis = null;
		try {
			jedis = (Jedis)Main.jedisPool.getResource();
			return jedis.get(key.toLowerCase());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(ChatColor.RED + "Jedis failure");
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}


	public static String hgetValue(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = (Jedis)Main.jedisPool.getResource();
			return jedis.hget(key.toLowerCase(), field.toLowerCase());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(ChatColor.RED + "Jedis failure");
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	public static void setValue (String key, String value, int ttl) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable()
		{
			@Override
			public void run() {
				Jedis jedis = null;
				try {
					jedis = (Jedis)Main.jedisPool.getResource();
					jedis.set(key.toLowerCase(), value);
					jedis.expire(key.toLowerCase(), ttl);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(ChatColor.RED + "Jedis failure");
				} finally {
					if (jedis != null) {
						jedis.close();
					}
				}
			}
		});
	}
}
