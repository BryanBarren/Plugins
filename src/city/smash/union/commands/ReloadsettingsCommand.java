package city.smash.union.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import city.smash.union.Variables;
import city.smash.union.utils.BossBarUtils;
import city.smash.union.utils.ChatUtils;
import city.smash.union.utils.TablistUtils;
import city.smash.union.utils.TitleUtils;

public class ReloadsettingsCommand implements Command {
	@Override
	public void handleCommand(CommandSender sender, String[] args) {
		if (sender instanceof Player && !sender.hasPermission("admin")) {
			Bukkit.dispatchCommand(sender, "/fakecommand");
			return;
		}
		ChatUtils.sendMessage(sender, "&aReload complete!");
		Variables.loadInfoFromMysql();
		BossBarUtils.barCache.clear();
		TablistUtils.header = null;
		TablistUtils.footer = null;
		TitleUtils.loadLoginTitle();
	}
}