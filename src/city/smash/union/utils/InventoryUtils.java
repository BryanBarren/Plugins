package city.smash.union.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import city.smash.union.scheduler.Gametick;

public class InventoryUtils {

	public static ItemStack unequppiedItem = new ItemStack(Material.FIREWORK_CHARGE, 1);

	public static void refreshHotbar(Player player, boolean isInSpawn) {

		if (!Gametick.isGameTicking(player)) {
			return;
		}
		
		if (DataUtils.getPlayer(player).isVanished()) {
			Inventory inventory = player.getInventory();
			for (int i = 0; i<inventory.getSize(); i++) {
				inventory.clear(i);
			}
		}

		//pregame (pregame layout)
		
		//midgame (dont touch shit)
	}
}
