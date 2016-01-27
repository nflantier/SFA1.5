package noelflantier.sfartifacts.client.gui.manual;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.lwjgl.input.Keyboard;
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
	public Hashtable<String,Integer> links = new Hashtable<String,Integer>();
	public boolean catOpen = false;
	public int tickKeyBack = 10;
	public int currentTickKeyBack = 0;
	public List<String> history = new ArrayList<String>();
	public int maxHistorySize = 10;
	public String currentCat = "index";
	
	public BaseManual(){
		super();
		this.xSize = 300;
		this.ySize = 194;
		this.history.add("index");
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
		this.links = new Hashtable<String,Integer>();
		this.fullComponentList = new Hashtable<String, GuiComponent>();
		super.initGui();
	}
	
	@Override
	public boolean doesGuiPauseGame(){
        return false;
    }
	
	public void addHistory(String key){
		if(this.history.size()>=this.maxHistorySize)
			this.history.remove(0);
		this.history.add(this.history.size(),key);
	} 
	
	@Override
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);
		Enumeration<String> enumKey = this.fullComponentList.keys();
		while (enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    if(this.fullComponentList.get(key).clicked(x, y)){
	    		if(this.links.get(key)>0){
	    			this.history.clear();
	    			this.mc.thePlayer.closeScreen();
	    			player.openGui(SFArtifacts.instance, this.links.get(key), Minecraft.getMinecraft().theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
	    		}else{
	    			this.currentCat = key;
	    			addHistory(key);
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
		if(Keyboard.isKeyDown(Keyboard.KEY_BACK) && currentTickKeyBack<=0){
			currentTickKeyBack = tickKeyBack;
			if(this.history.size()>1){
				int r = this.history.size()-1;
				this.history.remove(r);
				this.currentCat = this.history.get(r-1);
    			this.catOpen = true;
    			this.initGui();
			}
		}else if(currentTickKeyBack>0)
			currentTickKeyBack-=1;
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
		    	
		Enumeration<String> enumKey = this.fullComponentList.keys();
		while (enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    this.fullComponentList.get(key).draw(x, y);
		}
    }
}
