package noelflantier.sfartifacts.client.gui.bases;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public abstract class GuiSFA  extends GuiContainer{

	//BUTTON RELATIVE TO THE SCREEN
	//X & Y PARAMS OF MOUSECLIKED AND DRAW FUNCTIONS RELATIVE TO SCREEN
	
	public boolean componentloaded = false;
	public Hashtable<String, GuiComponent> componentList = new Hashtable<String, GuiComponent>();
	public String curentToolTipComponent = "";
	public Hashtable<String, Integer> txtFieldComponent = new Hashtable<String, Integer>();
	public ArrayList<Character> onlyNumericKey= new ArrayList<Character>(){{
		add('0');add('1');add('2');add('3');add('4');add('5');add('6');add('7');add('8');add('9');add('+');add('-');
		}};
	public ArrayList<Integer> genericKey = new ArrayList<Integer>(){{
		add(14);add(203);add(208);add(200);add(205);add(1);add(211);
		}};
	
	public GuiSFA(Container container) {
		super(container);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		if(txtFieldComponent.size()<=0)
			return;		
		Iterator <Map.Entry<String,Integer>>iterator = txtFieldComponent.entrySet().iterator();
        while (iterator.hasNext()){
        	Map.Entry<String,Integer> entry = iterator.next();
		    if(this.componentList.containsKey(entry.getKey()))
		    	this.componentList.get(entry.getKey()).textFieldList.get(entry.getValue()).updateCursorCounter();
        }
	}
	
	public GuiTextField getTFFocused(){
		GuiTextField f = null;
		boolean flag = false;
		Iterator <Map.Entry<String,Integer>>iterator = txtFieldComponent.entrySet().iterator();
        while (iterator.hasNext()){
        	Map.Entry<String,Integer> entry = iterator.next();
		    if(this.componentList.containsKey(entry.getKey()))
		    	flag = this.componentList.get(entry.getKey()).textFieldList.get(entry.getValue()).isFocused();
		    if(flag){
		    	f = this.componentList.get(entry.getKey()).textFieldList.get(entry.getValue());
		    	break;
		    }
        }
        return f;
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		if(txtFieldComponent.size()<=0){
			super.keyTyped(par1, par2);
			return;
		}else{
			GuiTextField tf = getTFFocused();
			if(( par2 == this.mc.gameSettings.keyBindInventory.getKeyCode() || par2 == 1 ) && tf==null) {
				super.keyTyped(par1, par2);
	    	}
			if(tf!=null){
				if(tf instanceof GuiSFATextField){
					boolean flag = false;
					if( ((GuiSFATextField)tf).isOnlyNumeric ){
						flag = genericKey.contains(par2) || onlyNumericKey.contains(par1);
					}
					if(flag)
						tf.textboxKeyTyped(par1, par2);
				}else
					tf.textboxKeyTyped(par1, par2);
			}
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);

		if(txtFieldComponent.size()>0){		
			Iterator <Map.Entry<String,Integer>>iterator = txtFieldComponent.entrySet().iterator();
	        while (iterator.hasNext()){
	        	Map.Entry<String,Integer> entry = iterator.next();
			    if(this.componentList.containsKey(entry.getKey()))
			    	this.componentList.get(entry.getKey()).textFieldList.get(entry.getValue()).mouseClicked(x-guiLeft, y-guiTop, button);
	        }
		}
		
	}
	
	public void loadComponents(){
		if(this.componentloaded)return;
		this.componentloaded = true;
	}
	
	public abstract void updateToolTips(String key);
	
	//DRAW FIRST PLAN COORD RELATIVE TO SCREEN
	@Override
    public void drawScreen(int x, int y, float f){
    	super.drawScreen(x, y, f);
		Locale.setDefault(Locale.US);
		if(curentToolTipComponent != ""){
	    	this.updateToolTips(curentToolTipComponent);
    		((GuiToolTips)this.componentList.get(curentToolTipComponent)).showToolTips(x, y);
		}
		drawOver(x,y);
	}
	
	public void drawOver(int x, int y){
		
	}
	
	//DRAW MIDDLE PLAN COORD RELATIVE TO GUILEFT & GUITOP
	@Override
    public void drawGuiContainerForegroundLayer(int x, int y){
		boolean ttk = false;
		Enumeration<String> enumKey = this.componentList.keys();
		while (enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    this.componentList.get(key).draw(x, y);
	    	if(this.componentList.get(key) instanceof GuiToolTips && this.componentList.get(key).isMouseHover(x,y)){
	    		curentToolTipComponent = key;
	    		ttk = true;
	    	}
		}
		if(!ttk)
			curentToolTipComponent="";
    }

	//DRAW BACK PLAN COORD RELATIVE TO GUILEFT & GUITOP
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		
	}
	
	public GuiButton getButtonById(int id){
		GuiButton tbt = null;
		for(int i = 0;i<this.buttonList.size();i++)
			if(this.buttonList.get(i) instanceof GuiButton && ((GuiButton)this.buttonList.get(i)).id==id)
				tbt = (GuiButton)this.buttonList.get(i);
		return tbt;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.loadComponents();
		Enumeration<String> enumKey = this.componentList.keys();
		while (enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    for (GuiButton gb : this.componentList.get(key).buttonList){
		    	gb.visible = true;
    			this.buttonList.add(gb);
    		}
		}
	}
}
