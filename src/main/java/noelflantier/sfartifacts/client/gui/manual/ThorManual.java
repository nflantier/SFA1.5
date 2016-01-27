package noelflantier.sfartifacts.client.gui.manual;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiImage;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.helpers.PillarMaterials;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.RecipeInput;
import noelflantier.sfartifacts.common.recipes.RecipeOutput;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.HammerUpgradesRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.InjectorRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig.Pillar;

public class ThorManual extends BaseManual{
	
	public ThorManual(EntityPlayer player){
		super(player);
	}
	
	public void drawCat(String cat, int x, int y, float f){
		this.fullComponentList.put("manuals", new GuiComponent(this.guiLeft+this.xSize/2-60, this.guiTop+10, 50, 10){{
			addText("MANUALS", 0, 0);
			isLink = true;
			defColor = EnumChatFormatting.BLACK;
		}});
		this.links.put("manuals", ModGUIs.guiIDManual);

		this.fullComponentList.put("slash", new GuiComponent(this.guiLeft+this.xSize/2-12, this.guiTop+10, 50, 10){{
			addText("  /", 0, 0);
			defColor = EnumChatFormatting.BLACK;
		}});
		
		if(cat.equals("index")){
			this.fullComponentList.put("title", new GuiComponent(this.guiLeft+this.xSize/2+10, this.guiTop+10, 50, 10){{
				addText("Thor Manual", 0, 0);
				defColor = EnumChatFormatting.BLACK;
			}});
		}else{
			this.fullComponentList.put("index", new GuiComponent(this.guiLeft+this.xSize/2+10, this.guiTop+10, 50, 10){{
				addText(">Thor Manual<", 0, 0);
				isLink = true;
				defColor = EnumChatFormatting.BLACK;
			}});
			this.links.put("index", -1);
		}
		
		if(cat.equals("materials")){

			this.fullComponentList.put("m1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					addText("There is 3 types of materials :  ",0,0);
					addText("Agardite : basic asgardian material (RF ratio :"+PillarMaterials.values()[2].energyRatio+")",10,5);
					addText("Agardian Steel : iron in the injector (RF ratio :"+PillarMaterials.values()[1].energyRatio+")",10,0);
					addText("Agardian Bronze : gold in the injector (RF ratio :"+PillarMaterials.values()[0].energyRatio+")",10,0);
					addText("They all handle energy and fluids with their proper",0,5);
					addText("efficiency ratio. So it will affect the structure",0,0);
					addText("or the block efficency that your willing to make",0,0);
					addText("with the said material.",0,0);
					defColor = EnumChatFormatting.BLACK;
				}}
			);

		}else if(cat.equals("pillars")){

			this.fullComponentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("Pillars are multiblock structures, there is 3 models",0,0);
					addText("possible, you can build them with asgardian materials.",0,0);
					addText("The best way to aquire the materials needed to build",0,0);
					addText("a pillar is to find a village they often have some",0,0);
					addText("pillar ruins.",0,0);
					addText("Care to not mine or destroy the master block of the",0,10);
					addText("structure, you can find it at the center bottom layer,",0,0);
					addText("or you will loose all the data of the structure.",0,0);
				}}
			);
			this.fullComponentList.put("pillars2", new GuiComponent(this.guiLeft+10, this.guiTop+145, 35, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Next->", 0, 0);
				isLink = true;
			}});
			this.links.put("pillars2", -1);

		}else if(cat.equals("pillars2")){

			/*this.componentList.put("im", 
				new GuiImage(this.guiLeft+10, this.guiTop+30, 85, 105, 
						new ResourceLocation(References.MODID+":textures/manual/all_crop.png"))
			);*/
			this.fullComponentList.put("p2", 
					new GuiComponent(this.guiLeft+100, this.guiTop+30, 100, 10){{
						defColor = EnumChatFormatting.BLACK;
						addText("Pillars can be linked wirelessly,",0,0);
						addText("with the basic hammer, to",0,0);
						addText("any asgardian machine or any other",0,0);
						addText("mod machines which accept RF.",0,0);
						addText("Once linked it will provide energy",0,0);
						addText("in equal share to all the connected",0,0);
						addText("machines.",0,0);
						addText("Or pillars can directly recieve",0,10);
						addText("/transmit fluid and energy by their",0,0);
						addText("interface :",0,0);
					}}
				);
			/*this.componentList.put("im2", 
					new GuiImage(this.guiLeft+150, this.guiTop+130, 16, 16, 
							new ResourceLocation(References.MODID+":textures/blocks/asgardite_full_interface_out.png"))
				);*/
			this.fullComponentList.put("pillars", new GuiComponent(this.guiLeft+10, this.guiTop+145, 35, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("<-Prev", 0, 0);
				isLink = true;
			}});
			this.links.put("pillars", -1);
		}else if(cat.equals("bhammer2")){
			this.fullComponentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("Basic mode :",0,0);
					addText("- Build pillars : once the structure is done, right",10,0);
					addText("click on the highest block of the pillar and it will",10,0);
					addText("setup the structure if you did it right.",10,0);
					
					addText("- Link pillars and machines : once you have a working",10,10);
					addText("pillar right click on it and then shift right click",10,0);
					addText("on any asgardian or RF machine even from other",10,0);
					addText(" mods. Then the pillar will distribute his RF power",10,0);
					addText("to the connected machines.",10,0);
				}}
			);	
		this.fullComponentList.put("bhammer", new GuiComponent(this.guiLeft+10, this.guiTop+145, 35, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("<-Prev", 0, 0);
				isLink = true;
			}});
			this.links.put("bhammer", -1);	
		}else if(cat.equals("bhammer")){

			this.fullComponentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("The Basic hammer has 2 modes you can change them by",0,0);
					addText("shift right clicking it in on an empty space.",0,0);
					addText("Construction mode :",0,10);
					addText("- Show pillars structures : place an asgardian block",10,0);
					addText("of any materials on the ground and shift right click",10,0);
					addText("it to see in 3D all the pillars structures you can do.",10,0);
				}}
			);			
			
			this.fullComponentList.put("bhammer2", new GuiComponent(this.guiLeft+10, this.guiTop+145, 35, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Next->", 0, 0);
				isLink = true;
			}});
			this.links.put("bhammer2", -1);
			
		}else if(cat.equals("liquefier")){

			this.fullComponentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("The liquefier will need water, RF power and some",0,0);
					addText("asgardite to create liquefied asgardite needed",0,0);
					addText("for the injector or producing more RF in your pillar,",0,0);
					
					addText("You can place a liquefier directly sided to a pillar",0,10);
					addText("interface, link it wirelessly with the basic hammer or",0,0);
					addText("use cables, pipes from other mods. Once connected, it",0,0);
					addText("will fill the pillar fluid container.",0,0);
					
					addText("Water can be inserted from all sides. Asgardite can be",0,10);
					addText("inserted from all sides. Liquefied asgardite can only",0,0);
					addText("be extracted from the back.",0,0);
				}}
			);

		}else if(cat.equals("injector")){

			this.fullComponentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("The injector will need liquefied asgardite and RF power",0,0);
					addText("to transform ordinary materials into adgardian",0,0);
					addText("materials.",0,0);
					
					addText("You can place an injector directly sided to a pillar",0,10);
					addText("interface, link it wirelessly with the basic hammer or",0,0);
					addText("use cables, pipes from other mods. Once connected, it",0,0);
					addText("will extract from the pillar fluid container.",0,0);
					
					addText("Liquefied asgardite can only be inserted from the",0,10);
					addText("back. Recipes materials can be inserted from all sides.",0,0);
				}}
			);

		}else if(cat.equals("irecipe")){
			this.fullComponentList.put("p", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("Recipe you can do in the injector :",0,0);
				}});
			this.fullComponentList.put("p1", 
					new GuiComponent(this.guiLeft+15, this.guiTop+40, 100, 10){{
						defColor = EnumChatFormatting.BLACK;
						globalScale = 0.6F;
						addText("",0,0);
						for(Map.Entry<String, ISFARecipe> entry : RecipesRegistry.instance.getRecipesForUsage(InjectorRecipesHandler.USAGE_INJECTOR).entrySet()){
							String str = "";
							for(RecipeOutput o : entry.getValue().getOutputs()){
								str+=StatCollector.translateToLocal(o.getName()+".name");
							}
							str+=" ( ";
							int k = 0;
							for(RecipeInput i : entry.getValue().getInputs()){
								str+=i.getStackSize()+"  "+StatCollector.translateToLocal(i.getName()+".name");
								if(k+1!= entry.getValue().getInputs().size())
									str+=", ";
								k+=1;
							}
							addText(str+" | "+entry.getValue().getEnergyCost()+" RF | "+entry.getValue().getFluidCost()+" MB )",0,0);
						}
					}}
				);
		}else if(cat.equals("hammers")){

			this.fullComponentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("You will need an hammer stand to invok Thor's hammer, ",0,0);
					addText("just link it with the basic hammer to a pillar, if the",0,0);
					addText("pillar has sufficient energy (at least "+ModConfig.rfNeededThorHammer+" RF)",0,0);
					addText("you will be able to invok the hammer.",0,0);
					addText("The hammer stand act like an inventory only holding",0,10);
					addText("one hammer, once in the inventory the hammer will",0,0);
					addText("recharge itself if the hammer stand is connected to",0,0);
					addText("a pillar with power.",0,0);
				}}
			);

		}else if(cat.equals("thammer")){

			this.fullComponentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("Thor's Hammer has 4 modes (right click), you can",0,0);
					addText("change them by shift right clicking it :",0,0);
					addText("- Mining (default) : you can throw your hammer",5,0);
					addText("it will mine everything on its way.",5,0);
					addText("- Lightning : you can strike block or entity, from",5,0);
					addText("a distance, with lightning it will cause damage and fire.",5,0);
					addText("- Moving : you can throw your hammer and move with it.",5,0);
					addText("- Teleporting : you can set teleport point everywhere.",5,0);

					addText("At any moment you can use (left click) your hammer as",0,10);
					addText("a multitool, it act like a pickaxe, a shovel and a sword",0,0);
					addText("in a more efficient way.",0,0);
				}}
			);
		}else if(cat.equals("hu3")){
			this.fullComponentList.put("p1", 
					new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
						defColor = EnumChatFormatting.BLACK;
						addText("-Teleport : give you the ability to set coordinate",0,10);
						addText("and teleport to it. Item : 9*Asgardium Pearl.",0,0);
					}}
				);
			this.fullComponentList.put("hu2", new GuiComponent(this.guiLeft+10, this.guiTop+145, 35, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("<-Prev", 0, 0);
				isLink = true;
			}});
			this.links.put("hu2", -1);
		}else if(cat.equals("hu2")){
			this.fullComponentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("-Config upgrade : give you the ability to config your",0,10);
					addText("hammer every where instead of only in the hammer",0,0);
					addText("stand. Item : Control Panel.",0,0);
					addText("-Enchants : you can enchant your hammer with vanilla",0,10);
					addText("minecraft tools enchants. Item : Any enchanted books.",0,0);
					addText("-Moving upgrade : give you the ability to use the",0,10);
					addText("moving mode. Item : Uber Mighty Feather.",0,0);
				}}
			);
			this.fullComponentList.put("hu3", new GuiComponent(this.guiLeft+50, this.guiTop+145, 35, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Next->", 0, 0);
				isLink = true;
			}});
			this.links.put("hu3", -1);
			this.fullComponentList.put("hu", new GuiComponent(this.guiLeft+10, this.guiTop+145, 35, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("<-Prev", 0, 0);
				isLink = true;
			}});
			this.links.put("hu", -1);
		}else if(cat.equals("hu")){
			this.fullComponentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("To apply upgrade to your hammer place it in an",0,0);
					addText("hammer stand, drop the items needed for the upgrade ",0,0);
					addText("on top of the stand and right click the top of the",0,0);
					addText("stand with a basic hammer until it disappear.",0,0);
				}}
			);
		}else if(cat.equals("hurecipe")){
			this.fullComponentList.put("p1", 
					new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
						defColor = EnumChatFormatting.BLACK;
						addText("Upgrades for Thor's hammer :",0,0);
						addText("",0,0);
						for(Map.Entry<String, ISFARecipe> entry : 
							RecipesRegistry.instance.getRecipesForUsage(HammerUpgradesRecipesHandler.USAGE_HAMMER_UPGRADES).entrySet()){
							String str = ""+entry.getKey()+" : ( ";
							int k = 0;
							for(RecipeInput i : entry.getValue().getInputs()){
								str+=i.getStackSize()+"  "+StatCollector.translateToLocal(i.getName()+".name");
								if(k+1!= entry.getValue().getInputs().size())
									str+=", ";
								k+=1;
							}
							addText(str+" )",0,0);
						}
					}}
				);
		}else if(cat.equals("hc")){

			this.fullComponentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("You can configure your hammer when it is in an",0,0);
					addText("hammer stand or if you have the config upgrade",0,0);
					addText("installed,you can press H (by default) when your are",0,0);
					addText("holding it.",0,0);
					addText("You can configure the enchants your hammer has.",0,10);
					addText("Just click on enchants in the config GUI and",0,0);
					addText("disable or enable enchants you allready have installed.",0,0);
				}}
			);
		}else if(cat.equals("ps")){

			Locale.setDefault(Locale.US);
			this.fullComponentList.put("p1", 
					new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
						defColor = EnumChatFormatting.BLACK;
						addText("To see all the pillars structures you can do",0,0);
						addText("right click a block of asgardian material with",0,0);
						addText("a basic hammer set in construction mode :",0,0);
					}}
				);
			this.fullComponentList.put("p2", 
					new GuiComponent(this.guiLeft-12, this.guiTop+40, 100, 10){{
						defColor = EnumChatFormatting.BLACK;
						globalScale = 0.8F;
						for(Map.Entry<String, Pillar> entry :PillarsConfig.getInstance().nameToPillar.entrySet()){
							addText(entry.getKey()+" : "+entry.getValue().mapStructure.size()+" blocks; "+
									NumberFormat.getNumberInstance().format(entry.getValue().energyCapacity)+" RF; "+
									NumberFormat.getNumberInstance().format(entry.getValue().fluidCapacity)+" FL; ratio : "+
									entry.getValue().naturalRatio+".",0,0);
						}
					}}
				);
		}else if(cat.equals("pe")){
			this.fullComponentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("Pillar will passively produce energy depending on : their",0,0);
					addText("material ratio, their structural ratio, the height",0,0);
					addText("there at (higher will produce more) and if it is raining",0,0);
					addText("or not (it will produce more while raining ).",0,0);
					addText("To produce additional energy you can attach a lightning",0,10);
					addText("rod stand to the top of the pillar, it will produce RF",0,0);
					addText("if you put a lightning rod in it.",0,0);
				}}
			);
		}else if(cat.equals("pfe")){
			this.fullComponentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("An other way to produce tons of energy its to use",0,0);
					addText("liquified asgardite inside the pillar, if you connect",0,0);
					addText("a control panel to the pillar you will be able to",0,0);
					addText("control the amount of fluid per tick your pillar will",0,0);
					addText("consume to produce additional energy.",0,0);
				}}
			);
		}else if(cat.equals("uf")){
			this.fullComponentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("Mighty feather are dropped by chiken, dead of a",0,0);
					addText("lightning strike, the easyest way to get some its",0,0);
					addText("to install the lightning mode on your hammer and",0,0);
					addText("kill some chicken with it. Then you will be able",0,0);
					addText("to craft Uber mighty feather.",0,0);
				}}
			);
		}else if(cat.equals("howto")){
			this.fullComponentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("To start you will need to build a pillar, you can go",0,0);
					addText("mine asgardite or find villages and salvage their ruins",0,0);
					addText("to gather materials.",0,0);
					addText("You will need to make an hammer stand and connect it",0,0);
					addText("to a pillar, if you don't have enough energy stored",0,0);
					addText("or you don't want to wait the pillar to slowly fill",0,0);
					addText("itself, maybe you'll need a liquefier and an injector",0,0);
					addText("to have better materials to build better pillars to",0,0);
					addText("be able to invok Thor's hammer.",0,0);
					addText("After invoking enjoy your hammer upgrade it and be OP.",0,0);
				}}
			);
		}else if(cat.equals("tips")){
			this.fullComponentList.put("p1", 
				new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
					defColor = EnumChatFormatting.BLACK;
					addText("To find a lot of asgardite ore easily, find an extreme",0,0);
					addText("hills biome, you'll see ores on the surface.",0,0);
					addText("Steel or bronze pillars are worth building instead",0,10);
					addText("of waiting your asgardite pillar to fill.",0,0);
				}}
			);
		}else if(cat.equals("index")){
			this.fullComponentList.put("materials", new GuiComponent(this.guiLeft+10, this.guiTop+30, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Materials", 0, 0);
				isLink = true;
			}});
			this.links.put("materials", -1);

			this.fullComponentList.put("pillars", new GuiComponent(this.guiLeft+10, this.guiTop+40, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Pillars", 0, 0);
				isLink = true;
			}});
			this.links.put("pillars", -1);

			this.fullComponentList.put("bhammer", new GuiComponent(this.guiLeft+10, this.guiTop+50, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Basic Hammer", 0, 0);
				isLink = true;
			}});
			this.links.put("bhammer", -1);


			this.fullComponentList.put("liquefier", new GuiComponent(this.guiLeft+10, this.guiTop+60, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Liquefier", 0, 0);
				isLink = true;
			}});
			this.links.put("liquefier", -1);


			this.fullComponentList.put("injector", new GuiComponent(this.guiLeft+10, this.guiTop+70, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Injector", 0, 0);
				isLink = true;
			}});
			this.links.put("injector", -1);

			this.fullComponentList.put("hammers", new GuiComponent(this.guiLeft+10, this.guiTop+80, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("HammerStand", 0, 0);
				isLink = true;
			}});
			this.links.put("hammers", -1);
			
			this.fullComponentList.put("thammer", new GuiComponent(this.guiLeft+10, this.guiTop+90, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Thor's Hammer", 0, 0);
				isLink = true;
			}});
			this.links.put("thammer", -1);

			this.fullComponentList.put("uf", new GuiComponent(this.guiLeft+10, this.guiTop+100, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("(Uber) Mighty Feather", 0, 0);
				isLink = true;
			}});
			this.links.put("uf", -1);

			this.fullComponentList.put("howto", new GuiComponent(this.guiLeft+10, this.guiTop+120, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("How to progress", 0, 0);
				isLink = true;
			}});
			this.links.put("howto", -1);

			this.fullComponentList.put("tips", new GuiComponent(this.guiLeft+10, this.guiTop+130, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Some tips", 0, 0);
				isLink = true;
			}});
			this.links.put("tips", -1);
			
			this.fullComponentList.put("ps", new GuiComponent(this.guiLeft+150, this.guiTop+30, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Pillar structures", 0, 0);
				isLink = true;
			}});
			this.links.put("ps", -1);
			
			this.fullComponentList.put("pe", new GuiComponent(this.guiLeft+150, this.guiTop+40, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Pillar passive energy", 0, 0);
				isLink = true;
			}});
			this.links.put("pe", -1);
			
			this.fullComponentList.put("pfe", new GuiComponent(this.guiLeft+150, this.guiTop+50, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Pillar fluid energy", 0, 0);
				isLink = true;
			}});
			this.links.put("pfe", -1);
			
			this.fullComponentList.put("hu", new GuiComponent(this.guiLeft+150, this.guiTop+60, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Hammer upgrades", 0, 0);
				isLink = true;
			}});
			this.links.put("hu", -1);
			
			this.fullComponentList.put("hurecipe", new GuiComponent(this.guiLeft+150, this.guiTop+70, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Upgrades recipes", 0, 0);
				isLink = true;
			}});
			this.links.put("hurecipe", -1);
			
			this.fullComponentList.put("hc", new GuiComponent(this.guiLeft+150, this.guiTop+80, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Hammer config", 0, 0);
				isLink = true;
			}});
			this.links.put("hc", -1);
			
			this.fullComponentList.put("irecipe", new GuiComponent(this.guiLeft+150, this.guiTop+90, 100, 10){{
				defColor = EnumChatFormatting.BLACK;
				addText("Injector recipes", 0, 0);
				isLink = true;
			}});
			this.links.put("irecipe", -1);
		}
	}
	
}
