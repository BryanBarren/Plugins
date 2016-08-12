package city.smash.union.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import city.smash.union.Main;

public class DroppedItemUtils {
	public static Item dropItemWithRemoval(ItemStack item, Location loc, Vector vec, Long delay) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("tagged");
		item.setItemMeta(meta);
		Item droppedItem = loc.getWorld().dropItemNaturally(loc, item);
		droppedItem.setVelocity(vec);
		droppedItem.setPickupDelay(99999999);
		Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
			@Override
			public void run() {
				droppedItem.remove();
			}
		}, delay);
		return droppedItem;
	}
}
