package city.smash.union.classes;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import com.gmail.filoghost.holographicdisplays.api.Hologram;

public class SmashEntity {
	private Entity entity;
	private Hologram hologram;
	private Location loc;
	public SmashEntity(Entity entity, Hologram hologram, Location loc) {
		this.entity = entity;
		this.hologram = hologram;
		this.loc = loc;
	}
	public Entity getEntity() {
		return entity;
	}
	public Location getLoc() {
		return loc;
	}
	public Hologram getHologram() {
		return hologram;
	}
}
