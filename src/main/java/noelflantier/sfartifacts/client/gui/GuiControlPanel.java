package noelflantier.sfartifacts.client.gui;

import java.text.NumberFormat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiRender;
import noelflantier.sfartifacts.client.gui.bases.GuiSFA;
import noelflantier.sfartifacts.client.gui.bases.GuiToolTips;
import noelflantier.sfartifacts.common.blocks.tiles.TileControlPannel;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileMasterPillar;
import noelflantier.sfartifacts.common.container.ContainerControlPanel;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketPillarGui;

@SideOnly(Side.CLIENT)
public class GuiControlPanel extends GuiSFA{

	private TileControlPannel tile;
	private TileMasterPillar pillar;
	private static final ResourceLocation bground = new ResourceLocation(References.MODID+":textures/gui/guiControlPanel.png");
	
	public GuiControlPanel(InventoryPlayer inventory, TileControlPannel tile) {
		super(new ContainerControlPanel(inventory, tile));
		this.tile = tile;
		this.xSize = 192;
		this.ySize = 187;
	}

	@Override
	public void initGui(){
		TileEntity t = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(this.tile.getMasterX(), this.tile.getMasterY(), this.tile.getMasterZ());
		if(t!=null && t instanceof TileMasterPillar)
			this.pillar = (TileMasterPillar)t;
		
		super.initGui();
		
		this.getButtonById(0).visible = true;
		this.getButtonById(1).visible = true;
	}
	
	@Override
	public void updateScreen(){
		super.updateScreen();
		if(this.pillar==null) return;
		
		this.componentList.get("fluiddraining").replaceString(0, "Fluid draining : "+this.pillar.amountToExtract+" MB/T");
		this.componentList.get("penergy").replaceString(0, "Passive gain : "+this.pillar.passiveEnergy+" RF/T");
		this.componentList.get("en").replaceString(1, String.format("%s / %s RF",NumberFormat.getNumberInstance().format(pillar.getEnergyStored(ForgeDirection.UNKNOWN))
				,NumberFormat.getNumberInstance().format(pillar.energyCapacity)));
		this.componentList.get("fl").replaceString(1, String.format("%s / %s MB",NumberFormat.getNumberInstance().format(pillar.tank.getFluidAmount())
				,NumberFormat.getNumberInstance().format(pillar.tankCapacity)));
		this.componentList.get("fenergy").replaceString(0, "Fluid gain : "+this.pillar.fluidEnergy+" RF/T");
		this.componentList.get("tenergy").replaceString(0, "Total : "+(this.pillar.getEnergyStored(ForgeDirection.UNKNOWN)-this.pillar.lastEnergyStoredAmount)+" RF/T");
		
		/*int margebt = this.fontRendererObj.getStringWidth(this.componentList.get("fluiddraining").stringList.get(0));
		this.componentList.get("fluiddraining").buttonList.get(0).xPosition = 20+this.guiLeft+margebt;
		this.componentList.get("fluiddraining").buttonList.get(1).xPosition = 20+this.guiLeft+21+margebt;*/
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if(this.pillar==null) return;
		
		if(button.id==0){
			PacketHandler.INSTANCE.sendToServer(new PacketPillarGui(this.pillar.xCoord,this.pillar.yCoord,this.pillar.zCoord, -10));
		}else if(button.id==1){
			PacketHandler.INSTANCE.sendToServer(new PacketPillarGui(this.pillar.xCoord,this.pillar.yCoord,this.pillar.zCoord, 10));
		}else if(button.id==2){
			PacketHandler.INSTANCE.sendToServer(new PacketPillarGui(this.pillar.xCoord,this.pillar.yCoord,this.pillar.zCoord, -100));
		}else if(button.id==3){
			PacketHandler.INSTANCE.sendToServer(new PacketPillarGui(this.pillar.xCoord,this.pillar.yCoord,this.pillar.zCoord, +100));
		}else if(button.id==4){
			PacketHandler.INSTANCE.sendToServer(new PacketPillarGui(this.pillar.xCoord,this.pillar.yCoord,this.pillar.zCoord, -1000));
		}else if(button.id==5){
			PacketHandler.INSTANCE.sendToServer(new PacketPillarGui(this.pillar.xCoord,this.pillar.yCoord,this.pillar.zCoord, +1000));
		}
	}
	
