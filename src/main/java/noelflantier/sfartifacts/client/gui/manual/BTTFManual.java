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
						addText("You can setup the inductor transmission in it gui.",0,0);
						addText("The inductor will share all its power equally, to all the",0,0);
						addText("other connected inductors.",0,0);
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
			
		}
	}

}
