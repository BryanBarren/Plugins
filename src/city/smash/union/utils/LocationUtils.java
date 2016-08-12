package city.smash.union.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.commons.dbcp2.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import city.smash.union.Main;
import city.smash.union.classes.SmashLoc;

public class LocationUtils {
	
	public static boolean isCloseToGround(Player player) {
		if (((Entity)player).isOnGround()) {
			return true;
		} else {
			if (player.getLocation().add(0,-0.25, 0).getBlock().getType().isSolid()) {
				return true;
			}
			return false;
		}
	}
	
	public static boolean isCloseToGroundForCast(Player player) {
		if (((Entity)player).isOnGround()) {
			return true;
		} else {
			if (player.getLocation().add(0,-2, 0).getBlock().getType().isSolid()) {
				return true;
			}
			return false;
		}
	}
	/**
	 * Checks for lowest location that is not occupied by a block
	 * @param loc
	 * @return Location that is raised/lowered to point that is not in water or a block
	 */
	public static Location fixPoint(Location loc) {
		while (true) {
			if (loc.getY() >= 255) {
				loc.setY(255);
			}
			else if (loc.getY() <= 1) {
				loc.setY(1);
				return loc;
			}
			else {
				if (loc.getBlock().getType().isSolid() || loc.getBlock().isLiquid()) {	
					loc.add(0, 1, 0);
				} else if (!loc.clone().add(0, -1, 0).getBlock().getType().isSolid() && !loc.clone().add(0, -1, 0).getBlock().isLiquid()) {
					loc.add(0, -1, 0);
				} else {
					loc.setY(loc.getBlockY());
					return loc;
				}
			}
		}
	}
	
