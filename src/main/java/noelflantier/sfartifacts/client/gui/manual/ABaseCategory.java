package noelflantier.sfartifacts.client.gui.manual;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiSFAScreen;

public abstract class ABaseCategory{

	public Hashtable<String,ABaseCategory> listCategory = new Hashtable<String,ABaseCategory>();
	public Hashtable<String, GuiComponent> componentList = new Hashtable<String, GuiComponent>();
	public ABaseCategory master;
	public int x;
	public int y;
	
	public ABaseCategory(String name, int x, int y){
		this.x = x;
		this.y = y;
		this.master = this;
	}
	
	public ABaseCategory(String name, int x, int y, ABaseCategory master){
		this.x = x;
		this.y = y;
		this.master = master;
	}
	
	public abstract void initComponent();
	public Hashtable<String, GuiComponent> getComponents(){
		return componentList;
	}
}
