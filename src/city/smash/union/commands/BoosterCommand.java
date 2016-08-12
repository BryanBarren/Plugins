package city.smash.union.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import city.smash.union.gui.GUIs;

public class BoosterCommand implements Command {

	@Override
	public void handleCommand(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			ArrayList<String> nArgs = new ArrayList<String>();
			nArgs.add("1");
			nArgs.add("isdirect");
			GUIs.openGUI((Player)sender, "booster", nArgs);
		}
	}

}
