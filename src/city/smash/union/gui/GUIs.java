package city.smash.union.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import city.smash.union.Main;
import city.smash.union.utils.DataUtils;

public class GUIs implements Listener {

	public static void openGUI(final Player player, final String guiName) {
		DataUtils.getPlayer(player).getChestMap().clear();
		getGuiByName(guiName).openGUI(player, new ArrayList<String>());
	}
	public static void openGUI(final Player player, final String guiName, String...args) {
		DataUtils.getPlayer(player).getChestMap().clear();
		getGuiByName(guiName).openGUI(player, Arrays.asList(args));
	}
	public static void openGUI(final Player player, final String guiName, List<String> args) {
		DataUtils.getPlayer(player).getChestMap().clear();
		getGuiByName(guiName).openGUI(player, args);
	}

	private static HashMap<String, GUI> guiMap = new HashMap<String, GUI>();

	public static GUI getGuiByName(String guiName) {
		if (!guiMap.containsKey(guiName)) {
			String spellClass = GUIs.class.getPackage().getName() + "." + guiName.substring(0, 1).toUpperCase() + guiName.substring(1).toLowerCase() + "GUI";
			try {
				guiMap.put(guiName, (GUI) Class.forName(spellClass).newInstance());
			} catch (Exception e) {
				System.out.println("GUI lookup failure for gui:" + guiName);
			}
		}
		return guiMap.get(guiName);
	}

	/**
	 * Formats slot of player's inventory.
	 * @param player Player to format slot of
	 * @param slot Slot ID
	 * @param typeOfCommand Type of command to run on click. For full list, check GUI.class
	 * @param command Command/args to run under typeOfCommand. For more info, check GUI.class
	 */

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();

		if (!DataUtils.getPlayer(player).getChestMap().isEmpty()) {
			event.setCancelled(true);
			if (player.getOpenInventory() != null && player.getOpenInventory().getType() == InventoryType.CHEST) {
				if (event.getInventory() != null && event.getInventory().getType() == InventoryType.CHEST) {
					if (DataUtils.getPlayer(player).getChestMap().containsKey(event.getSlot()) && DataUtils.getPlayer(player).getChestMap().get(event.getSlot()).containsKey(event.getClick())) {
						Bukkit.getScheduler().runTask(Main.instance, DataUtils.getPlayer(player).getChestMap().get(event.getSlot()).get(event.getClick()));
					}
				}
			}
		}
	}

	@EventHandler
	public void onClose(final InventoryCloseEvent event) {
		DataUtils.getPlayer((Player) event.getPlayer()).getChestMap().clear();
	}
}