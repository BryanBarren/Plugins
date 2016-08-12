package city.smash.union.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.dbcp2.Utils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import city.smash.union.Main;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class TitleUtils {
	static String[] titles;
	
	public static void loadLoginTitle() {
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {
			@Override
			public void run() {
				Connection connection = null;
				ResultSet rs = null;
				Statement s = null;
				try {
					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserversettings");
					s = connection.createStatement();
					String query = "select text from servermessages where name='Title-Moshpit-Join'";
					rs = s.executeQuery(query);
					if (rs.next()) {
						titles = rs.getString("text").replace("\\n", "\n").split("\n");
					}
				} catch (Exception exception) {
					System.out.println(ChatColor.RED + "Mysql Error getting guild tag!");
				} finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(rs);
					Utils.closeQuietly(s);
				}
			}
		});
	}

	public static void sendLoginTitle(Player player) {
		if (titles.length > 0) {
			if (titles.length > 1) {
				sendTitle(player, titles[0], titles[1], 20, 120, 20);
			} else {
				sendTitle(player, titles[0], null, 20, 120, 20);
			}
		}
	}
	
	public static void sendTitle (Player player, String title, String subtitle, int fadeintime, int staytime, int fadeouttime) {
		IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', title) + "\"}");
		PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
		
		if (subtitle != null && subtitle != "") {
			chatTitle = ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', subtitle) + "\"}");
			titlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatTitle);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
		}
		
		PacketPlayOutTitle lengthPacket = new PacketPlayOutTitle(fadeintime, staytime, fadeouttime);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(lengthPacket);
	}
	
}
