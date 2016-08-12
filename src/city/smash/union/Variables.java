package city.smash.union;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.commons.dbcp2.Utils;

public class Variables {
	final public static HashMap<String, String> settingsMap = new HashMap<String, String>();

	public static void loadInfoFromMysql() {
		loadSettings();
	}

	public static Long getLong (String value) {
		value = value.toLowerCase();

		if (!settingsMap.containsKey(value)) {
			throw new RuntimeException("Funtion error");
		}
		else {
			return Long.parseLong(settingsMap.get(value));
		}
	}
	/**zen
	 * Gets a Int from settingsMap Map
	 * @param value Name of Value
	 */
	public static Integer getInt (String value) {
		value = value.toLowerCase();

		if (!settingsMap.containsKey(value)) {
			throw new RuntimeException("Funtion error");
		}
		else {
			return Integer.parseInt(settingsMap.get(value));
		}
	}
	/**
	 * Gets a Double from settingsMap Map
	 * @param value Name of Value
	 */
	public static Double getDouble (String value) {
		value = value.toLowerCase();

		if (!settingsMap.containsKey(value)) {
			throw new RuntimeException("Funtion error");
		}
		else {
			return Double.parseDouble(settingsMap.get(value));
		}
	}
	/**
	 * Gets a Float from settingsMap Map
	 * @param value Name of Value
	 */
	public static Float getFloat (String value) {
		value = value.toLowerCase();

		if (!settingsMap.containsKey(value)) {
			throw new RuntimeException("Funtion error");
		}
		else {
			return Float.parseFloat(settingsMap.get(value));
		}
	}
	/**
	 * Gets a String from settingsMap Map
	 * @param value Name of Value
	 */
	public static String getString (String value) {
		value = value.toLowerCase();

		if (!settingsMap.containsKey(value)) {
			throw new RuntimeException("Funtion error");
		}
		else {
			return settingsMap.get(value);
		}
	}

	public static void loadSettings() {
		Connection connection = null;
		ResultSet rs = null;
		Statement s = null;
		try {
			connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserversettings");
			s = connection.createStatement();
			s.setFetchSize(1000000);
			String query = "Select * from unionsettings where id like 'settings-%'";
			rs = s.executeQuery(query);

			while (rs.next()) {
				String[] info = rs.getString("id").toLowerCase().split("-");
				settingsMap.put(info[1].toLowerCase(), rs.getString("value"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utils.closeQuietly(connection);
			Utils.closeQuietly(rs);
			Utils.closeQuietly(s);
		}
	}
}
