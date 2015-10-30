package noelflantier.sfartifacts.client.gui;

import java.util.Enumeration;
import java.util.Hashtable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiRender;
import noelflantier.sfartifacts.client.gui.bases.GuiSFA;
import noelflantier.sfartifacts.client.gui.bases.GuiSFAScreen;
import noelflantier.sfartifacts.client.gui.bases.GuiScrollable;
import noelflantier.sfartifacts.common.container.ContainerTeleport;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketTeleport;
import noelflantier.sfartifacts.common.network.messages.PacketUpgradeHammer;

public class GuiTeleport  extends GuiSFA{
	
	public ResourceLocation bground = new ResourceLocation(References.MODID+":textures/gui/guiTeleport.png");
	public EntityPlayer player;
	public GuiScrollable coord = new GuiScrollable(10);
	public ItemStack hammer;
	public int selectedCoord = -1;
	public int tmpsc = -1;
	public int tickInput = 0;
	public int tmpti = -1;
	public int tickUpdateList = 0;
	private GuiTextField nameTf;
	private GuiTextField coordTf;
	
	public GuiTeleport(InventoryPlayer inventory){
		super(new ContainerTeleport(inventory));
		this.xSize = 198;
		this.ySize = 179;
	}

	public GuiTeleport(EntityPlayer player){
		this(player.inventory);
		this.player = player;
		this.hammer = this.player.inventory.getCurrentItem();
	}

	@Override
	public void loadComponents(){
		super.loadComponents();
		
		this.componentList.put("tfname", new GuiComponent(10, 15, 50, 10){{
			defColor = EnumChatFormatting.DARK_GRAY;
			addText("Name :", 0, 0);
			addTextField(35,-5,68,16);
		}});
		this.txtFieldComponent.put("tfname", 0);
		
		this.componentList.put("tfcoord", new GuiComponent(10, 35, 50, 10){{
			defColor = EnumChatFormatting.DARK_GRAY;
			addText("D,X,Y,Z :", 0, 0);
			addTextField(41,-5,68,16);
		}});
		this.componentList.get("tfcoord").textFieldList.get(0).setMaxStringLength(20);
		this.componentList.get("tfcoord").textFieldList.get(0).setText(this.player.dimension+","+((int)this.player.posX)+","+((int)this.player.posY)+","+((int)this.player.posZ));
		this.txtFieldComponent.put("tfcoord", 0);
		
		this.componentList.put("li", new GuiComponent(10, 55, 50, 10){{
			defColor = EnumChatFormatting.DARK_GRAY;
			addText("List :", 0, 0);
		}});
		this.componentList.put("btadd", new GuiComponent(this.xSize/2-83, 15, 10, 10){{
			addSfaButton(0,guiLeft+134,guiTop-7,20,20,"+");
		}});
		this.componentList.put("btsupp", new GuiComponent(this.xSize/2-62, 15, 10, 10){{
			addSfaButton(1,guiLeft+134,guiTop-7,20,20,"-");
		}});
		this.componentList.put("bttp", new GuiComponent(this.xSize/2-83, 36, 10, 10){{
			addSfaButton(2,guiLeft+134,guiTop-7,42,20,"GO");
		}});

		if (!hammer.getTagCompound().hasKey("TeleportCoord", 9))
			hammer.getTagCompound().setTag("TeleportCoord", new NBTTagList());

		setList();
	}
	
	public void setList(){
		this.coord.componentList.clear();
		NBTTagList nbttaglist = this.hammer.stackTagCompound.getTagList("TeleportCoord", 10);
        int y = 10;
        for(int i = 0 ; i<nbttaglist.tagCount() ; i++){
			String name = nbttaglist.getCompoundTagAt(i).getString("name");
			String c = nbttaglist.getCompoundTagAt(i).getString("coord");
			String[] st = c.split(",");
			int[] ct = new int[st.length];
			for(int k=0;k<st.length;k++)
				ct[k] = Integer.parseInt(st[k]);
	    	GuiComponent gce = new GuiComponent(10, y+55, 150,10);
			gce.defColor = EnumChatFormatting.DARK_GRAY;
	    	gce.isLink=true;
			gce.addText(name+" "+Minecraft.getMinecraft().theWorld.provider.getProviderForDimension(ct[0]).getDimensionName()+" "+ct[1]+" "+ct[2]+" "+ct[3], 0,0);
			this.coord.addComponent(i, gce);
			y += 10;
        }
	}
	
	public void getInput(){
		
		this.tickInput--;
		if(tickInput>0)return;
		
		this.tickInput = 0;
		int w = Mouse.getEventDWheel();

		if(w<0 && this.tmpti!=w){
			this.coord.incIndex();
			this.tickInput = 2;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			this.coord.incIndex();
			this.tickInput = 10;
		}else if(w>0 && this.tmpti!=w){
			this.coord.decIndex();
			this.tickInput = 2;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			this.coord.decIndex();
			this.tickInput = 10;
		}else if((Keyboard.isKeyDown(this.mc.gameSettings.keyBindInventory.getKeyCode()) || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) 
				&& !this.componentList.get("tfname").textFieldList.get(0).isFocused() && !this.componentList.get("tfcoord").textFieldList.get(0).isFocused()){
			//this.player.closeScreen();
		}
		this.tmpti = w;
	}
	
