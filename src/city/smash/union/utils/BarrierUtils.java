package city.smash.union.utils;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import city.smash.union.Main;

public class BarrierUtils {
	public static UUID newBarrier(Player player, Double amount, Long timeFor) {
		UUID id = UUID.randomUUID();
		
		DataUtils.getPlayer(player).getBarrierMap().put(id, amount);

		refreshBarrierDisplay(player);
		
		Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {

			@Override
			public void run() {
				BarrierUtils.removeBarrier(player, id);
			}
			
		}, timeFor);
		
		return id;
	}
	
	public static Double takeDamage (Player player, Double amount) {
		
		HashSet<UUID> deleteMap = new HashSet<UUID>();
		
		for (UUID barrierID : DataUtils.getPlayer(player).getBarrierMap().keySet()) {
			Double barrierAmt = DataUtils.getPlayer(player).getBarrierMap().get(barrierID);
			
			if (barrierAmt <= amount) {
				amount -= barrierAmt;
				deleteMap.add(barrierID);
			} else {
				barrierAmt-=amount;
				amount = 0D;
				DataUtils.getPlayer(player).getBarrierMap().put(barrierID, barrierAmt);
			}
		}
		
		for (UUID id : deleteMap) {
			DataUtils.getPlayer(player).getBarrierMap().remove(id);
		}
		
		refreshBarrierDisplay(player);
		return amount;
	}
	
	public static void removeBarrier(Player player, UUID barrierID) {
		DataUtils.getPlayer(player).getBarrierMap().remove(barrierID);
		refreshBarrierDisplay(player);
	}
	
	public static void refreshBarrierDisplay(Player player) {
		CraftPlayer cp = (CraftPlayer) player;
		Double totalBarrier = 0D;
		for (Double barrierAmt : DataUtils.getPlayer(player).getBarrierMap().values()) {
			totalBarrier+=barrierAmt;
		}
		cp.getHandle().setAbsorptionHearts((float)(totalBarrier/50));
		ScoreboardUtils.refreshHPIndicator(player);
	}
	
	public static Double getBarrierHP(Player player) {
		Double amount = 0D;
		for (Double barrierHP : DataUtils.getPlayer(player).getBarrierMap().values()) {
			amount += barrierHP;
		}
		return amount;
	}
}
