package city.smash.union.commands;

import org.bukkit.command.CommandSender;

public interface Command {
	public abstract void handleCommand(CommandSender sender, String[] args);
}