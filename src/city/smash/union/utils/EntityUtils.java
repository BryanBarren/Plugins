package city.smash.union.utils;

import org.bukkit.entity.Entity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class EntityUtils {
	/**
	 * Disables AI of entity
	 * @param entity Entity to disable AI for
	 */
	public static void toggleAI(Entity entity, boolean hasAI) {
		net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
		NBTTagCompound tag = nmsEntity.getNBTTag();
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		nmsEntity.c(tag);
		tag.setInt("NoAI", hasAI ? 0 : 1);
		nmsEntity.f(tag);
	}
}