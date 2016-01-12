package noelflantier.sfartifacts.client.gui.bases;

import java.util.Hashtable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.References;

public class GuiScrollable {

	public int maxComponent = 10;
	public int currentIndex = 0;
	public int dir = 0;
	public Hashtable<Integer, GuiComponent> componentList = new Hashtable<Integer, GuiComponent>();
	public int x,y;
	public int tickDelay = 2;
	public int tickInput = 0;
	public boolean showArrows = false;
	public int tickInputWheel = 0;
	public int tickDelayWheel = 10;
	private static final ResourceLocation guiselements = new ResourceLocation(References.MODID+":textures/gui/guisElements.png");
	public GuiImage arrowUp = new GuiImage(64, 16, 32,32 , 0F, 0.5F, 0.5F, 1F, guiselements);
	public GuiImage arrowDown = new GuiImage(64, 16, 32,32 , 0.5F, 0.5F, 1F, 1F, guiselements);
	
	
	public GuiScrollable(int maxComponent){
		this.maxComponent = maxComponent; 
	}
	
	public void addComponent(int id, GuiComponent gc){
		this.componentList.put(id, gc);
	}
	
	public void input(){
		this.inputKeyBoard();
		this.inputMouse();
	}
	
	public void inputKeyBoard(){
		this.tickInput--;
		if(tickInput>0)return;
		
		this.tickInput = 0;
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			this.incIndex();
			this.tickInput = tickDelay;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			this.decIndex();
			this.tickInput = tickDelay;
		}
		return;
	}
	
	public void inputMouse(){
		this.tickInputWheel--;
		if(tickInputWheel>0)return;
		
		this.tickInputWheel = 0;
		int w = Mouse.getEventDWheel();
		if(w<0){
			this.incIndex();
			this.tickInputWheel = tickDelayWheel;
		}
		if(w>0){
			this.decIndex();
			this.tickInputWheel = tickDelayWheel;
		}
		return;
	}
	
	public void incIndex(){
		this.currentIndex = this.componentList.size()>this.maxComponent?this.currentIndex<this.componentList.size()-this.maxComponent?this.currentIndex+1:this.componentList.size()-this.maxComponent:0;
	}
	
	public void decIndex(){
		this.currentIndex = this.currentIndex>0?this.currentIndex-1:0;
	}
	
	public void setArrowsPositionAndAlpha(int x, int y, int dec, float alpha){
		arrowUp.x = x;
		arrowUp.y = y;
		arrowDown.x = x;
		arrowDown.y = y+dec;
		arrowUp.alpha = alpha;
		arrowDown.alpha = alpha;
	} 
	
	public void showTheArrows(int x, int y){
		arrowUp.draw(x, y);
		arrowDown.draw(x, y);
	}
	
	public void show(int x, int y){
		if(showArrows)
			showTheArrows(x,y);
		for(int i = this.currentIndex ; i < this.currentIndex + this.maxComponent ; i++){
			if(this.componentList.get(i)==null)break;
	    	this.componentList.get(i).scrolableMarge = this.currentIndex*10;
		    this.componentList.get(i).draw(x,y);
		}
	}
}
