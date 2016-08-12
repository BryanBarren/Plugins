package city.smash.union.utils;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import city.smash.union.classes.SmashTask;

public class TaskUtils {

	public static HashMap<UUID, SmashTask> taskid = new HashMap<UUID, SmashTask>();

	public static void killTask(UUID tsid) {
		if (taskid.containsKey(tsid)) {
			taskid.get(tsid).killTask();
		}
	}

	public static void startTask(UUID tsid, int id, Player player, String spell) {
		taskid.put(tsid, new SmashTask(tsid, id, player, spell.toLowerCase()));
	}

	public static boolean taskIsAlive(UUID tsid) {
		return taskid.containsKey(tsid);
	}

	public static UUID newTaskCommand(Player player, Runnable runnable) {
		UUID _uid = UUID.randomUUID();
		DataUtils.getPlayer(player).getTaskCommandList().put(_uid, runnable);
		return _uid;
	}
}
