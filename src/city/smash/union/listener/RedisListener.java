package city.smash.union.listener;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;

import city.smash.union.Main;
import city.smash.union.utils.ChatUtils;
import city.smash.union.utils.SoundUtils;
import redis.clients.jedis.JedisPubSub;

public class RedisListener extends JedisPubSub {

	@Override
	public void onMessage(String channel, final String msg) {
		String command = null;
		String messageToProcess = null;
		if (msg.contains("-")) {
			command = msg.split("-")[0];
			messageToProcess = "";
			if (msg.length() != command.length()) {
				messageToProcess = msg.substring(command.length()+1);
			}
		} else {
			command = msg;
		}
		final String message = messageToProcess;
		Player p;
		switch (command.toLowerCase()) {
		case "runcommand":
			Bukkit.getScheduler().runTask(Main.instance, new Runnable() {
				@Override
				public void run() {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message);
				}
			});
			break;
		case "tabcomplete":
			String player = message.substring(0, message.indexOf(" "));
			String[] tabComplete = message.substring(message.indexOf(" ") + 1).split(" ");
			if (message.length() <= 16) {
				p = Bukkit.getPlayerExact(player);
			} else {
				p = Bukkit.getPlayer(UUID.fromString(player));
			}
			if (p != null) {;
				Bukkit.getScheduler().runTask(Main.instance, new Runnable() {
					@Override
					public void run() {
						PacketContainer packet = new PacketContainer(PacketType.Play.Server.TAB_COMPLETE);
						packet.getStringArrays().write(0, tabComplete);

						try {
							ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				});
			}
			break;
		case "broadcast":
			Bukkit.getScheduler().runTask(Main.instance, new Runnable() {
				@Override
				public void run() {
					ChatUtils.broadcastMessage(message);
				}
			});
			break;
		case "reloadranks":
			Bukkit.getScheduler().runTask(Main.instance, new Runnable() {
				@Override
				public void run() {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "permissions refresh");
				}
			});
			break;
		case "killping":
			Bukkit.getScheduler().runTask(Main.instance, new Runnable() {
				@Override
				public void run() {
					Bukkit.shutdown();
				}
			});
			break;
		case "addvoteplayer":
			if (message.length() <= 16) {
				p = Bukkit.getPlayerExact(message);
			} else {
				p = Bukkit.getPlayer(UUID.fromString(message));
			}
			if (p != null) {
				SoundUtils.playGUISoundClaim(p);
			}
			break;
		}
	}
}
