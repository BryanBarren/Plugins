package city.smash.union.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import city.smash.union.utils.ChatUtils;

public class ServerusageCommand implements Command {

	@Override
	public void handleCommand(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			if (!sender.hasPermission("admin")) {
				Bukkit.dispatchCommand(sender, "/fakecommand");
			}
		}

		ChatUtils.sendMessage(sender, "&7&m-----------------------------------------------------");
		ChatUtils.sendMessage(sender, "&eShowing Uptime for Server");
		ChatUtils.sendMessage(sender, "");
		ChatUtils.sendMessage(sender, "&7Free ram: &6" + String.valueOf(Runtime.getRuntime().freeMemory() / 1024L / 1024L) + "MB");
		ChatUtils.sendMessage(sender, "&7Max Ram: &6" + String.valueOf(Runtime.getRuntime().maxMemory() / 1024L / 1024L) + "MB");
		ChatUtils.sendMessage(sender, "&7Cores Active: &6" + String.valueOf(Runtime.getRuntime().availableProcessors()));
		ChatUtils.sendMessage(sender, "&7&m-----------------------------------------------------");
	}

}
