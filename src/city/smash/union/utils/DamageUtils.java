package city.smash.union.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import city.smash.union.Main;
import city.smash.union.Variables;
import city.smash.union.classes.SmashPlayer;
import city.smash.union.deatheffects.DeathEffectUtils;
import city.smash.union.events.SmashDamageEvent;
import city.smash.union.events.SmashPredamageEvent;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.MobEffectList;

public class DamageUtils {
	
	public static boolean applyKnockback (Player attacker, Player victim, Vector vector) {
		if (victim == null) {
			return false;
		}
		victim.setVelocity(victim.getVelocity().add(vector));
		return true;
	}
	
	/**
	 * Heals player instantly
	 * @param player Targeted player to heal
	 * @param amount Amount to heal
	 * @param spell Spell name. Will automatically get config name.
	 */
	public static void regenHealth(Player player, Double amount, String spell) {
		regenHealth(player, amount, 0, 0L, false, spell);
	}
	/**
	 * Heals a player with the following parameters
	 * @param player Targeted player to heal
	 * @param amount Health to heal per loop
	 * @param times Total times to heal, set to 0 for instant heal
	 * @param timePerTick Delay between heals
	 * @param cancelOnDamage If true, healing will stop if a player is damaged
	 * @param spell Spell name. Will automatically get config name.
	 */
	public static void regenHealth(final Player player, final Double amount, final int times, final Long timePerTick, final boolean cancelOnDamage, final String spell) {
		if (times == 0) {
			Double heal = amount;
			if (heal + player.getHealth() > player.getMaxHealth()) {
				heal = player.getMaxHealth()-player.getHealth();
			}
			
			String msg = "";
			if (heal == 0) {
				msg = Variables.getString("heal_healfail");
				msg = msg.replaceAll("%s", spell);
			} else {
				msg = Variables.getString("heal_healmessage");
				msg = msg.replaceAll("%s", spell);
				msg = msg.replaceAll("%h", Integer.toString((heal.intValue())));
				HealthUtils.addHealth(player, heal);
				
				HologramUtils.displayAlertHolo(player, "&a+" + Integer.toString(heal.intValue()) + " ❤");
			}
			
			ChatUtils.sendMessage(player, msg);
			
			StatisticUtils.addHeal(player, heal.intValue());
			
		} else {
			
			final UUID tsid = UUID.randomUUID();
			int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable() {

				int i = 0;
				Long lastDamage = DataUtils.getPlayer(player).getLastDamage();
				
				@Override
				public void run() {
					if (!player.isOnline()) {
						TaskUtils.killTask(tsid);
						return;
					}
					
					player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100000, 5, true, true));
					
					if (cancelOnDamage && lastDamage != DataUtils.getPlayer(player).getLastDamage()) {
						player.removePotionEffect(PotionEffectType.REGENERATION);
						String m = Variables.getString("heal_healcancelonhitmessage");
						m = m.replaceAll("%s", spell);
						ChatUtils.sendMessage(player, m);
						
						TaskUtils.killTask(tsid);
						return;
					}
					Double heal = amount;
					if (heal + player.getHealth() > player.getMaxHealth()) {
						heal = player.getMaxHealth()-player.getHealth();
					}
					if (heal == 0) {
						ChatUtils.sendMessage(player, Variables.getString("heal_healfail").replace("%s", spell));
						if (cancelOnDamage) {
							player.removePotionEffect(PotionEffectType.REGENERATION);
							TaskUtils.killTask(tsid);
							return;
						}
					} else {
						regenHealth(player, heal, spell);
					}
					
					//add to total heal

					i++;
					if (i >= times) {
						player.removePotionEffect(PotionEffectType.REGENERATION);

						String m = Variables.getString("heal_healexpire");
						m = m.replaceAll("%s", spell);
						ChatUtils.sendMessage(player, m);

						TaskUtils.killTask(tsid);
						return;
					}
				}
			}, timePerTick, timePerTick);
			TaskUtils.startTask(tsid, id, player, spell);
		}
	}
	
	public static void Death (Player victim) {
		DeathEffectUtils.playDeathEffect(victim);
		SoundUtils.playSound(Sound.HURT_FLESH, victim.getEyeLocation(), 2F, 0F);
		
		Player attacker = DataUtils.getPlayer(victim).getLastAttacker();
		
		StatisticUtils.addDeath(victim);
		
		if (attacker == null) {
			TitleUtils.sendTitle(victim, "&c&lYou have died!", "&7&lMistakes were made", 10, 80, 10);
			ChatUtils.sendMessage(victim, "&7&m-----------------------------------------------------");
			ChatUtils.sendMessage(victim, "&8[&c&lDeath&8] &7Mistakes were made.");
			ChatUtils.sendMessage(victim, "&7&m-----------------------------------------------------");
		} else {
			
			String prefixAttacker = DataUtils.getPlayer(attacker).getPrefix();
			String colorAttacker = prefixAttacker.substring(0, 2);
			ChatUtils.sendMessage(victim, "&7&m-----------------------------------------------------");
			ChatUtils.sendMessage(victim, "&8[&c&lDeath&8] &7You have been slain by " + prefixAttacker + attacker.getDisplayName());
			ChatUtils.sendMessage(victim, "&7&m-----------------------------------------------------");
			TitleUtils.sendTitle(victim, "&c&lYou have been slain!", "&7&lKiller: " + colorAttacker + attacker.getDisplayName(), 10, 80, 10);
			
			MoneyUtils.deathCoinsPayout(victim);

			StatisticUtils.addDeath(attacker);
		}
		victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0, true, true));
		
