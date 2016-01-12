package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import noelflantier.sfartifacts.References;

public class RenderItemLightningRod  implements IItemRenderer{
	
	private ResourceLocation resLRpilon= new ResourceLocation(References.MODID+":textures/items/models/ltrpilon.obj");
	private ResourceLocation resLRdownring= new ResourceLocation(References.MODID+":textures/items/models/ltrdownring.obj");
	private ResourceLocation resLRupring= new ResourceLocation(References.MODID+":textures/items/models/ltrupring.obj");
	private ResourceLocation resLRball= new ResourceLocation(References.MODID+":textures/items/models/ltrball.obj");

	private IModelCustom modelLRpilon;
	private IModelCustom modelLRdownring;
	private IModelCustom modelLRupring;
	private IModelCustom modelLRball;
	
	public RenderItemLightningRod()
	{
		this.modelLRpilon = AdvancedModelLoader.loadModel(this.resLRpilon);
		this.modelLRdownring = AdvancedModelLoader.loadModel(this.resLRdownring);
		this.modelLRupring = AdvancedModelLoader.loadModel(this.resLRupring);
		this.modelLRball = AdvancedModelLoader.loadModel(this.resLRball);
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
		case EQUIPPED:
			return true;
		case EQUIPPED_FIRST_PERSON:
			return true;
		case ENTITY:
			return true;
		case INVENTORY:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

		GL11.glPushMatrix();
			
			switch(type){
			case ENTITY:
				GL11.glTranslatef(+0.0F,0.0F,+0.0F);
				GL11.glScalef(0.8F, 0.8F, 0.8F);
				break;
			case EQUIPPED_FIRST_PERSON:
				//GL11.glTranslatef(+1.0F,+0.0F,+0.0F);
				GL11.glScalef(0.5F, 0.5F, 0.5F);
				break;
			case INVENTORY:
				GL11.glScalef(0.4F, 0.4F, 0.4F);
				GL11.glTranslatef(-0.0F,-1.0F,+0.0F);
				GL11.glRotatef(45, 0.0F, 1.0F, 0.0F);
				break;
			case EQUIPPED:
				GL11.glScalef(0.8F, 0.8F, 0.8F);
				GL11.glTranslatef(+0.8F,+0.0F,+1.0F);
				break;
			default:
			}
	
			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(References.MODID+":textures/items/models/ltrpilon.png"));
			this.modelLRpilon.renderAll();
			if( item.getItemDamage()>0 ){
				Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(References.MODID+":textures/items/models/ltrdownring.png"));
					this.modelLRdownring.renderAll();
					if( item.getItemDamage()>1 ){
						Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(References.MODID+":textures/items/models/ltrupring.png"));
						this.modelLRupring.renderAll();
						if( item.getItemDamage()>2 ){
							Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(References.MODID+":textures/items/models/ltrball.png"));
							this.modelLRball.renderAll();
						}
					}
			}
		GL11.glPopMatrix();
	}

}
