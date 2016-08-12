package city.smash.union.classes;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import city.smash.union.utils.TaskUtils;

public class SmashTask {
	private int id;
	private Player player;
	private String type;
	private UUID uuid;

	public SmashTask(UUID uuid, int id, Player player, String type) {
		this.id = id;
		this.player = player;
		this.type = type;
		this.uuid = uuid;
	}

	public int getID() {
		return id;
	}

	public Player getOwner() {
		return player;
	}

	public String getType() {
		return type;
	}

	public UUID getUUID() {
		return uuid;
	}

	public void killTask() {
		Bukkit.getScheduler().cancelTask(id);
		TaskUtils.taskid.remove(uuid);
	}
}
