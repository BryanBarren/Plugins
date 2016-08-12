package city.smash.union.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import city.smash.union.utils.ChatUtils;

public class SpeedCommand implements Command {
	@Override
	public void handleCommand(CommandSender sender, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player)sender;
		} else {
			return;
		}
		
		if (!player.hasPermission("staff")) {
			Bukkit.dispatchCommand(sender, "/fakecommand");
			return;
		}
		if (args.length <= 0) {
			ChatUtils.sendMessage(sender, "&cYou must supply a speed to set!");
			return;
		}
		if (player.isFlying()) {
			player.setFlySpeed(Float.parseFloat(args[0]));
		} else {
			if (!player.hasPermission("admin")) {
				ChatUtils.sendMessage(sender, "&c&lYou are not allowed to use for ground speed!");
				return;
			}
			player.setWalkSpeed(Float.parseFloat(args[0]));
		}
	}
}
