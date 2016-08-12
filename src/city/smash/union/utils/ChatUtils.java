package city.smash.union.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp2.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.TextComponent;

public class ChatUtils {
	/**
	 * Sends a message to player. Automatically color coded
	 * @param p Player to message
	 * @param msg The message
	 */
	public static void sendMessage (Player player, String msg) {
		if (msg == null) {
			return;
		}
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		msg = msg.replace("**d5bb4e12-3bde-4bdb-80cf-58f4e46c22de**", "&");
		player.sendMessage(msg);
	}
	
	public static void sendRawMessage (Player player, TextComponent text) {
		player.spigot().sendMessage(text);
	}
	
	public static void broadcastMessage (String msg /*, String avoidKey*/) {
		if (msg == null) {
			return;
		}
		for (Player player : Bukkit.getOnlinePlayers()) {
			sendMessage(player, msg);
		}
	}
	
	public static String getPrefix(Player player) {
		if (Bukkit.isPrimaryThread()) {
			throw new RuntimeException("MySQL on Blocking Thread");
		}
		String prefix = "";
		
		Connection connection = null;
		ResultSet rs = null;
		Statement s = null;
		try {
			connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:serverranks");
			s = connection.createStatement();
			String query = "select coalesce(metadata.string_value, '&7') as prefix from memberships left join entities on memberships.group_id = entities.id left join metadata on memberships.group_id = metadata.entity_id where memberships.member='" + player.getUniqueId().toString().replace("-", "") + "' order by entities.priority desc limit 1";
			rs = s.executeQuery(query);

			if (rs.next()) {
				prefix = ChatColor.translateAlternateColorCodes('&', rs.getString("prefix"));
			} else {
				prefix = "§7";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utils.closeQuietly(connection);
			Utils.closeQuietly(rs);
			Utils.closeQuietly(s);
		}
		return prefix;
	}

	public static void sendMessage(CommandSender sender, String message) {
		if (sender instanceof Player) {
			sendMessage((Player)sender, message);
		} else if (sender instanceof ConsoleCommandSender) {
			Bukkit.getConsoleSender().sendMessage((ChatColor.translateAlternateColorCodes('&', message)));
		}
	}
}
