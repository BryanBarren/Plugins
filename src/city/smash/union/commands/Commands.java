package city.smash.union.commands;

import java.util.HashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import city.smash.union.classes.SmashGame;

public class Commands implements CommandExecutor, Listener {
	@Override
	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		//testcode
		if (command.getName().equalsIgnoreCase("ex")) {
			SmashGame.getGameInstance().startGame();
			return true;
		}
		
		getCommandByName(command.getName()).handleCommand(player, args);
		return true;
	}
	private static HashMap<String, city.smash.union.commands.Command> commandMap = new HashMap<String, city.smash.union.commands.Command>();

	public static city.smash.union.commands.Command getCommandByName(String commandName) {
		if (!commandMap.containsKey(commandName)) {
			String spellClass = Commands.class.getPackage().getName() + "." + commandName.substring(0, 1).toUpperCase() + commandName.substring(1).toLowerCase() + "Command";
			try {
				commandMap.put(commandName, (city.smash.union.commands.Command) Class.forName(spellClass).newInstance());
			} catch (Exception e) {
				System.out.println("Spell lookup reflection failure for spell:" + commandName);
			}
		}
		return commandMap.get(commandName);
	}
}