//		victim.teleport(Main.SpawnLoc);
		//Location teleport to team spawn.
		victim.setVelocity(new Vector(0,0,0));
		
	}
	
	public static boolean MeleeDamage (Player attacker, Player victim) {
		EntityHuman e = ((EntityHuman)((CraftPlayer)attacker).getHandle());
		boolean flag = (e.fallDistance > 0.0F) && (!e.onGround) && (!e.k_()) && (!e.V()) && (!e.hasEffect(MobEffectList.BLINDNESS)) && (e.vehicle == null);
		
		double damage = Variables.getDouble("basedamage") * (flag ? 1.5 : 1);
		
		if (victim.isBlocking()) {
			damage=damage/2;
		}
		
		return Damage(attacker, victim, damage);
	}
	//
	public static boolean Damage (Player attacker, Player victim, Double damage) {
		
		if (victim.isDead()) {
			return false;
		}
		if (attacker != null && !attacker.isOnline()) {
			return false;
		}
		if (DataUtils.getPlayer(victim).isVanished()) {
			return false;
		}
		if (attacker != null && DataUtils.getPlayer(attacker).isVanished()) {
			return false;
		}
		
		//Multiply and reduce by team stats.
		
		SmashPredamageEvent event = new SmashPredamageEvent(attacker, victim, damage);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return false;
		}
		damage = event.getDamage();

		victim.playEffect(EntityEffect.HURT);
		Location loc = victim.getLocation().add(0,1,0);
		ParticleUtils.SendPacket(ParticleUtils.createBlockPacket(EnumParticle.BLOCK_CRACK, 152, 0, loc, new Vector(0.1, 0.1, 0.1), 0F, 50), loc);
		ParticleUtils.SendPacket(ParticleUtils.createNormalPacket(EnumParticle.VILLAGER_ANGRY, victim.getEyeLocation(), new Vector(0,0,0), 0F, 1), loc);
		
		Double totalDamage = damage;
		
		damage = BarrierUtils.takeDamage(victim, damage);

		HologramUtils.displayAlertHolo(victim, "&c-"+Integer.toString(totalDamage.intValue())+" ❤");
		
		boolean result = addDamage(attacker, victim, damage);
		
		Bukkit.getPluginManager().callEvent(new SmashDamageEvent(attacker, victim, totalDamage));
				
		return result;
	}
	
	private static boolean addDamage(Player attacker, Player victim, Double damage) {
		//mysql shit
		if (attacker == null) {
			attacker = DataUtils.getPlayer(victim).getLastAttacker();
		}
		if (damage > victim.getHealth()) {
			damage = victim.getHealth();
		}
		
		SmashPlayer Victim = DataUtils.getPlayer(victim);
		Victim.setLastDamage(System.currentTimeMillis());
		Victim.setLastAttacker(attacker);

		StatisticUtils.addDamageTaken(victim, damage);
		
		if (attacker != null) {

			StatisticUtils.addDamageDelt(attacker, damage);
			
			if (Victim.getDamagers().containsKey(attacker)) {
				Victim.getDamagers().put(attacker, Victim.getDamagers().get(attacker) + damage);
			} else {
				Victim.getDamagers().put(attacker, damage);
			}
		}
		
		return HealthUtils.addHealth(victim, damage * -1);
	}
}
