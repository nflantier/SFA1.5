package noelflantier.sfartifacts.client.gui.manual;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.common.handlers.ModGUIs;

public class BTTFManual  extends BaseManual{

	public BTTFManual(EntityPlayer player){
		super(player);
	}

	public void drawCat(String cat, int x, int y, float f){
		this.componentList.put("manuals", new GuiComponent(this.guiLeft+this.xSize/2-60, this.guiTop+10, 50, 10){{
			addText("MANUALS", 0, 0);
			isLink = true;
			defColor = EnumChatFormatting.BLACK;
		}});
		this.links.put("manuals", ModGUIs.guiIDManual);		
		
		this.componentList.put("slash", new GuiComponent(this.guiLeft+this.xSize/2-12, this.guiTop+10, 50, 10){{
			addText("  /", 0, 0);
			defColor = EnumChatFormatting.BLACK;
		}});
		
		if(cat.equals("index")){
			this.componentList.put("title", new GuiComponent(this.guiLeft+this.xSize/2+10, this.guiTop+10, 100, 10){{
				addText("BTTF Manual", 0, 0);
				defColor = EnumChatFormatting.BLACK;
			}});
		}else{
			this.componentList.put("index", new GuiComponent(this.guiLeft+this.xSize/2+10, this.guiTop+10, 100, 10){{
				addText(">BTTF Manual<", 0, 0);
				isLink = true;
				defColor = EnumChatFormatting.BLACK;
			}});
			this.links.put("index", -1);
		}
		
		if(cat.equals("mrfusion")){
			this.componentList.put("p1", 
					new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
						defColor = EnumChatFormatting.BLACK;
						addText("Mr Fusion is a powerfull generator it can provide",0,0);
						addText("1.21 GW or GRF/T using fusion of everything you can",0,0);
						addText("find. However it will be more efficient with organic",0,0);
						addText("materials like foods and with any kind of liquids.",0,0);
					}}
				);

		}else if(cat.equals("inductor")){
			this.componentList.put("p1", 
					new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
						defColor = EnumChatFormatting.BLACK;
						addText("Inductor can transmit power wirelessly.",0,0);
						addText("To associate inductors you need to right click them with",0,0);
						addText("the basic hammer set in basic mode.",0,0);
						addText("The first inductor right clicked will provide energy",0,0);
						addText("to the other one.",0,0);
						addText("You can setup the inductor transmission in it gui.",0,0);
						addText("The inductor will share all its power equally, to all the",0,0);
						addText("other connected inductors.",0,0);
					}}
				);

		}else if(cat.equals("silicon")){
			this.componentList.put("p1", 
					new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
						defColor = EnumChatFormatting.BLACK;
						addText("To makes chips you will need some pure silicon.",0,0);
						addText("Pure silicon is made by smelting clear silicon",0,0);
						addText("in a furnace, if you want to craft chips with",0,0);
						addText("the pure silicon you'll have to hurry because",0,0);
						addText("pure silicon will naturaly come back to clear",0,0);
						addText("silicon after some times and you'll have to",0,0);
						addText("recook it.",0,0);
						addText("Clear silicon is made by smelting raw silicon",0,0);
						addText("in a furnace.",0,0);
					}}
				);

		}else if(cat.equals("index")){

			this.componentList.put("mrfusion", new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Mr Fusion", 0, 0);
				isLink = true;
			}});
			this.links.put("mrfusion", -1);

			this.componentList.put("inductor", new GuiComponent(this.guiLeft+10, this.guiTop+40, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Inductor", 0, 0);
				isLink = true;
			}});
			this.links.put("inductor", -1);

			this.componentList.put("silicon", new GuiComponent(this.guiLeft+10, this.guiTop+50, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Silicon and Chips", 0, 0);
				isLink = true;
			}});
			this.links.put("silicon", -1);
			
		}
	}

}