	@Override
    public void drawGuiContainerForegroundLayer(int x, int y){
		super.drawGuiContainerForegroundLayer(x-guiLeft,y-guiTop);
		this.getInput();
		
		for(int i = this.coord.currentIndex ; i < this.coord.currentIndex + this.coord.maxComponent ; i++){
			if(this.coord.componentList.get(i)==null)break;
	    	this.coord.componentList.get(i).scrolableMarge = this.coord.currentIndex*10;
		    this.coord.componentList.get(i).draw(x-guiLeft,y-guiTop);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int mouseX, int mouseY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
	}

	/*@Override
	protected void keyTyped(char par1, int par2) {
		//super.keyTyped(par1, par2);
	    if(par2 == this.mc.gameSettings.keyBindInventory.getKeyCode() && !this.componentList.get("tfname").textFieldList.get(0).isFocused() && !this.componentList.get("tfcoord").textFieldList.get(0).isFocused()) {
	      super.keyTyped(par1, par2);
	    }
	    this.componentList.get("tfcoord").textFieldList.get(0).textboxKeyTyped(par1, par2);
	    this.componentList.get("tfname").textFieldList.get(0).textboxKeyTyped(par1, par2);
	}*/
	
	@Override
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);

		//this.componentList.get("tfcoord").textFieldList.get(0).mouseClicked(x-guiLeft, y-guiTop, button);
		//this.componentList.get("tfname").textFieldList.get(0).mouseClicked(x-guiLeft, y-guiTop, button);
		
		Enumeration<Integer> enumKeyE = this.coord.componentList.keys();		
		while (enumKeyE.hasMoreElements()) {
		    int key = enumKeyE.nextElement();
	    	if(this.coord.componentList.get(key).isMouseHover(x-guiLeft, y-guiTop)){
	    			if(this.selectedCoord == key)
	    				this.selectedCoord = -1;
	    			else
	    				this.selectedCoord = key;
	    		}
	    	}
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		//this.componentList.get("tfcoord").textFieldList.get(0).updateCursorCounter();
		//this.componentList.get("tfname").textFieldList.get(0).updateCursorCounter();
		if(this.selectedCoord!=-1 && this.coord.componentList.get(this.selectedCoord)!=null){
			this.coord.componentList.get(this.selectedCoord).defColor = EnumChatFormatting.GREEN;
			this.getButtonById(1).enabled = true;
			this.getButtonById(2).enabled = true;
		}else{
			this.getButtonById(1).enabled = false;
			this.getButtonById(2).enabled = false;
		}
		if(this.tmpsc!=-1  && this.coord.componentList.get(this.tmpsc)!=null && ( this.selectedCoord==-1 || this.tmpsc!=this.selectedCoord))
			this.coord.componentList.get(this.tmpsc).defColor = EnumChatFormatting.DARK_GRAY;
		this.tmpsc = this.selectedCoord;
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if (button.id == 0 && this.componentList.get("tfcoord").textFieldList.get(0).getText()!="" && this.componentList.get("tfname").textFieldList.get(0).getText()!=""){//ADDING COORD
			String[] st = this.componentList.get("tfcoord").textFieldList.get(0).getText().split(",");
			if(st.length==4){
				if (st[0].matches("[+-]?[0-9]+") && st[1].matches("[+-]?[0-9]+") && st[2].matches("[+-]?[0-9]+") && st[3].matches("[+-]?[0-9]+")){
					if(!DimensionManager.isDimensionRegistered(Integer.parseInt(st[0])))
						return;
					if (!hammer.getTagCompound().hasKey("TeleportCoord", 9))
						hammer.getTagCompound().setTag("TeleportCoord", new NBTTagList());
					NBTTagList nbttaglist = hammer.stackTagCompound.getTagList("TeleportCoord", 10);
			        NBTTagCompound nbttagcompound = new NBTTagCompound();
			        nbttagcompound.setString("name", this.componentList.get("tfname").textFieldList.get(0).getText());
			        nbttagcompound.setString("coord", this.componentList.get("tfcoord").textFieldList.get(0).getText());
			        nbttaglist.appendTag(nbttagcompound);
			        setList();
					PacketHandler.INSTANCE.sendToServer(new PacketTeleport(0, this.componentList.get("tfname").textFieldList.get(0).getText(),this.componentList.get("tfcoord").textFieldList.get(0).getText()));
					
				}
			}
		}else if (button.id == 1 && this.selectedCoord!=-1){//DELETING COORD
			
			NBTTagList nbttaglist1 = hammer.stackTagCompound.getTagList("TeleportCoord", 10);
			if(nbttaglist1.getCompoundTagAt(this.selectedCoord)!=null)
				nbttaglist1.removeTag(this.selectedCoord);
			setList();
			PacketHandler.INSTANCE.sendToServer(new PacketTeleport(1, this.selectedCoord));
			this.tmpsc = -1;
			this.selectedCoord = -1;
			
		}else if (button.id == 2 && this.selectedCoord!=-1){//TELEPORTING
            //this.player.travelToDimension(0);
			NBTTagList nbttaglist = hammer.stackTagCompound.getTagList("TeleportCoord", 10);
			if(nbttaglist.getCompoundTagAt(this.selectedCoord)!=null){
				PacketHandler.INSTANCE.sendToServer(new PacketTeleport(2, nbttaglist.getCompoundTagAt(this.selectedCoord).getString("coord")));
			}
		}
	}
	
	@Override
	public void initGui() {
		this.componentList = new Hashtable<String, GuiComponent>();
		super.initGui();

		this.getButtonById(1).enabled = false;
		this.getButtonById(2).enabled = false;
		
	}
	
	@Override
	public boolean doesGuiPauseGame(){
        return false;
    }

	@Override
	public void updateToolTips(String key) {		
	}
}
