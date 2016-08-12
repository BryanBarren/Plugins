package city.smash.union.listener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.commons.dbcp2.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import city.smash.union.Main;
import city.smash.union.classes.SmashGlobalSetting;
import city.smash.union.scheduler.Gametick;
import city.smash.union.utils.ChatUtils;
import city.smash.union.utils.DataUtils;
import city.smash.union.utils.SettingsUtils;
import city.smash.union.utils.SoundUtils;
import net.md_5.bungee.api.ChatColor;

public class ChatListener implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();

//		event.setCancelled(true);

//		if (!Gametick.isGameTicking(player)) {
//			return;
//		}

//		if (!SettingsUtils.isEnabled(player, SmashGlobalSetting.CHAT_GLOBAL)) {
//			ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
//			ChatUtils.sendMessage(player, "&cYou cannot chat since you have Global Chat disabled.");
//			ChatUtils.sendMessage(player, "&7Enable Global Chat in your &e/settings &7to chat.");
//			ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
//			return;
//		}

//		String prefix = DataUtils.getPlayer(player).getPrefix();
//		String chatcolor;
//		if (player.hasPermission("hero") || player.hasPermission("staff")) {
//			chatcolor = "§f";
//		} else {
//			chatcolor = "§7";
//
//		}
//		if (player.hasPermission("admin")) {
//			event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
//		} else {
//			event.setMessage(event.getMessage().replace("&", "**d5bb4e12-3bde-4bdb-80cf-58f4e46c22de**"));
//		}
//
//		for (Player target : Bukkit.getOnlinePlayers()) {
//			if (!SettingsUtils.isEnabled(target, SmashGlobalSetting.CHAT_GLOBAL)) {
//				event.getRecipients().remove(target);
//			}
//		}
//
//		for (Player target : event.getRecipients()) {
//			if (SettingsUtils.isEnabled(target, SmashGlobalSetting.CHAT_NAME_ALERT)) {
//				if (event.getMessage().contains(target.getName()) && target != player) {
//					ChatUtils.sendMessage(target, prefix + player.getDisplayName() + chatcolor + " » " + event.getMessage().replaceFirst(target.getName(), "§e" + target.getName() + chatcolor));
//					SoundUtils.playSound(Sound.ORB_PICKUP, target, 1F, 1F);
//					continue;
//				}
//			}
//			ChatUtils.sendMessage(target, prefix + player.getDisplayName() + chatcolor + " » " + event.getMessage());
//		}
//
//		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {
//			@Override
//			public void run() {
//				Connection connection = null;
//				Statement s = null;
//				try {
//					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserverdata");
//					s = connection.createStatement();
//					String query = "Insert into chatlog (uuid, time, server, message) VALUES ('"+player.getUniqueId().toString()+"', "+System.currentTimeMillis()+", '"+Main.getServerName()+"', '"+player.getDisplayName() + " » " + ChatColor.stripColor(event.getMessage()).replace("\\", "\\\\").replace("\"", "\\\"").replace("\'", "\'\'")+"')";
//					s.executeUpdate(query);
//				} catch (Exception e) {
//					e.printStackTrace();
//				} finally {
//					Utils.closeQuietly(connection);
//					Utils.closeQuietly(s);
//				}
//			}
//		});
	}
}
