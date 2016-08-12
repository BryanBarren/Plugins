package city.smash.union.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import city.smash.union.Main;
import city.smash.union.Variables;
import city.smash.union.utils.ChatUtils;
import city.smash.union.utils.DataUtils;
import city.smash.union.utils.EncodingUtils;
import city.smash.union.utils.TaskUtils;

public class SmashGame {
	private static SmashGame gameInstance = null;

	public static SmashGame getGameInstance() {
		return gameInstance;
	}
	public static void newGame() {
		gameInstance = new SmashGame(EncodingUtils.decodeBase64("ew0KICAibmFtZSI6ICJQb2xvTWFwIiwNCiAgInNpemVvZnRlYW0iOiA2LA0KICAibWluaW11bXBsYXllcnN0YXJ0IjogMjAsDQogICJtaW5pbXVtcGxheWVyY2FuY2VsIjogMTYsDQogICJraWxsc3Rvd2luIjogNTAsDQogICJnYW1lc3RhcnR0aW1lciI6IDI1LA0KICAiZ2FtZXRpbWVyIjogMzYwMDAwMCwNCiAgInNwYXducHJvdGVjdGlvbiI6IDIwMCwNCiAgImxvYmJ5c3Bhd24iOiB7DQogICAgIngiOiAtNC41LA0KICAgICJ5IjogMTI4LA0KICAgICJ6IjogMTA0LjUsDQogICAgInlhdyI6IDE4MCwNCiAgICAicGl0Y2giOiAwDQogIH0sDQogICJsb2JieWFhYmIiOiB7DQogICAgInhhIjogMiwNCiAgICAieWEiOiAxMjAsDQogICAgInphIjogOTYsDQogICAgInhiIjogLTE3LA0KICAgICJ5YiI6IDE0NiwNCiAgICAiemIiOiAxMTUNCiAgfSwNCiAgInRlYW1zIjogew0KICAgICJyZWQiOiB7DQogICAgICAibmFtZSI6ICJSZWQgVGVhbSIsDQogICAgICAiY29sb3IiOiAiwqdjIiwNCiAgICAgICJpY29uIjogIjEyMDowIiwNCiAgICAgICJzcGF3bnBvaW50cyI6IFsNCiAgICAgICAgew0KICAgICAgICAgICJ4IjogMTUsDQogICAgICAgICAgInkiOiAxNSwNCiAgICAgICAgICAieiI6IDE1LA0KICAgICAgICAgICJ5YXciOiA5MCwNCiAgICAgICAgICAicGl0Y2giOiAwDQogICAgICAgIH0sDQogICAgICAgIHsNCiAgICAgICAgICAieCI6IDE1LA0KICAgICAgICAgICJ5IjogMTUsDQogICAgICAgICAgInoiOiAxNSwNCiAgICAgICAgICAieWF3IjogOTAsDQogICAgICAgICAgInBpdGNoIjogMA0KICAgICAgICB9DQogICAgICBdLA0KICAgICAgImNsYXNzdmlsbGFnZXJzIjogWw0KICAgICAgICB7DQogICAgICAgICAgIngiOiAyLjUsDQogICAgICAgICAgInkiOiAxMDAsDQogICAgICAgICAgInoiOiAyNC41LA0KICAgICAgICAgICJ5YXciOiA5MCwNCiAgICAgICAgICAicGl0Y2giOiAwDQogICAgICAgIH0NCiAgICAgIF0sDQogICAgICAidGVhbXVwZ3JhZGVsb2NhdGlvbnMiOiB7DQogICAgICAgICJzdHJlbmd0aCI6IFsNCiAgICAgICAgICB7DQogICAgICAgICAgICAieCI6IDEsDQogICAgICAgICAgICAieSI6IDEwMCwNCiAgICAgICAgICAgICJ6IjogMjYNCiAgICAgICAgICB9DQogICAgICAgIF0sDQogICAgICAgICJzcGVlZCI6IFsNCiAgICAgICAgICB7DQogICAgICAgICAgICAieCI6IDIsDQogICAgICAgICAgICAieSI6IDEwMCwNCiAgICAgICAgICAgICJ6IjogMjYNCiAgICAgICAgICB9DQogICAgICAgIF0sDQogICAgICAgICJyZXNpc3RhbmNlIjogWw0KICAgICAgICAgIHsNCiAgICAgICAgICAgICJ4IjogMywNCiAgICAgICAgICAgICJ5IjogMTAwLA0KICAgICAgICAgICAgInoiOiAyNg0KICAgICAgICAgIH0NCiAgICAgICAgXSwNCiAgICAgICAgIm1vbmV5Z2FpbiI6IFsNCiAgICAgICAgICB7DQogICAgICAgICAgICAieCI6IDQsDQogICAgICAgICAgICAieSI6IDEwMCwNCiAgICAgICAgICAgICJ6IjogMjYNCiAgICAgICAgICB9DQogICAgICAgIF0sDQogICAgICAgICJtYXhoZWFsdGgiOiBbDQogICAgICAgICAgew0KICAgICAgICAgICAgIngiOiA1LA0KICAgICAgICAgICAgInkiOiAxMDAsDQogICAgICAgICAgICAieiI6IDI2DQogICAgICAgICAgfQ0KICAgICAgICBdDQogICAgICB9DQogICAgfQ0KICB9DQp9"));
	}

