package noelflantier.sfartifacts.common.helpers.guis;

import java.util.Hashtable;

import net.minecraft.client.gui.GuiScreen;

public class GuiSFAScreen extends GuiScreen{

	public boolean componentloaded = false;
	public Hashtable<String, GuiComponent> componentList = new Hashtable<String, GuiComponent>();
	public int guiLeft;
	public int guiTop;
	public int xSize = 176;
	public int ySize = 166;
	
	public GuiSFAScreen(){
		super();
	}
	
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_){
        super.keyTyped(p_73869_1_, p_73869_2_);
        if (p_73869_2_ == 1 || p_73869_2_ == this.mc.gameSettings.keyBindInventory.getKeyCode())
        {
            this.mc.thePlayer.closeScreen();
        }
    }
	
	public void loadComponents(){
		if(this.componentloaded)return;
		this.componentloaded = true;
	}
	
	@Override
	public void initGui() {
		super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
		this.loadComponents();
	}
}
