package noelflantier.sfartifacts.client.gui;

import java.util.Hashtable;

import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.manual.ABaseCategory;
import noelflantier.sfartifacts.client.gui.manual.NThorManual;

public class NIndexManual extends ABaseCategory{
	
	public String CAT_THOR = "THOR";
	public String CAT_HULK = "HULK";
	public String CAT_CAPTAIN = "CAPTAIN";
	public String CAT_BTTF = "BTTF";
	
	public NIndexManual(String name, int x, int y) {
		super(name,x,y);
		initComponent();
	}
	
	@Override
	public void initComponent() {
		this.componentList.put(CAT_THOR, new GuiComponent(this.x+10, this.y+10, 100, 10){{
			addText("Thor", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_THOR, new NThorManual(CAT_THOR,this.x, this.y, this));
		
		this.componentList.put(CAT_HULK, new GuiComponent(this.x+10, this.y+20, 100, 10){{
			addText("Hulk", 0, 0);
			isLink = true;
		}});
		//listCategory.put(CAT_HULK, new CaptainManual());
		this.componentList.put(CAT_CAPTAIN, new GuiComponent(this.x+10, this.y+30, 100, 10){{
			addText("Captain america", 0, 0);
			isLink = true;
		}});
		//listCategory.put(CAT_CAPTAIN, new HulkManual());
		this.componentList.put(CAT_BTTF, new GuiComponent(this.x+10, this.y+40, 100, 10){{
			addText("Back to the Future", 0, 0);
			isLink = true;
		}});
		//listCategory.put(CAT_BTTF, new BTTFManual());
	}
}
