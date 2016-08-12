package city.smash.union.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp2.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import city.smash.union.Main;
import city.smash.union.classes.SmashPlayer;

public class StatisticUtils {
	public static void updateTimeAndDistance (Player player) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {
			@Override
			public void run() {
				Connection connection = null;
				Statement s = null;
				try {
					SmashPlayer data = DataUtils.getPlayer(player);
					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
					s = connection.createStatement();
					String query = "Update moshpitstats set " +
							"timeplayed=timeplayed+" + (System.currentTimeMillis() - data.getJoinTime()) + 
							", distancetraveled=distancetraveled+" + distanceMoved(player) +
							" where uuid='"+ player.getUniqueId().toString() +"'";
					s.executeUpdate(query);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(s);
				}

				if (!player.isOnline()) {
					DataUtils.removePlayer(player);
				} else {
					clearDistanceMoved(player);
					clearTime(player);
				}
			}

		});
	}

	private static int distanceMoved (Player player) {
		int result = 0;
		result+=player.getStatistic(Statistic.BOAT_ONE_CM);
		result+=player.getStatistic(Statistic.CLIMB_ONE_CM);
		result+=player.getStatistic(Statistic.CROUCH_ONE_CM);
		result+=player.getStatistic(Statistic.DIVE_ONE_CM);
		result+=player.getStatistic(Statistic.FALL_ONE_CM);
		result+=player.getStatistic(Statistic.FLY_ONE_CM);
		result+=player.getStatistic(Statistic.HORSE_ONE_CM);
		result+=player.getStatistic(Statistic.MINECART_ONE_CM);
		result+=player.getStatistic(Statistic.PIG_ONE_CM);
		result+=player.getStatistic(Statistic.SPRINT_ONE_CM);
		result+=player.getStatistic(Statistic.SWIM_ONE_CM);
		result+=player.getStatistic(Statistic.WALK_ONE_CM);
		return result;
	}
	
	public static void clearTime(Player player) {
		DataUtils.getPlayer(player).setJoinTime(System.currentTimeMillis());
	}

	public static void clearDistanceMoved(Player player) {
		player.setStatistic(Statistic.BOAT_ONE_CM, 0);
		player.setStatistic(Statistic.CLIMB_ONE_CM, 0);
		player.setStatistic(Statistic.CROUCH_ONE_CM, 0);
		player.setStatistic(Statistic.DIVE_ONE_CM, 0);
		player.setStatistic(Statistic.FALL_ONE_CM, 0);
		player.setStatistic(Statistic.FLY_ONE_CM, 0);
		player.setStatistic(Statistic.HORSE_ONE_CM, 0);
		player.setStatistic(Statistic.MINECART_ONE_CM, 0);
		player.setStatistic(Statistic.PIG_ONE_CM, 0);
		player.setStatistic(Statistic.SPRINT_ONE_CM, 0);
		player.setStatistic(Statistic.SWIM_ONE_CM, 0);
		player.setStatistic(Statistic.WALK_ONE_CM, 0);
	}

	public static void addHeal(Player player, double heal) {
		//		DataUtils.getPlayer(player).setDamageHealed(DataUtils.getPlayer(player).getDamageHealed() + heal);
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {

			@Override
			public void run() {
				Connection connection = null;
				Statement s = null;
				try {
					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
					s = connection.createStatement();
					String query = "Update moshpitstats set damagehealed=damagehealed+"+heal+" where uuid='"+ player.getUniqueId().toString() +"'";

					s.executeUpdate(query);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(s);
				}
			}

		});
	}

	public static void addDamageTaken(Player player, double damage) {
		//		DataUtils.getPlayer(player).setDamageTaken(DataUtils.getPlayer(player).getDamageTaken() + damage);
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {

			@Override
			public void run() {
				Connection connection = null;
				Statement s = null;
				try {
					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
					s = connection.createStatement();
					String query = "Update moshpitstats set damagetaken=damagetaken+"+damage+" where uuid='"+ player.getUniqueId().toString() +"'";

					s.executeUpdate(query);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(s);
				}
			}

		});
	}

	public static void addDamageDelt(Player player, double damage) {
		//		DataUtils.getPlayer(player).setDamageDelt(DataUtils.getPlayer(player).getDamageDelt() + damage);

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {

			@Override
			public void run() {
				Connection connection = null;
				Statement s = null;
				try {
					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
					s = connection.createStatement();
					String query = "Update moshpitstats set damagedelt=damagedelt+"+damage+" where uuid='"+ player.getUniqueId().toString() +"'";

					s.executeUpdate(query);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(s);
				}
			}

		});
	}

	public static void addSpellCast(Player player) {
		//		DataUtils.getPlayer(player).setSpellsCasted(DataUtils.getPlayer(player).getSpellsCasted() + 1);

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {

			@Override
			public void run() {
				Connection connection = null;
				Statement s = null;
				try {
					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
					s = connection.createStatement();
					String query = "Update moshpitstats set spellscasted=spellscasted+1 where uuid='"+ player.getUniqueId().toString() +"'";

					s.executeUpdate(query);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(s);
				}
			}

		});
	}

	public static void addDeath(Player player) {
		//		DataUtils.getPlayer(player).setDeaths(DataUtils.getPlayer(player).getDeaths() + 1);

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {

			@Override
			public void run() {
				Connection connection = null;
				Statement s = null;
				try {
					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
					s = connection.createStatement();
					String query = "Update moshpitstats set deaths=deaths+1 where uuid='"+ player.getUniqueId().toString() +"'";

					s.executeUpdate(query);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(s);
				}
			}

		});
	}

	public static void addAssist(Player player, Player victim, int damage, int totalDamage) {
		if (player.isOnline()) {
			DataUtils.getPlayer(player).setAssists(DataUtils.getPlayer(player).getAssists() + 1);
			ScoreboardUtils.setScoreboardSuffix(player, "assists", Integer.toString(DataUtils.getPlayer(player).getAssists()));
		}

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {

			@Override
			public void run() {
				Connection connection = null;
				Statement s = null;
				try {
					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
					s = connection.createStatement();
					String query = "Update moshpitstats set assists=assists+1 where uuid='"+ player.getUniqueId().toString() +"'";
					s.executeUpdate(query);
					query = "INSERT INTO moshpitfightlog (uuid, uuidvictim, time, type, spells, damagedelt, totaldamagedelt) VALUES ('"+player.getUniqueId().toString()+"', '"+victim.getUniqueId().toString()+"', "+(int)(System.currentTimeMillis()/1000)+", 'assist', (select equippedspells from moshpitstats where uuid = '"+player.getUniqueId().toString()+"'), "+damage+", "+totalDamage+")";
					s.executeUpdate(query);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(s);
				}
				if (player.isOnline()) {
					getAssists(player, true);
					ScoreboardUtils.refreshAssists(player);
				}
				
			}

		});
	}

	public static int getAssists(Player player, boolean checkMysql) {

		if (checkMysql) {
			if (Bukkit.isPrimaryThread()) {
				throw new RuntimeException("MySQL on Blocking Thread");
			}
			
			Connection connection = null;
			ResultSet rs = null;
			Statement s = null;
			try {
				connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
				s = connection.createStatement();
				String query = "Select assists from moshpitstats where uuid='"+ player.getUniqueId().toString() +"'";

				rs = s.executeQuery(query);

				if (rs.next()) {
					DataUtils.getPlayer(player).setAssists(rs.getInt("assists"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				Utils.closeQuietly(connection);
				Utils.closeQuietly(s);
			}
		}
		return DataUtils.getPlayer(player).getAssists();
	}

	public static int getKills(Player player, boolean checkMysql) {

		if (checkMysql) {
			if (Bukkit.isPrimaryThread()) {
				throw new RuntimeException("MySQL on Blocking Thread");
			}
			
			Connection connection = null;
			ResultSet rs = null;
			Statement s = null;
			try {
				connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
				s = connection.createStatement();
				String query = "Select kills from moshpitstats where uuid='"+ player.getUniqueId().toString() +"'";

				rs = s.executeQuery(query);

				if (rs.next()) {
					DataUtils.getPlayer(player).setKills(rs.getInt("kills"));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				Utils.closeQuietly(connection);
				Utils.closeQuietly(rs);
				Utils.closeQuietly(s);
			}
		}
		return DataUtils.getPlayer(player).getKills();
		
	}

	public static void addKill(Player player, Player victim, int damage, int totalDamage) {

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {

			@Override
			public void run() {
				Connection connection = null;
				Statement s = null;
				try {
					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
					s = connection.createStatement();
					String query = "Update moshpitstats set kills=kills+1 where uuid='"+ player.getUniqueId().toString() +"'";
					s.executeUpdate(query);
					query = "INSERT INTO moshpitfightlog (uuid, uuidvictim, time, type, spells, damagedelt, totaldamagedelt) VALUES ('"+player.getUniqueId().toString()+"', '"+victim.getUniqueId().toString()+"', "+(int)(System.currentTimeMillis()/1000)+", 'kill', (select equippedspells from moshpitstats where uuid = '"+player.getUniqueId().toString()+"'), "+damage+", "+totalDamage+")";
					s.executeUpdate(query);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(s);
				}
				getKills(player, true);
				ScoreboardUtils.refreshKills(player);
			}

		});
	}
}
