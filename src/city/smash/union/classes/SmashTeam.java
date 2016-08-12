package city.smash.union.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Skull;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;

import city.smash.union.Main;
import city.smash.union.Variables;
import city.smash.union.utils.ChatUtils;
import city.smash.union.utils.HeadUtils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class SmashTeam {
	private String id;
	private String name;
	
	public String getName() {
		return name;
	}
	
	private String color;
	
	public String getColor() {
		return color;
	}
	
	private ItemStack icon;

	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Location> spawnPoints = new ArrayList<Location>();
	private ArrayList<SmashNPC> classVillagers = new ArrayList<SmashNPC>();
	
	private int kills;
	private int deaths;
	private int assists;
	private HashMap<SmashUpgradeType, SmashUpgrade> teamUpgrades = new HashMap<SmashUpgradeType, SmashUpgrade>();

	public void addPlayer (Player player) {
		players.add(player);
		ChatUtils.broadcastMessage(SmashGame.getGameInstance().formatPlaceholders(Variables.getString("messages_jointeam"), player));
	}
	public void removePlayer (Player player) {
		players.remove(player);
	}
	
	public boolean canJoin() {
		for (SmashTeam team : SmashGame.getGameInstance().getTeamList()) {
			if (team.getPlayers().size() < players.size()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean joinTeam(Player player) {
		if (canJoin()) {
			addPlayer(player);
			return true;
		} else {
			return false;
		}
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public void setupTeam() {
		for (SmashNPC npc : classVillagers) {
			npc.getEntity().spawn(npc.getLoc());
			((Villager)npc.getEntity().getEntity()).setProfession(Profession.LIBRARIAN);
			
			Hologram hologram = HologramsAPI.createHologram(Main.instance, npc.getLoc().clone().add(0, 2.56, 0));
			VisibilityManager vm = hologram.getVisibilityManager();
			hologram.appendTextLine("§a§lClass Selector");
			hologram.appendTextLine("§f§lRight Click");
			for (Player target : getPlayers()) {
				vm.showTo(target);
			}
			vm.setVisibleByDefault(false);
		}
		
		for (SmashUpgrade smashUpgrade : teamUpgrades.values()) {
			smashUpgrade.instantiateUpgrade();
		}
		
		for (Player player : getPlayers()) {
			warpPlayer(player);
		}
	}
	
	public void warpPlayer(Player player) {
		player.teleport(spawnPoints.get(Main.random.nextInt(spawnPoints.size())));
	}
	
	public SmashTeam(String teamID, JSONObject teamJson) {
		this.id = teamID;
		this.name = teamJson.getString("name");
		this.color = teamJson.getString("color");
		
		JSONArray villagersJson = teamJson.getJSONArray("classvillagers");
		for (int i = 0; i < villagersJson.length(); i++) {
			JSONObject villagerJson = villagersJson.getJSONObject(i);
			Location villagerLoc = new Location(Bukkit.getWorlds().get(0), villagerJson.getDouble("x"), villagerJson.getDouble("y"), villagerJson.getDouble("z"), (float)villagerJson.getDouble("yaw"), (float)villagerJson.getDouble("pitch"));

			NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.VILLAGER, teamID + "_Class_Villager_" + i);
			
			classVillagers.add(new SmashNPC(npc, villagerLoc));
		}
		
		JSONObject teamUpgradesJson = teamJson.getJSONObject("teamupgradelocations");
		for (String _upgrade : teamUpgradesJson.keySet()) {
			System.out.println(_upgrade);
			SmashUpgradeType upgradeID = SmashUpgradeType.valueOf(_upgrade.toUpperCase());
			SmashUpgrade smashUpgrade = new SmashUpgrade(upgradeID, this);
			teamUpgrades.put(upgradeID, smashUpgrade);
			
			JSONArray teamUpgrade = teamUpgradesJson.getJSONArray(_upgrade);
			for (int i = 0; i < teamUpgrade.length(); i++) {
				JSONObject locationJson = teamUpgrade.getJSONObject(i);
				Location location = new Location(Bukkit.getWorlds().get(0), locationJson.getDouble("x"), locationJson.getDouble("y"), locationJson.getDouble("z"), 0, 0);
				if (locationJson.has("skinid")) {
					if (location.getBlock().getState() instanceof Skull) {
						HeadUtils.setSkullBlockWithTextureID(locationJson.getString("skinid"), location.getBlock());
					}
				}
				smashUpgrade.addBlockLocation(location.getBlock().getLocation());
			}
		}

		JSONArray spawnPointsJson = teamJson.getJSONArray("classvillagers");
		for (int i = 0; i < spawnPointsJson.length(); i++) {
			JSONObject spawnPointJson = spawnPointsJson.getJSONObject(i);
			Location spawnLoc = new Location(Bukkit.getWorlds().get(0), spawnPointJson.getDouble("x"), spawnPointJson.getDouble("y"), spawnPointJson.getDouble("z"), (float)spawnPointJson.getDouble("yaw"), (float)spawnPointJson.getDouble("pitch"));
			spawnPoints.add(spawnLoc);
		}
	}
}