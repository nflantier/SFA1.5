package noelflantier.sfartifacts.client.gui;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.client.gui.bases.GuiButtonSFA;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiRender;
import noelflantier.sfartifacts.client.gui.bases.GuiSFATextField;
import noelflantier.sfartifacts.client.gui.bases.GuiScrollable;
import noelflantier.sfartifacts.client.gui.bases.GuiToolTips;
import noelflantier.sfartifacts.common.blocks.tiles.TileSoundEmitter;
import noelflantier.sfartifacts.common.container.ContainerSoundEmitter;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.helpers.SoundEmitterHelper;
import noelflantier.sfartifacts.common.helpers.SoundEmitterHelper.MobsPropertiesForSpawing;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnchantHammer;
import noelflantier.sfartifacts.common.network.messages.PacketMightyFoundryGui;
import noelflantier.sfartifacts.common.network.messages.PacketSoundEmitterGui;

public class GuiSoundEmitter extends GuiMachine{

	private static final ResourceLocation bground = new ResourceLocation(References.MODID+":textures/gui/guiSoundEmiter.png");
	private static final ResourceLocation guiselements = new ResourceLocation(References.MODID+":textures/gui/guisElements.png");
	TileSoundEmitter tile;
	private int buttonPressed = -1;
	private int tickButton = 2;
	private int currentTickButton = -1;
	private boolean isScanning = false;
	private boolean isEmitting = false;
	private int currentTickScanning = 0;
	private int currentRes = 0;
	public ArrayList<Integer> lastScanningResult;
	public int lastFrequency;
	public GuiScrollable listFrequency = new GuiScrollable(10);
	public boolean listFrequencyOpen = false;
	Map<Integer, String[]> allFrequency;
	
	public GuiSoundEmitter(InventoryPlayer inventory, TileSoundEmitter tile) {
		super(new ContainerSoundEmitter(inventory, tile));
		this.xSize = 176;
		this.ySize = 200;
		this.tile = tile;
		hasSidedBt[sidedButton.get(machineButtonO1)] = true;
		sidedBtHasPopUp[sidedButton.get(machineButtonO1)] = true;
		sidedBtTick[sidedButton.get(machineButtonO1)] = "List";
		sidedBtTock[sidedButton.get(machineButtonO1)] = "List";
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.lastFrequency = this.tile.frequencyEmited;
	}
	
