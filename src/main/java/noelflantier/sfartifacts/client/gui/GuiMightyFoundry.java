package noelflantier.sfartifacts.client.gui;

import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.blocks.tiles.TileLiquefier;
import noelflantier.sfartifacts.common.blocks.tiles.TileMightyFoundry;
import noelflantier.sfartifacts.common.gui.ContainerMightyFoundry;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.helpers.Molds;
import noelflantier.sfartifacts.common.helpers.guis.GuiComponent;
import noelflantier.sfartifacts.common.helpers.guis.GuiImage;
import noelflantier.sfartifacts.common.helpers.guis.GuiRender;
import noelflantier.sfartifacts.common.helpers.guis.GuiSFA;
import noelflantier.sfartifacts.common.helpers.guis.GuiToolTips;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketMightyFoundryGui;
import noelflantier.sfartifacts.common.network.messages.PacketUpgradeHammer;

public class GuiMightyFoundry extends GuiMachine{
	
	private static final ResourceLocation guiselements = new ResourceLocation(References.MODID+":textures/gui/guisElements.png");
	private static final ResourceLocation bground = new ResourceLocation(References.MODID+":textures/gui/guiMightyFoundry.png");
	TileMightyFoundry tile;
	public int stId = 36;
	public boolean hasMold = false;
	
	public GuiMightyFoundry(InventoryPlayer inventory, TileMightyFoundry tile) {
		super(new ContainerMightyFoundry(inventory, tile));
		this.xSize = 176;
		this.ySize = 200;
		this.tile = tile;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		this.hasMold = this.inventorySlots.getSlot(this.stId+1).getHasStack();
		
		if(this.tile.isLocked){
			((GuiImage)this.componentList.get("im")).minu=0F;
			((GuiImage)this.componentList.get("im")).minv=0F;
			((GuiImage)this.componentList.get("im")).maxu=0.5F;
			((GuiImage)this.componentList.get("im")).maxv=0.5F;
		}else{
			((GuiImage)this.componentList.get("im")).minu=0.5F;
			((GuiImage)this.componentList.get("im")).minv=0F;
			((GuiImage)this.componentList.get("im")).maxu=1F;
			((GuiImage)this.componentList.get("im")).maxv=0.5F;	
		}
		
		if(this.hasMold){
			this.getButtonById(0).enabled = true;
			int mid = ItemNBTHelper.getInteger(this.inventorySlots.getSlot(this.stId+1).getStack(), "idmold", 0);
			Molds m = Molds.getMold(mid);
			this.componentList.get("wca").replaceString(0, ""+m.name);
			
			DecimalFormat f = new DecimalFormat();
			f.setMaximumFractionDigits(2);
			this.componentList.get("wpr").replaceString(0, ""+f.format(tile.progression*100)+"%");
		}else{
			this.getButtonById(0).enabled =false;
			this.componentList.get("wca").replaceString(0, "Nothing...");
			this.componentList.get("wpr").replaceString(0, "0%");
		}
	}

	@Override
	public void initGui() {
		super.initGui();

		this.hasMold = this.inventorySlots.getSlot(this.stId+1).getHasStack();
		
		this.getButtonById(0).visible = true;
		if(!this.hasMold)
			this.getButtonById(0).enabled = false;
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if(button.id==0){
			if(this.tile.isLocked){
				PacketHandler.INSTANCE.sendToServer(new PacketMightyFoundryGui(this.tile, 2));
				this.tile.isLocked = false;
			}else{
				PacketHandler.INSTANCE.sendToServer(new PacketMightyFoundryGui(this.tile, 1));
				this.tile.isLocked = true;
			}
		}
	}
	
