package city.smash.union.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp2.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SkinUtils {
	public static String getSkinFromCache(Player player) {
		if (DataUtils.getPlayer(player).getSkinID() != null) {
			return DataUtils.getPlayer(player).getSkinID();
		}
		if (Bukkit.isPrimaryThread()) {
			throw new RuntimeException("MySQL on Blocking Thread");
		}

		Connection connection = null;
		ResultSet rs = null;
		Statement s = null;
		try {
			connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
			s = connection.createStatement();
			String query = "select * from skincache Where uuid='"+ player.getUniqueId().toString() +"'";
			rs = s.executeQuery(query);

			boolean insert = false;
			
			if (rs.next()) {
				if (System.currentTimeMillis() - rs.getLong("lastupdate") < 3600000) {
					DataUtils.getPlayer(player).setSkinID(rs.getString("skin"));
					return DataUtils.getPlayer(player).getSkinID();
				}
			} else {
				insert = true;
			}
			
			String skinID = null;
			try {
				URLConnection con = new URL( "http://skins.minecraft.net/MinecraftSkins/" + player.getName() + ".png" ).openConnection();
				con.connect();
				InputStream is = con.getInputStream();
				skinID = con.getURL().toString().split("texture/")[1];
				is.close();
			} catch (IOException e) {
				System.out.println("Error getting skin");
				skinID = "4b92cb43333aa621c70eef4ebf299ba412b446fe12e341ccc582f3192189";
			}
			
			DataUtils.getPlayer(player).setSkinID(skinID);
			
			if (insert) {
				query = "Insert into skincache (lastupdate, skin, uuid) values (" + Long.toString(System.currentTimeMillis()) + ", '" + skinID + "', '" + player.getUniqueId().toString() + "')";
			} else {
				query = "Update skincache set lastupdate=" + Long.toString(System.currentTimeMillis()) + ", skin='" + skinID + "' where uuid='" + player.getUniqueId().toString() + "'";
			}
			s.executeUpdate(query);

			return DataUtils.getPlayer(player).getSkinID();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utils.closeQuietly(connection);
			Utils.closeQuietly(rs);
			Utils.closeQuietly(s);
		}
		return null;

	}
}
