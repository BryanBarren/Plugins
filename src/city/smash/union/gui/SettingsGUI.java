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

import city.smash.union.classes.SmashGlobalSetting;
import city.smash.union.classes.SmashInventory;
import city.smash.union.classes.SmashSetting;
import city.smash.union.utils.HeadUtils;
import city.smash.union.utils.SettingsUtils;
import city.smash.union.utils.SoundUtils;
import city.smash.union.utils.WarningUtils;

public class SettingsGUI extends GUI {

	private void addBackArrow(Player player, SmashInventory map, Inventory inventory, List<String> args, int slot) {
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§a§lGo Back");
		item.setItemMeta(meta);
		inventory.setItem(slot, item);
		map.formatSlotDefault(slot, new Runnable() {
			@Override
			public void run() {
				if (args.isEmpty() || args.get(0).equalsIgnoreCase("main")) {
					GUIs.openGUI(player, "profile");
				} else {
					if (args.contains("isdirect")) {
						GUIs.openGUI(player, "settings", "main", "isdirect");
					} else {
						GUIs.openGUI(player, "settings", "main");
					}
				}
				SoundUtils.playGUISoundClick(player);
			}
		});
	}

	@Override
	public void openGUI(Player player, List<String> args) {

		SmashInventory map = new SmashInventory(player);
		if (args.isEmpty() || args.get(0).equalsIgnoreCase("main")) {
			int slot;
			ItemStack item;
			ItemMeta meta;
			Inventory inventory = null;
			if (args.contains("isdirect")) {
				inventory = Bukkit.createInventory(null, 45, "§8Settings");
			} else {
				inventory = Bukkit.createInventory(null, 54, "§8Settings");
				addBackArrow(player, map, inventory, args, 49);
			}
			ArrayList<String> lore;

			{
				slot = 20;
				item = HeadUtils.getSkull("2e2cc42015e6678f8fd49ccc01fbf787f1ba2c32bcf559a015332fc5db50");
				meta = item.getItemMeta();
				meta.addItemFlags(ItemFlag.values());
				lore = new ArrayList<String>();
				meta.setDisplayName("§a§lGlobal Settings");
				lore.add("§7Change your Settings");
				lore.add("");
				lore.add("§7» §a§lClick to view your Settings §7«");
				meta.setLore(lore);
				item.setItemMeta(meta);
				inventory.setItem(slot, item);
				map.formatSlotDefault(slot, new Runnable() {
					@Override
					public void run() {
						if (args.contains("isdirect")) {
							GUIs.openGUI(player, "settings", "global", "isdirect");
						} else {
							GUIs.openGUI(player, "settings", "global");
						}
						SoundUtils.playGUISoundClick(player);
					}
				});
			}

			{
				slot = 24;
				item = HeadUtils.getSkull("c3687e25c632bce8aa61e0d64c24e694c3eea629ea944f4cf30dcfb4fbce071");
				meta = item.getItemMeta();
				meta.addItemFlags(ItemFlag.values());
				lore = new ArrayList<String>();
				meta.setDisplayName("§a§lLegends of MC Settings");
				lore.add("§7Change your Settings");
				lore.add("");
				lore.add("§7» §a§lClick to view your Settings §7«");
				meta.setLore(lore);
				item.setItemMeta(meta);
				inventory.setItem(slot, item);
				map.formatSlotDefault(slot, new Runnable() {
					@Override
					public void run() {
						if (args.contains("isdirect")) {
							GUIs.openGUI(player, "settings", "local", "isdirect");
						} else {
							GUIs.openGUI(player, "settings", "local");
						}
						SoundUtils.playGUISoundClick(player);
					}
				});
			}
			player.openInventory(inventory);
			map.loadInventory();
		}
		else if (args.get(0).equalsIgnoreCase("global")) {
			int slot;
			ItemStack item;
			ItemMeta meta;
			Inventory inventory = Bukkit.createInventory(null, 45, "§8Global Settings");

			ArrayList<String> lore;
			{
				slot = 10;

				SmashSetting setting = SmashGlobalSetting.CHAT_GLOBAL;
				item = new ItemStack(Material.PAPER, 1);
				meta = item.getItemMeta();
				meta.addItemFlags(ItemFlag.values());
				lore = new ArrayList<String>();
				meta.setDisplayName("§a§lGlobal Chat");
				lore.add("§7Toggles the visibility");
				lore.add("§7of public chat.");
				meta.setLore(lore);
				item.setItemMeta(meta);
				inventory.setItem(slot, item);

				slot += 9;
				item = new ItemStack(Material.INK_SACK, 1);
				meta = item.getItemMeta();

				lore = new ArrayList<String>();
				meta.setDisplayName("§a§lToggle Global Chat");
				if (SettingsUtils.isEnabled(player, setting)) {
					item.setDurability((short)10);
					lore.add("§7Current Setting: §aEnabled");
				} else {
					item.setDurability((short)8);
					lore.add("§7Current Setting: §cDisabled");
				}

				meta.addItemFlags(ItemFlag.values());
				lore.add("");
				lore.add("§7» §a§lClick to toggle §7«");
				meta.setLore(lore);
				item.setItemMeta(meta);
				inventory.setItem(slot, item);
				map.formatSlotDefault(slot, new Runnable() {
					@Override
					public void run() {
						SettingsUtils.flipSetting(player, setting);
						GUIs.openGUI(player, "settings", args);
						SoundUtils.playGUISoundClick(player);
					}
				});
			}
			{
				slot = 12;
				SmashSetting setting = SmashGlobalSetting.CHAT_BROADCAST;
				item = new ItemStack(Material.SIGN, 1);
				meta = item.getItemMeta();
				meta.addItemFlags(ItemFlag.values());
				lore = new ArrayList<String>();
				meta.setDisplayName("§a§lBroadcasts");
				lore.add("§7Toggles the visibility");
				lore.add("§7of automated broadcasts.");
				meta.setLore(lore);
				item.setItemMeta(meta);
				inventory.setItem(slot, item);

				slot += 9;
				item = new ItemStack(Material.INK_SACK, 1);
				meta = item.getItemMeta();

				lore = new ArrayList<String>();
				meta.setDisplayName("§a§lToggle Broadcasts");
				if (SettingsUtils.isEnabled(player, setting)) {
					item.setDurability((short)10);
					lore.add("§7Current Setting: §aEnabled");
				} else {
					item.setDurability((short)8);
					lore.add("§7Current Setting: §cDisabled");
				}

				meta.addItemFlags(ItemFlag.values());
				lore.add("");
				lore.add("§7» §a§lClick to toggle §7«");
				meta.setLore(lore);
				item.setItemMeta(meta);
				inventory.setItem(slot, item);
				map.formatSlotDefault(slot, new Runnable() {
					@Override
					public void run() {

						if (!player.hasPermission("overlord") && !player.hasPermission("helper") && !player.hasPermission("builder")) {
							if (WarningUtils.shouldWarn(player, "settings-broadcast-error")) {
								WarningUtils.noRankWarn(player, "overlord");
							} else {
								return;
							}
							SettingsUtils.setEnabled(player, setting, true);
							GUIs.openGUI(player, "settings", args);
							return;
						}

						SettingsUtils.flipSetting(player, setting);
						GUIs.openGUI(player, "settings", args);
						SoundUtils.playGUISoundClick(player);
					}
				});
			}
			{
				slot = 14;
				SmashSetting setting = SmashGlobalSetting.CHAT_NAME_ALERT;
				item = HeadUtils.getSkull("d295a929236c1779eab8f57257a86071498a4870196941f4bfe1951e8c6ee21a");
				item.setDurability((short)3);
				meta = item.getItemMeta(); 
				meta.addItemFlags(ItemFlag.values());
				lore = new ArrayList<String>();
				meta.setDisplayName("§a§lChat Alerts");
				lore.add("§7Toggles the alert when you are");
				lore.add("§7mentioned in global chat.");
				meta.setLore(lore);
				item.setItemMeta(meta);
				inventory.setItem(slot, item);

				slot += 9;
				item = new ItemStack(Material.INK_SACK, 1);
				meta = item.getItemMeta();

				lore = new ArrayList<String>();
				meta.setDisplayName("§a§lToggle Chat Alerts");
				if (SettingsUtils.isEnabled(player, setting)) {
					item.setDurability((short)10);
					lore.add("§7Current Setting: §aEnabled");
				} else {
					item.setDurability((short)8);
					lore.add("§7Current Setting: §cDisabled");
				}

				meta.addItemFlags(ItemFlag.values());
				lore.add("");
				lore.add("§7» §a§lClick to toggle §7«");
				meta.setLore(lore);
				item.setItemMeta(meta);
				inventory.setItem(slot, item);
				map.formatSlotDefault(slot, new Runnable() {
					@Override
					public void run() {
						SettingsUtils.flipSetting(player, setting);
						GUIs.openGUI(player, "settings", args);
						SoundUtils.playGUISoundClick(player);
					}
				});
			}
			{
				slot = 16;
				SmashSetting setting = SmashGlobalSetting.CHAT_PRIVATE;
				item = new ItemStack(Material.NAME_TAG, 1);
				meta = item.getItemMeta();
				meta.addItemFlags(ItemFlag.values());

				lore = new ArrayList<String>();
				meta.setDisplayName("§a§lPrivate Messages");
				lore.add("§7Toggles the ability to send");
				lore.add("§7and recieve private messages");
				meta.setLore(lore);

				item.setItemMeta(meta);
				inventory.setItem(slot, item);

				slot += 9;
				item = new ItemStack(Material.INK_SACK, 1);
				meta = item.getItemMeta();

				lore = new ArrayList<String>();
				meta.setDisplayName("§a§lToggle Private Messages");
				if (SettingsUtils.isEnabled(player, setting)) {
					item.setDurability((short)10);
					lore.add("§7Current Setting: §aEnabled");
				} else {
					item.setDurability((short)8);
					lore.add("§7Current Setting: §cDisabled");
				}

				meta.addItemFlags(ItemFlag.values());
				lore.add("");
				lore.add("§7» §a§lClick to toggle §7«");
				meta.setLore(lore);
				item.setItemMeta(meta);
				inventory.setItem(slot, item);
				map.formatSlotDefault(slot, new Runnable() {
					@Override
					public void run() {
						SettingsUtils.flipSetting(player, setting);
						GUIs.openGUI(player, "settings", args);
						SoundUtils.playGUISoundClick(player);
					}
				});
			}

			addBackArrow(player, map, inventory, args, 40);

			player.openInventory(inventory);
			map.loadInventory();
		}

		else if (args.get(0).equalsIgnoreCase("local")) {
			//todo
		}

	}
}
