package city.smash.union.utils;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.TileEntitySkull;

public class HeadUtils {
	public static ItemStack getSkull(String textureID) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		if (textureID == null || textureID.isEmpty()) {
			textureID = "4b92cb43333aa621c70eef4ebf299ba412b446fe12e341ccc582f3192189";
		}
		ItemMeta headMeta = head.getItemMeta();
		
		GameProfile profile = getSkinProfile(textureID);
		
		Field profileField = null;

		try {
			profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, profile);
		} catch (Exception e) {
			e.printStackTrace();
		}

		head.setItemMeta(headMeta);
		return head;
	}


	public static String getSkullURL(ItemStack head) {
		return getSkullURL(head.getItemMeta());
	}

	public static String getSkullURL(ItemMeta meta) {
		Field profileField = null;
		try {
			profileField = meta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);

			return getTextureFromProfile((GameProfile) profileField.get(meta));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getTextureFromProfile(GameProfile profile) {
		String result = "";
		for (Property test : profile.getProperties().get("textures")) {
			if (test.getName().equalsIgnoreCase("textures")) {
				result = EncodingUtils.decodeBase64(test.getValue());
			}
		}
		return result;
	}

	public static String getSkullTextureID(ItemStack head) {
		return getSkullTextureID(head.getItemMeta());
	}

	public static String getSkullTextureID(ItemMeta meta) {
		if (HeadUtils.getSkullURL(meta) == null) {
			return null;
		}
		return HeadUtils.getSkullURL(meta).split("http://textures.minecraft.net/texture/")[1].split("\"")[0];
	}
	
	public static GameProfile getSkinProfile(String skinURL) {
		GameProfile newSkinProfile = new GameProfile(UUID.randomUUID(), null);
		newSkinProfile.getProperties().put("textures", new Property("textures", EncodingUtils.encodeString("{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/"+skinURL+"\"}}}")));
		return newSkinProfile;
	}

	public static String getSkullBlockTexutreID(Block skull) {
		if(skull.getType() != Material.SKULL) {
			return null;
		}
		TileEntitySkull skullTile = (TileEntitySkull)((CraftWorld)skull.getWorld()).getHandle().getTileEntity(new BlockPosition(skull.getX(), skull.getY(), skull.getZ()));
		return getTextureFromProfile(skullTile.getGameProfile()).split("http://textures.minecraft.net/texture/")[1].split("\"")[0];
	}
	
	public static void setSkullBlockWithTextureID(String skinURL, Block skull) {
		if(skull.getType() != Material.SKULL) {
			return;
		}
		TileEntitySkull skullTile = (TileEntitySkull)((CraftWorld)skull.getWorld()).getHandle().getTileEntity(new BlockPosition(skull.getX(), skull.getY(), skull.getZ()));
		skullTile.setGameProfile(getSkinProfile(skinURL));
		skull.getState().update();
	}

}
