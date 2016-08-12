package city.smash.union.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import city.smash.union.utils.DataUtils;

public class SmashInventory {
	private HashMap<Integer, HashMap<ClickType, Runnable>> map;
	private Player player;
	ArrayList<ClickType> defaultClickSet = new ArrayList<ClickType>(Arrays.asList(ClickType.LEFT, ClickType.SHIFT_LEFT, ClickType.RIGHT, ClickType.SHIFT_RIGHT));
	
	public SmashInventory(Player player) {
		this.map = new HashMap<Integer, HashMap<ClickType, Runnable>>();
		this.player = player;
	}
	public void loadInventory() {
		DataUtils.getPlayer(this.player).setChestMap(this.map);
	}
	public void formatSlotDefault(Integer slot, Runnable command) {
		this.map.putIfAbsent(slot, new HashMap<ClickType, Runnable>());
		for (ClickType type : defaultClickSet) {
			this.map.get(slot).put(type, command);
		}
	}
	public void formatSlotForClickType(Integer slot, Runnable command, ClickType...types) {
		this.map.putIfAbsent(slot, new HashMap<ClickType, Runnable>());
		for (ClickType type : types) {
			this.map.get(slot).put(type, command);
		}
	}
	public HashMap<Integer, HashMap<ClickType, Runnable>> getMap() {
		return map;
	}
	public Player getPlayer() {
		return player;
	}
}
