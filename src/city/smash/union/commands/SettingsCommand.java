package city.smash.union.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import city.smash.union.gui.GUIs;

public class SettingsCommand implements Command {

	@Override
	public void handleCommand(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			GUIs.openGUI(player, "settings", "main", "isdirect");
		}
	}
}
