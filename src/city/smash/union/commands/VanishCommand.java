package city.smash.union.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import city.smash.union.utils.ChatUtils;
import city.smash.union.utils.DataUtils;
import city.smash.union.utils.VanishUtils;

public class VanishCommand implements Command {
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
		if (DataUtils.getPlayer(player).isVanished()) {
			ChatUtils.sendMessage(sender, "&7&m-----------------------------------------------------");
			ChatUtils.sendMessage(sender, "&cUnvanished");
			ChatUtils.sendMessage(sender, "&7&m-----------------------------------------------------");
			DataUtils.getPlayer(player).setVanished(false);

			VanishUtils.vanishRecheck(player);
			player.setAllowFlight(false);
		} else {
			ChatUtils.sendMessage(sender, "&7&m-----------------------------------------------------");
			ChatUtils.sendMessage(sender, "&aVanished");
			ChatUtils.sendMessage(sender, "&7&m-----------------------------------------------------");
			DataUtils.getPlayer(player).setVanished(true);

			VanishUtils.vanishRecheck(player);
			player.setAllowFlight(true);
		}
	}
}
