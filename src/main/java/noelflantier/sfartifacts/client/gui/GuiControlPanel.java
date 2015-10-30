package noelflantier.sfartifacts.client.gui;

import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Locale;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.client.gui.bases.GuiButtonSFA;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiRender;
import noelflantier.sfartifacts.client.gui.bases.GuiSFA;
import noelflantier.sfartifacts.client.gui.bases.GuiToolTips;
import noelflantier.sfartifacts.common.blocks.tiles.TileControlPannel;
import noelflantier.sfartifacts.common.blocks.tiles.pillar.TileMasterPillar;
import noelflantier.sfartifacts.common.container.ContainerControlPanel;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;
import noelflantier.sfartifacts.common.helpers.PillarStructures;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketPillarConfig;

@SideOnly(Side.CLIENT)
public class GuiControlPanel extends GuiSFA{

	private TileControlPannel tile;
	private TileMasterPillar pillar;
	private int buttonPressed = -1;
	private int tickButton = 2;
	private int currentTickButton = -1;
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
		
		int margebt = this.fontRendererObj.getStringWidth(this.componentList.get("fluiddraining").stringList.get(0));
		this.componentList.get("fluiddraining").buttonList.get(0).xPosition = 20+this.guiLeft+margebt;
		this.componentList.get("fluiddraining").buttonList.get(1).xPosition = 20+this.guiLeft+21+margebt;
		
		for(int i =0; i<this.buttonList.size();i++){
			if(this.buttonList.get(i) instanceof GuiButtonSFA){
				if(((GuiButtonSFA)this.buttonList.get(i)).pressed && ((GuiButton)this.buttonList.get(i)).id==this.buttonPressed){
					this.actionPerformed(((GuiButtonSFA)this.buttonList.get(i)));
				}
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if(this.pillar==null) return;
		
		if(button.id==0){
			if(this.buttonPressed==0 || this.buttonPressed==-1){
				if(this.currentTickButton<0){
					this.currentTickButton = this.tickButton;
					PacketHandler.INSTANCE.sendToServer(new PacketPillarConfig(this.pillar.xCoord,this.pillar.yCoord,this.pillar.zCoord, -10));
				}else
					this.currentTickButton-=1;
			}
			this.buttonPressed = 0;
		}else if(button.id==1){
			if(this.buttonPressed==1 || this.buttonPressed==-1){
				if(this.currentTickButton<0){
					this.currentTickButton = this.tickButton;
					PacketHandler.INSTANCE.sendToServer(new PacketPillarConfig(this.pillar.xCoord,this.pillar.yCoord,this.pillar.zCoord, 10));
				}else
					this.currentTickButton-=1;
			}
			this.buttonPressed = 1;
		}
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_){
		super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
		if(p_146286_3_==0 || p_146286_3_==1)this.buttonPressed=-1;
	}
	
	@Override
	public void loadComponents(){
		super.loadComponents();
		if(this.pillar==null)return;

		this.componentList.put("cpan", new GuiComponent(8, 6, 100, 10){{
			addText("Control Panel :", 0, 0);
		}});
		
		this.componentList.put("fluiddraining", new GuiComponent(18, 120){{
			addSfaButton(0,guiLeft+113,guiTop-7,20,20,"-");
			addSfaButton(1,guiLeft+134,guiTop-7,20,20,"+");
			addText("Fluid draining : "+pillar.amountToExtract+" MB/T", 0, 0);
		}});

		this.componentList.put("penergy", new GuiToolTips(guiLeft+18, guiTop+90, 150, 10, this.width){{
			addText("Passive gain : "+pillar.passiveEnergy+" RF/T", -guiLeft, -guiTop);
		}});

		this.componentList.put("fenergy", new GuiToolTips(guiLeft+18, guiTop+105, 150, 10, this.width){{
			addText("Fluid gain : "+pillar.fluidEnergy+" RF/T",  -guiLeft, -guiTop);
		}});
				
		this.componentList.put("tenergy", new GuiComponent(18, 135){{
			addText("Total : "+(pillar.getEnergyStored(ForgeDirection.UNKNOWN)-pillar.lastEnergyStoredAmount)+" RF/T",  0, 0);
		}});		
		
		this.componentList.put("title", new GuiComponent(85, 6){{
			addText(String.format(""+PillarStructures.getStructureFromId(pillar.structureId).name())+" "+String.format(""+PillarMaterials.getMaterialFromId(pillar.materialId).name()),  0, 0);
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
				((GuiToolTips)this.componentList.get("penergy")).addContent(this.fontRendererObj,String.format("%s", "Material natural gain * "));
				((GuiToolTips)this.componentList.get("penergy")).addContent(this.fontRendererObj,String.format("%s","( Type ratio"));
				((GuiToolTips)this.componentList.get("penergy")).addContent(this.fontRendererObj,String.format("%s","+ Rain ratio"));
				((GuiToolTips)this.componentList.get("penergy")).addContent(this.fontRendererObj,String.format("%s","+ Height ratio )"));
				break;
			case "fenergy" :
				((GuiToolTips)this.componentList.get("fenergy")).addContent(this.fontRendererObj,String.format("%s","Material natural gain"));
				((GuiToolTips)this.componentList.get("fenergy")).addContent(this.fontRendererObj,String.format("%s","* Type ratio"));
				((GuiToolTips)this.componentList.get("fenergy")).addContent(this.fontRendererObj,String.format("%s","* Fluid draining amount"));
				break;
			default:
				break;
		}
	}

}
