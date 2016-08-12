package city.smash.union.utils;

import java.lang.reflect.Field;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFirework;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import net.minecraft.server.v1_8_R3.EntityFireworks;

public class FireworkUtils {
	
	
	public static void launchInstantFirework(FireworkEffect effect, Location loc) {
		launchFirework(effect, loc, 1);
	}
	
	public static void launchFirework(FireworkEffect effect, Location loc, int delayTime) {
		
		if (delayTime < 1) {
			delayTime = 1;
		}
		
		final Firework f = loc.getWorld().spawn(loc, Firework.class);
		FireworkMeta fm = f.getFireworkMeta();
		fm.addEffect(effect);
		fm.setPower(100);
		f.setFireworkMeta(fm);

		EntityFireworks ef = ((CraftFirework)f).getHandle();
		if (delayTime < ef.expectedLifespan) {
			try {
				Field ticksFlown = EntityFireworks.class.getDeclaredField("ticksFlown");
				ticksFlown.setAccessible(true);
				ticksFlown.setInt(ef, ef.expectedLifespan - delayTime);
				ticksFlown.setAccessible(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
