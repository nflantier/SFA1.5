package noelflantier.sfartifacts.client.gui.manual;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.RecipeMightyFoundry;
import noelflantier.sfartifacts.common.recipes.RecipeMold;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.MightyFoundryRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.MoldRecipesHandler;

public class CaptainManual extends BaseManual{

	public CaptainManual(EntityPlayer player){
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
				addText("Captain Manual", 0, 0);
				defColor = EnumChatFormatting.BLACK;
			}});
		}else{
			this.componentList.put("index", new GuiComponent(this.guiLeft+this.xSize/2+10, this.guiTop+10, 100, 10){{
				addText(">Captain Manual<", 0, 0);
				isLink = true;
				defColor = EnumChatFormatting.BLACK;
			}});
			this.links.put("index", -1);
		}
		
		
		if(cat.equals("vore")){
			this.componentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("Vibranium ores are found only under Savanna biomes",0,0);
					addText("(Savanna, Savanna M, Savanna Plateau, Savanna Plateau",0,0);
					addText(" M). Vibranium ores need to be 'cooked' for you to be",0,0);
					addText("able to mine them into dust. The only way to cook them",0,0);
					addText("it's to surround them with lava, they will start heating",0,0);
					addText("and become cooked after some time. Then you will be",0,0);
					addText("able to mine them into their precious dust.",0,0);
					addText("You can find them between the Y levels 3 to 18.",0,0);
				}}
			);
		}else if(cat.equals("foundry")){

			this.componentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("To use the foundry you need Molds.",0,0);
					addText("Once you have a valid mold put it in the foundry and",0,0);
					addText("lock it. The foundry works only if the mold is locked,",0,0);
					addText("if you unlock it and get the mold out the progress on",0,0);
					addText("the mold will be deleted. The foundry works with RF",0,0);
					addText("energy but if you provide it lava, the process will be",0,0);
					addText("accelerate. You can put items to melt once the mold is",0,0);
					addText("locked.",0,0);
				}}
			);
		}else if(cat.equals("molds")){

			this.componentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("Molds are used in the Mighty Foundry.",0,0);
					addText("To fill a mold right click it and put in it inventory",0,0);
					addText("the sand in the shape of the mold you want.",0,0);
					addText("If the mold is valid you can see the name of the item",0,0);
					addText("on the mold annotations.",0,0);
				}}
			);
		}else if(cat.equals("foundryr")){
			this.componentList.put("p1", 
					new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
						defColor = EnumChatFormatting.BLACK;
						addText("Recipe you can do in the foundry :",0,0);
						addText("",0,0);
						for (Map.Entry<String, ISFARecipe> entry : RecipesRegistry.instance.getRecipesForUsage(MightyFoundryRecipesHandler.USAGE_MIGHTY_FOUNDRY).entrySet()){
							String str = StatCollector.translateToLocal(entry.getValue().getOutputs().get(0).getName()+".name");
							str+= " : "+RecipeMightyFoundry.class.cast(entry.getValue()).getItemQuantity()+" "+StatCollector.translateToLocal(entry.getValue().getInputs().get(0).getItemStack().getUnlocalizedName()+".name");
							addText(str,10,0);
						}
					}}
				);
		}else if(cat.equals("moldp")){
			this.componentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("1 = piece of sand | 0 = nothing",0,0);
				}}
			);
			int k = 0;
			for(Map.Entry<String, ISFARecipe> entry : RecipesRegistry.instance.getRecipesForUsage(MoldRecipesHandler.USAGE_MOLD).entrySet()){
				this.componentList.put("pm"+k, 
					new GuiComponent(this.guiLeft+10+k*100, this.guiTop+40, 100, 10){{
						defColor = EnumChatFormatting.BLACK;
						addText(""+entry.getValue().getUid()+" :",0,0);
						for(int i =0;i<RecipeMold.class.cast(entry.getValue()).getTabShape().length;i++){
							String bin = Integer.toBinaryString(RecipeMold.class.cast(entry.getValue()).getTabShape()[i]);
							int l = 9-bin.length();
							for(int j=0;j<l;j++)
								bin = "0"+bin;
							addText(bin,10,0);
						}
					}}
				);
				k+=1;
			}
		}else if(cat.equals("shield")){

			this.componentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("Vibranium shield can block damages from ennemies.",0,0);
					addText("Its a powerfull combat tool it can be use as a",0,0);
					addText("melee weapons or ranged if you throw it at any",0,0);
					addText("entities.",0,0);
					addText("Depending on the config :",0,0);
					addText("- Damages will only be blocked on a certain",10,0);
					addText("angle around you.",10,0);
					addText("- You have to press SHIFT to shield you and block",10,0);
					addText("damages.",10,0);
					addText("To see how to forge it go look at Molding Recipe.",0,0);
				}}
			);
		}else if(cat.equals("index")){

			this.componentList.put("vore", new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Vibranium ore", 0, 0);
				isLink = true;
			}});
			this.links.put("vore", -1);

			this.componentList.put("molds", new GuiComponent(this.guiLeft+10, this.guiTop+40, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Molds", 0, 0);
				isLink = true;
			}});
			this.links.put("molds", -1);
			
			this.componentList.put("foundry", new GuiComponent(this.guiLeft+10, this.guiTop+50, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Mighty foundry", 0, 0);
				isLink = true;
			}});
			this.links.put("foundry", -1);
			
			this.componentList.put("foundryr", new GuiComponent(this.guiLeft+10, this.guiTop+60, 120, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Molding Recipe", 0, 0);
				isLink = true;
			}});
			this.links.put("foundryr", -1);
			
			this.componentList.put("moldp", new GuiComponent(this.guiLeft+10, this.guiTop+70, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Molds pattern", 0, 0);
				isLink = true;
			}});
			this.links.put("moldp", -1);
			
			this.componentList.put("shield", new GuiComponent(this.guiLeft+10, this.guiTop+80, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Vibranium Shield", 0, 0);
				isLink = true;
			}});
			this.links.put("shield", -1);
		}
	}
}
