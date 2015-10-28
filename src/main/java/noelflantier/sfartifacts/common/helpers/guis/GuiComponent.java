package noelflantier.sfartifacts.common.helpers.guis;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.EnumChatFormatting;

public class GuiComponent {

	public int x,y;
	public List<GuiButton> buttonList = new ArrayList<GuiButton>();
	public List<GuiTextField> textFieldList  = new ArrayList<GuiTextField>();
	public Hashtable<Integer,String> stringList = new Hashtable<Integer,String>();
	public Hashtable<Integer,Integer[]> cStringList = new Hashtable<Integer,Integer[]>();
	public String text;
	public int strX,strY;
	public float globalScale = 1;
	public boolean visible = true;
	public int width,height;
	public boolean isLink = false;
	public boolean isScrolable = false;
	public int scrolableMarge = 0;
	public int globalDecY = 0;
	public final int ystr = 10;
    public FontRenderer fr;
    public int stringID = -1;
    public EnumChatFormatting defColor = EnumChatFormatting.DARK_GRAY;
    public EnumChatFormatting color = EnumChatFormatting.DARK_GRAY;
    public EnumChatFormatting linkColor = EnumChatFormatting.GRAY;
	
	public GuiComponent(int x, int y){
		this.x = x;
		this.y = y;
        this.fr = Minecraft.getMinecraft().fontRenderer;
	}

	public GuiComponent(int x, int y, int w, int h){
		this(x,y);
		this.width = w;
		this.height = h;
	}
	
	public void reset(){
		this.buttonList.clear();
		this.textFieldList.clear();
		this.stringList.clear();
		this.cStringList.clear();
		this.globalDecY = 0;
		this.stringID = -1;
		this.strY = 0;
		this.strX = 0;
	}
	
	public void addTextField(int decx, int decy, int width, int height){
		GuiTextField tf = new GuiTextField(Minecraft.getMinecraft().fontRenderer,this.x+decx,this.y+decy,width,height);
		tf.setCanLoseFocus(true);
		tf.setMaxStringLength(10);
		tf.setFocused(false);
		this.textFieldList.add(tf);
	}
	public void addSFATextField(int decx, int decy, int width, int height){
		GuiSFATextField tf = new GuiSFATextField(Minecraft.getMinecraft().fontRenderer,this.x+decx,this.y+decy,width,height);
		tf.setCanLoseFocus(true);
		tf.setMaxStringLength(10);
		tf.setFocused(false);
		this.textFieldList.add(tf);
	}
	public void addButton(int id, int decx, int decy, int width, int height, String str){
		GuiButton bt = new GuiButton(id,this.x+decx,this.y+decy,width,height,str);
		bt.visible = false;
		this.buttonList.add(bt);
	}

	public void addSfaButton(int id, int decx, int decy, int width, int height, String str){
		GuiButtonSFA bt = new GuiButtonSFA(id,this.x+decx,this.y+decy,width,height,str);
		bt.visible = false;
		this.buttonList.add(bt);
	}
	
	public void addButton(GuiButton bt){
		this.buttonList.add(bt);
	}
	
	public int nextStringId(){
		this.stringID+=1;
		return this.stringID;
	}
	
	public void addText(String str, int decx, int decy){
		int nid = this.nextStringId();
		this.stringList.put(nid,str);
		this.cStringList.put(nid, new Integer[]{decx,decy+strY+globalDecY});
		
		this.globalDecY+=decy;
		this.strY += this.ystr;
	}
	
	public boolean clicked(int x, int y){
		return this.isLink && this.isMouseHover(x, y);
	}
	
	public void replaceString(int id, String str){
		this.stringList.replace(id, str);
	}
	
	public void draw(int x, int y){
		this.color = this.defColor;
		if(this.isLink && this.isMouseHover(x, y)){
    		this.color = this.linkColor;
		}
		Iterator<GuiTextField> tfl = textFieldList.iterator();
		while (tfl.hasNext()) {
		    tfl.next().drawTextBox();
		}
		
		Iterator<Map.Entry<Integer,String>> it = stringList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer,String> entry = it.next();
			Integer[] t = this.cStringList.get(entry.getKey());
			if(globalScale!=1){
				GL11.glPushMatrix();
				GL11.glScalef(globalScale, globalScale, globalScale);
			}
			int xp = globalScale!=1?(int) (this.x+t[0]+((globalScale)*this.x)):this.x+t[0];
			int yp = globalScale!=1?(int) (this.y-this.scrolableMarge+t[1]+((globalScale)*this.y)):this.y-this.scrolableMarge+t[1];
			this.fr.drawString(String.format("%s%s%s",this.color,entry.getValue(), EnumChatFormatting.RESET), xp, yp, 4210752);
			if(globalScale!=1)
				GL11.glPopMatrix();
		}
		this.color = this.defColor;
	}
	
	public void setText(String txt){
		this.text = txt;
	}
	
	public boolean isMouseHover(int mx, int my){
		return mx<=this.x+this.width && mx>=this.x && my<this.y-this.scrolableMarge+this.height && my>this.y-this.scrolableMarge;
	}
}
