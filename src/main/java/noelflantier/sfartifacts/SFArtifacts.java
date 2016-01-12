package noelflantier.sfartifacts;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import noelflantier.sfartifacts.common.CommonProxy;
import noelflantier.sfartifacts.common.handlers.ModItems;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION)
public class SFArtifacts
{
	
	public static CreativeTabs sfTabs;
	@SidedProxy(clientSide = "noelflantier.sfartifacts.client.ClientProxy", serverSide = "noelflantier.sfartifacts.common.CommonProxy")
	public static CommonProxy myProxy;
	
	@Instance(References.MODID)
	public static SFArtifacts instance;
	
	public static SimpleNetworkWrapper networkWrapper;
	
    @EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
    	sfTabs = new CreativeTabs("SFArtifacts"){
    		@SideOnly(Side.CLIENT)
    		public Item getTabIconItem(){
    			return ModItems.itemBasicHammer;
    		}
    	};
    	myProxy.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	myProxy.init(event);
    }
    
    @EventHandler
    public void postinit(FMLPostInitializationEvent event)
    {
    	myProxy.postinit(event);
    }
}
