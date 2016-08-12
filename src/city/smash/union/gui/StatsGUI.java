package city.smash.union.gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import city.smash.union.Main;
import city.smash.union.classes.SmashInventory;
import city.smash.union.utils.ChatUtils;
import city.smash.union.utils.NumberUtils;
import city.smash.union.utils.SoundUtils;
import city.smash.union.utils.TimeUtils;
import city.smash.union.utils.UUIDUtils;

public class StatsGUI extends GUI {
	@Override
	public void openGUI(Player player, List<String> args) {

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {

			@Override
			public void run() {
				String _uid = player.getUniqueId().toString();

				int slot;
				ItemStack item;
				ItemMeta meta;
				SmashInventory map = new SmashInventory(player);
				ArrayList<String> lore;
				Inventory inventory;

				Connection connection = null;
				ResultSet rs = null;
				Statement s = null;
				try {
					if (!args.isEmpty() && !args.get(0).equalsIgnoreCase(player.getName())) {
						_uid = UUIDUtils.getUUIDOf(args.get(0)).toString();
					}

					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
					s = connection.createStatement();
					String query = "Select * from globalstats left join moshpitstats on moshpitstats.uuid = globalstats.uuid where globalstats.uuid='"+_uid+"'";
					rs = s.executeQuery(query);
					if (rs.next()) {

						inventory = Bukkit.createInventory(null, 45, "§8Stats of " + rs.getString("playername"));
						breakLoop:{
							for (String check : args) {
								if (check.equalsIgnoreCase("isdirect")) {
									break breakLoop;
								}
							}
							inventory = Bukkit.createInventory(null, 54, "§8Stats of " + rs.getString("playername"));
						}

						slot = 22;
						item = new ItemStack(Material.BLAZE_ROD, 1);
						meta = item.getItemMeta();
						meta.addItemFlags(ItemFlag.values());
						lore = new ArrayList<String>();
						meta.setDisplayName("§a§lLegends of MC Stats");
						lore.add("§7Kills: §6" + NumberUtils.addSeperators(rs.getInt("kills")));
						lore.add("§7Deaths: §6" + NumberUtils.addSeperators(rs.getInt("deaths")));
						lore.add("§7Assists: §6" + NumberUtils.addSeperators(rs.getInt("assists")));
						lore.add("");
						lore.add("§7Damage Dealt: §6" + NumberUtils.addSeperators((rs.getDouble("damagedelt")/1000D)) + "k§c❤");
						lore.add("§7Damage Taken: §6" + NumberUtils.addSeperators((rs.getDouble("damagetaken")/1000D)) + "k§c❤");
						lore.add("§7Damage Healed: §6" + NumberUtils.addSeperators((rs.getDouble("damagehealed")/1000D)) + "k§c❤");
						lore.add("");
						lore.add("§7Spells Purchased: §6" + NumberUtils.addSeperators(rs.getString("purchasedspells").split(" - ").length));
						lore.add("§7Spells Casted: §6" + NumberUtils.addSeperators((rs.getDouble("spellscasted")/1000D)) + "k");
						lore.add("§7Distance Traveled: §6" + NumberUtils.addSeperators((rs.getDouble("distancetraveled")/1000D)) + "km");
						lore.add("§7Time Played: §6" + TimeUtils.milisToDurationString(rs.getLong("moshpitstats.timeplayed")));
						meta.setLore(lore);
						item.setItemMeta(meta);
						inventory.setItem(slot, item);

						meta.setLore(lore);
						item.setItemMeta(meta);
						inventory.setItem(slot, item);

					} else {
						ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
						ChatUtils.sendMessage(player, "&cThis player does not exist! Please double check the name.");
						ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
					ChatUtils.sendMessage(player, "&cError while trying to retreive the player's stats!");
					ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
					return;
				} finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(rs);
					Utils.closeQuietly(s);
				}

				breakLoop:{
					for (String check : args) {
						if (check.equalsIgnoreCase("isdirect")) {
							break breakLoop;
						}
					}
					slot = 49;
					item = new ItemStack(Material.ARROW, 1);
					meta = item.getItemMeta();
					meta.addItemFlags(ItemFlag.values());
					meta.setDisplayName("§6§lGo Back");
					item.setItemMeta(meta);
					inventory.setItem(slot, item);
					map.formatSlotDefault(slot, new Runnable() {
						@Override
						public void run() {
							GUIs.openGUI(player, "profile");
							SoundUtils.playGUISoundClick(player);
						}
					});
				}

				Inventory i = inventory;
				Bukkit.getScheduler().runTask(Main.instance, new Runnable() {
					@Override
					public void run() {
						player.openInventory(i);
						map.loadInventory();
					}
				});
			}

		});
	}
}
