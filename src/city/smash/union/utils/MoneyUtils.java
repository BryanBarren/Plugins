package city.smash.union.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;

import org.apache.commons.dbcp2.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import city.smash.union.Main;
import city.smash.union.Variables;
import city.smash.union.utils.BoosterUtils.BoosterResult;

public class MoneyUtils { 
	public static int addCoinsWithMulti(Player player, int coins) {

		if (Bukkit.isPrimaryThread()) {
			throw new RuntimeException("MySQL on Blocking Thread");
		}

		int rankmulti = getMultiplier(player);
		BoosterResult result = BoosterUtils.getMultiplierStatus(player);

		double multi;
		multi = (rankmulti == 1) ? result.getMulti() : result.getMulti() + rankmulti;
		if (multi < 1) {
			multi = 1;
		}

		coins = (int)(coins * multi);

		addCoins(player, coins);

		return coins;
	}

	public static int addCoinsServerBooster(Player player, int coins) {

		if (Bukkit.isPrimaryThread()) {
			throw new RuntimeException("MySQL on Blocking Thread");
		}

		BoosterResult result = BoosterUtils.getMultiplierStatus(player);

		double multi = result.getMulti();
		if (multi < 1) {
			multi = 1;
		}

		coins = (int)(coins * multi);

		addCoins(player, coins);

		return coins;
	}

	public static void setCoins(Player player, int coins) {
		setCoins(player.getName(), coins);
	}

