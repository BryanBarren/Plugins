package city.smash.union.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import city.smash.union.gui.GUIs;

public class ProfileCommand implements Command {
	@Override
	public void handleCommand(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			GUIs.openGUI((Player)sender, "profile");
		}
	}
}
