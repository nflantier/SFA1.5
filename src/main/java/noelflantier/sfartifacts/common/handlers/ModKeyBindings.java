package noelflantier.sfartifacts.common.handlers;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import noelflantier.sfartifacts.References;

public class ModKeyBindings {

	public static KeyBinding hammerConfig;
	
	public static void loadBindings(){
    	hammerConfig = new KeyBinding("Hammer Config Panel", Keyboard.KEY_H, References.NAME);
    	ClientRegistry.registerKeyBinding(hammerConfig);
	}
}
