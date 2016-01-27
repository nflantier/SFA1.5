package noelflantier.sfartifacts.client.gui.manual;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.client.gui.NIndexManual;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiSFAScreen;

public class NGuiManual extends GuiSFAScreen{

	public ResourceLocation bgroundL = new ResourceLocation(References.MODID+":textures/gui/guiManualsLeft.png");
	public ResourceLocation bgroundR = new ResourceLocation(References.MODID+":textures/gui/guiManualsRight.png");
	public static final String CAT_INDEX = "INDEX";
	public ABaseCategory index;
	public String currentKey = "";
	public ABaseCategory currentCategory;
	
	public NGuiManual(){
		super();
		this.xSize = 360;
		this.ySize = 190;
	}
	public NGuiManual(EntityPlayer player){
		this();
	}
	
	public void loadComponents(){
		super.loadComponents();
		index = new NIndexManual(CAT_INDEX,this.guiLeft, this.guiTop);
		currentCategory = index;
		this.fullComponentList.clear();
		this.fullComponentList.putAll(currentCategory.getComponents());
	}
	
	public void setCategory(){
		if(!currentCategory.listCategory.containsKey(currentKey))
			return;
		this.fullComponentList.clear();
		this.fullComponentList.putAll(currentCategory.listCategory.get(currentKey).getComponents());
		currentCategory = currentCategory.listCategory.get(currentKey);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);
		Enumeration<String> enumKey = this.fullComponentList.keys();
		while (enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    if(this.fullComponentList.get(key).clicked(x, y)){
		    	currentKey = key;
		    	setCategory();
	    		break;
		    }
		}
	}
	
	@Override
	public boolean doesGuiPauseGame(){
        return false;
    }
	
	@Override
    public void drawScreen(int x, int y, float f){
		Minecraft.getMinecraft().getTextureManager().bindTexture(bgroundL);
		GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 180, this.ySize);
			GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(bgroundR);
		GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			this.drawTexturedModalRect(guiLeft+180, guiTop, 0, 0, 180, this.ySize);
			GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		 
    	super.drawScreen(x, y, f);
    	
		Enumeration<String> enumKey = this.fullComponentList.keys();
		while (enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    this.fullComponentList.get(key).draw(x, y);
		}
    }
}
