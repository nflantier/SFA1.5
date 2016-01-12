package noelflantier.sfartifacts.client.gui.manual;

import java.util.Enumeration;
import java.util.Hashtable;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiSFAScreen;
import noelflantier.sfartifacts.common.handlers.ModGUIs;

public class GuiManual extends GuiSFAScreen{

	public ResourceLocation bground = new ResourceLocation(References.MODID+":textures/gui/guiManuals.png");
	public Hashtable<String,Integer> manuals = new Hashtable<String,Integer>();
	public EntityPlayer player;
	
	public GuiManual(){
		super();
		this.xSize = 250;
		this.ySize = 194;
	}

	public GuiManual(EntityPlayer player){
		this();
		this.player = player;
	}
	
	@Override
	public void loadComponents(){
		super.loadComponents();
		GuiComponent gc = new GuiComponent(this.guiLeft+10, this.guiTop+10, 60, 10);
		gc.addText("Thor manual", 0, 0);
		gc.isLink = true;
		gc.defColor = EnumChatFormatting.BLACK;
		this.componentList.put("thor", gc);
		this.manuals.put("thor", ModGUIs.guiIDThorManual);

		this.componentList.put("captain", new GuiComponent(this.guiLeft+10, this.guiTop+20, 60, 10){{
			addText("Captain manual", 0, 0);
			isLink = true;
			defColor = EnumChatFormatting.BLACK;
		}});
		this.manuals.put("captain", ModGUIs.guiIDCaptainManual);

		this.componentList.put("hulk", new GuiComponent(this.guiLeft+10, this.guiTop+30, 60, 10){{
			addText("Hulk manual", 0, 0);
			isLink = true;
			defColor = EnumChatFormatting.BLACK;
		}});
		this.manuals.put("hulk", ModGUIs.guiIDHulkManual);

		this.componentList.put("bttf", new GuiComponent(this.guiLeft+10, this.guiTop+40, 60, 10){{
			addText("Back to the future", 0, 0);
			isLink = true;
			defColor = EnumChatFormatting.BLACK;
		}});
		this.manuals.put("bttf", ModGUIs.guiIDBackToTheFuture);
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
	            this.mc.thePlayer.closeScreen();
	    		player.openGui(SFArtifacts.instance, this.manuals.get(key), Minecraft.getMinecraft().theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
	    		break;	
		    }
		}
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
