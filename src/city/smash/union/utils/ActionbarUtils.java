package city.smash.union.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class ActionbarUtils {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void sendActionBar(Player player, String message) {
		String nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		try {
			message = ChatColor.translateAlternateColorCodes('&', message);
			Class c1 = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
			Object p = c1.cast(player);
			Object ppoc = null;
			Class c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
			Class c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
			Class c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
			Class c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
			Object o = c2.getConstructor(new Class[] { String.class }).newInstance(new Object[] { message });
			ppoc = c4.getConstructor(new Class[] { c3, Byte.TYPE })
					.newInstance(new Object[] { o, Byte.valueOf((byte) 2) });
			Method m1 = c1.getDeclaredMethod("getHandle", new Class[0]);
			Object h = m1.invoke(p, new Object[0]);
			Field f1 = h.getClass().getDeclaredField("playerConnection");
			Object pc = f1.get(h);
			Method m5 = pc.getClass().getDeclaredMethod("sendPacket", new Class[] { c5 });
			m5.invoke(pc, new Object[] { ppoc });
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
