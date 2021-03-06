package noelflantier.sfartifacts.client;


import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import noelflantier.sfartifacts.client.render.RenderBlockControlPanel;
import noelflantier.sfartifacts.client.render.RenderBlockHammerStand;
import noelflantier.sfartifacts.client.render.RenderBlockInductor;
import noelflantier.sfartifacts.client.render.RenderBlockInjector;
import noelflantier.sfartifacts.client.render.RenderBlockLightningRodStand;
import noelflantier.sfartifacts.client.render.RenderBlockLiquefier;
import noelflantier.sfartifacts.client.render.RenderBlockMaterials;
import noelflantier.sfartifacts.client.render.RenderBlockMightyFoundry;
import noelflantier.sfartifacts.client.render.RenderBlockMrFusion;
import noelflantier.sfartifacts.client.render.RenderBlockRecharger;
import noelflantier.sfartifacts.client.render.RenderBlockSoundEmitter;
import noelflantier.sfartifacts.client.render.RenderEntityHammerInvoking;
import noelflantier.sfartifacts.client.render.RenderEntityHammerMining;
import noelflantier.sfartifacts.client.render.RenderEntityHoverBoard;
import noelflantier.sfartifacts.client.render.RenderEntityHulk;
import noelflantier.sfartifacts.client.render.RenderEntityShieldThrow;
import noelflantier.sfartifacts.client.render.RenderItemFluidModule;
import noelflantier.sfartifacts.client.render.RenderItemHoverBoard;
import noelflantier.sfartifacts.client.render.RenderItemLightningRod;
import noelflantier.sfartifacts.client.render.RenderItemThorHammer;
import noelflantier.sfartifacts.client.render.RenderItemUberMightyFeather;
import noelflantier.sfartifacts.client.render.RenderItemVibraniumShield;
import noelflantier.sfartifacts.common.CommonProxy;
import noelflantier.sfartifacts.common.blocks.BlockControlPannel;
import noelflantier.sfartifacts.common.blocks.BlockInjector;
import noelflantier.sfartifacts.common.blocks.BlockLightningRodStand;
import noelflantier.sfartifacts.common.blocks.BlockLiquefier;
import noelflantier.sfartifacts.common.blocks.BlockMaterialsTE;
import noelflantier.sfartifacts.common.blocks.BlockSoundEmitter;
import noelflantier.sfartifacts.common.blocks.tiles.TileControlPannel;
import noelflantier.sfartifacts.common.blocks.tiles.TileHammerStand;
import noelflantier.sfartifacts.common.blocks.tiles.TileInductor;
import noelflantier.sfartifacts.common.blocks.tiles.TileInjector;
import noelflantier.sfartifacts.common.blocks.tiles.TileLightningRodStand;
import noelflantier.sfartifacts.common.blocks.tiles.TileLiquefier;
import noelflantier.sfartifacts.common.blocks.tiles.TileMightyFoundry;
import noelflantier.sfartifacts.common.blocks.tiles.TileMrFusion;
import noelflantier.sfartifacts.common.blocks.tiles.TileRecharger;
import noelflantier.sfartifacts.common.blocks.tiles.TileRenderPillarModel;
import noelflantier.sfartifacts.common.blocks.tiles.TileSoundEmitter;
import noelflantier.sfartifacts.common.entities.EntityHammerInvoking;
import noelflantier.sfartifacts.common.entities.EntityHammerMinning;
import noelflantier.sfartifacts.common.entities.EntityHoverBoard;
import noelflantier.sfartifacts.common.entities.EntityHulk;
import noelflantier.sfartifacts.common.entities.EntityShieldThrow;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.handlers.ModEventsClient;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.handlers.ModKeyBindings;
import noelflantier.sfartifacts.common.handlers.ModKeyInput;

