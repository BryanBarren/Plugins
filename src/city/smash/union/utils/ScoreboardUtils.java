package city.smash.union.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.dbcp2.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import city.smash.union.Main;
import city.smash.union.classes.SmashUnionSetting;

public class ScoreboardUtils {
	public static void setScoreboardPrefix(Player player, String team, String prefix) {
		if (!player.isOnline()) {
			return;
		}

		prefix = ChatColor.translateAlternateColorCodes('&', prefix);
		if (prefix.length() > 16) {
			prefix = prefix.substring(0, 17);
		}
		player.getScoreboard().getTeam(team.toLowerCase()).setPrefix(prefix);
	}

	public static void setScoreboardSuffix(Player player, String team, String suffix) {
		if (!player.isOnline()) {
			return;
		}

		suffix = ChatColor.translateAlternateColorCodes('&', suffix);
		if (suffix.length() > 16) {
			suffix = suffix.substring(0, 17);
		}
		player.getScoreboard().getTeam(team.toLowerCase()).setSuffix(suffix);
	}

	public static void refreshHPIndicator(Player player) {
		if (!player.isOnline()) {
			return;
		}

		for (Player target : Bukkit.getOnlinePlayers()) {
			if (target == player) {
				continue;
			}
			target.getScoreboard().getObjective(DisplaySlot.BELOW_NAME).getScore(player.getName()).setScore((int)Math.ceil(player.getHealth() + BarrierUtils.getBarrierHP(player)));
		}
		setScoreboardSuffix(player, "health", Integer.toString((int)Math.ceil(player.getHealth() + BarrierUtils.getBarrierHP(player))) + "§7/" + Integer.toString((int)player.getMaxHealth()) + "§c❤");
		if (SettingsUtils.isEnabled(player, SmashUnionSetting.LOW_HEALTH_INDICATOR)) {
			BorderUtils.updateHealthBorder(player);
		}
	}

