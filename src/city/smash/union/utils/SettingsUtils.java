package city.smash.union.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.commons.dbcp2.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import city.smash.union.Main;
import city.smash.union.classes.SmashGlobalSetting;
import city.smash.union.classes.SmashSetting;
import city.smash.union.classes.SmashUnionSetting;

public class SettingsUtils {
	public static boolean isEnabled(Player player, SmashSetting setting) {
		return getValue(player, setting) != -1;
	}
	public static short getValue(Player player, SmashSetting setting) {
		if (DataUtils.getPlayer(player).getSettings().containsKey(setting)) {
			return DataUtils.getPlayer(player).getSettings().get(setting);
		}
		return -1;
	}
	
	public static void setValue(Player player, SmashSetting setting, Short value) {
		if (value == -1) {
			DataUtils.getPlayer(player).getSettings().remove(setting);
		} else {
			DataUtils.getPlayer(player).getSettings().put(setting, value);
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {
			@Override
			public void run() {
				
				HashMap<String, String> settingsToUpload = new HashMap<String, String>();
				for (SmashSetting testSetting : DataUtils.getPlayer(player).getSettings().keySet()) {
					if (DataUtils.getPlayer(player).getSettings().get(testSetting) != -1) {
						String key = testSetting.getTable();
						if (DataUtils.getPlayer(player).getSettings().get(testSetting) == 0) {
							if (settingsToUpload.containsKey(key)) {
								settingsToUpload.put(key, settingsToUpload.get(key) + "-" + testSetting.getValue());
							} else {
								settingsToUpload.put(key, testSetting.getValue());
							}
						} else {
							if (settingsToUpload.containsKey(key)) {
								settingsToUpload.put(key, settingsToUpload.get(key) + "-" + testSetting.getValue() + ":" + DataUtils.getPlayer(player).getSettings().get(testSetting));
							} else {
								settingsToUpload.put(key, testSetting.getValue() + ":" + DataUtils.getPlayer(player).getSettings().get(testSetting));
							}
						}
					}
				}
				Connection connection = null;
				Statement s = null;
				try {
					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
					s = connection.createStatement();
					for (SmashSetting testSetting : DataUtils.getPlayer(player).getSettings().keySet()) {
						if (settingsToUpload.containsKey(testSetting.getTable())) {
							String query = "Update "+testSetting.getTable()+" set "+testSetting.getColumn()+"='"+settingsToUpload.get(testSetting.getTable())+"' where uuid='"+ player.getUniqueId().toString() +"'";
							s.executeUpdate(query);
							settingsToUpload.remove(testSetting.getTable());
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(s);
				}
			}
		});
	}

	public static void setEnabled(Player player, SmashSetting setting, Boolean value) {
		if (!value) {
			setValue(player, setting, (short)-1);
		} else {
			setValue(player, setting, (short)-0);
		}
	}

	public static void flipSetting(Player player, SmashSetting setting) {
		setEnabled(player, setting, !isEnabled(player, setting));
	}

	public static void instantiateSettings(Player player) {
		if (Bukkit.isPrimaryThread()) {
			throw new RuntimeException("MySQL on Blocking Thread");
		}
		
		SmashSetting[] checkMap = {SmashGlobalSetting.CHAT_BROADCAST, SmashUnionSetting.LOW_HEALTH_INDICATOR};
		DataUtils.getPlayer(player).getSettings().clear();
		for (SmashSetting settingType : checkMap) {
			Connection connection = null;
			ResultSet rs = null;
			Statement s = null;
			try {
				connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
				s = connection.createStatement();
				String query = "Select "+settingType.getColumn()+" from "+settingType.getTable()+" where uuid='"+ player.getUniqueId().toString() +"'";
				rs = s.executeQuery(query);

				if (rs.next()) {
					String[] settingStringList = rs.getString("settings").split("-");

					for (String settingString : settingStringList) {
						SmashSetting setting = getSettingFromValue(settingString.split(":")[0], settingType.getTable());
						if (setting != null) {
							Short value = 0;
							try {
								value = Short.parseShort(settingString.split(":")[1]);
							} catch (Exception e) {
								value = 0;
							}
							DataUtils.getPlayer(player).getSettings().put(setting, value);
						}
					}
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
	public static SmashSetting getSettingFromValue(String value, String type) {
		switch (type.toLowerCase()) {
		case "globalstats":
			for (SmashGlobalSetting setting : SmashGlobalSetting.values()) {
				if (setting.getValue().equalsIgnoreCase(value)) {
					return setting;
				}
			}
			break;
		case "unionstats":
			for (SmashUnionSetting setting : SmashUnionSetting.values()) {
				if (setting.getValue().equalsIgnoreCase(value)) {
					return setting;
				}
			}
			break;
		}
		return null;
	}
}