	@Override
	public void updateToolTips(String key) {		
		switch(key){
			case "tank" :
				((GuiToolTips)this.componentList.get("tank")).addContent(this.fontRendererObj, this.tile.tank.getFluidAmount()+" MB");
				((GuiToolTips)this.componentList.get("tank")).addContent(this.fontRendererObj, "/ "+this.tile.tank.getCapacity()+" MB");
				break;
			case "energy" :
				((GuiToolTips)this.componentList.get("energy")).addContent(this.fontRendererObj, this.tile.getEnergyStored(ForgeDirection.UNKNOWN)+" RF");
				((GuiToolTips)this.componentList.get("energy")).addContent(this.fontRendererObj, "/ "+this.tile.getMaxEnergyStored(ForgeDirection.UNKNOWN)+" RF");
				break;
			default:
				break;
		}
	}

	@Override
	public void loadComponents(){
		super.loadComponents();
		this.componentList.put("tank", 
				new GuiToolTips(guiLeft+8, guiTop+23, 14, 47, this.width)
				);

		this.componentList.put("energy", 
				new GuiToolTips(guiLeft+25, guiTop+23, 14, 47, this.width)
				);
		
		GuiComponent gc = new GuiComponent(68, 22);
		gc.addSfaButton(0,this.guiLeft,this.guiTop,22,20,"");
		this.componentList.put("lock", gc);
		
		this.componentList.put("im", 
				new GuiImage(64, 16, 32,32 , 0F, 0F, 0.5F, 0.5F,
						guiselements)
				);
		if(this.tile.isLocked){
			((GuiImage)this.componentList.get("im")).minu=0F;
			((GuiImage)this.componentList.get("im")).minv=0F;
			((GuiImage)this.componentList.get("im")).maxu=0.5F;
			((GuiImage)this.componentList.get("im")).maxv=0.5F;
		}else{
			((GuiImage)this.componentList.get("im")).minu=0.5F;
			((GuiImage)this.componentList.get("im")).minv=0F;
			((GuiImage)this.componentList.get("im")).maxu=1F;
			((GuiImage)this.componentList.get("im")).maxv=0.5F;	
		}
		this.componentList.put("mf", new GuiComponent(6, 5, 100, 10){{
			addText("Mighty Foundry :", 0, 0);
		}});
		this.componentList.put("in", new GuiComponent(6, 108, 100, 10){{
			addText("Inventory :", 0, 0);
		}});
		
		this.componentList.put("cast", new GuiComponent(42, 45, 100, 10){{
			addText("Casting :", 0, 0);
		}});
		this.componentList.put("wca", new GuiComponent(42, 55, 100, 10){{
			defColor = EnumChatFormatting.GRAY;
			addText("Nothing...", 0, 0);
		}});
		this.componentList.put("prog", new GuiComponent(42, 75, 100, 10){{
			addText("Progression :", 0, 0);
		}});
		this.componentList.put("wpr", new GuiComponent(42, 85, 100, 10){{
			defColor = EnumChatFormatting.GRAY;
			addText("0%", 0, 0);
		}});

		this.componentManual.put("so", new GuiComponent(guiLeft+18, guiTop+12, 100, 10){{
			globalScale = 0.6F;
			addText("This machine is used to forge items.", 0, 0);
			addText("To do so you need a valid mold and the materials", 0, 0);
			addText("needed to forge the item linked to the mold.", 0, 0);
			addText("The mold need to be locked for the foundry to", 0, 0);
			addText("start forging.", 0, 0);
			addText("Adding lava will make the foundry really faster.", 0, 0);
		}});
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
		GuiRender.renderEnergy(tile.energyCapacity, tile.getEnergyStored(ForgeDirection.UNKNOWN), guiLeft+25, guiTop+23,this.zLevel, 14, 47, 176, 0);
		if(tile.progression>0)
			GuiRender.renderTask(100, 100-(int)(tile.progression*100), guiLeft+119, guiTop+76, this.zLevel, 22, 16, 176, 47);
		GuiRender.renderFluid(tile.tank, guiLeft+8, guiTop+23, this.zLevel, 14, 47);
		super.drawGuiContainerBackgroundLayer(partialTickTime, x, y);

	}


}
