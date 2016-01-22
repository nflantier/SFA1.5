package noelflantier.sfartifacts.common.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.References;

public class SoundHelper {
	public static enum Sounds{
		ANVIL("random.anvil_use"),
		PORTALTRAVEL("portal.travel"),
		BASICHAMMER(new ResourceLocation(References.MODID+":basicHammer"));
		
		public boolean isVanilla = false;
		public String sound;
		public ResourceLocation rl;
		
		Sounds(String s){
			this.sound = s;
			isVanilla = true;
			rl = null;
		}
		Sounds(ResourceLocation r){
			this.sound = null;
			isVanilla = false;
			rl = r;
		}
	};
	
	public static void playPositionedSound(Sounds s, Minecraft m, double x, double y, double z, float volume){
		if(s.isVanilla){
			m.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(s.sound), volume, 1.0F, (float)x, (float)y, (float)z));
		}else{
			m.getSoundHandler().playSound(new PositionedSoundRecord(s.rl, volume, 1.0F, (float)x, (float)y, (float)z));
		}
	}
}
