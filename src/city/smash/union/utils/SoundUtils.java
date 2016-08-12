package city.smash.union.utils;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import city.smash.union.Main;

public class SoundUtils {
	private static HashSet<Long> soundHashSet = new HashSet<Long>();
	/**
	 * Plays sound at location. Everyone will hear this
	 * @param location Location to play sound
	 * @param sound Sound to play
	 * @param volume Volume of sound
	 * @param pitch Pitch of Sound
	 */
	public static void playSound(Sound sound, Location location, Float volume, Float pitch){
		addSound(location);
		double rolloffDistance = Math.max(16, 16 * volume);

		for (Player player : location.getWorld().getPlayers()) {
			if (location.distanceSquared(player.getLocation()) < rolloffDistance * rolloffDistance)
			{
				player.playSound(location, sound, volume, pitch);
			}
		}
	}
	/**
	 * Plays sound at location. Only the targeted player will hear
	 * @param player Player to send sound to
	 * @param sound Sound to play
	 * @param volume Volume of sound
	 * @param pitch Pitch of Sound
	 */
	public static void playSound(Sound sound, Player player, Float volume, Float pitch){
		addSound(player.getLocation());
		player.playSound(player.getLocation(), sound, volume, pitch);
	}

	public static void playSound(String sound, Location location, Float volume, Float pitch){
		addSound(location);
		double rolloffDistance = Math.max(16, 16 * volume);

		for (Player player : location.getWorld().getPlayers()) {
			if (location.distanceSquared(player.getLocation()) < rolloffDistance * rolloffDistance)
			{
				player.playSound(location, sound, volume, pitch);
			}
		}
	}
	public static void playSound(String sound, Player player, Float volume, Float pitch){
		addSound(player.getLocation());
		player.playSound(player.getLocation(), sound, volume, pitch);
	}

	public static void addSound(Location loc) {
		long hash=5;
		hash=(long) (hash*17+ ((Math.round((((loc.getX()*8))-4)/8)*8)+4));
		hash=(long) (hash*17+ ((Math.round((((loc.getY()*8))-4)/8)*8)+4));
		hash=(long) (hash*17+ ((Math.round((((loc.getZ()*8))-4)/8)*8)+4));

		soundHashSet.add(hash);
		long h = hash;
		Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {

			@Override
			public void run() {
				if (soundHashSet.contains(h)) {
					soundHashSet.remove(h);
				}
			}

		}, 1L);
	}

	public static void playGUISoundGood(Player player) {
		playSound(Sound.ANVIL_BREAK, player, 1F, 2F);
	}

	public static void playGUISoundClaim(Player player) {
		playSound(Sound.LEVEL_UP, player, 1F, 1F);
	}
	public static void playGUISoundClick(Player player) {
		playSound(Sound.CLICK, player, 0.33F, 1F);
	}
	public static void playGUISoundBad(Player player) {
		playSound(Sound.ANVIL_LAND, player, 1F, 1F);
	}

	public static boolean isServerSound(long hash) {
		return soundHashSet.contains(hash);
	}
	public static String getSoundOfBlockBreak(int id, String type) {
		switch (id) {
		case 145:
			return "block.anvil."+type.toLowerCase();
		case 35:
		case 81:
		case 171:
		case 354:
			return "block.cloth."+type.toLowerCase();
		case 20:
		case 102:
		case 95:
		case 160:
		case 174:
		case 79:
		case 89:
		case 169:
		case 120:
			return "block.glass."+type.toLowerCase();
		case 0:
		case 2:
		case 19:
		case 18:
		case 161:
		case 31:
		case 32:
		case 106:
		case 111:
		case 175:
		case 37:
		case 38:
		case 39:
		case 46:
		case 296:
		case 338:
		case 170:
		case 391:
		case 110:
		case 392:
			return "block.grass."+type.toLowerCase();
		case 3:
		case 13:
		case 82:
			return "block.gravel."+type.toLowerCase();
		case 65:
			return "block.ladder."+type.toLowerCase();
		case 12:
		case 88:
			return "block.sand."+type.toLowerCase();
		case 165:
			return "block.slime."+type.toLowerCase();
		case 78:
		case 80:
			return "block.snow."+type.toLowerCase();
		case 1:
		case 4:
		case 7:
		case 14:
		case 15:
		case 16:
		case 21:
		case 22:
		case 23:
		case 24:
		case 25:
		case 27:
		case 28:
		case 66:
		case 157:
		case 29:
		case 33:
		case 30:
		case 42:
		case 41:
		case 44:
		case 45:
		case 48:
		case 49:
		case 331:
		case 56:
		case 57:
		case 61:
		case 67:
		case 70:
		case 330:
		case 73:
		case 77:
		case 84:
		case 87:
		case 98:
		case 97:
		case 101:
		case 109:
		case 108:
		case 112:
		case 113:
		case 114:
		case 372:
		case 116:
		case 379:
		case 380:
		case 121:
		case 128:
		case 129:
		case 133:
		case 130:
		case 131:
		case 138:
		case 139:
		case 390:
		case 397:
		case 152:
		case 153:
		case 155:
		case 156:
		case 154:
		case 158:
		case 159:
		case 167:
		case 168:
		case 172:
		case 173:
		case 179:
		case 180:
		case 182:
			return "block.stone."+type.toLowerCase();
		case 5:
		case 17:
		case 162:
		case 355:
		case 50:
		case 47:
		case 107:
		case 53:
		case 85:
		case 324:
		case 54:
		case 58:
		case 323:
		case 69:
		case 72:
		case 96:
		case 126:
		case 188:
		case 189:
		case 190:
		case 191:
		case 192:
		case 183:
		case 184:
		case 185:
		case 186:
		case 187:
		case 86:
		case 91:
		case 356:
		case 404:
		case 103:
		case 134:
		case 135:
		case 136:
		case 163:
		case 164:
		case 143:
		case 148:
		case 147:
		case 146:
		case 151:
		case 425:
		case 427:
		case 428:
		case 429:
		case 430:
		case 431:
			return "block.wood."+type.toLowerCase();
		default:
			System.out.println("§c§lERROR: Sound id " + id + " no found in breakEnums!");
			return "block.anvil.fall";
		}
	}
}
