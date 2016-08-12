package city.smash.union.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SmashPredamageEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private Player attacker;
	private Player victim;
	private double damage;
	private boolean isCancelled = false;

	public SmashPredamageEvent(Player attacker, Player victim, double damage) {
		this.attacker = attacker;
		this.victim = victim;
		this.damage = damage;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Player getAttacker() {
		return attacker;
	}

	public Player getVictim() {
		return victim;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled=isCancelled;
	}
}
