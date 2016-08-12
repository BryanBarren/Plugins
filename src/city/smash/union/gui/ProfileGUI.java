package city.smash.union.gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.Utils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import city.smash.union.Main;
import city.smash.union.classes.SmashInventory;
import city.smash.union.utils.ChatUtils;
import city.smash.union.utils.DataUtils;
import city.smash.union.utils.HeadUtils;
import city.smash.union.utils.JedisUtils;
import city.smash.union.utils.RankUtils;
import city.smash.union.utils.SoundUtils;
import city.smash.union.utils.TimeUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ProfileGUI extends GUI {

	@Override
	public void openGUI(Player player, List<String> args) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable(){
			@Override
			public void run() {
				int slot;
				ItemStack item;
				ItemMeta meta;
				Inventory inventory = Bukkit.createInventory(null, 36, "§8Your Profile");
				SmashInventory map = new SmashInventory(player);
				ArrayList<String> lore;

				slot = 11;
				item = new ItemStack(Material.POTION, 1);
				item = new ItemStack(Material.POTION, 1);
				PotionMeta potmeta = (PotionMeta) item.getItemMeta();;
				potmeta.setMainEffect(PotionEffectType.INCREASE_DAMAGE);
				
				lore = new ArrayList<String>();
				potmeta.setDisplayName("§a§lBoosters");
				lore.add("§7Boosters temporarily increases");
				lore.add("§7your coin multiplier.");
				lore.add("§7You can purchase them on the");
				lore.add("§7server store @ §astore.smash.city");
				lore.add("");
				lore.add("§7» §a§lClick to view your Boosters §7«");
				potmeta.setLore(lore);
				item.setItemMeta(potmeta);
				inventory.setItem(slot, item);
				map.formatSlotDefault(slot, new Runnable() {
					@Override
					public void run() {
						GUIs.openGUI(player, "booster");
						SoundUtils.playGUISoundClick(player);
					}
				});

				slot = 12;
				item = new ItemStack(Material.COMMAND, 1);
				meta = item.getItemMeta();
				lore = new ArrayList<String>();
				meta.setDisplayName("§a§lPersonal Settings");
				lore.add("§7Customize your playing");
				lore.add("§7experiance to suit your");
				lore.add("§7liking.");
				lore.add("");
				lore.add("§7» §a§lClick to view your Settings §7«");
				meta.setLore(lore);
				item.setItemMeta(meta);
				inventory.setItem(slot, item);
				map.formatSlotDefault(slot, new Runnable() {
					@Override
					public void run() {
						GUIs.openGUI(player, "settings");
						SoundUtils.playGUISoundClick(player);
					}
				});

				slot = 13;
				item = new ItemStack(Material.GOLD_INGOT, 1);
				meta = item.getItemMeta();
				lore = new ArrayList<String>();
				meta.setDisplayName("§a§lSupport the Server");
				lore.add("§7You can purchase Ranks,");
				lore.add("§7Coins, and Boosters on");
				lore.add("§7our server store. All");
				lore.add("§7donations will help keep");
				lore.add("§7the server alive running!");
				lore.add("");
				lore.add("§a§lHero Benefits:");
				lore.add("§7Earn more coins, and share it with others");
				lore.add("§7Prefix next to your name in chat");
				lore.add("§7Access to more Gifts from the Kind Villager");
				lore.add("§7No chat restrictions");
				lore.add("§7Start your own guild");
				lore.add("");
				lore.add("§7» §a§lClick to visit the store §7«");
				meta.setLore(lore);
				item.setItemMeta(meta);
				inventory.setItem(slot, item);
				map.formatSlotDefault(slot, new Runnable() {
					@Override
					public void run() {
						SoundUtils.playGUISoundClick(player);
						player.closeInventory();
						TextComponent message = null;
						TextComponent part;

						part = new TextComponent( "-----------------------------------------------------\n" );
						part.setColor(ChatColor.GRAY);
						part.setBold(false);
						part.setItalic(false);
						part.setStrikethrough(true);
						message = part;

						part = new TextComponent( "Upgrade your rank for exclusive perks today!\n" );
						part.setColor(ChatColor.GOLD);
						part.setBold(true);
						part.setItalic(false);
						part.setStrikethrough(false);
						part.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to visit the server store").color(ChatColor.GOLD).bold(true).create()));
						part.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://store.smash.city"));
						message.addExtra(part);

						part = new TextComponent( "(Click this to visit the server store)\n" );
						part.setColor(ChatColor.GRAY);
						part.setBold(false);
						part.setItalic(true);
						part.setStrikethrough(false);
						part.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to visit the server store").color(ChatColor.GOLD).bold(true).create()));
						part.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://store.smash.city"));
						message.addExtra(part);

						part = new TextComponent( "-----------------------------------------------------" );
						part.setColor(ChatColor.GRAY);
						part.setBold(false);
						part.setStrikethrough(true);
						part.setItalic(false);
						message.addExtra(part);

						ChatUtils.sendRawMessage(player, message);
					}
				});

				slot = 14;
				
				item = new ItemStack(Material.BANNER, 1);
				BannerMeta bmeta = (BannerMeta) item.getItemMeta();
				bmeta.setBaseColor(DyeColor.RED);
				
				lore = new ArrayList<String>();
				bmeta.setDisplayName("§a§lGuild Info");
				lore.add("§7Guilds allow you to");
				lore.add("§7communicate, chat,");
				lore.add("§7and play with one another.");
				lore.add("");
				lore.add("§a§lGuild Stats:");
				
				String GUID = null;
				String GuildName = null;
				String query = null;
				String GuildTag = null;
				String GuildOpen = null;
				String ReqRank = null;
				String GuildRank = null;
				long JoinDate = 0;
				long CreateDate = 0;
				int GuildSize = 0;
				int GuildOnline = 0;
				int GuildMaxSize = 200;

				Connection connection = null;
				ResultSet rs = null;
				Statement s = null;
				tryBlock: try {
					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
					s = connection.createStatement();

					query = "select * from guild where type='guild' and guid=(select guid from guild where type='player' and name='" + player.getUniqueId().toString() + "')";
					rs = s.executeQuery(query);
					if (!rs.next()) {
						lore.add("§7You are not in a");
						lore.add("§7Guild. You may create");
						lore.add("§7a Guild now by using");
						lore.add("§e/guild create [guild name]");
						break tryBlock;
					} else {
						rs.previous();
						while (rs.next()) {
							if (rs.getString("type").equalsIgnoreCase("guild")) {
								GuildName = rs.getString("name");
								GuildOpen = rs.getString("joinmode");
								GuildTag = rs.getString("tag");
								ReqRank = rs.getString("reqrank");
								CreateDate = rs.getLong("firstjoin");
								GUID = rs.getString("guid");
							}
						}
					}
					Utils.closeQuietly(rs);

					query = "select * from guild where type='player' and guid='" + GUID + "'";

					rs = s.executeQuery(query);
					if (!rs.next()) {
						lore.add("§7You are not in a");
						lore.add("§7Guild. You may create");
						lore.add("§7a Guild now by using");
						lore.add("§e/guild create [guild name]");
						break tryBlock;
					} else {
						rs.previous();
						while (rs.next()) {
							if (!JedisUtils.isOnline(rs.getString("name"))) {
								GuildOnline++;
							}
							if (rs.getString("Name").equalsIgnoreCase(player.getUniqueId().toString())) {
								GuildRank = rs.getString("Permission");
								JoinDate = rs.getLong("Firstjoin");
							}
						}
					}

					lore.add("§7Guild Name: §a" + GuildName);
					if (GuildTag != null) {
						lore.add("§7Guild Tag: §a" + GuildTag);
					} else {
						lore.add("§7Guild Tag: §cNot Set");
					}
					lore.add("§7Guild Position: §a" + GuildRank.toUpperCase().substring(0, 1) + GuildRank.toLowerCase().substring(1, GuildRank.length()));
					lore.add("");
					lore.add("§7Guild Capacity: §a" + GuildSize + "/"+ GuildMaxSize);
					lore.add("§7Online Members: §a" + GuildOnline + "/"+ GuildSize);
					lore.add("§7Founding Date: §a" + TimeUtils.milisToDateString(CreateDate));
					lore.add("§7Joined on: §a" + TimeUtils.milisToDateString(JoinDate));
					
					lore.add("§7Guild Status: §a" + GuildOpen.toUpperCase().substring(0, 1) + GuildOpen.toLowerCase().substring(1, GuildOpen.length()));
					if (GuildOpen.equalsIgnoreCase("open")) {
						if (ReqRank == null) {
							lore.add("§7Guild Requirements: §cNone");
						}
						else {
							lore.add("§7Guild Requirements: " + RankUtils.getColorOfRank(ReqRank) + ReqRank.toUpperCase() + "§7+");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(rs);
					Utils.closeQuietly(s);
				}
				map.formatSlotDefault(slot, new Runnable() {
					@Override
					public void run() {
						SoundUtils.playGUISoundClick(player);
						JedisUtils.publishCommand("bungee", "fakechat-"+player.getUniqueId().toString()+" guild help");
						player.closeInventory();
					}
				});
				
				bmeta.setLore(lore);
				item.setItemMeta(bmeta);
				inventory.setItem(slot, item);
 
				slot = 15;
				item = new ItemStack(Material.PAPER, 1);
				meta = item.getItemMeta();
				lore = new ArrayList<String>();
				meta.setDisplayName("§a§lStats Viewer");
				lore.add("§7Shows your stats of all gamemodes.");
				lore.add("§a§lHERO§a+ §7can check the");
				lore.add("§7stats of any player by using");
				lore.add("§e/stats [player name]");
				lore.add("§7");
				lore.add("§7» §a§lClick to view your stats §7«");
				meta.setLore(lore);
				item.setItemMeta(meta);
				inventory.setItem(slot, item);
				map.formatSlotDefault(slot, new Runnable() {
					@Override
					public void run() {
						SoundUtils.playGUISoundClick(player);
						GUIs.openGUI(player, "stats");
					}
				});

				slot = 22;
				item = HeadUtils.getSkull(DataUtils.getPlayer(player).getSkinID());
				meta = item.getItemMeta();
				lore = new ArrayList<String>();
				meta.setDisplayName("§a§lPlayer Info");
				lore.add("§7Rank: " + RankUtils.getColorOfRank(RankUtils.getRank(player)) + RankUtils.getRank(player).toUpperCase());

				lore.add("");
				
				try {
					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
					s = connection.createStatement();
					query = "select firstjoin, globalstats.timeplayed from globalstats left join moshpitstats on moshpitstats.uuid=globalstats.uuid where globalstats.uuid='"+player.getUniqueId().toString()+"'";
					rs = s.executeQuery(query);
					
					if (rs.next()) {
						lore.add("§7Player Since: §6" + TimeUtils.milisToDateString(rs.getLong("firstjoin")));
						lore.add("§7Total Play Time: §6" + TimeUtils.milisToDurationString(rs.getLong("globalstats.timeplayed")));
					}

				}
				catch (Exception e) {
					lore.add("§cThere was an error getting your information!");
					e.printStackTrace();
				} finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(rs);
					Utils.closeQuietly(s);
				}
				
				meta.setLore(lore);
				item.setItemMeta(meta);
				inventory.setItem(slot, item);

				Bukkit.getScheduler().runTask(Main.instance, new Runnable() {
					@Override
					public void run() {
						player.openInventory(inventory);
						map.loadInventory();
					}
				});
			}
		});
	}
}
