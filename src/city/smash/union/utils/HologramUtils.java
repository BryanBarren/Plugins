package city.smash.union.utils;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;

import city.smash.union.Main;
import city.smash.union.Variables;
import net.md_5.bungee.api.ChatColor;

public class HologramUtils {
	/**
	 * Displays a hologram with random offset at player. This defaults to display the hologram for ALL players
	 * @param player Location of player to spawn hologram at
	 * @param line Display text on hologram, can be color coded with "&"
	 */
	public static void displayAlertHolo(Player player, String line) {
		displayAlertHolo (player, line.split("\n"), Variables.getLong("holoalert_lastfor"), new HashSet<Player>());
	}
	/**
	 * Displays a hologram with random offset at player. This defaults to display the hologram for ALL players
	 * @param player Location of player to spawn hologram at
	 * @param line Display text on hologram, can be color coded with "&"
	 * @param lastFor Time in ticks before the hologram is automatically removed
	 */
	public static void displayAlertHolo(Player player, String line, Long lastFor) {
		displayAlertHolo (player, line.split("\n"), lastFor, new HashSet<Player>());
	}
	/**
	 * Displays a hologram with random offset at player. This defaults to display the hologram for ALL players
	 * @param player Location of player to spawn hologram at
	 * @param line Display an array of text on hologram, can be color coded with "&".
	 * @param lastFor Time in ticks before the hologram is automatically removed
	 */
	public static void displayAlertHolo(Player player, String[] line, Long lastFor) {
		displayAlertHolo (player, line, lastFor, new HashSet<Player>());
	}
	/**
	 * Displays a hologram with random offset at player. Shows hologram ONLY to specified "showTo" player
	 * @param player Location of player to spawn hologram at
	 * @param line Display text on hologram, can be color coded with "&"
	 * @param lastFor Time in ticks before the hologram is automatically removed
	 * @param showTo Player to display hologram to. ONLY this player will see the hologram
	 */
	public static void displayAlertHolo(Player player, String line, Long lastFor, Player showTo) {
		HashSet<Player> playersToShow = new HashSet<Player>();
		playersToShow.add(showTo);
		displayAlertHolo (player, line.split("\n"), lastFor, playersToShow);
	}
	/**
	 * Displays a hologram with random offset at player. Shows hologram ONLY to specified "showTo" player
	 * @param player Location of player to spawn hologram at
	 * @param line Display an array of text on hologram, can be color coded with "&".
	 * @param lastFor Time in ticks before the hologram is automatically removed
	 * @param showTo Player to display hologram to. ONLY this player will see the hologram
	 */
	public static void displayAlertHolo(Player player, String[] line, Long lastFor, Player showTo) {
		HashSet<Player> playersToShow = new HashSet<Player>();
		playersToShow.add(showTo);
		displayAlertHolo (player, line, lastFor, playersToShow);
	}
	/**
	 * Displays a hologram with random offset at player. Shows hologram ONLY to specified list of players
	 * @param player Location of player to spawn hologram at
	 * @param line Display an array of text on hologram, can be color coded with "&".
	 * @param lastFor Time in ticks before the hologram is automatically removed
	 * @param playersToShow HashSet of players to display hologram to. ONLY these player will see the hologram
	 */
	public static void displayAlertHolo(Player player, String line, Long lastFor, HashSet<Player> playersToShow) {
		displayAlertHolo (player, line.split("\n"), lastFor, playersToShow);
	}
	/**
	 * Displays a hologram with random offset at player. MAIN COMMAND
	 * @param player Location of player to spawn hologram at
	 * @param line Display an array of text on hologram, can be color coded with "&".
	 * @param lastFor Time in ticks before the hologram is automatically removed
	 * @param playersToShow HashSet of players to display hologram to. O/LY these player will see the hologram. If null or empty, will display to everyone
	 */
	public static void displayAlertHolo(Player player, String[] line, Long lastFor, HashSet<Player> playersToShow) {
		double radius = Variables.getDouble("HoloAlert_radius") + Main.random.nextDouble()*0.5;
		double y = (Main.random.nextDouble()*0.75) - 0.5;
		Vector vector = VectorUtils.getRandomCircleVector().multiply(radius);
		displayHolo(player.getEyeLocation().add(vector).add(0, y, 0), line, lastFor, playersToShow);
	}
	/**
	 * Displays a hologram at location. This defaults to display the hologram for ALL players
	 * @param location Location to display hologram at
	 * @param line Display text on hologram, can be color coded with "&".
	 * @param lastFor Time in ticks before the hologram is automatically removed
	 */
	public static void displayHolo(Location location, String line, Long lastFor) {
		displayHolo (location, line.split("\n"), lastFor, new HashSet<Player>());
	}
	/**
	 * Displays a hologram at location. This defaults to display the hologram for ALL players
	 * @param location Location to display hologram at
	 * @param line Display an array of text on hologram, can be color coded with "&".
	 * @param lastFor Time in ticks before the hologram is automatically removed
	 */
	public static void displayHolo(Location location, String[] line, Long lastFor) {
		displayHolo (location, line, lastFor, new HashSet<Player>());
	}
	/**
	 * Displays a hologram at location. Shows hologram ONLY to specified "showTo" player
	 * @param location Location to display hologram at
	 * @param line Display text on hologram, can be color coded with "&".
	 * @param lastFor Time in ticks before the hologram is automatically removed
	 * @param showTo Player to display hologram to. ONLY this player will see the hologram
	 */
	public static void displayHolo(Location location, String line, Long lastFor, Player showTo) {
		HashSet<Player> playersToShow = new HashSet<Player>();
		playersToShow.add(showTo);
		displayHolo (location, line.split("\n"), lastFor, playersToShow);
	}
	/**
	 * Displays a hologram at location. Shows hologram ONLY to specified "showTo" player
	 * @param location Location to display hologram at
	 * @param line Display an array of text on hologram, can be color coded with "&".
	 * @param lastFor Time in ticks before the hologram is automatically removed
	 * @param showTo Player to display hologram to. ONLY this player will see the hologram
	 */
	public static void displayHolo(Location location, String[] line, Long lastFor, Player showTo) {
		HashSet<Player> playersToShow = new HashSet<Player>();
		playersToShow.add(showTo);
		displayHolo (location, line, lastFor, playersToShow);
	}
	/**
	 * Displays a hologram at location. Shows hologram ONLY to specified list of players
	 * @param location Location to display hologram at
	 * @param line Display text on hologram, can be color coded with "&".
	 * @param lastFor Time in ticks before the hologram is automatically removed
	 * @param playersToShow HashSet of players to display hologram to. ONLY these player will see the hologram
	 */
	public static void displayHolo(Location location, String line, Long lastFor, HashSet<Player> playersToShow) {
		displayHolo (location, line.split("\n"), lastFor, playersToShow);
	}
	/**
	 * Displays a hologram at location. MAIN COMMAND
	 * @param location Location to display hologram at
	 * @param line Display text on hologram, can be color coded with "&".
	 * @param lastFor Time in ticks before the hologram is automatically removed
	 * @param playersToShow HashSet of players to display hologram to. ONLY these player will see the hologram. If null or empty, will display to everyone
	 */
	public static void displayHolo(Location location, String[] line, Long lastFor, HashSet<Player> playersToShow) {

		final Hologram hologram = HologramsAPI.createHologram(Main.instance, location.clone().add(0,0.28*line.length,0));
		if (!playersToShow.isEmpty()) {
			VisibilityManager vm = hologram.getVisibilityManager();
			for (Player target : playersToShow) {
				vm.showTo(target);
			}
			vm.setVisibleByDefault(false);
		}
		for (String currentLine : line) {
			hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', currentLine));
		}

		if (lastFor >= 0) {
			Main.instance.getServer().getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
				@Override
				public void run() {
					hologram.delete();
				}
			}, lastFor);
		}
	}
}
