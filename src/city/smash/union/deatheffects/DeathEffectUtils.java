package city.smash.union.deatheffects;

import java.util.HashMap;

import org.bukkit.entity.Player;

import city.smash.union.Main;

public class DeathEffectUtils {
	public static String getCurrentDeatheffect(Player player) {
		switch (Main.random.nextInt(3)) {
		case 0:
			return "default";
		case 1:
			return "pileofbones";
		case 2:
			return "tornado";
		}
		return "default";
	}
	
	public static void playDeathEffect(Player player) {
		getEffectByName(getCurrentDeatheffect(player)).play(player);
	}
	
	public static HashMap<String, DeathEffect> effectMap = new HashMap<String, DeathEffect>();
	
	public static DeathEffect getEffectByName(String effectName) {
		if (!effectMap.containsKey(effectName)) {
			String spellClass = DeathEffectUtils.class.getPackage().getName() + "." + effectName.substring(0, 1).toUpperCase() + effectName.substring(1).toLowerCase();
			try {
				effectMap.put(effectName, (DeathEffect) Class.forName(spellClass).newInstance());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Death Effect lookup reflection failure for spell:" + effectName);
			}
		}
		return effectMap.get(effectName);
	}
}