	@Override
	public void drawPopUp(int x, int y, int key){
		super.drawPopUp(x,y,key);
		if(key == machineButtonO1){
			this.drawBackgroundPopUp(guiLeft+7, guiTop+5);
			this.listFrequency.show(x,y);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if(this.isAnyPopUpOpen && !byPassPopUp)
			return;
		if(byPassPopUp)
			byPassPopUp = false;
		System.out.println(button.id);
		if(button.id==0){//-
			if(this.buttonPressed==0 || this.buttonPressed==-1){
				if(this.currentTickButton<0){
					this.currentTickButton = this.tickButton;
					long i = Long.parseLong(this.componentList.get("tffreq").textFieldList.get(0).getText());
					i=i>SoundEmitterHelper.highestFrequency?SoundEmitterHelper.highestFrequency:i;
					this.componentList.get("tffreq").textFieldList.get(0).setText((i-1<SoundEmitterHelper.lowestFrequency?SoundEmitterHelper.lowestFrequency:i-1)+"");
				}else
					this.currentTickButton-=1;
			}
			this.buttonPressed = 0;
		}else if(button.id==1){//+
			if(this.buttonPressed==1 || this.buttonPressed==-1){
				if(this.currentTickButton<0){
					this.currentTickButton = this.tickButton;
					long i = Integer.parseInt(this.componentList.get("tffreq").textFieldList.get(0).getText());
					i=i<SoundEmitterHelper.lowestFrequency?SoundEmitterHelper.lowestFrequency:i;
					this.componentList.get("tffreq").textFieldList.get(0).setText((i+1>SoundEmitterHelper.highestFrequency?SoundEmitterHelper.highestFrequency:i+1)+"");
				}else
					this.currentTickButton-=1;
			}
			this.buttonPressed = 1;
		}else if(button.id==2){//SCAN
			if(this.isScanning)
				return;
			startScanning(false);
		}else if(button.id==3){//EMIT
			if(this.isScanning)
				return;
			if(!this.tile.isEmitting){
				startScanning(true);
			}else{
				//SET IS EMITING FALSE WHEN SOMEHTING IS ALLREADY EMITITNG
				PacketHandler.INSTANCE.sendToServer(new PacketSoundEmitterGui(this.tile.xCoord,this.tile.yCoord,this.tile.zCoord,this.lastFrequency,2));
				this.tile.isEmitting = false;
				this.tile.frequencyEmited = 0;
				this.tile.entitiesNameForSpawning = null;
				this.tile.mpForSpawning = null;
				this.tile.entityNameForSpawning = "";
				
				this.componentList.get("txta").addText("stop_emitting____"+this.tile.frequencyEmited, 0, 0);
				this.getButtonById(3).displayString = "EMIT";
			}
		}
	}

	public void scanning(){
		if(this.currentTickScanning==50 || this.currentTickScanning==30 || this.currentTickScanning==95)
			this.componentList.get("txta").replaceString(2, "..");
		if(this.currentTickScanning==5 || this.currentTickScanning==45 || this.currentTickScanning==55 || this.currentTickScanning==90)
			this.componentList.get("txta").replaceString(2, "....");
		if(this.currentTickScanning==10 || this.currentTickScanning==40 || this.currentTickScanning==60 || this.currentTickScanning==85)
			this.componentList.get("txta").replaceString(2, "......");
		if(this.currentTickScanning==15 || this.currentTickScanning==35 || this.currentTickScanning==65 || this.currentTickScanning==85)
			this.componentList.get("txta").replaceString(2, "........");
		if(this.currentTickScanning==20 || this.currentTickScanning==30 || this.currentTickScanning==70 || this.currentTickScanning==80)
			this.componentList.get("txta").replaceString(2, "..........");
		if(this.currentTickScanning==25 || this.currentTickScanning==75)
			this.componentList.get("txta").replaceString(2, "............");
		if(this.currentTickScanning==100)
			this.componentList.get("txta").replaceString(2, "_________");
		if(this.currentTickScanning==105){
			String str = this.lastScanningResult.size()+"  echo found.";
			this.componentList.get("txta").addText(""+str, 0, 0);
		}
		if(this.currentTickScanning==110+this.currentRes*5 && this.currentRes<this.lastScanningResult.size()){
			this.componentList.get("txta").addText(SoundEmitterHelper.spawningRulesIDForRules.get(this.lastScanningResult.get(this.currentRes)).nameEntity, 0, 0);
			this.componentList.get("txta").addText("c:_"+this.getStrConditionsFrequency(this.lastFrequency), 0, 0);
			this.currentRes++;
		}
		if(this.currentTickScanning==115+this.lastScanningResult.size()*5)
			this.componentList.get("txta").addText("_________", 0, 0);
		if(this.currentTickScanning==115+this.lastScanningResult.size()*5)
			this.componentList.get("txta").addText("end", 0, 0);
		
		if(this.currentTickScanning>=150+this.lastScanningResult.size()*5){
			this.isScanning = false;
			this.currentRes = 0;
			this.currentTickScanning=0;
			loadListFrequency();
			if(this.isEmitting){
				this.componentList.get("txta").addText("start_emit:", 0, 0);
				if(this.lastScanningResult.size()>0){
					this.componentList.get("txta").addText("emitting____"+this.lastFrequency, 0, 0);
					this.componentList.get("txta").addText("c:_"+this.getStrConditionsFrequency(this.lastFrequency), 0, 0);
					this.getButtonById(3).displayString = "STOP";
				}else
					this.componentList.get("txta").addText("cant_emit_", 0, 0);
			}
		}
	}
	
	public void startScanning(boolean emit){
		if(!this.isScanning){
			checkFrequency();
			this.isEmitting = false;
			this.componentList.get("txta").reset();
			this.lastFrequency = Integer.parseInt(this.componentList.get("tffreq").textFieldList.get(0).getText());
			
			//SET THE SELECETED FREQ
			PacketHandler.INSTANCE.sendToServer(new PacketSoundEmitterGui(this.tile.xCoord,this.tile.yCoord,this.tile.zCoord,this.lastFrequency,1));
			this.tile.frequencySelected = this.lastFrequency;
			
			this.lastScanningResult = SoundEmitterHelper.getIdsForFrequency(this.lastFrequency);
			this.isScanning = true;
			this.componentList.get("txta").addText("start_scan:", 0, 0);
			this.componentList.get("txta").addText("f_"+this.lastFrequency, 0, 0);
			this.componentList.get("txta").addText("..", 0, 0);
			if(this.lastScanningResult.size()>0){
				if(!ModConfig.areFrequenciesShown){
					PacketHandler.INSTANCE.sendToServer(new PacketSoundEmitterGui(this.tile.xCoord,this.tile.yCoord,this.tile.zCoord,this.lastFrequency,4));
					String[] s = SoundEmitterHelper.getNameAndFrequency(this.lastFrequency);
					if(this.tile.listScannedFrequency.containsKey(this.lastFrequency))
						this.tile.listScannedFrequency.remove(this.lastFrequency);
					this.tile.listScannedFrequency.put(this.lastFrequency, s);
					
				}
			}
			if(emit){
				this.isEmitting = true;
				if(this.lastScanningResult.size()>0){
					//SET THE EMITE FREQ AND SET ISEMITING TO TRUE
					PacketHandler.INSTANCE.sendToServer(new PacketSoundEmitterGui(this.tile.xCoord,this.tile.yCoord,this.tile.zCoord,this.lastFrequency,0));
					this.tile.frequencyEmited = this.lastFrequency;
					PacketHandler.INSTANCE.sendToServer(new PacketSoundEmitterGui(this.tile.xCoord,this.tile.yCoord,this.tile.zCoord,this.lastFrequency,3));
					this.tile.isEmitting = true;
				}
			}
		}
	}
	
	public void checkFrequency(){
		long f = Long.parseLong(this.componentList.get("tffreq").textFieldList.get(0).getText());
		f=f>SoundEmitterHelper.highestFrequency?SoundEmitterHelper.highestFrequency:f;
		f=f<SoundEmitterHelper.lowestFrequency?SoundEmitterHelper.lowestFrequency:f;
		this.componentList.get("tffreq").textFieldList.get(0).setText(f+"");
	}
	
	@Override
	public void updateScreen(){
		super.updateScreen();
		
		if(this.isScanning){
			this.currentTickScanning+=1;
			scanning();
		}

		if(sidedBtOpen[sidedButton.get(machineButtonO1)]){
			this.listFrequency.input();
		}
		
		for(int i =0; i<this.buttonList.size();i++){
			if(this.buttonList.get(i) instanceof GuiButtonSFA){
				if(((GuiButtonSFA)this.buttonList.get(i)).pressed && ((GuiButton)this.buttonList.get(i)).id==this.buttonPressed){
					this.actionPerformed(((GuiButtonSFA)this.buttonList.get(i)));
				}
			}
		}
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_){
		super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
		if(p_146286_3_==0 || p_146286_3_==1)this.buttonPressed=-1;
	}

	@Override
	public void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);
		
