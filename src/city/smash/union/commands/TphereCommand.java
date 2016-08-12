package city.smash.union.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import city.smash.union.utils.ChatUtils;

public class TphereCommand implements Command {
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
		if (args.length != 1) {
			ChatUtils.sendMessage(sender, "&cInvalid target to summon");
			return;
		}
		Player target=Bukkit.getPlayerExact(args[0]);
		if (target == null) {
			ChatUtils.sendMessage(sender, "&cInvalid target to summon");
			return;
		}
		target.teleport(player);
		ChatUtils.sendMessage(sender, "&aSuccessfuly summoned " + target.getName());
	}
}