	private ArrayList<SmashTeam> teamList = new ArrayList<SmashTeam>();

	Comparator<SmashTeam> compareator = new Comparator<SmashTeam>() {
		public int compare(SmashTeam team1, SmashTeam team2) {
			return team1.getPlayers().size() - team2.getPlayers().size();
		}
	};

	//-1 In Progress
	//0 Filling up now
	//1 Full, not started
	private int gameStatus;
	private String gameType;
	private String map;
	private String mapName;

	private int sizeOfTeam;
	private int minimumPlayerStart;
	private int minimumPlayerCancel;
	private int killsToWin;

	private Location lobbySpawn;
	private Location lobbyAA;
	private Location lobbyBB;

	private int gameTimer;
	private int gameStartTimer;

	private UUID gameStartTSID = null;

	public Location getLobbySpawn() {
		return lobbySpawn;
	}

	public int getGameStatus() {
		return gameStatus;
	}

	public ArrayList<SmashTeam> getTeamList() {
		return teamList;
	}

	public int getMaxPlayers() {
		return getTeamList().size() * sizeOfTeam;
	}

	public void startGame() {

		balanceRemainingPlayers();

		for (SmashTeam team : teamList) {
			team.setupTeam();
		}

		removeLobby();

		ChatUtils.broadcastMessage(formatPlaceholders(Variables.getString("messages_gamestart"), null));
	}

	public String formatPlaceholders (String string, Player player) {
		if (player != null) {
			SmashTeam team = DataUtils.getPlayer(player).getTeam(true);
			if (team != null) {
				string = string.replace("%team%", team.getName());
				string = string.replace("%teamcolor%", team.getColor());
			}
			string = string.replace("%player%", player.getDisplayName());
		}

		string = string.replace("%online%", Integer.toString(Bukkit.getOnlinePlayers().size()));
		string = string.replace("%maxplayers%", Integer.toString(SmashGame.getGameInstance().getMaxPlayers()));

		string = string.replace("%startdelay%", Long.toString(gameStartTimer/20));
		string = string.replace("%playersneededforstart%", Integer.toString(Bukkit.getOnlinePlayers().size()-minimumPlayerStart));
		return string;
	}

