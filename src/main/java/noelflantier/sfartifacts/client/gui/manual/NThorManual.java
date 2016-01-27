package noelflantier.sfartifacts.client.gui.manual;

import java.util.Hashtable;

import net.minecraft.util.EnumChatFormatting;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.common.handlers.ModGUIs;

public class NThorManual extends ABaseCategory{
		
	public NThorManual(String name, int x, int y) {
		super(name,x,y);
		initComponent();
	}
	public NThorManual(String name, int x, int y, ABaseCategory master) {
		super(name,x,y, master);
		initComponent();
	}
	
	@Override
	public void initComponent() {
		this.componentList.put(NGuiManual.CAT_INDEX, new GuiComponent(this.x+10, this.y+10, 100, 10){{
			addText("Index", 0, 0);
			isLink = true;
		}});
		listCategory.put(NGuiManual.CAT_INDEX, this.master);
		
		this.componentList.put("hc", new GuiComponent(this.x+10, this.y+20, 100, 10){{
			addText("Hammer config", 0, 0);
			isLink = true;
		}});
		listCategory.put("hc", new DummyCategory("hc",this.x, this.y, this){{
			addComponent("home",
				new GuiComponent(x+10, y+10, 100, 10){{
					addText("Home",0,0);
					isLink = true;
				}});
			addCategory("home",this.master);
			addComponent("hcin",
				new GuiComponent(x+10, y+20, 100, 10){{
					addText("You can configure your hammer when it is in an",0,0);
					addText("hammer stand or if you have the config upgrade",0,0);
					addText("installed,you can press H (by default) when your are",0,0);
					addText("holding it.",0,0);
					addText("You can configure the enchants your hammer has.",0,10);
					addText("Just click on enchants in the config GUI and",0,0);
					addText("disable or enable enchants you allready have installed.",0,0);
				}});
			}}
		);
		
		this.componentList.put("irecipe", new GuiComponent(this.x+10, this.y+30, 100, 10){{
			addText("Injector recipes", 0, 0);
			isLink = true;
		}});
		listCategory.put("irecipe", new DummyCategory("irecipe",this.x, this.y, this));
	}
}
