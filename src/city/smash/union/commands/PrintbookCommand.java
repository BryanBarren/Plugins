package city.smash.union.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

import city.smash.union.utils.ChatUtils;

public class PrintbookCommand implements Command {

	@Override
	public void handleCommand(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (player.getInventory().getItemInHand().getType() == Material.WRITTEN_BOOK) {
				BookMeta meta = (BookMeta) player.getInventory().getItemInHand().getItemMeta();
				ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
				for (String page : meta.getPages()) {
					player.sendMessage("pages.add(\"" + page.replace("§", "^").replace("\n", "\\n") + "\");");
				}
				ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
			}
		}
	}
}