		if(sidedBtOpen[sidedButton.get(machineButtonO1)]){
			Enumeration<Integer> enumKeyE = this.listFrequency.componentList.keys();    		
			while (enumKeyE.hasMoreElements()) {
			    int key = enumKeyE.nextElement();
		    	if(this.listFrequency.componentList.get(key).isMouseHover(x, y)){
	    		 	int i = 0;        
	    			Iterator <Map.Entry<Integer, String[]>>iterator;
	    			if(ModConfig.areFrequenciesShown)
	    				iterator =  allFrequency.entrySet().iterator();
	    			else
	    				iterator = this.tile.listScannedFrequency.entrySet().iterator();
	    	        while (iterator.hasNext()){
	    	        	Map.Entry<Integer, String[]> entry = iterator.next();
	    	        	if(i==key){
	    	        		if(sidedBtOpen[sidedButton.get(machineButtonO1)])
	    	        			sidedBtOpen[sidedButton.get(machineButtonO1)] = false;
	    	        		if(!this.isScanning){
		    	        		this.componentList.get("tffreq").textFieldList.get(0).setText(""+entry.getKey());
		    	        		byPassPopUp = true;
		    	        		actionPerformed(this.getButtonById(3));
	    	        		}
	    	        		break;
	    	        	}
	    	        	i+=1;
	    	        }
		    	}
			}
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
		this.componentList.put("se", new GuiComponent(6, 5, 100, 10){{
			addText("Sound Emiter :", 0, 0);
		}});
		this.componentList.put("in", new GuiComponent(6, 165, 100, 10){{
			addText("Inventory :", 0, 0);
		}});

		this.componentList.put("chfreq", new GuiComponent(118, 31){{
			addSfaButton(0,guiLeft,guiTop,20,20,"-");
			addSfaButton(1,guiLeft+22,guiTop,20,20,"+");
		}});

		this.componentList.put("scan", new GuiComponent(118, 52){{
			addButton(2,guiLeft,guiTop,42,20,"SCAN");
		}});

		this.componentList.put("emit", new GuiComponent(118, 73){{
			addButton(3,guiLeft,guiTop,42,20,tile.isEmitting?"STOP":"EMIT");
		}});

		this.componentList.put("txta", new GuiComponent(48, 58, 100, 10){{
			defColor = EnumChatFormatting.DARK_GRAY;
			globalScale = 0.6F;
		}});
		if(this.tile.isEmitting){
			this.componentList.get("txta").addText("emitting____"+this.tile.frequencyEmited, 0, 0);
			this.componentList.get("txta").addText("c:_"+this.getStrConditionsFrequency(this.tile.frequencyEmited), 0, 0);
		}
		
		this.componentList.put("tffreq", new GuiComponent(44, 22, 50, 10){{
			defColor = EnumChatFormatting.DARK_GRAY;
			addText("Frequency :", 0, 0);
			addSFATextField(0,10,68,16);
		}});
		this.componentList.get("tffreq").textFieldList.get(0).setMaxStringLength(20);
		((GuiSFATextField)this.componentList.get("tffreq").textFieldList.get(0)).isOnlyNumeric = true;
		this.componentList.get("tffreq").textFieldList.get(0).setText(""+this.tile.frequencySelected);
		this.txtFieldComponent.put("tffreq", 0);
		
		this.componentManual.put("so", new GuiComponent(guiLeft+18, guiTop+12, 100, 10){{
			globalScale = 0.6F;
			addText("This machine will emit sound on a certain", 0, 0);
			addText("frequency. Different frequency will spawn", 0, 0);
			addText("different creature so be carefull.", 0, 0);
			addText("All the frequency discovered are saved in your", 0, 0);
			addText("list. To emit sounds, it use some RF and liquefied", 0, 0);
			addText("asgardite depending on the frequency.", 0, 0);
			addText("The majority of the creatures spawned will be", 0, 0);
			addText("attracted to the source of the sound.", 0, 0);
			addText("If you dont want to search all the frequencies,", 0, 0);
			addText("there is an option in the config file that", 0, 0);
			addText("will add all the possible frequencies and their", 0, 0);
			addText("echos to your list.", 0, 0);
		}});

		loadListFrequency();
	}
	