	public void addPlayer (Player player) {
		if (getGameStatus() == -1) {
			ChatUtils.broadcastMessage(formatPlaceholders(Variables.getString("messages_joingame"), player));
			return;
		} else {
			ChatUtils.broadcastMessage(formatPlaceholders(Variables.getString("messages_joingamelobby"), player));
		}

		if (gameStartTSID == null && Bukkit.getOnlinePlayers().size() >= minimumPlayerStart) {
			ChatUtils.broadcastMessage(formatPlaceholders(Variables.getString("messages_gamestartstart"), player));
			gameStartTSID = UUID.randomUUID();
			int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable() {
				int i = gameStartTimer;
				@Override
				public void run() {
					if (Bukkit.getOnlinePlayers().size() <= minimumPlayerCancel) {

						ChatUtils.broadcastMessage(formatPlaceholders(Variables.getString("messages_gamestartend"), player));

						TaskUtils.killTask(gameStartTSID);
						gameStartTSID = null;
					}

					if (i == 0) {;
					startGame();

					TaskUtils.killTask(gameStartTSID);
					return;
					} else {
						shell: {
						if (i%20 == 0) {
							int sec = i%20;
							if (sec > 5 && sec%5!=0) {
								break shell;
							}
							ChatUtils.broadcastMessage(formatPlaceholders(Variables.getString("messages_gamestartcountdown"), player));
						}
					}
					}

					i--;
				}
			}, 0, 1);
			TaskUtils.startTask(gameStartTSID, id, null, null);
		}
	}

	public void joinTeam(Player player, SmashTeam team) {
		team.addPlayer(player);
	}

	public void removePlayer(Player player) {
		if (getGameStatus() == -1) {
			ChatUtils.broadcastMessage(formatPlaceholders(Variables.getString("messages_quitgame"), player));
		} else {
			ChatUtils.broadcastMessage(formatPlaceholders(Variables.getString("messages_quitgamelobby"), player));
		}

		DataUtils.getPlayer(player).getTeam(false).removePlayer(player);
	}

	public void balanceRemainingPlayers() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (DataUtils.getPlayer(player).getTeam(false) != null) {
				continue;
			}
			Collections.sort(teamList, compareator);
			teamList.get(0).addPlayer(player);
		}
	}

	private void removeLobby() {
		for (int x = lobbyAA.getBlockX() ; x != lobbyBB.getBlockX() ; x += Math.signum(lobbyBB.getBlockX()-lobbyAA.getBlockX())) {
			for (int y = lobbyAA.getBlockY() ; y != lobbyBB.getBlockY() ; y += Math.signum(lobbyBB.getBlockY()-lobbyAA.getBlockY())) {
				for (int z = lobbyAA.getBlockZ() ; z != lobbyBB.getBlockZ() ; z += Math.signum(lobbyBB.getBlockZ()-lobbyAA.getBlockZ())) {
					(new Location(Bukkit.getWorlds().get(0), x, y, z, 0, 0)).getBlock().setType(Material.AIR);
				}
			}
		}
	}

	private SmashGame(String jsParse) {
		if (gameInstance != null) {
			return;
		}
		gameInstance = this;

		JSONObject json = new JSONObject(jsParse);
		this.mapName = json.getString("name");
		this.sizeOfTeam = json.getInt("sizeofteam");
		this.minimumPlayerStart = json.getInt("minimumplayerstart");
		this.killsToWin = json.getInt("killstowin");
		this.gameStartTimer = json.getInt("gamestarttimer");
		this.gameTimer = json.getInt("gametimer");

		JSONObject lobbySpawn = json.getJSONObject("lobbyspawn");
		this.lobbySpawn = new Location(Bukkit.getWorlds().get(0), lobbySpawn.getDouble("x"), lobbySpawn.getDouble("y"), lobbySpawn.getDouble("z"), (float)lobbySpawn.getDouble("yaw"), (float)lobbySpawn.getDouble("pitch"));

		JSONObject lobbyAABB = json.getJSONObject("lobbyaabb");
		this.lobbyAA = new Location(Bukkit.getWorlds().get(0), lobbyAABB.getDouble("xa"), lobbyAABB.getDouble("ya"), lobbyAABB.getDouble("za"), 0, 0);
		this.lobbyBB = new Location(Bukkit.getWorlds().get(0), lobbyAABB.getDouble("xb"), lobbyAABB.getDouble("yb"), lobbyAABB.getDouble("zb"), 0, 0);

		JSONObject teams = json.getJSONObject("teams");
		for (String teamID : teams.keySet()) {
			JSONObject teamJson = teams.getJSONObject(teamID);
			teamList.add(new SmashTeam(teamID, teamJson));
		}
	}
}
