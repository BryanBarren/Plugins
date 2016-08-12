package city.smash.union.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import city.smash.union.Main;
import city.smash.union.utils.SoundUtils;

public class PacketListener {

	public static void startListeners() {
		startNonServerSoundListener();
	}

	private static void startNonServerSoundListener() {
		Main.protocolManager.addPacketListener(
				new PacketAdapter(Main.instance, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
					@Override
					public void onPacketSending(PacketEvent event) {
						long hash=5;
						for(int i = 0; i< 3; i++) {
							hash=hash*17+(long)event.getPacket().getSpecificModifier(int.class).read(i);
						}
						if (!SoundUtils.isServerSound(hash)) {
							//						String soundName = event.getPacket().getSpecificModifier(SoundEffect.class).read(0).a.b(event.getPacket().getSpecificModifier(SoundEffect.class).read(0)).a();
							event.setCancelled(true);
						}
					}
				}
				);
	}
}
