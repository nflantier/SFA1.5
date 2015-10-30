package noelflantier.sfartifacts.client.gui.manual;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.common.handlers.ModGUIs;

public class HulkManual extends BaseManual{

	public HulkManual(EntityPlayer player){
		super(player);
	}
	@Override
	public void drawCat(String cat, int x, int y, float f) {
		this.componentList.put("manuals", new GuiComponent(this.guiLeft+this.xSize/2-60, this.guiTop+10, 50, 10){{
			addText("MANUALS", 0, 0);
			isLink = true;
			defColor = EnumChatFormatting.BLACK;
		}});
		this.manuals.put("manuals", ModGUIs.guiIDManual);		
		
		this.componentList.put("slash", new GuiComponent(this.guiLeft+this.xSize/2-12, this.guiTop+10, 50, 10){{
			addText("  /", 0, 0);
			defColor = EnumChatFormatting.BLACK;
		}});
		
		if(cat.equals("index")){
			this.componentList.put("title", new GuiComponent(this.guiLeft+this.xSize/2+10, this.guiTop+10, 100, 10){{
				addText("Hulk Manual", 0, 0);
				defColor = EnumChatFormatting.BLACK;
			}});
		}else{
			this.componentList.put("index", new GuiComponent(this.guiLeft+this.xSize/2+10, this.guiTop+10, 100, 10){{
				addText(">Hulk Manual<", 0, 0);
				isLink = true;
				defColor = EnumChatFormatting.BLACK;
			}});
			this.manuals.put("index", -1);
		}
		
		
		if(cat.equals("sound")){
			this.componentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("This machine will emit sound on a certain", 0, 0);
					addText("frequency. Different frequency will spawn", 0, 0);
					addText("different creature so be carefull.", 0, 0);
					addText("All the frequency discovered are saved in your", 0, 0);
					addText("list. To emit sounds, it use some RF and liquefied", 0, 0);
					addText("asgardite depending on the frequency.", 0, 0);
					addText("The majority of the creatures spawned will be", 0, 0);
					addText("attracted to the source of the sound.", 0, 0);
					addText("If you dont want to search all the frequencies,", 0, 0);
					addText("there is an option in the config file that", 0, 0);
					addText("will add all the possible frequencies and their", 0, 0);
					addText("echos to your list.", 0, 0);
				}}
			);
		}else if(cat.equals("hulk")){
			this.componentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("Hulk can be lure with the sound emitter,",0,0);
					addText("you can't kill him but if you hurt him",0,0);
					addText("really bad he will run away.",0,0);
				}}
			);
		}else if(cat.equals("index")){

			this.componentList.put("sound", new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Sound Emitter", 0, 0);
				isLink = true;
			}});
			this.manuals.put("sound", -1);

			this.componentList.put("hulk", new GuiComponent(this.guiLeft+10, this.guiTop+40, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Hulk", 0, 0);
				isLink = true;
			}});
			this.manuals.put("hulk", -1);
			
		}
	}

}
