package city.smash.union.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp2.Utils;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import city.smash.union.Main;
import net.md_5.bungee.api.ChatColor;

public class TablistUtils {
	public static String header = null;
	public static String footer = null;
	private static String prefix = "Tablist-Moshpit-";
	public static void setTablistHeader(Player player) {
		
		if (header == null || footer == null) {
			Connection connection = null;
			ResultSet rs = null;
			Statement s = null;
			try {
				connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserversettings");
				s = connection.createStatement();
				String query = "select * from servermessages Where name like '"+ prefix+"%'";
				rs = s.executeQuery(query);

				while (rs.next()) {
					switch (rs.getString("name").substring(prefix.length()).toLowerCase()) {
					case "header":
						header = ChatColor.translateAlternateColorCodes('&', rs.getString("text").replace("\\n", "\n"));
						break;
					case "footer":
						footer = ChatColor.translateAlternateColorCodes('&', rs.getString("text").replace("\\n", "\n"));
						break;
					default:
						System.out.println("-" +rs.getString("name").substring(prefix.length()-1).toLowerCase()+ "-");
					}
				}
				if (header == null) {
					header = "";
				}
				if (footer == null) {
					footer = "";
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				Utils.closeQuietly(connection);
				Utils.closeQuietly(rs);
				Utils.closeQuietly(s);
			}
		}
		
		PacketContainer pc = Main.protocolManager.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
		pc.getChatComponents().write(0, WrappedChatComponent.fromText(header)).write(1, WrappedChatComponent.fromText(footer));
		try {
			Main.protocolManager.sendServerPacket(player, pc);
		} catch (Exception ex) {
			System.out.println("TablistHead failure for " + player.getName() + "!");
		}
	}

}
