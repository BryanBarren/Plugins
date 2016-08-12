package city.smash.union.commands;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import city.smash.union.utils.ChatUtils;
import city.smash.union.utils.HeadUtils;

public class HeadutilsCommand implements Command {

	@Override
	public void handleCommand(CommandSender sender, String[] args) {
		
		Player player = null;
		if (sender instanceof Player) {
			player = (Player)sender;
			if (!player.hasPermission("admin")) {
				Bukkit.dispatchCommand(sender, "/fakecommand");
				return;
			}
		} else {
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
			ChatUtils.sendMessage(sender, "No targeted chest, checking hand");

			if (args.length == 1) {
				player.getInventory().addItem(HeadUtils.getSkull(args[0]));
			} else {
				ChatUtils.sendMessage(sender, HeadUtils.getSkullTextureID(player.getInventory().getItemInHand()));
			}
			
			return;
		}
		ChatUtils.sendMessage(sender, "&7&m-----------------------------------------------------");
		Inventory chestinv = ((Chest) t.getState()).getBlockInventory();
		for (int i=0; i<chestinv.getSize(); i++) {
			ItemStack item = chestinv.getItem(i);
			if (item != null) {
				ChatUtils.sendMessage(sender, item.getItemMeta().getDisplayName() + " - " + HeadUtils.getSkullURL(item).split("url\":\"")[1].split("\"")[0]);
			}
		}
		ChatUtils.sendMessage(sender, "&7&m-----------------------------------------------------");
		
	}

}
