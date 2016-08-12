package city.smash.union.classes;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import city.smash.union.Main;
import city.smash.union.Variables;
import city.smash.union.utils.ChatUtils;
import city.smash.union.utils.NumberUtils;
import city.smash.union.utils.RomanUtils;
import city.smash.union.utils.SignUtils;
import city.smash.union.utils.SoundUtils;
import city.smash.union.utils.UpgradeUtils;

public class SmashUpgrade implements Listener {
	private Integer depositInterval = Variables.getInt("points_depositInterval");
	private SmashUpgradeType type;
	private SmashTeam team;
	private Integer level = 1;
	private Integer currentBalance = 0;
	private ArrayList<Location> blockLocations = new ArrayList<Location>();
	public SmashUpgrade(SmashUpgradeType type, SmashTeam team) {
		this.type = type;
		this.team = team;
	}
	public SmashUpgradeType getType() {
		return type;
	}
	public SmashTeam getTeam() {
		return team;
	}
	public Integer getLevel() {
		return level;
	}
	public Integer getCurrentBalance() {
		return currentBalance;
	}
	public Integer getCost() {
		return UpgradeUtils.getNextCost(this);
	}
	public Integer getRemaningCost() {
		return UpgradeUtils.getNextCost(this) - getCurrentBalance();
	}
	public ArrayList<Location> getBlockLocations() {
		return blockLocations;
	}
	public void addBlockLocation(Location loc) {
		blockLocations.add(loc.getBlock().getLocation());
	}
	public void depositBalanceToNextLevel(Player player) {
		int playerBalance = 1000;
		depositBalance(player, (getRemaningCost() < playerBalance ? getRemaningCost() : playerBalance));
	}
	public void depositBalance(Player player, Integer amount) {
		
		if (getCost() <= -1) {
			ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
			ChatUtils.sendMessage(player, "&cThis combat upgrade has already hit its max level.");
			ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
			return;
		}
		
		int playerBalance = 1000;
		
		int amountToDeposit = (getRemaningCost() < amount ? getRemaningCost() : amount);
		amountToDeposit = playerBalance < amountToDeposit ? playerBalance : amountToDeposit;

		currentBalance += amountToDeposit;
		playerBalance -= amountToDeposit;
		
		ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
		ChatUtils.sendMessage(player, "&aYou contributed " + NumberUtils.addSeperators(amountToDeposit) + " to " + UpgradeUtils.getUpgradeName(this.getType()));
		ChatUtils.sendMessage(player, "&7&m-----------------------------------------------------");
		
		if (currentBalance >= getCost()) {
			currentBalance -= getCost();
			level++;
			for (Player target : getTeam().getPlayers()) { 
				if (target == null || !target.isOnline()) {
					continue;
				}
				ChatUtils.sendMessage(target, "&7&m-----------------------------------------------------");
				ChatUtils.sendMessage(target, "&a" + player.getDisplayName() + " has upgraded " + UpgradeUtils.getUpgradeName(this.getType()) + " &ato level " + RomanUtils.toRoman(level) + " &7("+level+")");
				ChatUtils.sendMessage(target, "&7&m-----------------------------------------------------");
				SoundUtils.playGUISoundClaim(target);
			}
		} else {
			SoundUtils.playGUISoundGood(player);
		}

		updateSigns();
	}
	public void instantiateUpgrade() {
		Bukkit.getPluginManager().registerEvents(this, Main.instance);
		
		updateSigns();
	}
	public void updateSigns() {
		for (Location loc : getBlockLocations()) {
			if (loc.getBlock().getState() instanceof Sign) {
				SignUtils.updateSign(this, loc.getBlock());
			}
		}
	}
	@EventHandler 
	public void onPlayerInteract(PlayerInteractEvent e) {
		if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && getBlockLocations().contains(e.getClickedBlock().getLocation())) {
			if (getTeam().getPlayers().contains(e.getPlayer())) {
				if (e.getPlayer().isSneaking()) {
					depositBalanceToNextLevel(e.getPlayer());
				} else {
					depositBalance(e.getPlayer(), depositInterval);
				}
			} else {
				ChatUtils.sendMessage(e.getPlayer(), "&7&m-----------------------------------------------------");
				ChatUtils.sendMessage(e.getPlayer(), "&cYou cannot contribute to an enemies' combat upgrades!");
				ChatUtils.sendMessage(e.getPlayer(), "&7&m-----------------------------------------------------");
				SoundUtils.playGUISoundBad(e.getPlayer());
			}
		}
	}
}
