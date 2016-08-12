package city.smash.union.classes;

import org.bukkit.Location;

import net.citizensnpcs.api.npc.NPC;

public class SmashNPC {
	private NPC npc;
	private Location loc;
	public SmashNPC(NPC npc, Location loc) {
		this.npc = npc;
		this.loc = loc;
	}
	public NPC getEntity() {
		return npc;
	}
	public Location getLoc() {
		return loc;
	}
}
