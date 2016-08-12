package city.smash.union.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import city.smash.union.utils.ChatUtils;

public class HelpCommand implements Command {

	@Override
	public void handleCommand(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
			ChatUtils.sendMessage(player, "&a&lServer Help");
			ChatUtils.sendMessage(player, "");
			ChatUtils.sendMessage(player, "&7Check out the tutorial book in spawn to learn how to play");
			ChatUtils.sendMessage(player, "&aLegends of MC&7. Visit our forums for help from the community at");
			ChatUtils.sendMessage(player, "§ewww.smash.city&7. Additionally, if you need any assistance");
			ChatUtils.sendMessage(player, "&7regarding a purchase, email us at §esupport@smash.city");
			ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
		}
	}
}