	public static void setPoint(String name, SmashLoc loc) {
		new BukkitRunnable()
		{
			@Override
			public void run() {
				Connection connection = null;
				ResultSet rs = null;
				Statement s = null;
				try {
					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserversettings");
					s = connection.createStatement();
					String query = "select * from locationmap Where name='"+name+"'";
					rs = s.executeQuery(query);
					if (rs.next()) {
						query = "update locationmap set world='"+loc.getLocation().getWorld().getName()+"', x="+loc.getLocation().getX()+", y="+loc.getLocation().getY()+", z="+loc.getLocation().getZ()+", yaw="+loc.getLocation().getYaw()+", pitch="+loc.getLocation().getPitch()+", extradata='"+loc.getData()+"' where name='"+name+"'";
						s.executeUpdate(query);
					} else {
						query = "insert into locationmap (name, world, x, y, z, yaw, pitch, extradata) VALUES ('"+name+"', '"+loc.getLocation().getWorld().getName()+"', "+loc.getLocation().getX()+", "+loc.getLocation().getY()+", "+loc.getLocation().getZ()+", "+loc.getLocation().getYaw()+", "+loc.getLocation().getPitch()+", '"+loc.getData()+"')";
						s.executeUpdate(query);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(rs);
					Utils.closeQuietly(s);
				}
			}
		}
		.runTaskAsynchronously(Main.instance);
	}
	
	public static void deletePoint(String name) {
		new BukkitRunnable()
		{
			@Override
			public void run() {
				Connection connection = null;
				Statement s = null;
				try {
					connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserversettings");
					s = connection.createStatement();
					String query = "delete from locationmap Where name='"+name+"'";
					s.executeUpdate(query);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Utils.closeQuietly(connection);
					Utils.closeQuietly(s);
				}
				
			}
		}
		.runTaskAsynchronously(Main.instance);
	}
	
	/**
	 * DOES NOT RUN ASYNC, ONLY USE WHEN NEEDED (SERVER START)
	 * @param mysqlPrefix
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, SmashLoc> getPointByPrefix(String mysqlPrefix) {
		if (Bukkit.isPrimaryThread()) {
			throw new RuntimeException("MySQL on Blocking Thread");
		}
		Connection connection = null;
		ResultSet rs = null;
		Statement s = null;
		try {
			connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserversettings");
			s = connection.createStatement();
			String query = "select * from locationmap Where name like '"+mysqlPrefix+"%'";
			rs = s.executeQuery(query);
			if (rs.next()) {
				rs.beforeFirst();
				HashMap<String, SmashLoc> returnMap = new HashMap<String, SmashLoc>();
				while (rs.next()) {
					Location loc = new Location(Bukkit.getWorlds().get(0), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch"));
					returnMap.put(rs.getString("name"), new SmashLoc(loc, rs.getString("extradata")));
				}
				return returnMap;
			} else {
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utils.closeQuietly(connection);
			Utils.closeQuietly(rs);
			Utils.closeQuietly(s);
		}
		return new HashMap<String, SmashLoc>();
	}
	
	/**
	 * DOES NOT RUN ASYNC, ONLY USE WHEN NEEDED (SERVER START)
	 * @param mysqlName
	 * @return
	 * @throws Exception
	 */
	public static SmashLoc getPointByNameExact(String mysqlName) {
		if (Bukkit.isPrimaryThread()) {
			throw new RuntimeException("MySQL on Blocking Thread");
		}
		Connection connection = null;
		ResultSet rs = null;
		Statement s = null;
		try {
			connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:mcserversettings");
			s = connection.createStatement();
			String query = "select * from locationmap Where name='"+mysqlName+"'";
			rs = s.executeQuery(query);
			if (rs.next()) {
				SmashLoc loc = new SmashLoc(new Location(Bukkit.getWorlds().get(0), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch")), rs.getString("extradata"));
				return loc;
			} else {
				return new SmashLoc(new Location(Bukkit.getWorlds().get(0), 0,0,0));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utils.closeQuietly(connection);
			Utils.closeQuietly(rs);
			Utils.closeQuietly(s);
		}
		return null;
	}

	public static ArrayList<Material> avoidBlocks = new ArrayList<Material>(Arrays.asList(Material.TRAPPED_CHEST, Material.BREWING_STAND, Material.ENDER_PORTAL_FRAME, Material.ENCHANTMENT_TABLE, Material.ANVIL, Material.ACACIA_DOOR, Material.DARK_OAK_DOOR, Material.IRON_DOOR, Material.IRON_DOOR_BLOCK, Material.BIRCH_DOOR, Material.JUNGLE_DOOR, Material.SPRUCE_DOOR, Material.TRAP_DOOR, Material.IRON_TRAPDOOR, Material.WOODEN_DOOR, Material.STEP, Material.WOOD_STEP, Material.STONE_SLAB2, Material.IRON_FENCE, Material.NETHER_FENCE, Material.FENCE, Material.FENCE_GATE, Material.ACACIA_FENCE, Material.ACACIA_FENCE_GATE, Material.BIRCH_FENCE, Material.BIRCH_FENCE_GATE, Material.DARK_OAK_FENCE, Material.DARK_OAK_FENCE_GATE, Material.JUNGLE_FENCE, Material.JUNGLE_FENCE_GATE, Material.SPRUCE_FENCE, Material.SPRUCE_FENCE_GATE, Material.ACACIA_STAIRS, Material.BIRCH_WOOD_STAIRS, Material.BRICK_STAIRS, Material.COBBLESTONE_STAIRS, Material.DARK_OAK_STAIRS, Material.JUNGLE_WOOD_STAIRS, Material.NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.RED_SANDSTONE_STAIRS, Material.SANDSTONE_STAIRS, Material.SMOOTH_STAIRS, Material.SPRUCE_WOOD_STAIRS, Material.WOOD_STAIRS, Material.CHEST, Material.ENDER_CHEST, Material.HOPPER, Material.DAYLIGHT_DETECTOR, Material.DAYLIGHT_DETECTOR_INVERTED, Material.STAINED_GLASS_PANE, Material.THIN_GLASS, Material.GOLD_PLATE, Material.IRON_PLATE, Material.STONE_PLATE, Material.WOOD_PLATE, Material.SIGN, Material.SIGN_POST, Material.WALL_SIGN, Material.BANNER, Material.STANDING_BANNER, Material.WALL_BANNER));

	public static void surfacePlayer(Player player) {
		Block block1 = player.getLocation().getBlock();
		Block block2 = player.getLocation().add(0, 1, 0).getBlock();
		if (block1.getType().isSolid()) {
			if (!avoidBlocks.contains(block1.getType())) {
				player.teleport(fixPoint(player.getLocation()).add(0, 0.25, 0));
			}
		}
		else if (block2.getType().isSolid()) {
			if (!avoidBlocks.contains(block2.getType())) {
				player.teleport(fixPoint(player.getLocation()).add(0, 0.25, 0));
			}
		}
	}
}
