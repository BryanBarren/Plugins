package city.smash.union.listener;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.meta.ItemMeta;

import city.smash.union.utils.EntityUtils;

import org.bukkit.event.inventory.InventoryClickEvent;

public class WorldprotectListener implements Listener {

	@EventHandler
	public void targetEvent(EntityTargetEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			if (event.getTarget() instanceof Player) {
				if (!event.getEntity().hasMetadata("targeter")) {
					event.setCancelled(true);
				}
			} else {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void SignChangeEvent(SignChangeEvent e) {
		if (e.getPlayer().hasPermission("admin")) {
			for (int i = 0; i < e.getLines().length; i++) {
				e.setLine(i, ChatColor.translateAlternateColorCodes('&', e.getLine(i)));
			}
		}
	}
	
	@EventHandler
	public void BlockExpEvent(BlockExpEvent event) {
		event.setExpToDrop(0);
	}

	
	@EventHandler
	public void unloadChunk(ChunkUnloadEvent event) {
		for (Entity e : event.getChunk().getEntities()) {
			e.remove();
		}
	}
	
	@EventHandler
	public void StructureGrowEvent(org.bukkit.event.world.StructureGrowEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void BlockDispenseEvent(BlockDispenseEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void blockDamage(BlockDamageEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void damageEvent(EntityDamageEvent event){
		event.setCancelled(true);
	}

	@EventHandler
	public void onSpawn(CreatureSpawnEvent event){
		if (event.getSpawnReason() == SpawnReason.NETHER_PORTAL) {
			event.setCancelled(true);
		}
		EntityUtils.toggleAI(event.getEntity(), false);
	}

	@EventHandler
	public void onItemSpawn(ItemSpawnEvent event){
		if (!event.getEntity().getItemStack().getItemMeta().hasDisplayName() || !event.getEntity().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("tagged")) {
			event.setCancelled(true);
		} else {
			ItemMeta meta = event.getEntity().getItemStack().getItemMeta();
			meta.setDisplayName(UUID.randomUUID().toString());
			event.getEntity().getItemStack().setItemMeta(meta);
		}
	}
	
	@EventHandler
	public void onItemDrop (PlayerDropItemEvent e) {
		if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			ItemMeta meta = e.getItemDrop().getItemStack().getItemMeta();
			meta.setDisplayName("tagged");
			e.getItemDrop().getItemStack().setItemMeta(meta);
			return;
		}
		e.setCancelled(true);
	}
	@EventHandler
	public void playerRegen(EntityRegainHealthEvent event){
		event.setCancelled(true);
	}
	@EventHandler
	public void foodChange(FoodLevelChangeEvent event){
		event.setCancelled(true);
		((Player) event.getEntity()).setFoodLevel(20);
	}
	@EventHandler
	public void rain(WeatherChangeEvent e) {
		if (e.toWeatherState()) {
			e.setCancelled(true);
			e.getWorld().setStorm(false);
		}
	}
	
	public void blockEvent(BlockPhysicsEvent e) {
		e.setCancelled(true);
	}
	@EventHandler
	public void BlockSpreadEvent(BlockSpreadEvent event){
		event.setCancelled(true);
	}
	@EventHandler
	public void handleBlockFade(BlockFadeEvent event) {
		event.setCancelled(true);
	}
	@EventHandler
	public void handleBlockDecay(BlockFormEvent event) {
		event.setCancelled(true);
	}
	@EventHandler 
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.getAction() == Action.PHYSICAL){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void PlayerInteractEntityEvent(PlayerInteractEntityEvent e) {
		if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE) || e.getRightClicked() instanceof NPC) {
			return;
		}
		e.setCancelled(true);
	}
	@EventHandler
	public void InventoryClickEvent(InventoryClickEvent e) {
		if (e.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}
		e.setCancelled(true);
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}
		e.setCancelled(true);
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			if (e.getBlock().getType().isOccluding()) {
				Block testBlock = e.getBlock().getLocation().add(0,-1,0).getBlock();
				if (testBlock.getType() == Material.GRASS) {
					testBlock.setType(Material.DIRT);
				}
			}
			return;
		}
		e.setCancelled(true);
	}
	@EventHandler
	public void onLiquidFlow(BlockFromToEvent e) {
		e.setCancelled(true);
	}
	@EventHandler
	public void onBlockGrow(BlockGrowEvent e) {
		e.setCancelled(true);
	}
	@EventHandler
	public void onFallingBlockLand(EntityChangeBlockEvent event){
		event.getEntity().remove();
		event.setCancelled(true);
	}
	@EventHandler
	public void EntityBlockFormEvent(EntityBlockFormEvent event){
		event.setCancelled(true);
	}
	@EventHandler
	public void BlockBurnEvent(BlockBurnEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void leavesDecay(LeavesDecayEvent e) {
		e.setCancelled(true);
	}
	@EventHandler
	public void BlockIgniteEvent(BlockIgniteEvent e) {
		if (e.getIgnitingEntity() instanceof Player && ((Player)e.getIgnitingEntity()).getGameMode() == GameMode.CREATIVE) {
			return;
		}
		e.setCancelled(true);
	}	
}
