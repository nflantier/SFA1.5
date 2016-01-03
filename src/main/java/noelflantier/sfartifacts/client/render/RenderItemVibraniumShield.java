package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModEvents;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;

public class RenderItemVibraniumShield  implements IItemRenderer{
	
	private ResourceLocation objVS = new ResourceLocation(References.MODID+":textures/items/models/vibraniumshield.obj");
	private ResourceLocation textVS = new ResourceLocation(References.MODID+":textures/items/models/vibraniumshield.png");
	private ResourceLocation textVSCA = new ResourceLocation(References.MODID+":textures/items/models/captainamericashield.png");
	private ResourceLocation objHS = new ResourceLocation(References.MODID+":textures/items/models/handleshield.obj");
	private ResourceLocation textHS = new ResourceLocation(References.MODID+":textures/items/models/handleshield.png");
	private IModelCustom modelVibraniumShield;
	private IModelCustom modelHandleShield;

	public RenderItemVibraniumShield(){
		this.modelVibraniumShield = AdvancedModelLoader.loadModel(this.objVS);
		this.modelHandleShield = AdvancedModelLoader.loadModel(this.objHS);
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack,ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
		GL11.glPushMatrix();
		ResourceLocation textVSt = textVS;
		if(stack.getItemDamage()==1)
			textVSt = textVSCA;
		
		switch(type){
		case ENTITY:
			GL11.glScalef(1.5F, 1.5F, 1.5F);
			Minecraft.getMinecraft().renderEngine.bindTexture(textVSt);
			this.modelVibraniumShield.renderAll();
			Minecraft.getMinecraft().renderEngine.bindTexture(textHS);
			this.modelHandleShield.renderAll();
			break;
		case EQUIPPED_FIRST_PERSON:
			if(!ItemNBTHelper.getBoolean(stack, "IsThrown", false)){
				if(ItemNBTHelper.getBoolean(stack, "CanBlock", false)){
					GL11.glRotatef(-10,1F, 0F,0F);
					GL11.glRotatef(30,0F, 1F,0F);
					GL11.glRotatef(-70,0F, 0F,1F);
					GL11.glTranslatef(-0.4F, 0F, 0.8F);
					GL11.glScalef(1.5F, 1.5F,1.5F);
					Minecraft.getMinecraft().renderEngine.bindTexture(textVSt);
					this.modelVibraniumShield.renderAll();
					GL11.glRotatef(90,0F, 1F, 0F);
					Minecraft.getMinecraft().renderEngine.bindTexture(textHS);
					this.modelHandleShield.renderAll();
				}else{
					GL11.glRotatef(10,1F, 0F,0F);
					GL11.glRotatef(30,0F, 1F,0F);
					GL11.glRotatef(270,0F, 0F,1F);
					GL11.glTranslatef(0F, 0F, 1.8F);
					GL11.glScalef(1.5F, 1.5F,1.5F);
					Minecraft.getMinecraft().renderEngine.bindTexture(textVSt);
					this.modelVibraniumShield.renderAll();
					GL11.glRotatef(90,0F, 1F, 0F);
					Minecraft.getMinecraft().renderEngine.bindTexture(textHS);
					this.modelHandleShield.renderAll();
				}
			}
			break;
		case INVENTORY:
			GL11.glScalef(0.8F, 0.8F, 0.8F);
			GL11.glTranslatef(0F, 0F, 0F);
			GL11.glRotatef(30,1F, 0F,0F);
			Minecraft.getMinecraft().renderEngine.bindTexture(textVSt);
			this.modelVibraniumShield.renderAll();
			break;
		case EQUIPPED:
			if(!ItemNBTHelper.getBoolean(stack, "IsThrown", false)){
				if(ItemNBTHelper.getBoolean(stack, "CanBlock", false)){
					GL11.glRotatef(-80,1F, 0F,0F);
					GL11.glRotatef(50,0F, 0F,1F);
					GL11.glTranslatef(0F, -1.1F,1F);
					
					GL11.glScalef(1.5F, 1.5F,1.5F);
					Minecraft.getMinecraft().renderEngine.bindTexture(textVSt);
					this.modelVibraniumShield.renderAll();
					GL11.glRotatef(90,0F, 1F, 0F);
					Minecraft.getMinecraft().renderEngine.bindTexture(textHS);
					this.modelHandleShield.renderAll();
				}else{

					//GL11.glRotatef(90,0F, 1F,0F);
					GL11.glRotatef(0,1F, 0F,0F);
					GL11.glRotatef(-130,0F, 1F,0F);
					GL11.glRotatef(90,0F, 0F,1F);
					GL11.glTranslatef(0.5F, 0.2F, -1.5F);
					GL11.glScalef(1.5F, 1.5F,1.5F);
					Minecraft.getMinecraft().renderEngine.bindTexture(textVSt);
					this.modelVibraniumShield.renderAll();
					GL11.glRotatef(90,0F, 1F, 0F);
					Minecraft.getMinecraft().renderEngine.bindTexture(textHS);
					this.modelHandleShield.renderAll();
				}
			}
			break;
		default:
		}
		GL11.glPopMatrix();
	}

}
