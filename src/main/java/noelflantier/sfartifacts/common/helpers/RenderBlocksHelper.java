package noelflantier.sfartifacts.common.helpers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderBlocksHelper {

	/*static RenderBlocksHelper rbh;
	
	public RenderBlocksHelper(IBlockAccess par1iBlockAccess) {
		super(par1iBlockAccess);
	}
  
	public static RenderBlocksHelper getRenderBlocksHelper(){    
		if(rbh == null) {
	      rbh = new RenderBlocksHelper(Minecraft.getMinecraft().theWorld);
	    }
		return rbh ;
	}*/

	public static void renderFace(IBlockAccess blockAccess, RenderBlocks renderer, Block block, int x, int y, int z, int face, IIcon icon){

        switch(face){
        	case 0:
        		renderFace(blockAccess, renderer, block, x, y, z, "y-", icon);
        		break;
        	case 1:
        		renderFace(blockAccess, renderer, block, x, y, z, "y+", icon);
        		break;
        	case 2:
        		renderFace(blockAccess, renderer, block, x, y, z, "z-", icon);
        		break;
        	case 3:
        		renderFace(blockAccess, renderer, block, x, y, z, "z+", icon);
        		break;
        	case 4:
        		renderFace(blockAccess, renderer, block, x, y, z, "x-", icon);
        		break;
        	case 5:
        		renderFace(blockAccess, renderer, block, x, y, z, "x+", icon);
        		break;
        }
	}
	
	public static void renderSimpleFace(double x, double y, double z, String face, IIcon icon, float alpha, double decU, double decV){
        switch(face){
		case "x-":
	        renderSimpleXneg( x, y, z, icon, alpha, decU, decV);
			break;
		case "x+":
			renderSimpleXPos(x, y, z, icon, alpha, decU, decV);
			break;
		case "y-":
	        renderSimpleYNeg( x, y, z, icon, alpha, decU, decV);
			break;
		case "y+":
	        renderSimpleYPos( x, y, z, icon, alpha, decU, decV);
			break;
		case "z-":
	        renderSimpleZNeg( x, y, z, icon, alpha, decU, decV);
			break;
		case "z+":
	        renderSimpleZPos( x, y, z, icon, alpha, decU, decV);
			break;
		case "z":
	        renderSimpleZNeg( x, y, z, icon, alpha, decU, decV);
	        renderSimpleZPos( x, y, z, icon, alpha, decU, decV);
			break;
		case "x":
			renderSimpleXneg(x, y, z, icon, alpha, decU, decV);
			renderSimpleXPos(x, y, z, icon, alpha, decU, decV);
			break;
		case "y":
			renderSimpleYNeg(x, y, z, icon, alpha, decU, decV);
			renderSimpleYPos(x, y, z, icon, alpha, decU, decV);
			break;
		default:
			break;
        }
        
	}
	
	public static void renderFace(IBlockAccess blockAccess, RenderBlocks renderer, Block block, int x, int y, int z, String face, IIcon icon){
        int l = block.colorMultiplier(blockAccess, x, y, z);
        float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }
        switch(face){
		case "x-":
	        renderXneg(renderer, block, x, y, z, f, f1, f2, icon);
			break;
		case "x+":
	        renderXpos(renderer, block, x, y, z, f, f1, f2, icon);
			break;
		case "y-":
	        renderYneg(renderer, block, x, y, z, f, f1, f2, icon);
			break;
		case "y+":
	        renderYpos(renderer, block, x, y, z, f, f1, f2, icon);
			break;
		case "z-":
	        renderZneg(renderer, block, x, y, z, f, f1, f2, icon);
			break;
		case "z+":
	        renderZpos(renderer, block, x, y, z, f, f1, f2, icon);
			break;
		case "z":
	        renderZneg(renderer, block, x, y, z, f, f1, f2, icon);
	        renderZpos(renderer, block, x, y, z, f, f1, f2, icon);
			break;
		case "x":
			renderXneg(renderer, block, x, y, z, f, f1, f2, icon);
			renderXpos(renderer, block, x, y, z, f, f1, f2, icon);
			break;
		case "y":
	        renderYneg(renderer, block, x, y, z, f, f1, f2, icon);
	        renderYpos(renderer, block, x, y, z, f, f1, f2, icon);
			break;
		default:
			break;
        }
        
	}
	
	public static void renderSimpleYNeg(double x, double y, double z, IIcon ic, float alpha, double decU, double decV){	
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Tessellator tessellator = Tessellator.instance;
        double ts = 16.0D;
        double minU = (double)ic.getInterpolatedU(0 * ts);
        double maxU = (double)ic.getInterpolatedU(1 * ts);
        double minV = (double)ic.getInterpolatedV(0 * ts);
        double maxV = (double)ic.getInterpolatedV(1 * ts);
        tessellator.startDrawingQuads();

       
		if(alpha<1){
			GL11.glEnable(GL11.GL_BLEND);
			//GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1F, 1F, 1F, alpha);
		}
    	tessellator.setColorRGBA_F((float)1, (float)1, (float)1, (float)alpha);
    	tessellator.setBrightness(240);
        tessellator.addVertexWithUV(x+1-decU, y+decU, z+decV, maxU, maxV);
        tessellator.addVertexWithUV(x+1-decU, y+decU, z+1-decV, maxU, minV);
        tessellator.addVertexWithUV(x+decU, y+decU, z+1-decV, minU, minV);
        tessellator.addVertexWithUV(x+decU, y+decU, z+decV, minU, maxV);
        tessellator.draw();
        if(alpha<1){
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			//GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
        }
	}
	
	public static void renderSimpleYPos(double x, double y, double z, IIcon ic, float alpha, double decU, double decV){	
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Tessellator tessellator = Tessellator.instance;
        double ts = 16.0D;
        double minU = (double)ic.getInterpolatedU(0 * ts);
        double maxU = (double)ic.getInterpolatedU(1 * ts);
        double minV = (double)ic.getInterpolatedV(0 * ts);
        double maxV = (double)ic.getInterpolatedV(1 * ts);
        tessellator.startDrawingQuads();

       
		if(alpha<1){
			GL11.glEnable(GL11.GL_BLEND);
			//GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1F, 1F, 1F, alpha);
		}
    	tessellator.setColorRGBA_F((float)1, (float)1, (float)1, (float)alpha);
    	tessellator.setBrightness(240);
        tessellator.addVertexWithUV(x+decU, y+1-decU, z+decV, maxU, maxV);
        tessellator.addVertexWithUV(x+decU, y+1-decU, z+1-decV, maxU, minV);
        tessellator.addVertexWithUV(x+1-decU, y+1-decU, z+1-decV, minU, minV);
        tessellator.addVertexWithUV(x+1-decU, y+1-decU, z+decV, minU, maxV);
        tessellator.draw();
        if(alpha<1){
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			//GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
        }
	}
	
	public static void renderSimpleXneg(double x, double y, double z, IIcon ic, float alpha, double decU, double decV){	
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Tessellator tessellator = Tessellator.instance;
        double ts = 16.0D;
        double minU = (double)ic.getInterpolatedU(0 * ts);
        double maxU = (double)ic.getInterpolatedU(1 * ts);
        double minV = (double)ic.getInterpolatedV(0 * ts);
        double maxV = (double)ic.getInterpolatedV(1 * ts);
        tessellator.startDrawingQuads();

       
		if(alpha<1){
			GL11.glEnable(GL11.GL_BLEND);
			//GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1F, 1F, 1F, alpha);
		}
    	tessellator.setColorRGBA_F((float)1, (float)1, (float)1, (float)alpha);
    	tessellator.setBrightness(240);
        tessellator.addVertexWithUV(x+decU, y+decV, z+1-decU, maxU, maxV);
        tessellator.addVertexWithUV(x+decU, y+1-decV, z+1-decU, maxU, minV);
        tessellator.addVertexWithUV(x+decU, y+1-decV, z+decU, minU, minV);
        tessellator.addVertexWithUV(x+decU, y+decV, z+decU, minU, maxV);
        tessellator.draw();
        if(alpha<1){
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			//GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
        }
	}
	
	public static void renderSimpleXPos(double x, double y, double z, IIcon ic, float alpha, double decU, double decV){	
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Tessellator tessellator = Tessellator.instance;
        double ts = 16.0D;
        double minU = (double)ic.getInterpolatedU(0 * ts);
        double maxU = (double)ic.getInterpolatedU(1 * ts);
        double minV = (double)ic.getInterpolatedV(0 * ts);
        double maxV = (double)ic.getInterpolatedV(1 * ts);
        tessellator.startDrawingQuads();

       
		if(alpha<1){
			GL11.glEnable(GL11.GL_BLEND);
			//GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1F, 1F, 1F, alpha);
		}
    	tessellator.setColorRGBA_F((float)1, (float)1, (float)1, (float)alpha);
    	tessellator.setBrightness(240);
        tessellator.addVertexWithUV(x+1-decU, y+decV, z+decU, maxU, maxV);
        tessellator.addVertexWithUV(x+1-decU, y+1-decV, z+decU, maxU, minV);
        tessellator.addVertexWithUV(x+1-decU, y+1-decV, z+1-decU, minU, minV);
        tessellator.addVertexWithUV(x+1-decU, y+decV, z+1-decU, minU, maxV);
        tessellator.draw();
        if(alpha<1){
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			//GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
        }
	}
	
	public static void renderSimpleZNeg(double x, double y, double z, IIcon ic, float alpha, double decU, double decV){	
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Tessellator tessellator = Tessellator.instance;
        double ts = 16.0D;
        double minU = (double)ic.getInterpolatedU(0 * ts);
        double maxU = (double)ic.getInterpolatedU(1 * ts);
        double minV = (double)ic.getInterpolatedV(0 * ts);
        double maxV = (double)ic.getInterpolatedV(1 * ts);
        tessellator.startDrawingQuads();

       
		if(alpha<1){
			GL11.glEnable(GL11.GL_BLEND);
			//GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1F, 1F, 1F, alpha);
		}
    	tessellator.setColorRGBA_F((float)1, (float)1, (float)1, (float)alpha);
    	tessellator.setBrightness(240);
        tessellator.addVertexWithUV(x+decU, y+decV, z+decU, maxU, maxV);
        tessellator.addVertexWithUV(x+decU, y+1-decV, z+decU, maxU, minV);
        tessellator.addVertexWithUV(x+1-decU, y+1-decV, z+decU, minU, minV);
        tessellator.addVertexWithUV(x+1-decU, y+decV, z+decU, minU, maxV);
        tessellator.draw();
        if(alpha<1){
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			//GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
        }
	}
	
	public static void renderSimpleZPos(double x, double y, double z, IIcon ic, float alpha, double decU, double decV){	
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Tessellator tessellator = Tessellator.instance;
        double ts = 16.0D;
        double minU = (double)ic.getInterpolatedU(0 * ts);
        double maxU = (double)ic.getInterpolatedU(1 * ts);
        double minV = (double)ic.getInterpolatedV(0 * ts);
        double maxV = (double)ic.getInterpolatedV(1 * ts);
        tessellator.startDrawingQuads();

       
		if(alpha<1){
			GL11.glEnable(GL11.GL_BLEND);
			//GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1F, 1F, 1F, alpha);
		}
    	tessellator.setColorRGBA_F((float)1, (float)1, (float)1, (float)alpha);
    	tessellator.setBrightness(240);
        tessellator.addVertexWithUV(x+1-decU, y+decV, z+1-decU, maxU, maxV);
        tessellator.addVertexWithUV(x+1-decU, y+1-decV, z+1-decU, maxU, minV);
        tessellator.addVertexWithUV(x+decU, y+1-decV, z+1-decU, minU, minV);
        tessellator.addVertexWithUV(x+decU, y+decV, z+1-decU, minU, maxV);
        tessellator.draw();
        if(alpha<1){
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			//GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
        }
	}
	
	public static void renderXpos(RenderBlocks renderer, Block block, int x, int y, int z, float p_147751_5_, float p_147751_6_, float p_147751_7_,  IIcon icon){
		renderer.enableAO = true;
        boolean flag = false;
        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        boolean flag1 = true;
        int l = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);
        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        int i1;
        float f7;  
        
        if (renderer.renderMaxX >= 1.0D)
        {
            ++x;
        }

        renderer.aoLightValueScratchXYPN = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
        renderer.aoLightValueScratchXZPN = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
        renderer.aoLightValueScratchXZPP = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
        renderer.aoLightValueScratchXYPP = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
        renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
        renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
        renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
        renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
        flag2 = renderer.blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
        flag3 = renderer.blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();
        flag4 = renderer.blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();
        flag5 = renderer.blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();

        if (!flag3 && !flag5)
        {
            renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
            renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
        }
        else
        {
            renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z - 1);
        }

        if (!flag3 && !flag4)
        {
            renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
            renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
        }
        else
        {
            renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z + 1);
        }

        if (!flag2 && !flag5)
        {
            renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
            renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
        }
        else
        {
            renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z - 1);
        }

        if (!flag2 && !flag4)
        {
            renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
            renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
        }
        else
        {
            renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z + 1);
        }

        if (renderer.renderMaxX >= 1.0D)
        {
            --x;
        }

        i1 = l;

        if (renderer.renderMaxX >= 1.0D || !renderer.blockAccess.getBlock(x + 1, y, z).isOpaqueCube())
        {
            i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
        }

        f7 = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
        f3 = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + f7 + renderer.aoLightValueScratchXZPP) / 4.0F;
        f4 = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + f7) / 4.0F;
        f5 = (renderer.aoLightValueScratchXZPN + f7 + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
        f6 = (f7 + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
        renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
        renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, i1);
        renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, i1);
        renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, i1);

        if (flag1)
        {
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.6F;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.6F;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.6F;
        }
        else
        {
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
        }

        renderer.colorRedTopLeft *= f3;
        renderer.colorGreenTopLeft *= f3;
        renderer.colorBlueTopLeft *= f3;
        renderer.colorRedBottomLeft *= f4;
        renderer.colorGreenBottomLeft *= f4;
        renderer.colorBlueBottomLeft *= f4;
        renderer.colorRedBottomRight *= f5;
        renderer.colorGreenBottomRight *= f5;
        renderer.colorBlueBottomRight *= f5;
        renderer.colorRedTopRight *= f6;
        renderer.colorGreenTopRight *= f6;
        renderer.colorBlueTopRight *= f6;
        renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, icon);
	}
	
	public static void renderXneg(RenderBlocks renderer, Block block, int x, int y, int z, float p_147751_5_, float p_147751_6_, float p_147751_7_,  IIcon icon){
		renderer.enableAO = true;
        boolean flag = false;
        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        boolean flag1 = true;
        int l = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);
        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        int i1;
        float f7;  
        
        if (renderer.renderMinX <= 0.0D)
        {
            --x;
        }

        renderer.aoLightValueScratchXYNN = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
        renderer.aoLightValueScratchXZNN = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
        renderer.aoLightValueScratchXZNP = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
        renderer.aoLightValueScratchXYNP = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
        renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
        renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
        renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
        renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
        flag2 = renderer.blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
        flag3 = renderer.blockAccess.getBlock(x - 1, y - 1, z).getCanBlockGrass();
        flag4 = renderer.blockAccess.getBlock(x - 1, y, z - 1).getCanBlockGrass();
        flag5 = renderer.blockAccess.getBlock(x - 1, y, z + 1).getCanBlockGrass();

        if (!flag4 && !flag3)
        {
            renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
            renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
        }
        else
        {
            renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z - 1);
        }

        if (!flag5 && !flag3)
        {
            renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
            renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
        }
        else
        {
            renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z + 1);
        }

        if (!flag4 && !flag2)
        {
            renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
            renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
        }
        else
        {
            renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z - 1);
        }

        if (!flag5 && !flag2)
        {
            renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
            renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
        }
        else
        {
            renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z + 1);
        }

        if (renderer.renderMinX <= 0.0D)
        {
            ++x;
        }

        i1 = l;

        if (renderer.renderMinX <= 0.0D || !renderer.blockAccess.getBlock(x - 1, y, z).isOpaqueCube())
        {
            i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
        }

        f7 = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
        f6 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + f7 + renderer.aoLightValueScratchXZNP) / 4.0F;
        f3 = (f7 + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
        f4 = (renderer.aoLightValueScratchXZNN + f7 + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
        f5 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + f7) / 4.0F;
        renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, i1);
        renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, i1);
        renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, i1);
        renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, i1);

        if (flag1)
        {
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.6F;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.6F;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.6F;
        }
        else
        {
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
        }

        renderer.colorRedTopLeft *= f3;
        renderer.colorGreenTopLeft *= f3;
        renderer.colorBlueTopLeft *= f3;
        renderer.colorRedBottomLeft *= f4;
        renderer.colorGreenBottomLeft *= f4;
        renderer.colorBlueBottomLeft *= f4;
        renderer.colorRedBottomRight *= f5;
        renderer.colorGreenBottomRight *= f5;
        renderer.colorBlueBottomRight *= f5;
        renderer.colorRedTopRight *= f6;
        renderer.colorGreenTopRight *= f6;
        renderer.colorBlueTopRight *= f6;
        renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, icon);

	}
	
	public static void renderZpos(RenderBlocks renderer, Block block, int x, int y, int z, float p_147751_5_, float p_147751_6_, float p_147751_7_,  IIcon icon){
		renderer.enableAO = true;
        boolean flag = false;
        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        boolean flag1 = true;
        int l = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);
        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        int i1;
        float f7;  
        
			if (renderer.renderMaxZ >= 1.0D)
           {
               ++z;
           }

           renderer.aoLightValueScratchXZNP = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
           renderer.aoLightValueScratchXZPP = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
           renderer.aoLightValueScratchYZNP = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
           renderer.aoLightValueScratchYZPP = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
           renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
           renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
           renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
           renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
           flag2 = renderer.blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();
           flag3 = renderer.blockAccess.getBlock(x - 1, y, z + 1).getCanBlockGrass();
           flag4 = renderer.blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
           flag5 = renderer.blockAccess.getBlock(x, y - 1, z + 1).getCanBlockGrass();

           if (!flag3 && !flag5)
           {
               renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
               renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
           }
           else
           {
               renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
               renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y - 1, z);
           }

           if (!flag3 && !flag4)
           {
               renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
               renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
           }
           else
           {
               renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
               renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y + 1, z);
           }

           if (!flag2 && !flag5)
           {
               renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
               renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
           }
           else
           {
               renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
               renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y - 1, z);
           }

           if (!flag2 && !flag4)
           {
               renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
               renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
           }
           else
           {
               renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
               renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y + 1, z);
           }

           if (renderer.renderMaxZ >= 1.0D)
           {
               --z;
           }

           i1 = l;

           if (renderer.renderMaxZ >= 1.0D || !renderer.blockAccess.getBlock(x, y, z + 1).isOpaqueCube())
           {
               i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
           }

           f7 = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
           f3 = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + f7 + renderer.aoLightValueScratchYZPP) / 4.0F;
           f6 = (f7 + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
           f5 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
           f4 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
           renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, i1);
           renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, i1);
           renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
           renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, i1);

           if (flag1)
           {
               renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.8F;
               renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.8F;
               renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.8F;
           }
           else
           {
               renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
               renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
               renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
           }

           renderer.colorRedTopLeft *= f3;
           renderer.colorGreenTopLeft *= f3;
           renderer.colorBlueTopLeft *= f3;
           renderer.colorRedBottomLeft *= f4;
           renderer.colorGreenBottomLeft *= f4;
           renderer.colorBlueBottomLeft *= f4;
           renderer.colorRedBottomRight *= f5;
           renderer.colorGreenBottomRight *= f5;
           renderer.colorBlueBottomRight *= f5;
           renderer.colorRedTopRight *= f6;
           renderer.colorGreenTopRight *= f6;
           renderer.colorBlueTopRight *= f6;
           renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, icon);

	}
	
	public static void renderZneg(RenderBlocks renderer, Block block, int x, int y, int z, float p_147751_5_, float p_147751_6_, float p_147751_7_,  IIcon icon){
		
		renderer.enableAO = true;
        boolean flag = false;
        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        boolean flag1 = true;
        int l = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);
        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        int i1;
        float f7;
		if (renderer.renderMinZ <= 0.0D){
			--z;
        }

         renderer.aoLightValueScratchXZNN = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
         renderer.aoLightValueScratchYZNN = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
         renderer.aoLightValueScratchYZPN = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
         renderer.aoLightValueScratchXZPN = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
         renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
         renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
         renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
         renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
         flag2 = renderer.blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();
         flag3 = renderer.blockAccess.getBlock(x - 1, y, z - 1).getCanBlockGrass();
         flag4 = renderer.blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();
         flag5 = renderer.blockAccess.getBlock(x, y - 1, z - 1).getCanBlockGrass();

         if (!flag3 && !flag5)
         {
             renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
             renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
         }
         else
         {
             renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
             renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y - 1, z);
         }

         if (!flag3 && !flag4)
         {
             renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
             renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
         }
         else
         {
             renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
             renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y + 1, z);
         }

         if (!flag2 && !flag5)
         {
             renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
             renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
         }
         else
         {
             renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
             renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y - 1, z);
         }

         if (!flag2 && !flag4)
         {
             renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
             renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
         }
         else
         {
             renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
             renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y + 1, z);
         }

         if (renderer.renderMinZ <= 0.0D)
         {
             ++z;
         }

         i1 = l;

         if (renderer.renderMinZ <= 0.0D || !renderer.blockAccess.getBlock(x, y, z - 1).isOpaqueCube())
         {
             i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
         }

         f7 = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
         f3 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
         f4 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
         f5 = (renderer.aoLightValueScratchYZNN + f7 + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
         f6 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + f7) / 4.0F;
         renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
         renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, i1);
         renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, i1);
         renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, i1);

         if (flag1)
         {
             renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.8F;
             renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.8F;
             renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.8F;
         }
         else
         {
             renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
             renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
             renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
         }

         renderer.colorRedTopLeft *= f3;
         renderer.colorGreenTopLeft *= f3;
         renderer.colorBlueTopLeft *= f3;
         renderer.colorRedBottomLeft *= f4;
         renderer.colorGreenBottomLeft *= f4;
         renderer.colorBlueBottomLeft *= f4;
         renderer.colorRedBottomRight *= f5;
         renderer.colorGreenBottomRight *= f5;
         renderer.colorBlueBottomRight *= f5;
         renderer.colorRedTopRight *= f6;
         renderer.colorGreenTopRight *= f6;
         renderer.colorBlueTopRight *= f6;
         renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, icon);
	}
	
	public static void renderYneg(RenderBlocks renderer, Block block, int x, int y, int z, float p_147751_5_, float p_147751_6_, float p_147751_7_,  IIcon icon){
		 
		renderer.enableAO = true;
        boolean flag = false;
        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        boolean flag1 = true;
        int l = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);
        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        int i1;
        float f7;
		if (renderer.renderMinY <= 0.0D)
          {
              --y;
          }

          renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
          renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
          renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
          renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
          renderer.aoLightValueScratchXYNN = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
          renderer.aoLightValueScratchYZNN = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
          renderer.aoLightValueScratchYZNP = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
          renderer.aoLightValueScratchXYPN = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
          flag2 = renderer.blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();
          flag3 = renderer.blockAccess.getBlock(x - 1, y - 1, z).getCanBlockGrass();
          flag4 = renderer.blockAccess.getBlock(x, y - 1, z + 1).getCanBlockGrass();
          flag5 = renderer.blockAccess.getBlock(x, y - 1, z - 1).getCanBlockGrass();

          if (!flag5 && !flag3)
          {
              renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXYNN;
              renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXYNN;
          }
          else
          {
              renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
              renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z - 1);
          }

          if (!flag4 && !flag3)
          {
              renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXYNN;
              renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXYNN;
          }
          else
          {
              renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
              renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z + 1);
          }

          if (!flag5 && !flag2)
          {
              renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXYPN;
              renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXYPN;
          }
          else
          {
              renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
              renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z - 1);
          }

          if (!flag4 && !flag2)
          {
              renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXYPN;
              renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXYPN;
          }
          else
          {
              renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
              renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z + 1);
          }

          if (renderer.renderMinY <= 0.0D)
          {
              ++y;
          }

          i1 = l;

          if (renderer.renderMinY <= 0.0D || !renderer.blockAccess.getBlock(x, y - 1, z).isOpaqueCube())
          {
              i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
          }

          f7 = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
          f3 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
          f6 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
          f5 = (f7 + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
          f4 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + f7 + renderer.aoLightValueScratchYZNN) / 4.0F;
          renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, i1);
          renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, i1);
          renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, i1);
          renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, i1);

          if (flag1)
          {
              renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.5F;
              renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.5F;
              renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.5F;
          }
          else
          {
              renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.5F;
              renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.5F;
              renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.5F;
          }

          renderer.colorRedTopLeft *= f3;
          renderer.colorGreenTopLeft *= f3;
          renderer.colorBlueTopLeft *= f3;
          renderer.colorRedBottomLeft *= f4;
          renderer.colorGreenBottomLeft *= f4;
          renderer.colorBlueBottomLeft *= f4;
          renderer.colorRedBottomRight *= f5;
          renderer.colorGreenBottomRight *= f5;
          renderer.colorBlueBottomRight *= f5;
          renderer.colorRedTopRight *= f6;
          renderer.colorGreenTopRight *= f6;
          renderer.colorBlueTopRight *= f6;
          renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, icon);
          
	}
	
	public static void renderYpos(RenderBlocks renderer, Block block, int x, int y, int z, float p_147751_5_, float p_147751_6_, float p_147751_7_,  IIcon icon){
		renderer.enableAO = true;
        boolean flag = false;
        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        boolean flag1 = true;
        int l = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);
        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        int i1;
        float f7;
		if (renderer.renderMaxY >= 1.0D)
        {
            ++y;
        }

        renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
        renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
        renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
        renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
        renderer.aoLightValueScratchXYNP = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
        renderer.aoLightValueScratchXYPP = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
        renderer.aoLightValueScratchYZPN = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
        renderer.aoLightValueScratchYZPP = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
        flag2 = renderer.blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
        flag3 = renderer.blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
        flag4 = renderer.blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
        flag5 = renderer.blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();

        if (!flag5 && !flag3)
        {
            renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXYNP;
            renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXYNP;
        }
        else
        {
            renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z - 1);
        }

        if (!flag5 && !flag2)
        {
            renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXYPP;
            renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXYPP;
        }
        else
        {
            renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z - 1);
        }

        if (!flag4 && !flag3)
        {
            renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXYNP;
            renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXYNP;
        }
        else
        {
            renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z + 1);
        }

        if (!flag4 && !flag2)
        {
            renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXYPP;
            renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXYPP;
        }
        else
        {
            renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z + 1);
        }

        if (renderer.renderMaxY >= 1.0D)
        {
            --y;
        }

        i1 = l;

        if (renderer.renderMaxY >= 1.0D || !renderer.blockAccess.getBlock(x, y + 1, z).isOpaqueCube())
        {
            i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
        }

        f7 = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
        f6 = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + f7) / 4.0F;
        f3 = (renderer.aoLightValueScratchYZPP + f7 + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
        f4 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
        f5 = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
        renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, i1);
        renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, i1);
        renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, i1);
        renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
        renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_;
        renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_;
        renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_;
        renderer.colorRedTopLeft *= f3;
        renderer.colorGreenTopLeft *= f3;
        renderer.colorBlueTopLeft *= f3;
        renderer.colorRedBottomLeft *= f4;
        renderer.colorGreenBottomLeft *= f4;
        renderer.colorBlueBottomLeft *= f4;
        renderer.colorRedBottomRight *= f5;
        renderer.colorGreenBottomRight *= f5;
        renderer.colorBlueBottomRight *= f5;
        renderer.colorRedTopRight *= f6;
        renderer.colorGreenTopRight *= f6;
        renderer.colorBlueTopRight *= f6;
        renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, icon);
        
	}
}
