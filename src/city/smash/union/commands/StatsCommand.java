package city.smash.union.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import city.smash.union.gui.GUIs;
import city.smash.union.utils.ChatUtils;
import city.smash.union.utils.WarningUtils;

public class StatsCommand implements Command {

	@Override
	public void handleCommand(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			
			if (!player.hasPermission("hero")) {
				WarningUtils.noRankWarn(player, "hero");
				return;
			}
			
			if (args.length != 1) {
				ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
				ChatUtils.sendMessage(player, "&cYou must supply a valid playername to look up!");
				ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
				return;
			}
			
			if (args[0].length() > 16) {
				ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
				ChatUtils.sendMessage(player, "&cYou supplied an invalid name to stats check!");
				ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
				return;
			}
			char[] chars = args[0].toCharArray();
			for (char c : chars) {
				if(!Character.isLetterOrDigit(c) && c != '_') {
					ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
					ChatUtils.sendMessage(player, "&cYou supplied an invalid name to stats check!");
					ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
					return;
				}
			}
			
			GUIs.openGUI((Player)sender, "stats", args[0], "isdirect");
		}
	}
}
