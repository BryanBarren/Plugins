package city.smash.union.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import city.smash.union.Main;
import city.smash.union.utils.JedisUtils;

public class HandleviolationCommand implements Command {
	@Override
	public void handleCommand(CommandSender sender, String[] args) {

		if (sender instanceof Player) {
			Bukkit.dispatchCommand(sender, "/fakecommand");
			return;
		}
		
		String message = "NCPViolation-";
		for (String part : args) {
			message += part + " ";
		}
		message += Main.getServerName();
				
		JedisUtils.publishCommand("bungee", message);
		
	}
}