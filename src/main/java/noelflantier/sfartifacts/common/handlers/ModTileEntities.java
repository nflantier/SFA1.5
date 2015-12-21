package noelflantier.sfartifacts.common.handlers;

import noelflantier.sfartifacts.common.blocks.tiles.TileAsgardianMachine;
import noelflantier.sfartifacts.common.blocks.tiles.TileControlPannel;
import noelflantier.sfartifacts.common.blocks.tiles.TileHammerStand;
import noelflantier.sfartifacts.common.blocks.tiles.TileInductor;
import noelflantier.sfartifacts.common.blocks.tiles.TileInjector;
import noelflantier.sfartifacts.common.blocks.tiles.TileLightningRodStand;
import noelflantier.sfartifacts.common.blocks.tiles.TileLiquefier;
import noelflantier.sfartifacts.common.blocks.tiles.TileMachine;
import noelflantier.sfartifacts.common.blocks.tiles.TileMightyFoundry;
import noelflantier.sfartifacts.common.blocks.tiles.TileMrFusion;
import noelflantier.sfartifacts.common.blocks.tiles.TileRenderPillarModel;
import noelflantier.sfartifacts.common.blocks.tiles.TileSFA;
import noelflantier.sfartifacts.common.blocks.tiles.TileSoundEmitter;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileBlockPillar;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileInterfacePillar;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileMasterPillar;
import noelflantier.sfartifacts.common.helpers.RegisterHelper;

public class ModTileEntities {
	
	public static void loadTileEntities() {
    	RegisterHelper.registerTileEntity(TileLightningRodStand.class,"TileLightningRodStand");
    	RegisterHelper.registerTileEntity(TileHammerStand.class,"TileHammerStand");
    	RegisterHelper.registerTileEntity(TileControlPannel.class,"TileControlPanel");
    	RegisterHelper.registerTileEntity(TileLiquefier.class,"TileLiquefier");
    	RegisterHelper.registerTileEntity(TileInjector.class,"TileInjector");
    	RegisterHelper.registerTileEntity(TileMightyFoundry.class,"TileMightyFoundry");
    	RegisterHelper.registerTileEntity(TileSFA.class,"TileSFA");
    	RegisterHelper.registerTileEntity(TileAsgardianMachine.class,"TileAsgardianMachine");
    	RegisterHelper.registerTileEntity(TileMachine.class,"TileMachine");
    	RegisterHelper.registerTileEntity(TileSoundEmitter.class,"TileSoundEmiter");
    	RegisterHelper.registerTileEntity(TileMrFusion.class,"TileMrFusion");
    	RegisterHelper.registerTileEntity(TileInductor.class,"TileInductor");
    	
    	RegisterHelper.registerTileEntity(TileRenderPillarModel.class,"TileRenderPillarModel");
    	RegisterHelper.registerTileEntity(TileBlockPillar.class,"TileBlockPillar");
    	RegisterHelper.registerTileEntity(TileInterfacePillar.class,"TileInterfacePillar");
    	RegisterHelper.registerTileEntity(TileMasterPillar.class,"TileMasterPillar");
	}
}