	public static void refreshHPIndicatorOther(Player player) {
		if (!player.isOnline()) {
			return;
		}

		for (Player target : Bukkit.getOnlinePlayers()) {
			if (target == player) {
				continue;
			}
			player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME).getScore(target.getName()).setScore((int)Math.ceil(target.getHealth() + BarrierUtils.getBarrierHP(target)));
		}
		
	}

	public static void refreshKills(Player player) {
		if (!player.isOnline()) {
			return;
		}

		setScoreboardSuffix(player, "kills", NumberUtils.addSeperators(StatisticUtils.getKills(player, false)));
	}

	public static void refreshAssists(Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		setScoreboardSuffix(player, "assists", NumberUtils.addSeperators(StatisticUtils.getAssists(player, false)));
	}

	public static void createScoreboard(Player player) {
		ScoreboardManager sbManager = Bukkit.getScoreboardManager();
		Scoreboard scoreboard = sbManager.getNewScoreboard();

		scoreboard.registerNewObjective("Health", "dummy").setDisplaySlot(DisplaySlot.BELOW_NAME);
		scoreboard.getObjective(DisplaySlot.BELOW_NAME).setDisplayName("§c❤");

		Objective sidebar = scoreboard.registerNewObjective("General", "dummy");
		sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		sidebar.setDisplayName("§a§lLegends of MC");
		
		Team team;
		sidebar.getScore("§0§7§m").setScore(10);
		team = scoreboard.registerNewTeam("SEPERATOR1");
		team.addEntry("§0§7§m");
		team.setSuffix("----------------");

		sidebar.getScore("Kills: §a").setScore(9);
		team = scoreboard.registerNewTeam("kills");
		team.addEntry("Kills: §a");
		team.setSuffix("0");

		sidebar.getScore("Assists: §a").setScore(8);
		team = scoreboard.registerNewTeam("assists");
		team.addEntry("Assists: §a");

		sidebar.getScore("§1").setScore(7);

		sidebar.getScore("Coins: §a").setScore(6);
		team = scoreboard.registerNewTeam("coins");
		team.addEntry("Coins: §a");

		sidebar.getScore("Health: §a").setScore(5);
		team = scoreboard.registerNewTeam("health");
		team.addEntry("Health: §a");
		
		sidebar.getScore("§2").setScore(3);

		sidebar.getScore("§aplay.smash.city").setScore(2);

		sidebar.getScore("§1§7§m").setScore(1);
		team = scoreboard.registerNewTeam("SEPERATOR2");
		team.addEntry("§1§7§m");
		team.setSuffix("----------------");

		player.setScoreboard(scoreboard);
	}

	public static void refreshGuildTag(Player player) {
		player.setPlayerListName(DataUtils.getPlayer(player).getPrefix() + player.getDisplayName());
		for (Player target : Bukkit.getOnlinePlayers()) {
			Team t;
			
			if (target == player) {
				if (player.getScoreboard().getTeam(target.getName()) == null) {
					t = player.getScoreboard().registerNewTeam(target.getName());
					t.addEntry(target.getName());
				} else {
					t = player.getScoreboard().getEntryTeam(target.getName());
				}
				t.setPrefix(ChatColor.translateAlternateColorCodes('&', DataUtils.getPlayer(target).getPrefix()));
				if (DataUtils.getPlayer(target) == null) {
					ScoreboardUtils.fixTag(player, target);
					continue;
				}

				if (!DataUtils.getPlayer(target).getGuildTag().equalsIgnoreCase("")) {
					t.setSuffix(" §8[§a"+DataUtils.getPlayer(target).getGuildTag()+"§8]");
				} else {
					t.setSuffix("");
				}
				continue;
			}

			//Showing Target -> Player's tag
			if (target.getScoreboard().getTeam(player.getName()) == null) {
				t = target.getScoreboard().registerNewTeam(player.getName());
				t.addEntry(player.getName());
			} else {
				t = target.getScoreboard().getEntryTeam(player.getName());
			}

			t.setPrefix(ChatColor.translateAlternateColorCodes('&', DataUtils.getPlayer(player).getPrefix()));

			if (!DataUtils.getPlayer(player).getGuildTag().equalsIgnoreCase("")) {
				t.setSuffix(" §8[§a"+DataUtils.getPlayer(player).getGuildTag()+"§8]");
			} else {
				t.setSuffix("");
			}

			//Showing Player -> Target's tag
			if (DataUtils.getPlayer(target).getGuildTag() != null && DataUtils.getPlayer(target).getPrefix() != null) {
				if (player.getScoreboard().getTeam(target.getName()) == null) {
					t = player.getScoreboard().registerNewTeam(target.getName());
					t.addEntry(target.getName());
				} else {
					t = player.getScoreboard().getEntryTeam(target.getName());
				}
				t.setPrefix(ChatColor.translateAlternateColorCodes('&', DataUtils.getPlayer(target).getPrefix()));

				if (DataUtils.getPlayer(target) == null) {
					ScoreboardUtils.fixTag(player, target);
					continue;
				}

				if (!DataUtils.getPlayer(target).getGuildTag().equalsIgnoreCase("")) {
					t.setSuffix(" §8[§a"+DataUtils.getPlayer(target).getGuildTag()+"§8]");
				} else {
					t.setSuffix("");
				}
			}
		}
	}

	public static void fixTag(Player player, Player target) {
		if (DataUtils.getPlayer(target) == null) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {

				@Override
				public void run() {
					fixTag(player, target);
				}

			}, 1L);
			return;
		}
		Team t;
		if (player.getScoreboard().getTeam(target.getName()) == null) {
			t = player.getScoreboard().registerNewTeam(target.getName());
			t.addEntry(target.getName());
		} else {
			t = player.getScoreboard().getEntryTeam(target.getName());
		}
		if (!DataUtils.getPlayer(target).getGuildTag().equalsIgnoreCase("")) {
			t.setSuffix(" §8[§a"+DataUtils.getPlayer(target).getGuildTag()+"§8]");
		} else {
			t.setSuffix("");
		}
	}

	public static String loadGuildtag(Player player) {
		if (Bukkit.isPrimaryThread()) {
			throw new RuntimeException("MySQL on Blocking Thread");
		}

		String result = "";

		Connection connection = null;
		ResultSet rs = null;
		Statement s = null;
		try {
			connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
			s = connection.createStatement();
			String query = "select tag from guild where type='guild' and guid=(select guid from guild where type='player' and name='" + player.getUniqueId().toString() + "') and tag IS NOT NULL";
			rs = s.executeQuery(query);
			if (rs.next()) {
				result = rs.getString("tag");
			}
			DataUtils.getPlayer(player).setGuildTag(result);
		} catch (Exception exception) {
			System.out.println(ChatColor.RED + "Mysql Error getting guild tag!");
			player.kickPlayer("&c&lError retreiving your information! Please relog!");
		} finally {
			Utils.closeQuietly(connection);
			Utils.closeQuietly(rs);
			Utils.closeQuietly(s);
		}
		return result;
	}
}
