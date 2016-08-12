package city.smash.union.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import org.apache.commons.dbcp2.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Player;

import city.smash.union.Main;
import city.smash.union.gui.GUIs;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class BoosterUtils {

	public static BoosterResult getMultiplierStatus(Player player) {

		BoosterResult result = new BoosterResult(0D, "", 0l);

		if (Bukkit.isPrimaryThread()) {
			throw new RuntimeException("MySQL on Blocking Thread");
		}
		Connection connection = null;
		ResultSet rs = null;
		Statement s = null;
		try {
			connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
			s = connection.createStatement();
			String query = "select multiplier, message from booster where (owner='" + player.getUniqueId().toString() + "' and activeUntil>"+System.currentTimeMillis()+") or (owner='server' and activeUntil>"+System.currentTimeMillis()+")";
			rs = s.executeQuery(query);

			if (rs.next()) {
				rs.beforeFirst();

				while (rs.next()) {
					result.message+=", " + rs.getString("message");
					result.addMulti(rs.getDouble("multiplier"));
				}
				result.message = result.message.substring(2);
			}
		} catch (Exception exception) {
			System.out.println(ChatColor.RED + "Mysql Error getting boosterresult!");
		} finally {
			Utils.closeQuietly(connection);
			Utils.closeQuietly(rs);
			Utils.closeQuietly(s);
		}
		return result;
	}

	public static BoosterResult getBoosterStatus(Player player) {

		if (Bukkit.isPrimaryThread()) {
			throw new RuntimeException("MySQL on Blocking Thread");
		}
		Connection connection = null;
		ResultSet rs = null;
		Statement s = null;
		try {
			connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
			s = connection.createStatement();
			String query = "select activeUntil, multiplier, message from booster where owner='" + player.getUniqueId().toString() + "' and activeUntil>"+System.currentTimeMillis();
			rs = s.executeQuery(query);

			if (rs.next()) {
				return new BoosterResult(rs.getDouble("multiplier"), rs.getString("message"), rs.getLong("activeUntil"));
			}
		} catch (Exception exception) {
			System.out.println(ChatColor.RED + "Mysql Error getting boosterresult!");
		} finally {
			Utils.closeQuietly(connection);
			Utils.closeQuietly(rs);
			Utils.closeQuietly(s);
		}
		return new BoosterResult(0D, "", 0l);
	}
	
	public static void grantBooster(String player, String message, Double multiplier, Long duration, int amount) {
		if (Bukkit.isPrimaryThread()) {
			throw new RuntimeException("MySQL on Blocking Thread");
		}

		Connection connection = null;
		Statement s = null;
		try {
			connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
			s = connection.createStatement();
			
			String _uid = UUIDUtils.getUUIDOf(player).toString();

			String query = "insert into booster (uuid, duration, multiplier, owner, message) VALUES ";
			for (int i = 0; i<amount; i++) {
				query += "('"+UUID.randomUUID()+"', "+duration.toString()+", "+multiplier.toString()+", '"+_uid+"', '"+message+"'), ";
			}
			query = query.substring(0, query.length()-2);
			
			s.executeUpdate(query);
		} catch (Exception exception) {
			System.out.println(ChatColor.RED + "Mysql Error giving booster to " + player);
		} finally {
			Utils.closeQuietly(connection);
			Utils.closeQuietly(s);
		}
	}

	public static boolean hasBooster(Player player) {
		if (Bukkit.isPrimaryThread()) {
			throw new RuntimeException("MySQL on Blocking Thread");
		}
		return (getBoosterStatus(player).getActiveUntil()!=0);
	}

	public static class BoosterResult {
		double multi;
		String message;
		long activeuntil;
		public BoosterResult(Double multi, String message, long activeuntil) {
			this.multi=multi;
			this.message=message;
			this.activeuntil=activeuntil;
		}
		public void addMulti(Double multi) {
			this.multi += multi;
		}
		public long getActiveUntil() {
			return this.activeuntil;
		}
		public double getMulti() {
			return this.multi;
		}
	}

	static Comparator<BoosterListResult> compareator = new Comparator<BoosterListResult>() {
		@Override public int compare(BoosterListResult item1, BoosterListResult item2) {
			if (Long.compare(item2.getDuration(), item1.getDuration()) != 0) {
				return Long.compare(item2.getDuration(), item1.getDuration());
			}
			if (item2.getMulti()-item1.getMulti() != 0) {
				return (int)Math.copySign(1, item2.getMulti()-item1.getMulti());
			}
			return item2.getMessage().hashCode() - item1.getMessage().hashCode();
		}
	};

	public static ArrayList<BoosterListResult> getBoosterList(Player player) {
		ArrayList<BoosterListResult> result = new ArrayList<BoosterListResult>();
		if (Bukkit.isPrimaryThread()) {
			throw new RuntimeException("MySQL on Blocking Thread");
		}

		Connection connection = null;
		ResultSet rs = null;
		Statement s = null;
		try {
			connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
			s = connection.createStatement();
			String query = "select uuid, message, duration, multiplier from booster where owner='" + player.getUniqueId().toString() + "' and activeUntil = 0";
			rs = s.executeQuery(query);

			while (rs.next()) {
				result.add(new BoosterListResult(UUID.fromString(rs.getString("uuid")), rs.getString("message"), rs.getDouble("multiplier"), rs.getLong("duration")));
			}
		} catch (Exception exception) {
			System.out.println(ChatColor.RED + "Mysql Error getting boosterresult!");
		}
		finally {
			Utils.closeQuietly(connection);
			Utils.closeQuietly(rs);
			Utils.closeQuietly(s);
		}

		Collections.sort(result, compareator);

		return result;
	}

	public static void scheduleBoosterWarning(Player player) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {
			@Override
			public void run() {
				if (hasBooster(player)) {
					if (DataUtils.getPlayer(player).getBoosterExpirationWarningID() != -1) {
						Bukkit.getScheduler().cancelTask(DataUtils.getPlayer(player).getBoosterExpirationWarningID());
					}
					int id = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.instance, new Runnable() {
						long taskTime = getBoosterStatus(player).getActiveUntil();
						@Override
						public void run() {

							if (!player.isOnline()) {
								Bukkit.getScheduler().cancelTask(DataUtils.getPlayer(player).getBoosterExpirationWarningID());
								return;
							}
							if (System.currentTimeMillis() >= taskTime) {
								
								String _uid = TaskUtils.newTaskCommand(player, new Runnable() {

									@Override
									public void run() {
										ArrayList<String> args = new ArrayList<String>();
										args.add("1");
										args.add("isdirect");
										GUIs.openGUI(player, "booster", args);
									}
									
								}).toString();
								
								Bukkit.getScheduler().runTask(Main.instance, new Runnable() {

									@Override
									public void run() {
										TextComponent message = null;
										TextComponent part;

										part = new TextComponent( "-----------------------------------------------------\n" );
										part.setColor(ChatColor.GRAY);
										part.setBold(false);
										part.setItalic(false);
										message = part;

										part = new TextComponent( "Your active Booster has expired!\n" );
										part.setColor(ChatColor.RED);
										part.setBold(true);
										part.setItalic(false);
										part.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/taskcommand " + _uid));
										message.addExtra(part);

										part = new TextComponent( "(Click this to activate another Booster)\n" );
										part.setColor(ChatColor.GRAY);
										part.setBold(false);
										part.setItalic(true);
										part.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/taskcommand " + _uid));
										message.addExtra(part);

										part = new TextComponent( "-----------------------------------------------------" );
										part.setColor(ChatColor.GRAY);
										part.setBold(false);
										part.setItalic(false);
										message.addExtra(part);

										ChatUtils.sendRawMessage(player, message);
										Bukkit.getScheduler().cancelTask(DataUtils.getPlayer(player).getBoosterExpirationWarningID());
									}
								});
							}
						}
					}, 0L, 1L).getTaskId();
					DataUtils.getPlayer(player).setBoosterExpirationWarningID(id);
				}
			}
		});
	}

	public static void activateBooster(Player player, UUID uuid) {
		if (Bukkit.isPrimaryThread()) {
			throw new RuntimeException("MySQL on Blocking Thread");
		}

		if (hasBooster(player)) {
			ChatUtils.sendMessage(player, "&cYou may only have one booster active at a time!");
			return;
		}

		Connection connection = null;
		ResultSet rs = null;
		Statement s = null;
		try {
			connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
			s = connection.createStatement();
			String query = "select owner from booster where owner='" + player.getUniqueId().toString() + "' and uuid='"+uuid.toString()+"' and activeuntil=0";
			rs = s.executeQuery(query);

			if (!rs.next()) {
				ChatUtils.sendMessage(player, "&cError! You do not own this booster! Please contact staff if this issue persists");
				return;
			}

			query = "Update booster set activeuntil=duration+"+System.currentTimeMillis()+" where uuid='"+uuid+"' and owner='"+player.getUniqueId().toString()+"'";
			s.executeUpdate(query);

			ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
			ChatUtils.sendMessage(player, "&a&lYou successfully activated a Booster!");
			ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");

			scheduleBoosterWarning(player);

			Bukkit.getScheduler().runTask(Main.instance, new Runnable() {
				@Override
				public void run() {
					FireworkUtils.launchInstantFirework(FireworkEffect.builder().flicker(true).withColor(Color.AQUA).with(Type.BURST).build(), player.getLocation().add(0,1,0));
				}
			});

		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println(ChatColor.RED + "Mysql Error activateing booster!");
		}
		finally {
			Utils.closeQuietly(connection);
			Utils.closeQuietly(rs);
			Utils.closeQuietly(s);
		}
	}
	
	public static class BoosterListResult {
		double multi;
		long duration;
		UUID uuid;
		String message;
		public BoosterListResult(UUID uuid, String message, double multi, long duration) {
			this.uuid = uuid;
			this.message = message;
			this.multi = multi;
			this.duration = duration;
		}
		public String getMessage() {
			return this.message;
		}
		public double getMulti() {
			return this.multi;
		}
		public UUID getUUID() {
			return this.uuid;
		}
		public long getDuration() {
			return this.duration;
		}
	}
}