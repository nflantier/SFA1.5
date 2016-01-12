package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import noelflantier.sfartifacts.References;
import noelflantier.sfartifacts.common.items.ItemHoverBoard;

public class RenderItemHoverBoard  implements IItemRenderer{

	private ResourceLocation objMattelHB = new ResourceLocation(References.MODID+":textures/items/models/mattel_hoverboard.obj");
	private ResourceLocation textMattelHB = new ResourceLocation(References.MODID+":textures/items/models/mattel_hoverboard.png");
	private IModelCustom modelMattelHB;

	private ResourceLocation objPitBullHB = new ResourceLocation(References.MODID+":textures/items/models/pitbull_hoverboard_board.obj");
	private ResourceLocation textPitBullHB = new ResourceLocation(References.MODID+":textures/items/models/pitbull_hoverboard_board.png");
	private IModelCustom modelPitBullHB;
	
	private ResourceLocation objThruster = new ResourceLocation(References.MODID+":textures/items/models/pitbull_hoverboard_thruster.obj");
	private ResourceLocation textThruster = new ResourceLocation(References.MODID+":textures/items/models/pitbull_hoverboard_thruster.png");
	private IModelCustom modeThruster;
	
	public RenderItemHoverBoard(){
		this.modelMattelHB = AdvancedModelLoader.loadModel(this.objMattelHB);
		this.modelPitBullHB = AdvancedModelLoader.loadModel(this.objPitBullHB);
		this.modeThruster = AdvancedModelLoader.loadModel(this.objThruster);
	}
	
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type == ItemRenderType.ENTITY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
		GL11.glPushMatrix();
			if(stack.getItemDamage()>>1 == ItemHoverBoard.MATTEL_HOVERBOARD){
				GL11.glScalef(1.5F, 1.5F, 1.5F);
				Minecraft.getMinecraft().renderEngine.bindTexture(textMattelHB);
				this.modelMattelHB.renderAll();
			}
			if(stack.getItemDamage()>>1 == ItemHoverBoard.PITBULL_HOVERBOARD){
				GL11.glScalef(1.6F, 1.6F, 1.6F);
				Minecraft.getMinecraft().renderEngine.bindTexture(textPitBullHB);
				this.modelPitBullHB.renderAll();				
				Minecraft.getMinecraft().renderEngine.bindTexture(textThruster);
				this.modeThruster.renderAll();
				GL11.glTranslatef(0, 0, 0.69F);
				Minecraft.getMinecraft().renderEngine.bindTexture(textThruster);
				this.modeThruster.renderAll();
			}
		GL11.glPopMatrix();
	}

}
