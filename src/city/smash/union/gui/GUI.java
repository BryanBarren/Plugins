package city.smash.union.gui;

import java.util.List;

import org.bukkit.entity.Player;

public abstract class GUI {
	public abstract void openGUI(Player player, List<String> args);
}
