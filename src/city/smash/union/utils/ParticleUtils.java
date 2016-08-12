package city.smash.union.utils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class ParticleUtils {
	
	public static void playEffect(Effect eff, Location center, int data) {
		playEffect(eff, center, 125, data);
	}
	
	public static void playEffect(Effect eff, Location center) {
		playEffect(eff, center, 125, 0);
	}
	
	@SuppressWarnings("deprecation")
	public static void playEffect(Effect eff, Location center, int range, int data) {
		if (range > 125) {
			range = 125;
		}
		double squared = range * range;
		for (Player player : center.getWorld().getPlayers()) {
			//dist check later
			if (player.getLocation().toVector().setY(0).distanceSquared(center.toVector().setY(0)) < squared) {
				player.playEffect(center, eff, data);
			}
		}
	}
	/**
	 * Creates a color particle packet. Only works with MobSpell, MobSpellAmbient, RedDust and Note
	 * @return PacketPlayOutWorldParticles packet, can be cached.
	 * @param enumparticle EnumParticle to create
	 * @param loc Location of particle
	 * @param isNoteColor Bool of if the color is for a note.
	 * @param color Java Color of RGB to send.
	 */
	public static PacketPlayOutWorldParticles createColorPacket(EnumParticle enumparticle, Location loc, boolean isNoteColor, Color color) throws IllegalArgumentException {
		
		if (enumparticle != EnumParticle.REDSTONE && enumparticle != EnumParticle.SPELL_MOB && enumparticle != EnumParticle.SPELL_MOB_AMBIENT && enumparticle != EnumParticle.NOTE) {
			throw new IllegalArgumentException("This particle cannot be colored!");
		}
		if (isNoteColor) {
			if (color.getRed() < 0 || color.getRed() > 24) {
				throw new IllegalArgumentException("Note color must be between 24 and 0!");
			}
			return new PacketPlayOutWorldParticles(enumparticle, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), color.getRed()/24F, 0F, 0F, 1F, 0, 0);
		}
		if (color.getRed() < 1) {
			color = Color.fromRGB(1, color.getGreen(), color.getBlue());
		}
		if (color.getRed() < 0 || color.getRed() > 255 || color.getGreen() < 0 || color.getGreen() > 255 || color.getBlue() < 0 || color.getBlue() > 255) {
			throw new IllegalArgumentException("Color must be between 0 and 255!");
		}
		return new PacketPlayOutWorldParticles(enumparticle, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, 1F, 0, null);
	}
	/**
	 * Creates a directional particle packet.
	 * @return PacketPlayOutWorldParticles packet, can be cached.
	 * @param enumparticle EnumParticle to create
	 * @param loc Location of particle
	 * @param direction Vector of the velocity for particle
	 * @param speed Speed of particle to fly in direction
	 */
	public static PacketPlayOutWorldParticles createVectorPacket(EnumParticle enumparticle, Location loc, Vector direction, float speed) {
		if (enumparticle == EnumParticle.ITEM_CRACK && enumparticle == EnumParticle.BLOCK_DUST && enumparticle == EnumParticle.BLOCK_CRACK) {
			throw new IllegalArgumentException("Use createBlockVectorPacket for block particles!");
		}
		if (enumparticle == EnumParticle.REDSTONE && enumparticle == EnumParticle.SPELL_MOB_AMBIENT && enumparticle == EnumParticle.SPELL_MOB) {
			throw new IllegalArgumentException("This particle cannot be made into a vector particle");
		}
		return new PacketPlayOutWorldParticles(enumparticle, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), (float) direction.getX(), (float) direction.getY(), (float) direction.getZ(), speed, 0, null);

	}
	/**
	 * Creates a directional particle packet. BLOCKS ONLY
	 * @return PacketPlayOutWorldParticles packet, can be cached.
	 * @param enumparticle EnumParticle to create
	 * @param blockID ID of block
	 * @param blockData Data value of block
	 * @param loc Location of particle
	 * @param direction Vector of the velocity for particle
	 * @param speed Speed of particle to fly in direction
	 */
	public static PacketPlayOutWorldParticles createBlockVectorPacket(EnumParticle enumparticle, Location loc, Vector direction, float speed, Integer blockID, Integer blockData) {
		
		if (enumparticle != EnumParticle.ITEM_CRACK && enumparticle != EnumParticle.BLOCK_DUST && enumparticle != EnumParticle.BLOCK_CRACK) {
			throw new IllegalArgumentException("Use createVectorPacket for non block particles!");
		}
		if (blockID == null || blockData == null) {
			throw new IllegalArgumentException("You must have data for this particle!");
		}
		if (enumparticle == EnumParticle.ITEM_CRACK) {
			return new PacketPlayOutWorldParticles(enumparticle, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), (float) direction.getX(), (float) direction.getY(), (float) direction.getZ(), speed, 0, new int[]{blockID, blockData});
		} else {
			return new PacketPlayOutWorldParticles(enumparticle, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), (float) direction.getX(), (float) direction.getY(), (float) direction.getZ(), speed, 0, new int[]{blockID+(blockData*4096)});
		}
	}
	/**
	 * Creates a Block based particle
	 * @param enumparticle Particle Type: Item_Crack, Block_Dust, Block_Crack
	 * @param blockID ID of block
	 * @param blockData Data value of block
	 * @param loc Location of particle
	 * @param offset Vector of the offset for particle
	 * @param speed Speed of particle
	 * @param amount Amount of particles to spawn
	 */
	public static PacketPlayOutWorldParticles createBlockPacket(EnumParticle enumparticle, int blockID, int blockData, Location loc, Vector offset, float speed, int amount) {
		if (enumparticle != EnumParticle.ITEM_CRACK && enumparticle != EnumParticle.BLOCK_DUST && enumparticle != EnumParticle.BLOCK_CRACK) {
			throw new IllegalArgumentException("Please use createNormalPacket");
		}
		if (enumparticle == EnumParticle.ITEM_CRACK) {
			return new PacketPlayOutWorldParticles(enumparticle, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), (float) offset.getX(), (float) offset.getY(), (float) offset.getZ(), speed, amount, new int[]{blockID, blockData});
		} else {
			return new PacketPlayOutWorldParticles(enumparticle, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), (float) offset.getX(), (float) offset.getY(), (float) offset.getZ(), speed, amount, new int[]{blockID+(blockData*4096)});
		}
	}
	
	/**
	 * Creates a particle packet.
	 * @return PacketPlayOutWorldParticles packet, can be cached.
	 * @param enumparticle EnumParticle to create
	 * @param loc Location of particle
	 * @param offset Vector of the offset for particle
	 * @param speed Speed of particle
	 * @param amount Amount of particles to spawn
	 */
	public static PacketPlayOutWorldParticles createNormalPacket(EnumParticle enumparticle, Location loc, Vector offset, float speed, int amount) {
		
		if (enumparticle == EnumParticle.ITEM_CRACK || enumparticle == EnumParticle.BLOCK_DUST || enumparticle == EnumParticle.BLOCK_CRACK) {
			throw new IllegalArgumentException("Please use createBlockPacket");
		}
		return new PacketPlayOutWorldParticles(enumparticle, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), (float) offset.getX(), (float) offset.getY(), (float) offset.getZ(), speed, amount, null);
	}
	/**
	 * Sends a packet to all players but one
	 * @param packet Particle Packet
	 * @param player Player to display to
	 */
	public static void SendPacketToAllButOne(PacketPlayOutWorldParticles packet, Player playerToExclude, Location center) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player != playerToExclude) {
				SendPacket(packet, player, center);
			}
		}
	}
	
	public static int getViewDistance(Player player) {
		return 128;
	}
	
	/**
	 * Sends a packet to player
	 * @param packet Particle Packet
	 * @param player Player to display to
	 */
	public static void SendPacket(PacketPlayOutWorldParticles packet, Player player, Location center) {
		double range = getViewDistance(player);
		double squared = range * range;
		if (player.getLocation().distanceSquared(center) < squared) {
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
		}
	}
	/**
	 * Sends a packet to location. All players in range will see this packet.
	 * @param packet Particle Packet
	 * @param center Location to send particle to
	 * @param range Range to send to
	 */
	public static void SendPacket(PacketPlayOutWorldParticles packet, Location center) {
		for (Player player : center.getWorld().getPlayers()) {
			double range = getViewDistance(player);
			double squared = range * range;
			if (player.getLocation().toVector().setY(0).distanceSquared(center.toVector().setY(0)) < squared) {
				((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
			}
		}
	}

	public static void SendPacket(PacketPlayOutWorldParticles packet, Location center, int maxRange) {
		for (Player player : center.getWorld().getPlayers()) {
			double range = getViewDistance(player);
			if (range > maxRange) {
				range = maxRange;
			}
			double squared = range * range;
			if (player.getLocation().toVector().setY(0).distanceSquared(center.toVector().setY(0)) < squared) {
				((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
			}
		}
	}
}