	public String getStrNameId(int id){
		return SoundEmitterHelper.spawningRulesIDForRules.get(id).nameEntity;
	}

	public String getStrConditionsId(int id){
		String s = "";
		if(SoundEmitterHelper.spawningRulesIDForRules.get(id)!=null){
			s = SoundEmitterHelper.spawningRulesIDForRules.get(id).rfneeded
				+"RF "+SoundEmitterHelper.spawningRulesIDForRules.get(id).fluidneeded.amount+"MB";
		}
		return s;
	}

	public String getStrConditionsFrequency(int freq){
		String s = "";
		if(SoundEmitterHelper.getIdsForFrequency(freq)!=null && SoundEmitterHelper.getIdsForFrequency(freq).get(0)!=null 
				&& SoundEmitterHelper.spawningRulesIDForRules.get(SoundEmitterHelper.getIdsForFrequency(freq).get(0))!=null){
			s = SoundEmitterHelper.spawningRulesIDForRules.get(SoundEmitterHelper.getIdsForFrequency(freq).get(0)).rfneeded
				+"RF "+SoundEmitterHelper.spawningRulesIDForRules.get(SoundEmitterHelper.getIdsForFrequency(freq).get(0)).fluidneeded.amount+"MB";
		}
		return s;
	}
	
	public void loadListFrequency(){
		this.listFrequency = new GuiScrollable(10);
        int y = 10;
        int i = 0;

		Iterator <Map.Entry<Integer, String[]>>iterator;
		
		if(ModConfig.areFrequenciesShown){
			allFrequency = new HashMap<Integer, String[]>();
			Iterator <Map.Entry<Integer,Integer>>it = SoundEmitterHelper.frequencyForID.entrySet().iterator();
	        while (it.hasNext()){
	        	Map.Entry<Integer,Integer> entry = it.next();
	        	int freq = entry.getKey();
	        	int id = entry.getValue();
	        	if(!allFrequency.containsKey(freq))
	        		allFrequency.put(freq, new String[]{getStrNameId(id)});
	        	else
	        		allFrequency.get(freq)[allFrequency.get(id).length] = getStrNameId(id);
	        }
			iterator = allFrequency.entrySet().iterator();
		}else 
			iterator = this.tile.listScannedFrequency.entrySet().iterator();
		
        while (iterator.hasNext()){
        	Map.Entry<Integer, String[]> entry = iterator.next();

	    	GuiComponent gce = new GuiComponent(guiLeft+15, guiTop+y, 130,10);
			gce.addText(entry.getKey()+" : "+arrayToString(entry.getValue()), 0,0);
	    	gce.isLink=true;
	    	gce.defColor=EnumChatFormatting.DARK_GRAY;
			this.listFrequency.addComponent(i, gce);
			y += 10;
			i +=1;
        }
        this.listFrequency.showArrows = true;
        this.listFrequency.setArrowsPositionAndAlpha(this.guiLeft+142,this.guiTop-1,93,0.3F);
	}
	
