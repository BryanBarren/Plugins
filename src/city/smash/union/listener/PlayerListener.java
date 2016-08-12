package city.smash.union.listener;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import city.smash.union.Main;
import city.smash.union.Variables;
import city.smash.union.classes.SmashGame;
import city.smash.union.classes.SmashPlayer;
import city.smash.union.gui.GUIs;
import city.smash.union.scheduler.Gametick;
import city.smash.union.utils.BarrierUtils;
import city.smash.union.utils.BoosterUtils;
import city.smash.union.utils.BossBarUtils;
import city.smash.union.utils.ChatUtils;
import city.smash.union.utils.DataUtils;
import city.smash.union.utils.ManaUtils;
import city.smash.union.utils.ScoreboardUtils;
import city.smash.union.utils.SkinUtils;
import city.smash.union.utils.SoundUtils;
import city.smash.union.utils.StatisticUtils;
import city.smash.union.utils.TablistUtils;
import city.smash.union.utils.TitleUtils;
import city.smash.union.utils.VanishUtils;

public class PlayerListener implements Listener {

	@EventHandler
	public void deathEvent(PlayerDeathEvent event){
		event.setDroppedExp(0);
		event.getDrops().clear();
		event.setDeathMessage(null);
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		if (!Gametick.isGameTicking(e.getPlayer()) || e.getPlayer().getGameMode() == GameMode.CREATIVE || DataUtils.getPlayer(e.getPlayer()).isVanished()) {
			return;
		}

		e.getPlayer().setVelocity(new Vector(0,0,0));
	}

	@EventHandler
	public void onPlayerLogin(final PlayerLoginEvent e) {

		if (e.getPlayer().hasPermission("admin")) {
			e.getPlayer().setOp(true);
		} else {
			e.getPlayer().setOp(false);
		}


		if (Bukkit.hasWhitelist() && !e.getPlayer().isWhitelisted() && !e.getPlayer().hasPermission("staff")) {
			e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "\n§cThis server currently in maintenance mode.");
		}

		if (Bukkit.getOnlinePlayers().size() >= Variables.getInt("playercap_maxdonorslots")) {
			if (!e.getPlayer().hasPermission("staff")) {
				e.disallow(PlayerLoginEvent.Result.KICK_FULL, Variables.getString("playercap_maxdonorkickmessage"));
			}
		} else if (Bukkit.getOnlinePlayers().size() >= Variables.getInt("playercap_maxdefaultslots")) {
			if (!e.getPlayer().hasPermission("hero")) {
				e.disallow(PlayerLoginEvent.Result.KICK_FULL, Variables.getString("playercap_maxdefaultkickmessage"));
			}
		}

		if (SmashGame.getGameInstance().getGameStatus() != 0 && !e.getPlayer().hasPermission("staff")) {
			
		}
	}
	//
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent e) {
		//The player at this moment has already passed the checks in PlayerLogin

		Player player = e.getPlayer();
		SmashPlayer result = new SmashPlayer(player);
		DataUtils.playerInfo.put(player.getUniqueId(), result);
		player.teleport(SmashGame.getGameInstance().getLobbySpawn());

		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100000, 3, true, true));
		TitleUtils.sendTitle(player, "&c&lLogging In...", "&7&lPlease wait while we load your profile", 0, 1000000, 0);
		
		e.setJoinMessage(null);

		TablistUtils.setTablistHeader(player);
		BossBarUtils.startBossBar(player);
		StatisticUtils.clearDistanceMoved(player);

		player.setFireTicks(0);
		VanishUtils.vanishRecheck(e.getPlayer());
		player.setGameMode(GameMode.ADVENTURE);

		player.getInventory().clear();

		ScoreboardUtils.createScoreboard(player);

		ManaUtils.refreshEXP(e.getPlayer());
		player.setFoodLevel(20);
		player.spigot().setCollidesWithEntities(false);

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {

			@Override
			public void run() {
				
//				DataUtils.instantiatePlayerData(player);
				String gTag = ScoreboardUtils.loadGuildtag(player);
				DataUtils.getPlayer(player).setPrefix(ChatUtils.getPrefix(player));
				SkinUtils.getSkinFromCache(player);

				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {

					@Override
					public void run() {
						
						SmashGame.getGameInstance().addPlayer(player);
						
						DataUtils.getPlayer(player).setGuildTag(gTag);
						ScoreboardUtils.refreshGuildTag(player);

						for (PotionEffect potion : player.getActivePotionEffects()) {
							player.removePotionEffect(potion.getType());
						}

						TitleUtils.sendLoginTitle(player);

						player.setHealth(player.getMaxHealth());

						BarrierUtils.refreshBarrierDisplay(player);
						ScoreboardUtils.refreshHPIndicator(player);
						ScoreboardUtils.refreshAssists(player);
						ScoreboardUtils.refreshKills(player);
						ScoreboardUtils.refreshHPIndicatorOther(player);

						Gametick.startGametick(player);

						BoosterUtils.scheduleBoosterWarning(player);
						TablistUtils.setTablistHeader(player);
						VanishUtils.vanishRecheck(e.getPlayer());
					}
				}, 1L);
			}
		});
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		Player player = e.getPlayer();
		
		SmashGame.getGameInstance().removePlayer(player);

		if (SmashGame.getGameInstance().getGameStatus() == -1) {
			StatisticUtils.updateTimeAndDistance(player);
		}
		
		Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
			public void run(){
				if (player.isOnline()){
					return;
				}
				DataUtils.removePlayer(player);

				File file = new File(new File("world/playerdata"), player.getUniqueId() + ".dat");
				if(file.exists()){
					file.delete();
				}
				file = new File(new File("world/stats"), player.getUniqueId() + ".dat");
				if(file.exists()){
					file.delete();
				}

			}
		}, 20);
	}

		@EventHandler 
		public void onPlayerInteract(PlayerInteractEvent e) {
			if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
				return;
			}
			e.setCancelled(true);
			if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) {
				if (SmashGame.getGameInstance().getGameStatus() != -1) {
					switch (e.getPlayer().getInventory().getItemInHand().getType()) {
					case COMPASS:
						GUIs.openGUI(e.getPlayer(), "teamselect");
						SoundUtils.playGUISoundClick(e.getPlayer());
						break;
					default:
						break;
					}
				}
			}
		}
}