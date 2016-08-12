package city.smash.union.commands;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;

import city.smash.union.utils.ChatUtils;

public class PrintguiCommand implements Command {

	@Override
	public void handleCommand(CommandSender sender, String[] args) {

		Player player = null;
		if (sender instanceof Player) {
			player = (Player)sender;
		} else {
			return;
		}

		if (!player.hasPermission("admin")) {
			Bukkit.dispatchCommand(sender, "/fakecommand");
			return;
		}

		BlockIterator b = new BlockIterator(player, 5);
		Block t = null;
		while (t == null && b.hasNext()) {
			Block tempblock = b.next();
			if (tempblock.getState() instanceof Chest) {
				t = tempblock;
			}
		}
		if (t == null){
			ChatUtils.sendMessage(sender, "No targeted chest");
			return;
		}
		ChatUtils.sendMessage(sender, "&7&m-----------------------------------------------------");
		Inventory chestinv = ((Chest) t.getState()).getBlockInventory();

		player.sendMessage("int slot;");
		player.sendMessage("ItemStack item;");
		player.sendMessage("ItemMeta meta;");
		player.sendMessage("Inventory inventory = Bukkit.createInventory(null, invSize, \"§8Name\");");
		player.sendMessage("SmashInventory map = new SmashInventory(player);");
		player.sendMessage("ArrayList<String> lore;");

		player.sendMessage("");

		for (int i=0; i<chestinv.getSize(); i++) {
			ItemStack item = chestinv.getItem(i);
			if (item != null) {

				player.sendMessage("slot = " + i + ";");
				player.sendMessage("item = new ItemStack(Material." + item.getType().toString() + ", " + item.getAmount() + ");");
				ItemMeta meta = item.getItemMeta();
				if (item.getDurability() != (short)0) {
					player.sendMessage("item.setDurability((short)"+item.getDurability()+");");
				}

				player.sendMessage("meta = item.getItemMeta();");
				player.sendMessage("meta.addItemFlags(ItemFlag.values());");
				player.sendMessage("lore = new ArrayList<String>();");
				if (meta.hasEnchants()) {
					Map<Enchantment, Integer> enchantMap = meta.getEnchants();
					for (Enchantment enchant : enchantMap.keySet()) {
						player.sendMessage("meta.addEnchant(Enchantment." + enchant.getName() + ", " + meta.getEnchants().get(enchant) + " , true);");
					}
				}

				if (meta.hasDisplayName()) {
					player.sendMessage("meta.setDisplayName(\""+meta.getDisplayName().replace("§","&")+"\");");
				} else {
					player.sendMessage("meta.setDisplayName(\"§§§§§§\");");
				}

				if (meta.hasLore()) {
					List<String> lores = meta.getLore();
					for (String lore : lores) {
						player.sendMessage("lore.add(\"" + ChatColor.translateAlternateColorCodes('&', lore) +"\");");
					}
				} else {
					player.sendMessage("lore.add(\"§§§§§§\");");
				}
				
				player.sendMessage("meta.setLore(lore);");
				player.sendMessage("item.setItemMeta(meta);");

				player.sendMessage("inventory.setItem(slot, item);");
				player.sendMessage("map.formatSlotDefault(slot, new Runnable() {");
				player.sendMessage("	@Override");
				player.sendMessage("	public void run() {");
				player.sendMessage("	}");
				player.sendMessage("});");
				player.sendMessage("");
			}
		}
		ChatUtils.sendMessage(sender, "&7&m-----------------------------------------------------");
	}

}
