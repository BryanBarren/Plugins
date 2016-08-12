package city.smash.union.utils;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import city.smash.union.classes.SmashInventory;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class GuiItemUtils {

	public static void formatVanityChestItem(int slot, Inventory inventory, SmashInventory map) {
		ItemStack item = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§lAbout Mystery Chests");
		ArrayList<String> lore = new ArrayList<String>();

		lore.add("§7Mystery Chests contain");
		lore.add("§7vanity! You can open");
		lore.add("§7them in spawns and");
		lore.add("§7lobbies. They are earned"); 
		lore.add("§7by playing, voting, referring");
		lore.add("§7other players, and can be");
		lore.add("§7purchased online at");
		lore.add("§astore.smash.city");

		meta.setLore(lore);
		item.setItemMeta(meta);
		
		inventory.setItem(slot, item);
		
		map.formatSlotDefault(slot, new Runnable() {
			@Override
			public void run() {
				map.getPlayer().closeInventory();
				TextComponent message = null;
				TextComponent part;

				part = new TextComponent( "-----------------------------------------------------\n" );
				part.setColor(ChatColor.GRAY);
				part.setBold(false);
				part.setItalic(false);
				part.setStrikethrough(true);
				message = part;

				part = new TextComponent( "Purchase Mystery Chests here!\n" );
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

				ChatUtils.sendRawMessage(map.getPlayer(), message);

				SoundUtils.playGUISoundClick(map.getPlayer());

			}
		});
	}
	
	public static ItemStack getCoinBalanceItem(Player player) {
		ItemStack item = new ItemStack(Material.EMERALD, 1);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		
		meta.setDisplayName("§6§lYour Coin Balance");
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7Your balance: §6" + NumberUtils.addSeperators(MoneyUtils.getCoins(player)) + " coins");
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
