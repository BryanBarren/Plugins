package city.smash.union.classes;

import org.bukkit.Location;

public class SmashLoc {
	private Location loc;
	private String extraDat;
	public SmashLoc(Location loc, String extraDat) {
		this.loc = loc;
		this.extraDat = extraDat;
	}
	public SmashLoc(Location loc) {
		this.loc = loc;
	}
	public Location getLocation() {
		return this.loc;
	}
	public String getData() {
		return this.extraDat;
	}
}
