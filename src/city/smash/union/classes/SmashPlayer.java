package city.smash.union.classes;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import city.smash.union.utils.DataUtils;

public class SmashPlayer {
	public SmashPlayer(Player player) {
		this.joinTime = System.currentTimeMillis();
		this.player = player;
	}

	private SmashTeam team = null;
	
	private Player player;
	private Player lastAttacker = null;
	
	private String tabName;
	private String prefix;
	private String guildTag;
	private String skinID;
	private String unionEquippedHat;
	private String currentClass;
	
	private long lastMeleeHit = 0L;
	private long lastCombatTag = 0L;
	private long distanceTraveled = 0L;
	private long joinTime = 0L;
	private long lastDamage = 0L;
	private long lastFireDamage = 0L;

	private float mana = 0;

	private double damageDelt = 0;
	private double damageTaken = 0;
	private double damageHealed = 0;

	private int points = 0;
	private int killStreak = 0;
	private int kills = 0;
	private int deaths = 0;
	private int assists = 0;
	private int spellsCasted = 0;
	private int boosterExpirationWarningID = -1;

	private boolean isVanished = false;
	private boolean isClaiming = false;
	
	private HashMap<SmashSetting, Short> settingsMap = new HashMap<SmashSetting, Short>();
	private HashMap<Integer, HashMap<ClickType, Runnable>> chestMap = new HashMap<Integer, HashMap<ClickType, Runnable>>();
	private HashMap<String, Long> warningCooldown = new HashMap<String, Long>();
	private HashMap<Player, Double> damageMap = new HashMap<Player, Double>();
	private HashMap<UUID, Double> barrierMap = new HashMap<UUID, Double>();
	private HashMap<UUID, Runnable> taskCommandList = new HashMap<UUID, Runnable>();

	public Player getPlayer() {
		for (UUID testPlayer : DataUtils.playerInfo.keySet()) {
			if (this == DataUtils.playerInfo.get(testPlayer)) {
				return Bukkit.getPlayer(testPlayer);
			}
		}
		return null;
	}

	public long getLastDamage() {
		return lastDamage;
	}

	public SmashTeam getTeam(Boolean checkCache) {
		if (checkCache) {
			if (team == null) {
				team = getTeam(false);
			}
			return team;
		} else {
			for (SmashTeam team : SmashGame.getGameInstance().getTeamList()) {
				if (team.getPlayers().contains(player)) {
					return team;
				}
			}
			return null;
		}
	}
	
	public void setLastDamage(long lastDamage) {
		this.lastDamage = lastDamage;
	}
	
	public Player getLastAttacker() {
		return lastAttacker;
	}

	public String getTabName() {
		return tabName;
	}

	public String getGuildTag() {
		return guildTag;
	}

	public long getLastMeleeHit() {
		return lastMeleeHit;
	}

	public long getLastCombatTag() {
		return lastCombatTag;
	}

	public float getMana() {
		return mana;
	}

	public boolean isVanished() {
		return isVanished;
	}

	public HashMap<SmashSetting, Short> getSettings() {
		return settingsMap;
	}

	public HashMap<Integer, HashMap<ClickType, Runnable>> getChestMap() {
		return chestMap;
	}

	public HashMap<String, Long> getWarningCooldown() {
		return warningCooldown;
	}

	public HashMap<Player, Double> getDamagers() {
		return damageMap;
	}

	public HashMap<UUID, Double> getBarrierMap() {
		return barrierMap;
	}

	public void setLastAttacker(Player lastAttacker) {
		this.lastAttacker = lastAttacker;
	}
	
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public void setGuildTag(String guildTag) {
		this.guildTag = guildTag;
	}

	public void setLastMeleeHit(long lastMeleeHit) {
		this.lastMeleeHit = lastMeleeHit;
	}
	
	public void setLastCombatTag(long lastCombatTag) {
		this.lastCombatTag = lastCombatTag;
	}

	public void setMana(float mana) {
		this.mana = mana;
	}

	public void setVanished(boolean isVanish) {
		this.isVanished = isVanish;
	}
	
	public void setChestMap(HashMap<Integer, HashMap<ClickType, Runnable>> chestMap) {
		this.chestMap = chestMap;
	}

	public String getSkinID() {
		return skinID;
	}

	public void setSkinID(String skinID) {
		this.skinID = skinID;
	}

	public double getDamageTaken() {
		return damageTaken;
	}

	public void setDamageTaken(double damageTaken) {
		this.damageTaken = damageTaken;
	}

	public double getDamageDelt() {
		return damageDelt;
	}

	public void setDamageDelt(double damageDelt) {
		this.damageDelt = damageDelt;
	}

	public double getDamageHealed() {
		return damageHealed;
	}

	public void setDamageHealed(double damageHealed) {
		this.damageHealed = damageHealed;
	}

	public long getDistanceTraveled() {
		return distanceTraveled;
	}

	public void setDistanceTraveled(long distanceTraveled) {
		this.distanceTraveled = distanceTraveled;
	}

	public int getKillStreak() {
		return killStreak;
	}

	public void setKillStreak(int killStreak) {
		this.killStreak = killStreak;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getAssists() {
		return assists;
	}

	public void setAssists(int assists) {
		this.assists = assists;
	}

	public int getSpellsCasted() {
		return spellsCasted;
	}

	public void setSpellsCasted(int spellsCasted) {
		this.spellsCasted = spellsCasted;
	}

	public long getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(long joinTime) {
		this.joinTime = joinTime;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public int getBoosterExpirationWarningID() {
		return boosterExpirationWarningID;
	}

	public void setBoosterExpirationWarningID(int boosterExpirationWarningID) {
		this.boosterExpirationWarningID = boosterExpirationWarningID;
	}

	public HashMap<UUID, Runnable> getTaskCommandList() {
		return taskCommandList;
	}

	public String getUnionEquippedHat() {
		return unionEquippedHat;
	}

	public void setUnionEquippedHat(String unionEquippedHat) {
		this.unionEquippedHat = unionEquippedHat;
	}

	public String getCurrentClass() {
		return currentClass;
	}

	public void setCurrentClass(String currentClass) {
		this.currentClass = currentClass;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public boolean isClaiming() {
		return isClaiming;
	}

	public void setClaiming(boolean isClaiming) {
		this.isClaiming = isClaiming;
	}

	public long getLastFireDamage() {
		return lastFireDamage;
	}

	public void setLastFireDamage(long lastFireDamage) {
		this.lastFireDamage = lastFireDamage;
	}
}