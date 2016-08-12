package city.smash.union.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import city.smash.union.Main;
import city.smash.union.classes.SmashInventory;
import city.smash.union.utils.BoosterUtils;
import city.smash.union.utils.ChatUtils;
import city.smash.union.utils.DataUtils;
import city.smash.union.utils.NumberUtils;
import city.smash.union.utils.SoundUtils;
import city.smash.union.utils.TimeUtils;
import city.smash.union.utils.BoosterUtils.BoosterListResult;
import city.smash.union.utils.BoosterUtils.BoosterResult;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class BoosterGUI extends GUI {

	private void addBackArrow(Player player, SmashInventory map, Inventory inventory, List<String> args) {
		
		for (String check : args) {
			if (check.equalsIgnoreCase("isdirect")) {
				return;
			}
		}
		
		int slot = 48;
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
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
	
	@Override
	public void openGUI(Player player, List<String> args) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {
			@Override
			public void run() {

				Inventory inventory;
				SmashInventory map = new SmashInventory(player);
				int slot = 22;
				ItemStack item;
				ItemMeta meta;

				ArrayList<BoosterListResult> boosterList = BoosterUtils.getBoosterList(player);
				if (BoosterUtils.hasBooster(player)) {
					inventory = Bukkit.createInventory(null, 54, "§8Booster currently active!");
					
					item = new ItemStack(Material.POTION, 1);
					
					meta = item.getItemMeta();
					PotionMeta potmeta = (PotionMeta) meta;
					potmeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 0), false);
					
					BoosterResult br = BoosterUtils.getBoosterStatus(player);
					potmeta.setDisplayName("§a§lActive Booster");
					
					potmeta.addItemFlags(ItemFlag.values());
					ArrayList<String> lores = new ArrayList<String>();
					
					String multi;
					if (br.getMulti() == Math.round(br.getMulti())) {
						multi = NumberUtils.addSeperators((int)br.getMulti());
					} else {
						multi = NumberUtils.addSeperators(br.getMulti());
					}

					lores.add("");
					lores.add("§7Multiplier: §6" + multi + "x");
					lores.add("§7Duration: §6" + TimeUtils.milisToDurationString(br.getActiveUntil()-System.currentTimeMillis()));
					
					potmeta.setLore(lores);
					
					item.setItemMeta(potmeta);
					
					inventory.setItem(slot, item);
					map.formatSlotDefault(slot, new Runnable() {
						@Override
						public void run() {
							ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
							ChatUtils.sendMessage(player, "&aThis Booster expires in " + TimeUtils.milisToDurationString(br.getActiveUntil()-System.currentTimeMillis()));
							ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
							SoundUtils.playGUISoundClick(player);
						}
					});

					addBackArrow(player, map, inventory, args);
				}
				else if (boosterList.isEmpty()) {
					inventory = Bukkit.createInventory(null, 54, "§8Sorry, You have no Boosters!");
					item = new ItemStack(Material.BARRIER);
					meta = item.getItemMeta();
					meta.setDisplayName("§c§lYou have no Boosters!");
					ArrayList<String> lores = new ArrayList<String>();
					lores.add("");
					lores.add("§7You may purchase more Boosters");
					lores.add("§7online at §astore.smash.city");
					meta.setLore(lores);
					item.setItemMeta(meta);

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

							part = new TextComponent( "Purchase additional Boosters here!\n" );
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
							part.setStrikethrough(false);
							part.setItalic(true);
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
					inventory.setItem(slot, item);
					
					addBackArrow(player, map, inventory, args);
				} else {
					int listPerPage = 21;
					int page;
					try {
						page = Integer.parseInt(args.get(0));
					} catch (Exception e) {
						page = 1;
					}
					int maxPage = (int) Math.ceil(boosterList.size()/(double)listPerPage);
					int startIndex = (((page-1)*listPerPage));
					
					if (page > maxPage) {
						page = maxPage;
						startIndex = (((page-1)*listPerPage));
					}
					inventory = Bukkit.createInventory(null, 54, "§8Your Boosters (Page "+ page + "/" + maxPage + ")");
					
					slot = 10;
					for (int i = 0 ; i < listPerPage && i+startIndex < boosterList.size(); i++) {
						BoosterListResult booster = boosterList.get(i + startIndex);

						item = new ItemStack(Material.POTION, 1);
						
						PotionMeta potmeta = (PotionMeta) item.getItemMeta();
						
						potmeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 0), false);
						
						potmeta.addItemFlags(ItemFlag.values());
						String multi;
						if (booster.getMulti() == Math.round(booster.getMulti())) {
							multi = Integer.toString((int)booster.getMulti());
						} else {
							multi = Double.toString(booster.getMulti());
						}
						
						potmeta.setDisplayName("§a§lActivate Booster");
						ArrayList<String> lores = new ArrayList<String>();
						lores.add("");
						lores.add("§7Multiplier: §6" + multi + "x");
						lores.add("§7Duration: §6" + TimeUtils.milisToDurationString(booster.getDuration()));
						lores.add("");
						lores.add("§7» §a§lClick to Activate §7«");
						
						potmeta.setLore(lores);
						item.setItemMeta(potmeta);
						
						inventory.setItem(slot, item);
						
						map.formatSlotDefault(slot, new Runnable() {
							@Override
							public void run() {
								Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {
									@Override
									public void run() {
										DataUtils.getPlayer(player).setClaiming(true);
										BoosterUtils.activateBooster(player, booster.getUUID());
										DataUtils.getPlayer(player).setClaiming(false);
										Bukkit.getScheduler().runTask(Main.instance, new Runnable() {
											@Override
											public void run() {
												GUIs.openGUI(player, "booster", args);
												SoundUtils.playGUISoundClick(player);
											}
										});
									}
								});
							}
						});
						
						if ((slot + 2)%9==0) {
							slot+=3;
						} else {
							slot++;
						}
					}
					
					final int p = page;
					addBackArrow(player, map, inventory, args);
					if (page != 1) {
						slot = 45;

						item = new ItemStack(Material.PAPER);
						meta = item.getItemMeta();
						meta.setDisplayName("§6§lPrevious Page");
						item.setItemMeta(meta);
						inventory.setItem(slot, item);
						map.formatSlotDefault(slot, new Runnable() {
							@Override
							public void run() {
								ArrayList<String> nArgs = new ArrayList<String>();
								nArgs.add(Integer.toString(p-1));
								if (args.contains("isdirect")) {
									nArgs.add("isdirect");
								}
								GUIs.openGUI(player, "booster", nArgs);
								SoundUtils.playGUISoundClick(player);
							}
						});
					}
					if (page != maxPage) {
						slot = 53;
						
						item = new ItemStack(Material.PAPER);
						meta = item.getItemMeta();
						meta.setDisplayName("§6§lNext Page");
						item.setItemMeta(meta);
						inventory.setItem(slot, item);
						map.formatSlotDefault(slot, new Runnable() {
							@Override
							public void run() {
								ArrayList<String> nArgs = new ArrayList<String>();
								nArgs.add(Integer.toString(p+1));
								if (args.contains("isdirect")) {
									nArgs.add("isdirect");
								}
								GUIs.openGUI(player, "booster", nArgs);
								SoundUtils.playGUISoundClick(player);
							}
						});
					}
				}
				
				slot = 49;
				item = new ItemStack(Material.GOLD_INGOT);
				meta = item.getItemMeta();
				meta.setDisplayName("§6§lPurchase additional Boosters");
				ArrayList<String> lore = new ArrayList<String>();
				
				lore.add("§7Purchase additional Boosters");
				lore.add("§7online at §astore.smash.city");

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

						part = new TextComponent( "Purchase additional Boosters here!\n" );
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