	public String arrayToString(String[] p){
		String r = "";
		for(int i =0;i<p.length;i++)
			r = r+p[i]+",";
		return r.substring(0, r.length()-1);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
		GuiRender.renderEnergy(tile.energyCapacity, tile.getEnergyStored(ForgeDirection.UNKNOWN), guiLeft+25, guiTop+23,this.zLevel, 14, 47, 176, 0);
		GuiRender.renderFluid(tile.tank, guiLeft+8, guiTop+23, this.zLevel, 14, 47);
		super.drawGuiContainerBackgroundLayer(partialTickTime, x, y);
	}
	
	@Override
	public void updateToolTips(String key) {
		
		switch(key){
			case "tank" :
				((GuiToolTips)this.componentList.get("tank")).content =  new ArrayList<String>();
				((GuiToolTips)this.componentList.get("tank")).addContent(this.fontRendererObj, this.tile.tank.getFluidAmount()+" MB");
				((GuiToolTips)this.componentList.get("tank")).addContent(this.fontRendererObj, "/ "+this.tile.tank.getCapacity()+" MB");
				break;
			case "energy" :
				((GuiToolTips)this.componentList.get("energy")).content =  new ArrayList<String>();
				((GuiToolTips)this.componentList.get("energy")).addContent(this.fontRendererObj, this.tile.getEnergyStored(ForgeDirection.UNKNOWN)+" RF");
				((GuiToolTips)this.componentList.get("energy")).addContent(this.fontRendererObj, "/ "+this.tile.getMaxEnergyStored(ForgeDirection.UNKNOWN)+" RF");
				break;
			default:
				break;
		}	
	}
	
}
