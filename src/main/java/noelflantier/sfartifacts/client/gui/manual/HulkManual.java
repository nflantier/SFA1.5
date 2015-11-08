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
					addText("Hulk frequency's echo: -361",0,0);
					addText("Just before running away he will drop some flesh,",0,10);
					addText("try to use a weapon with loot to have multiple",0,0);
					addText("flesh dropped, you can eat them or try to make",0,0);
					addText("the Mighty Hulk Ring.",0,0);
				}}
			);
		}else if(cat.equals("hulkring")){
			this.componentList.put("p1", 
					new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
						defColor = EnumChatFormatting.BLACK;
						addText("The  Mighty Hulk Ring give you more health regen,",0,0);
						addText("more speed, step assist, augmented jump, prevent",0,0);
						addText("fall damages and your bares hand becomes stronger,",0,0);
						addText("you can mine and fight pretty efficiently with",0,0);
						addText("them. To equip the ring you have to use Baubles,",0,0);
						addText("and put the ring in a ring slot.",0,0);
					}}
				);

		}else if(cat.equals("freq")){
			this.componentList.put("p1", 
					new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
						defColor = EnumChatFormatting.BLACK;
						addText("If you want all the frequencies of all the mobs",0,0);
						addText("presents in your game, there is an option in the",0,0);
						addText("config to show them all in the sound emiter GUI.",0,0);
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
			
			this.componentList.put("hulkring", new GuiComponent(this.guiLeft+10, this.guiTop+50, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Mighty Hulk Ring", 0, 0);
				isLink = true;
			}});
			this.manuals.put("hulkring", -1);
			
			this.componentList.put("freq", new GuiComponent(this.guiLeft+10, this.guiTop+50, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Mob frequencies", 0, 0);
				isLink = true;
			}});
			this.manuals.put("freq", -1);
			
		}
	}

}
