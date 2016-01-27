package noelflantier.sfartifacts.client.gui.bases;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;

public class GuiComponent extends GuiComponentBase{

	public List<GuiButton> buttonList = new ArrayList<GuiButton>();
	public List<GuiTextField> textFieldList  = new ArrayList<GuiTextField>();
	public List<GuiImage> imageList = new ArrayList<GuiImage>();
	public List<GuiItemStack> itemStackList = new ArrayList<GuiItemStack>();
	public Hashtable<Integer,String> stringList = new Hashtable<Integer,String>();
	public Hashtable<Integer,Integer[]> cStringList = new Hashtable<Integer,Integer[]>();
	public int stringX;
	public int stringY;
	public float globalScale = 1;
	public boolean isVisible = true;
	public boolean isLink = false;
	public boolean isScrolable = false;
	public int scrollingYMarge = 0;
	public int globalYMarge = 0;
	public final int fontHeight = 10;
    public int stringID = -1;
    public EnumChatFormatting defColor = EnumChatFormatting.DARK_GRAY;
    public EnumChatFormatting color = EnumChatFormatting.DARK_GRAY;
    public EnumChatFormatting linkColor = EnumChatFormatting.GRAY;
	
	public GuiComponent(int x, int y){
		super(x,y);
	}

	public GuiComponent(int x, int y, int w, int h){
		super(x,y,w,h);
	}
	
	public void reset(){
		this.buttonList.clear();
		this.textFieldList.clear();
		this.stringList.clear();
		this.cStringList.clear();
		this.globalYMarge = 0;
		this.stringID = -1;
		this.stringY = 0;
		this.stringX = 0;
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

	public void addImage(GuiImage img){
		this.imageList.add(img);
	}

	public void addItemStack(GuiItemStack st){
		this.itemStackList.add(st);
	}
	
	public void addImageButton(GuiButtonImage bt, float baseU, float baseV, int elW, int elH, boolean enable){
		bt.baseU = baseU;
		bt.baseV = baseV;
		bt.elemPHeight = elH;
		bt.elemPWidth = elW;
		bt.enable = enable;
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
		this.cStringList.put(nid, new Integer[]{decx,decy+stringY+globalYMarge});
		
		this.globalYMarge+=decy;
		this.stringY += this.fontHeight;
	}
	
	public boolean clicked(int x, int y){
		return this.isLink && this.isMouseHover(x, y);
	}
	
	public void replaceString(int id, String str){
		this.stringList.replace(id, str);
	}
	
	public void draw(int x, int y){
		this.color = this.defColor;
		if(this.isLink && this.isMouseHover(x, y))
    		this.color = this.linkColor;
		Iterator<Map.Entry<Integer,String>> it = stringList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer,String> entry = it.next();
			Integer[] t = this.cStringList.get(entry.getKey());
			
			GL11.glPushMatrix();
				GL11.glScalef(globalScale, globalScale, 1);
				int xp = (int) (this.x+t[0]+(this.x-this.x*globalScale)/globalScale);
				int yp = (int) (this.y-this.scrollingYMarge+t[1]+((this.y-this.scrollingYMarge)-(this.y-this.scrollingYMarge)*globalScale)/globalScale);
				FR.drawString(String.format("%s%s%s", this.color, entry.getValue(), EnumChatFormatting.RESET), xp, yp, 4210752);
			GL11.glPopMatrix();
		}
		this.color = this.defColor;

		this.textFieldList.forEach((t)->t.drawTextBox());
		this.imageList.forEach((i)->i.draw(x, y));
		this.itemStackList.forEach((i)->i.draw(x, y));
	}
	
	public boolean isMouseHover(int mx, int my){
		return mx<=this.x+this.width && mx>=this.x && my<this.y-this.scrollingYMarge+this.height && my>this.y-this.scrollingYMarge;
	}
}
