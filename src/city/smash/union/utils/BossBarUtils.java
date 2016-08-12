package city.smash.union.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.dbcp2.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.inventivetalent.bossbar.BossBarAPI;
import org.json.JSONArray;
import org.json.JSONObject;

import city.smash.union.Main;
import net.md_5.bungee.api.ChatColor;
import us.myles.ViaVersion.api.ViaVersion;
import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.boss.BossStyle;

public class BossBarUtils {
	public static String bossbarPrefix = "bossbar-moshpit-";

	public static ArrayList<BossBarAnimationSegment> barCache = new ArrayList<BossBarAnimationSegment>();
	
	protected static class BossBarAnimationSegment {
		public int getDelay() {
			return delay;
		}
		public float getProgress() {
			return progress;
		}
		public BossStyle getStyle() {
			return style;
		}
		public BossColor getColor() {
			return color;
		}
		public String getText() {
			return text;
		}
		public BossBarAnimationSegment(int delay, float progress, BossStyle style, BossColor color, String text) {
			this.delay = delay;
			this.progress = progress;
			this.style = style;
			this.color = color;
			this.text = text;
		}
		protected int delay;
		protected float progress;
		protected BossStyle style;
		protected BossColor color;
		protected String text;
	}
	
	public static void startBossBar(Player player) {
		boolean is1_9 = (ViaVersion.getInstance().getPlayerVersion(player) > 47);
		
		UUID tsid = UUID.randomUUID();
		int id = Bukkit.getScheduler().runTaskTimer(Main.instance, new Runnable() {
			int pointer = -1;
			int i = Integer.MAX_VALUE;
			BossBar playerBar = null;
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (!player.isOnline()) {
					TaskUtils.killTask(tsid);
					return;
				}
				if (barCache.isEmpty()) {
					Connection connection = null;
					ResultSet rs = null;
					Statement s = null;
					try {
						connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserversettings");
						s = connection.createStatement();
						String query = "SELECT * FROM servermessages WHERE name LIKE '"+bossbarPrefix+"%'";
						rs = s.executeQuery(query);

						while (rs.next()) {
							JSONObject json = new JSONObject(EncodingUtils.decodeBase64(rs.getString("text")));
							JSONArray jsonArray = json.getJSONArray("Values");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject barInfo = jsonArray.getJSONObject(i);
								barCache.add(new BossBarAnimationSegment(barInfo.getInt("delay"), (float) barInfo.getDouble("progress"), BossStyle.valueOf(barInfo.getString("style").toUpperCase()), BossColor.valueOf(barInfo.getString("color").toUpperCase()), ChatColor.translateAlternateColorCodes('&', barInfo.getString("text"))));
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

				if (pointer < 0 || pointer >= barCache.size() || i >= barCache.get(pointer).getDelay()) {
					i=0;
					pointer++;
					if (pointer >= barCache.size()) {
						pointer=0;
					}
					BossBarAnimationSegment bar = barCache.get(pointer);

					if (is1_9) {
						if (playerBar == null) {
							playerBar = ViaVersion.getInstance().createBossBar("&8Starting Boss Bar...", 1, BossColor.WHITE, BossStyle.SOLID);
							playerBar.addPlayer(player);
						}
						playerBar.setColor(bar.getColor());
						playerBar.setTitle(bar.getText());
						playerBar.setHealth(bar.getProgress());
						playerBar.setStyle(bar.getStyle());
					} else {
						BossBarAPI.setMessage(player, bar.getText());
					}
				}
				i++;
			}
		}, 0L, 1L).getTaskId();
		TaskUtils.startTask(tsid, id, player, "bossbar-scheduler");
	}
}
