package city.smash.union.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import city.smash.union.Main;
import city.smash.union.utils.DataUtils;

public class TaskcommandCommand implements Command {

	@Override
	public void handleCommand(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			return;
		}
		Player player = ((Player) sender);
		
		UUID _uid;
		try {
			_uid = UUID.fromString(args[0]);
		} catch (Exception e) {
			Bukkit.dispatchCommand(sender, "/fakecommand");
			return;
		}
		
		if (!DataUtils.getPlayer(player).getTaskCommandList().containsKey(_uid)) {
			Bukkit.dispatchCommand(sender, "/fakecommand");
			return;
		}
		
		Bukkit.getScheduler().runTask(Main.instance, DataUtils.getPlayer(player).getTaskCommandList().get(_uid));
		
	}
	
}