	@Override
	public void loadComponents(){
		super.loadComponents();
		if(this.pillar==null)return;

		this.componentList.put("cpan", new GuiComponent(8, 6, 100, 10){{
			addText("Control Panel :", 0, 0);
		}});
		
		this.componentList.put("fluiddraining", new GuiComponent(18, 120){{
			addSfaButton(0,guiLeft+52,guiTop+10,20,20,"-");
			addSfaButton(1,guiLeft+73,guiTop+10,20,20,"+");
			addSfaButton(2,guiLeft+31,guiTop+10,20,20,"--");
			addSfaButton(3,guiLeft+94,guiTop+10,20,20,"++");
			addSfaButton(4,guiLeft+10,guiTop+10,20,20,"---");
			addSfaButton(5,guiLeft+115,guiTop+10,20,20,"+++");
			addText("Fluid draining : "+pillar.amountToExtract+" MB/T", 0, 0);
		}});

		this.componentList.put("penergy", new GuiToolTips(guiLeft+18, guiTop+90, 150, 10, this.width){{
			addText("Passive gain : "+pillar.passiveEnergy+" RF/T", -guiLeft, -guiTop);
		}});

		this.componentList.put("fenergy", new GuiToolTips(guiLeft+18, guiTop+105, 150, 10, this.width){{
			addText("Fluid gain : "+pillar.fluidEnergy+" RF/T",  -guiLeft, -guiTop);
		}});
				
		this.componentList.put("tenergy", new GuiComponent(18, 155){{
			addText("Total : "+(pillar.getEnergyStored(ForgeDirection.UNKNOWN)-pillar.lastEnergyStoredAmount)+" RF/T",  0, 0);
		}});		
		
		this.componentList.put("title", new GuiComponent(85, 6){{
			addText(String.format(""+pillar.namePillar)+" "+String.format(""+PillarMaterials.getMaterialFromId(pillar.materialId).name()),  0, 0);
		}});
		
		this.componentList.put("en", new GuiComponent(42, 25){{
			addText("Energy :",  0, 0);
			addText(String.format("%s / %s RF",NumberFormat.getNumberInstance().format(pillar.getEnergyStored(ForgeDirection.UNKNOWN))
					,NumberFormat.getNumberInstance().format(pillar.energyCapacity)),0,0);
		}});
		this.componentList.put("fl", new GuiComponent(42, 45){{
			addText("Fluid :",  0, 0);
			addText(String.format("%s / %s MB",NumberFormat.getNumberInstance().format(pillar.tank.getFluidAmount())
					,NumberFormat.getNumberInstance().format(pillar.tankCapacity)),0,0);
		}});
		this.componentList.put("det", new GuiComponent(8, 75){{
			addText("Energy details :",  0, 0);
		}});

	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int mouseX, int mouseY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
		if(this.pillar==null)return;
		GuiRender.renderEnergy(this.pillar.energyCapacity, this.pillar.getEnergyStored(ForgeDirection.UNKNOWN), guiLeft+25, guiTop+22,this.zLevel, 14, 47, 192, 0);
		GuiRender.renderFluid(this.pillar.tank, guiLeft+8, guiTop+22, this.zLevel, 14, 47);
	}

	@Override

	public void updateToolTips(String key){
		switch(key){
			case "penergy" :
				((GuiToolTips)this.componentList.get("penergy")).addContent(this.fontRendererObj,String.format("%s","pow( structure ratio, 2.2 )"));
				((GuiToolTips)this.componentList.get("penergy")).addContent(this.fontRendererObj,String.format("%s","* pow( material ratio"));
				((GuiToolTips)this.componentList.get("penergy")).addContent(this.fontRendererObj,String.format("%s","+ pow ( height ratio * 2, 4 )"));
				((GuiToolTips)this.componentList.get("penergy")).addContent(this.fontRendererObj,String.format("%s","+ rain ratio, 1.5)"));
				break;
			case "fenergy" :
				((GuiToolTips)this.componentList.get("fenergy")).addContent(this.fontRendererObj,String.format("%s","pow( pow( structure ratio, 1.1 )"));
				((GuiToolTips)this.componentList.get("fenergy")).addContent(this.fontRendererObj,String.format("%s","* pow( fluid amount/20+1, 1.5 )"));
				((GuiToolTips)this.componentList.get("fenergy")).addContent(this.fontRendererObj,String.format("%s","* pow( material ratio, 1.1 ), 0.85 )"));
				break;
			default:
				break;
		}
	}

}
