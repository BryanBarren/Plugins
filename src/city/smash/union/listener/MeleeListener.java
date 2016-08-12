package city.smash.union.listener;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import city.smash.union.Variables;
import city.smash.union.utils.DamageUtils;
import city.smash.union.utils.DataUtils;
import city.smash.union.utils.HitdetectionUtils;

public class MeleeListener implements Listener {

	@EventHandler
	public void hitEvent(EntityDamageByEntityEvent event){
		if (event.getDamager() instanceof Player) {
			if (((Player)event.getDamager()).getGameMode() == GameMode.CREATIVE) {
				return;
			}
			event.setCancelled(true);
			if (!((Player)event.getDamager()).getItemInHand().getType().name().contains("SWORD")) {
				return;
			}
			fakeHit((Player) event.getDamager());
		}
	}
	
	@EventHandler
	public void clickEvent(PlayerInteractEvent event){
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		event.setCancelled(true);
		fakeHit(event.getPlayer());
	}
	
	/**
	 * Sends a melee event
	 * @param attacker The attacker
	 * @param victim The Victim
	 */
	public static void playerHit(Player attacker, Player victim) {
		//add checks for pregame
		if (System.currentTimeMillis() - DataUtils.getPlayer(victim).getLastMeleeHit() < Variables.getInt("melee_hitdelay")) {
			return;
		}
		DataUtils.getPlayer(victim).setLastMeleeHit(System.currentTimeMillis());
		DamageUtils.MeleeDamage(attacker, victim);
	}
	/**
	 * Raycast a beam to simulate a hit
	 * @param player The beam to raycast for/from
	 */
	public static void fakeHit(Player player) {
		double accuracy = 0.1;
		double hitRange = 4.6;
		
		if (player.getWorld().getNearbyEntities(player.getEyeLocation(), hitRange, hitRange, hitRange).isEmpty()) {
			return;
		}

		Location loc = player.getEyeLocation().add(player.getLocation().getDirection().normalize().multiply(0));
		Vector v = player.getLocation().getDirection().normalize().multiply(accuracy);
		for (double i = 0; i < hitRange; i+=accuracy) {
			if (player.getWorld().getBlockAt(loc).getType().isSolid()) {
				return;
			}
			
			for (Player target : HitdetectionUtils.getPlayersAroundPoint(player, loc, 0)) {
				playerHit(player, target);
				return;
			}
			loc.add(v);
		}
	}
}