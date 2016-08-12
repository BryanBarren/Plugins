package city.smash.union.commands;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import city.smash.union.utils.ChatUtils;

public class DyecolorCommand implements Command {

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
		
		if (!player.getInventory().getItemInHand().getType().name().contains("LEATHER_")) {
			ChatUtils.sendMessage(player, "&cYou cannot dye/check this item.");
			return;
		}
		if (args.length != 1) {
			ItemStack item = player.getInventory().getItemInHand();
			LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
			ChatUtils.sendMessage(player, "&aThe item color of this item is " + meta.getColor().asRGB());
			return;
		}
		int color;
		try {
			color = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			ChatUtils.sendMessage(player, "&cYou provided an invalid color!");
			return;
		}
		
		ItemStack item = player.getInventory().getItemInHand();
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(color));
		item.setItemMeta(meta);
		player.getInventory().setItemInHand(item);
		ChatUtils.sendMessage(player, "&aItem Dyed!");
	}

}
