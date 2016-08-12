package city.smash.union.utils;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder.EnumWorldBorderAction;
import net.minecraft.server.v1_8_R3.WorldBorder;

public class BorderUtils {
	static int borderDist = 100000;
	
	public static void setPercentage(Player player, double percentage)
	{
		WorldBorder border = new WorldBorder();
		border.setCenter(player.getLocation().getX(), player.getLocation().getZ());
		border.setSize(borderDist);
		border.setWarningTime(15);
		
		if (percentage > 1) {
			percentage = 1;
		}

		int distEnd = (int) Math.abs((borderDist/2)/((percentage*0.99)-1));

		border.setWarningDistance(distEnd);
		
		for (EnumWorldBorderAction type : EnumWorldBorderAction.values()) {
			PacketPlayOutWorldBorder packet = new PacketPlayOutWorldBorder(border, type);
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
		}
		
	}
	
	public static void updateHealthBorder(Player player) {
		if (player.getHealth()/player.getMaxHealth() < 0.5) {
			double percentage = (0.5-(player.getHealth()/player.getMaxHealth())) * 2;
			setPercentage(player, percentage);
		} else {
			removeBorder(player);
		}
	}
	
	public static void removeBorder(Player player) {
		WorldBorder border = new WorldBorder();
		border.setCenter(0, 0);
		border.setSize(10000000);
		border.setWarningDistance(1);
		for (EnumWorldBorderAction type : EnumWorldBorderAction.values()) {
			PacketPlayOutWorldBorder packet = new PacketPlayOutWorldBorder(border, type);
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
		}
	}
}
