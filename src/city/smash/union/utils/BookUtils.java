package city.smash.union.utils;

import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import city.smash.union.utils.ReflectionUtils.PackageType;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;

public class BookUtils {

	/**
	 * Open a "Virtual" Book ItemStack.
	 * @param i Book ItemStack.
	 * @param p Player that will open the book.
	 * @return
	 */
	public static void openBook(ItemStack i, Player p) {
		int slot = p.getInventory().getHeldItemSlot();
		ItemStack old = p.getInventory().getItem(slot);
		p.getInventory().setItem(slot, i);
		PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(Unpooled.EMPTY_BUFFER));
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
		p.getInventory().setItem(slot, old);
	}

	/**
	 * Set the pages of the book in JSON format.
	 * @param metadata BookMeta of the Book ItemStack.
	 * @param pages Each page to be added to the book.
	 */
	@SuppressWarnings("unchecked")
	public static void setPages(BookMeta metadata, List<String> pages) {
		List<Object> p;
		Object page;

		try {
			p = (List<Object>) ReflectionUtils.getField(PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftMetaBook"), true, "pages").get(metadata);
			for (String text : pages) {
				page = ReflectionUtils.invokeMethod(ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("IChatBaseComponent$ChatSerializer").newInstance(), "a", text);
				p.add(page);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
