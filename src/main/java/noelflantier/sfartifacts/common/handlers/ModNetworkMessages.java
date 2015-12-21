package noelflantier.sfartifacts.common.handlers;

import cpw.mods.fml.relauncher.Side;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnchantHammer;
import noelflantier.sfartifacts.common.network.messages.PacketEnergy;
import noelflantier.sfartifacts.common.network.messages.PacketExtendedEntityProperties;
import noelflantier.sfartifacts.common.network.messages.PacketFluid;
import noelflantier.sfartifacts.common.network.messages.PacketHammerConfig;
import noelflantier.sfartifacts.common.network.messages.PacketInductor;
import noelflantier.sfartifacts.common.network.messages.PacketInjector;
import noelflantier.sfartifacts.common.network.messages.PacketInvokStarting;
import noelflantier.sfartifacts.common.network.messages.PacketLightningRodStand;
import noelflantier.sfartifacts.common.network.messages.PacketLiquefier;
import noelflantier.sfartifacts.common.network.messages.PacketMachine;
import noelflantier.sfartifacts.common.network.messages.PacketMightyFoundry;
import noelflantier.sfartifacts.common.network.messages.PacketMightyFoundryGui;
import noelflantier.sfartifacts.common.network.messages.PacketParticleGlobal;
import noelflantier.sfartifacts.common.network.messages.PacketParticleMoving;
import noelflantier.sfartifacts.common.network.messages.PacketPillar;
import noelflantier.sfartifacts.common.network.messages.PacketPillarConfig;
import noelflantier.sfartifacts.common.network.messages.PacketRenderPillarModel;
import noelflantier.sfartifacts.common.network.messages.PacketRenderUpdate;
import noelflantier.sfartifacts.common.network.messages.PacketSound;
import noelflantier.sfartifacts.common.network.messages.PacketSoundEmitter;
import noelflantier.sfartifacts.common.network.messages.PacketSoundEmitterGui;
import noelflantier.sfartifacts.common.network.messages.PacketTeleport;
import noelflantier.sfartifacts.common.network.messages.PacketUpgradeHammer;

public class ModNetworkMessages {

	public static void loadMessages() {
	    PacketHandler.INSTANCE.registerMessage(PacketEnergy.class, PacketEnergy.class, PacketHandler.nextId(), Side.CLIENT);
	    PacketHandler.INSTANCE.registerMessage(PacketFluid.class, PacketFluid.class, PacketHandler.nextId(), Side.CLIENT);
	    PacketHandler.INSTANCE.registerMessage(PacketRenderUpdate.class, PacketRenderUpdate.class, PacketHandler.nextId(), Side.CLIENT);
	    PacketHandler.INSTANCE.registerMessage(PacketLiquefier.class, PacketLiquefier.class, PacketHandler.nextId(), Side.CLIENT);
	    PacketHandler.INSTANCE.registerMessage(PacketInjector.class, PacketInjector.class, PacketHandler.nextId(), Side.CLIENT);
	    PacketHandler.INSTANCE.registerMessage(PacketMightyFoundry.class, PacketMightyFoundry.class, PacketHandler.nextId(), Side.CLIENT);
	    PacketHandler.INSTANCE.registerMessage(PacketSoundEmitter.class, PacketSoundEmitter.class, PacketHandler.nextId(), Side.CLIENT);
	    PacketHandler.INSTANCE.registerMessage(PacketPillar.class, PacketPillar.class, PacketHandler.nextId(), Side.CLIENT);
	    PacketHandler.INSTANCE.registerMessage(PacketLightningRodStand.class, PacketLightningRodStand.class, PacketHandler.nextId(), Side.CLIENT);
	    PacketHandler.INSTANCE.registerMessage(PacketSound.class, PacketSound.class, PacketHandler.nextId(), Side.CLIENT);
	    PacketHandler.INSTANCE.registerMessage(PacketParticleMoving.class, PacketParticleMoving.class, PacketHandler.nextId(), Side.CLIENT);
	    PacketHandler.INSTANCE.registerMessage(PacketParticleGlobal.class, PacketParticleGlobal.class, PacketHandler.nextId(), Side.CLIENT);
	    PacketHandler.INSTANCE.registerMessage(PacketRenderPillarModel.class, PacketRenderPillarModel.class, PacketHandler.nextId(), Side.CLIENT);
	    PacketHandler.INSTANCE.registerMessage(PacketExtendedEntityProperties.class, PacketExtendedEntityProperties.class, PacketHandler.nextId(), Side.CLIENT);
	    PacketHandler.INSTANCE.registerMessage(PacketInductor.class, PacketInductor.class, PacketHandler.nextId(), Side.CLIENT);
	    
	    PacketHandler.INSTANCE.registerMessage(PacketSoundEmitterGui.class, PacketSoundEmitterGui.class, PacketHandler.nextId(), Side.SERVER);	    
	    PacketHandler.INSTANCE.registerMessage(PacketMachine.class, PacketMachine.class, PacketHandler.nextId(), Side.SERVER);
	    PacketHandler.INSTANCE.registerMessage(PacketUpgradeHammer.class, PacketUpgradeHammer.class, PacketHandler.nextId(), Side.SERVER);
	    PacketHandler.INSTANCE.registerMessage(PacketMightyFoundryGui.class, PacketMightyFoundryGui.class, PacketHandler.nextId(), Side.SERVER);
	    PacketHandler.INSTANCE.registerMessage(PacketTeleport.class, PacketTeleport.class, PacketHandler.nextId(), Side.SERVER);
	    PacketHandler.INSTANCE.registerMessage(PacketInvokStarting.class, PacketInvokStarting.class, PacketHandler.nextId(), Side.SERVER);
	    PacketHandler.INSTANCE.registerMessage(PacketHammerConfig.class, PacketHammerConfig.class, PacketHandler.nextId(), Side.SERVER);
	    PacketHandler.INSTANCE.registerMessage(PacketEnchantHammer.class, PacketEnchantHammer.class, PacketHandler.nextId(), Side.SERVER);
	    PacketHandler.INSTANCE.registerMessage(PacketPillarConfig.class, PacketPillarConfig.class, PacketHandler.nextId(), Side.SERVER);
	    
		//PacketHandler.INSTANCE.registerMessage(MessageTESTHandler.class, MessageTEST.class, PacketHandler.nextId(), Side.CLIENT);
	}
}