public class ClientProxy extends CommonProxy{

	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);

		//MinecraftForgeClient.registerItemRenderer(ModItems.itemEnergyModule, new RenderItemEnergyModule());
		MinecraftForgeClient.registerItemRenderer(ModItems.itemFluidModule, new RenderItemFluidModule());
		MinecraftForgeClient.registerItemRenderer(ModItems.itemLightningRod, new RenderItemLightningRod());
		MinecraftForgeClient.registerItemRenderer(ModItems.itemThorHammer, new RenderItemThorHammer());
		MinecraftForgeClient.registerItemRenderer(ModItems.itemVibraniumShield, new RenderItemVibraniumShield());
		MinecraftForgeClient.registerItemRenderer(ModItems.itemHoverboard, new RenderItemHoverBoard());
		MinecraftForgeClient.registerItemRenderer(ModItems.itemUberMightyFeather, new RenderItemUberMightyFeather());
		

        BlockLightningRodStand.renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderBlockLightningRodStand tblrs = new RenderBlockLightningRodStand(ModBlocks.blockLightningRodStand);
    	RenderingRegistry.registerBlockHandler(tblrs);
		ClientRegistry.bindTileEntitySpecialRenderer(TileLightningRodStand.class, tblrs);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockLightningRodStand), tblrs);

		BlockLiquefier.renderId = RenderingRegistry.getNextAvailableRenderId();
		RenderBlockLiquefier rbl = new RenderBlockLiquefier(ModBlocks.blockLiquefier); 
    	RenderingRegistry.registerBlockHandler(rbl);
		ClientRegistry.bindTileEntitySpecialRenderer(TileLiquefier.class, rbl);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockLiquefier), rbl);

		BlockInjector.renderId = RenderingRegistry.getNextAvailableRenderId();
		RenderBlockInjector rbi = new RenderBlockInjector(ModBlocks.blockInjector);
    	RenderingRegistry.registerBlockHandler(rbi);
		ClientRegistry.bindTileEntitySpecialRenderer(TileInjector.class, rbi);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockInjector), rbi);
		
		BlockControlPannel.renderId = RenderingRegistry.getNextAvailableRenderId();
		RenderBlockControlPanel rbcp = new RenderBlockControlPanel();
		ClientRegistry.bindTileEntitySpecialRenderer(TileControlPannel.class, rbcp);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockControlPanel), rbcp);
		
		//BlockOreVibranium.renderId = RenderingRegistry.getNextAvailableRenderId();
		RenderBlockMightyFoundry rbmf = new RenderBlockMightyFoundry();
		ClientRegistry.bindTileEntitySpecialRenderer(TileMightyFoundry.class, rbmf);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockMightyFoundry), rbmf);
		
		RenderBlockHammerStand rbhs = new RenderBlockHammerStand();
		ClientRegistry.bindTileEntitySpecialRenderer(TileHammerStand.class, rbhs);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockHammerStand), rbhs);
		
		RenderBlockMrFusion rbmrf = new RenderBlockMrFusion();
		ClientRegistry.bindTileEntitySpecialRenderer(TileMrFusion.class, rbmrf);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockMrFusion), rbmrf);
		
		RenderBlockInductor rbin = new RenderBlockInductor();
		ClientRegistry.bindTileEntitySpecialRenderer(TileInductor.class, rbin);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockInductor), rbin);

		RenderBlockRecharger rbch = new RenderBlockRecharger();
		ClientRegistry.bindTileEntitySpecialRenderer(TileRecharger.class, rbch);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockRecharger), rbch);
		
		BlockSoundEmitter.renderId = RenderingRegistry.getNextAvailableRenderId();
		RenderBlockSoundEmitter rbse = new RenderBlockSoundEmitter();
    	RenderingRegistry.registerBlockHandler(rbse);
		ClientRegistry.bindTileEntitySpecialRenderer(TileSoundEmitter.class, rbse);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockSoundEmiter), rbse);
		
		BlockMaterialsTE.renderId = RenderingRegistry.getNextAvailableRenderId();
    	RenderBlockMaterials rmp = new RenderBlockMaterials();
		ClientRegistry.bindTileEntitySpecialRenderer(TileRenderPillarModel.class, rmp);
    	RenderingRegistry.registerBlockHandler(rmp);

        RenderingRegistry.registerEntityRenderingHandler(EntityHammerMinning.class, new RenderEntityHammerMining());
        RenderingRegistry.registerEntityRenderingHandler(EntityHammerInvoking.class, new RenderEntityHammerInvoking());
        RenderingRegistry.registerEntityRenderingHandler(EntityShieldThrow.class, new RenderEntityShieldThrow());
        RenderingRegistry.registerEntityRenderingHandler(EntityHulk.class, new RenderEntityHulk());
		RenderingRegistry.registerEntityRenderingHandler(EntityHoverBoard.class, new RenderEntityHoverBoard());
    			
    	ModKeyBindings.loadBindings();
		FMLCommonHandler.instance().bus().register(new ModKeyInput());
		FMLCommonHandler.instance().bus().register(new ModEventsClient());
	}

	@Override
	public void postinit(FMLPostInitializationEvent event) {
		super.postinit(event);
	}
	
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		 return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
		}
  
}
