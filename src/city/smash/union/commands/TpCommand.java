package city.smash.union.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import city.smash.union.utils.ChatUtils;
import city.smash.union.utils.DataUtils;

public class TpCommand implements Command {
	@Override
	public void handleCommand (CommandSender sender, String[] args) {
		
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
		if (args.length != 1) {
			ChatUtils.sendMessage(sender, "&cInvalid target to teleport to");
			return;
		}
		Player target=Bukkit.getPlayerExact(args[0]);
		if (target == null) {
			ChatUtils.sendMessage(sender, "&cInvalid target to teleport to");
			return;
		}
		if (!player.hasPermission("admin") && !DataUtils.getPlayer(player).isVanished()) {
			ChatUtils.sendMessage(sender, "&cYou may only teleport when vanished. Use /vanish to continue");
			return;
		}
		player.teleport(target);
		ChatUtils.sendMessage(sender, "&aSuccessfuly teleported to " + target.getName());
	}
}