package noelflantier.sfartifacts.client.gui.manual;

import java.util.Enumeration;
import java.util.Hashtable;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiSFAScreen;

public abstract class BaseManual extends GuiSFAScreen{
	public ResourceLocation bground = new ResourceLocation(References.MODID+":textures/gui/guiManuals.png");
	public EntityPlayer player;
	public Hashtable<String,Integer> manuals = new Hashtable<String,Integer>();
	public boolean catOpen = false;
	public String currentCat = "index";
	
	public BaseManual(){
		super();
		this.xSize = 300;
		this.ySize = 194;
	}

	public BaseManual(EntityPlayer player){
		this();
		this.player = player;
	}

	@Override
	public void loadComponents(){
		super.loadComponents();
		
		drawCat(this.currentCat, 1,1,1);
	}
	

	public abstract void drawCat(String cat, int x, int y, float f);

	@Override
	public void initGui() {
		this.manuals = new Hashtable<String,Integer>();
		this.componentList = new Hashtable<String, GuiComponent>();
		super.initGui();
	}
	
	@Override
	public boolean doesGuiPauseGame(){
        return false;
    }
	
	@Override
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);
		Enumeration<String> enumKey = this.componentList.keys();
		while (enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    if(this.componentList.get(key).clicked(x, y)){
	    		if(this.manuals.get(key)>0){
	    			this.mc.thePlayer.closeScreen();
	    			player.openGui(SFArtifacts.instance, this.manuals.get(key), Minecraft.getMinecraft().theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
	    		}else{
	    			this.currentCat = key;
	    			this.catOpen = true;
	    			this.initGui();
	    		}
	    		break;
		    }
		}
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
	}

	@Override
    public void drawScreen(int x, int y, float f){
    	super.drawScreen(x, y, f);

		GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.bground);
			GL11.glColor4f(1f, 1f, 1f, 1f);
			int xt = 222;
			int yt = 194;
			drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, xt-20, 100);
			drawTexturedModalRect(this.guiLeft+xt-20, this.guiTop, xt - (this.xSize-(xt-20)), 0, this.xSize-(xt-20), 100);
			drawTexturedModalRect(this.guiLeft, 100, 0, yt-(yt-100), xt-20, yt-100);
			drawTexturedModalRect(this.guiLeft+xt-20, 100, xt - (this.xSize-(xt-20)), yt-(yt-100), this.xSize-(xt-20), yt-100);
			GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		    	
		Enumeration<String> enumKey = this.componentList.keys();
		while (enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    this.componentList.get(key).draw(x, y);
		}
    }
}