	public static void setCoins(String player, int coins) {

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {
			@Override
			public void run() {
				Connection connection = null;
				Statement s = null;
				try {
					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
					s = connection.createStatement();
					String query = "Update unionstats set coins="+coins+" where uuid=(select uuid from globalstats where playername='"+player+"')";

					s.executeUpdate(query);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(s);
				}
			}
		});
	}

	public static void addCoins(Player player, int coins) {
		addCoins(player.getName(), coins);
	}

	public static void addCoins(String player, int coins) {

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {
			@Override
			public void run() {
				Connection connection = null;
				Statement s = null;
				try {
					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
					s = connection.createStatement();
					String query = "Update unionstats set coins=coins+"+coins+" where uuid=(select uuid from globalstats where playername='"+player+"')";

					s.executeUpdate(query);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(s);
				}
			}
		});
	}

	public static int getMultiplier(Player player) {
		if (player.hasPermission("youtuber")) {
			return 7;
		}
		else if (player.hasPermission("overlord")) {
			return 6;
		}
		else if (player.hasPermission("phoenix")) {
			return 5;
		}
		else if (player.hasPermission("titan")) {
			return 4;
		}
		else if (player.hasPermission("builder")) {
			return 3;
		}
		else if (player.hasPermission("hydra")) {
			return 3;
		}
		else if (player.hasPermission("hero")) {
			return 2;
		}
		else {
			return 1;
		}
	}

	public static double getMultiplierWithBooster(Player player) {
		double result = BoosterUtils.getBoosterStatus(player).getMulti();

		int multi;
		if (player.hasPermission("youtuber")) {
			multi = 7;
		}
		else if (player.hasPermission("overlord")) {
			multi = 6;
		}
		else if (player.hasPermission("phoenix")) {
			multi = 5;
		}
		else if (player.hasPermission("titan")) {
			multi = 4;
		}
		else if (player.hasPermission("builder")) {
			multi = 3;
		}
		else if (player.hasPermission("hydra")) {
			multi = 3;
		}
		else if (player.hasPermission("hero")) {
			multi = 2;
		}
		else {
			multi = 1;
		}

		return (result == 1) ? multi : multi + result;
	}

	public static int getMultiplier(String str) {
		if (str.equalsIgnoreCase("youtuber")) {
			return 7;
		}
		else if (str.equalsIgnoreCase("overlord")) {
			return 6;
		}
		else if (str.equalsIgnoreCase("phoenix")) {
			return 5;
		}
		else if (str.equalsIgnoreCase("titan")) {
			return 4;
		}
		else if (str.equalsIgnoreCase("builder")) {
			return 3;
		}
		else if (str.equalsIgnoreCase("hydra")) {
			return 3;
		}
		else if (str.equalsIgnoreCase("hero")) {
			return 2;
		}
		else {
			return 1;
		}
	}

	public static int getCoins(Player player) {
		if (Bukkit.isPrimaryThread()) {
			throw new RuntimeException("MySQL on Blocking Thread");
		}
		Connection connection = null;
		ResultSet rs = null;
		Statement s = null;
		try {
			connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
			s = connection.createStatement();
			String query = "Select coins from moshpitstats where uuid='"+ player.getUniqueId().toString() +"'";
			rs = s.executeQuery(query);

			int coins;
			if (rs.next()) {
				coins = rs.getInt("coins");
			} else {
				coins = 0;
			}
			return coins;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utils.closeQuietly(connection);
			Utils.closeQuietly(rs);
			Utils.closeQuietly(s);
		}
		return 0;
	}

	public static void deathCoinsPayout(Player victim) {

		String color = DataUtils.getPlayer(victim).getPrefix().substring(0, 2);

		double coinPerDamage = Variables.getDouble("Coins_coinPerDamage");
		double coinPerKill = Variables.getDouble("Coins_coinPerKill");
		double chanceOfCratePerDamage = Variables.getDouble("coins_chanceofcrateperdamage");

		Player lastAttacker = DataUtils.getPlayer(victim).getLastAttacker();

		HashMap<Player, Double> damagerSet = new HashMap<Player, Double>(DataUtils.getPlayer(victim).getDamagers());

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {
			@Override
			public void run() {
				double totalDamage = 0;
				for (Player target : damagerSet.keySet()) {
					totalDamage += damagerSet.get(target);
				}

				for (Player target : damagerSet.keySet()) {
					double damage = damagerSet.get(target);
					int coins = 0;

					double personalMulti = getMultiplierWithBooster(target);

					String boostedBy = "";

					double globalMulti = 1;
					for (Player target2 : damagerSet.keySet()) {
						double targetMulti = getMultiplierWithBooster(target2);
						double maxMulti = (personalMulti > targetMulti) ? personalMulti : targetMulti;

						if (maxMulti > 1) {
							if (personalMulti < targetMulti) {
								boostedBy = boostedBy + "&c, "+ DataUtils.getPlayer(target2).getPrefix().substring(0, 2) + target2.getDisplayName();
							}
						}
						globalMulti += maxMulti * (damagerSet.get(target2)/totalDamage);

						coins += damage * (maxMulti * (damagerSet.get(target2)/totalDamage));
					}
					coins *= coinPerDamage;
					if (boostedBy.length() > 4) {
						boostedBy= " by " + boostedBy.substring(4);
					}
					if (globalMulti > 1) {
						globalMulti--;
					}

					if (target == lastAttacker) {
						coins+=coinPerKill;
					}

					//Making Suffix
					BoosterResult result = BoosterUtils.getMultiplierStatus(target);
					String suffix = result.message;
					if (globalMulti != 1) {
						DecimalFormat decimalFormat = new DecimalFormat("#.00");

						String multiString = decimalFormat.format(globalMulti);
						if (globalMulti == Math.round(globalMulti)) {
							multiString = Integer.toString((int)globalMulti);
						}

						if (suffix.equalsIgnoreCase("")) {
							suffix = multiString+"x Multiplier" +boostedBy;
						} else {
							suffix = multiString+"x Multiplier"+boostedBy+", " + suffix;
						}
					}
					if (!suffix.equalsIgnoreCase("")) {
						suffix = " &8(&c"+suffix+"&8)";
					}
					//end of suffix


					int c = addCoinsServerBooster(target, coins);

					//					int crateLevel = -1;
					//					if (Main.random.nextDouble() < chanceOfCratePerDamage * damage * LootcrateUtils.getLootcrateMulti(target)) {
					//						crateLevel = LootcrateUtils.getGeneratedChestLevel(0);
					//						LootcrateUtils.grantCrate(target.getName(), crateLevel, "earned-Union");
					//					}
					//
					//					String s = suffix;
					//					int cL = crateLevel;
					//					int tDamage = (int) totalDamage;
					//					Bukkit.getScheduler().runTask(Main.instance, new Runnable() {
					//
					//						@Override
					//						public void run() {
					//
					//							if (target.isOnline()) {
					//								ChatUtils.sendMessage(target, "&7&m-----------------------------------------------------");
					//							}
					//							if (target.equals(lastAttacker)) {
					//								if (target.isOnline()) {
					//									ChatUtils.sendMessage(target, "&8[&c&lKill&8] &7You have slain " + color + victim.getDisplayName());
					//									ChatUtils.sendMessage(target, "&8[&c&lKill&8] &8(&6+"+c+" coins&8)" + s);
					//									TitleUtils.sendTitle(target, "&cYou've Slain " + color + victim.getDisplayName(), "&6+" + c + " coins", 20, 60, 20);
					//								}
					//
					//								StatisticUtils.addKill(target, victim, (int)damage, tDamage);
					//							} else {
					//								if (target.isOnline()) {
					//									ChatUtils.sendMessage(target, "&8[&e&lAssist&8] &7You've assisted " + DataUtils.getPlayer(lastAttacker).getPrefix().substring(0, 2) +lastAttacker.getDisplayName() +  " &7in slaying " + color + victim.getDisplayName());
					//									ChatUtils.sendMessage(target, "&8[&e&lAssist&8] &8(&6+"+c+" coins&8)" + s);
					//									TitleUtils.sendTitle(target, "&eYou've Assisted in Slaying " + color + victim.getDisplayName(), "&6+" + c + " coins", 20, 60, 20);
					//								}
					//
					//								StatisticUtils.addAssist(target, victim, (int)damage, tDamage);
					//							}
					//
					//							if (target.isOnline()) {
					//								if (cL != -1) {
					//									ChatUtils.sendMessage(target, "&8[&d&lTreasure&8] &7You have found a " + LootcrateUtils.getChestNamePrefix(cL) + "Mystery Chest&7!");
					//								}
					//								ChatUtils.sendMessage(target, "&7&m-----------------------------------------------------");
					//							}
					//
					//						}
					//					});
				}
			}
		});
	}
}
