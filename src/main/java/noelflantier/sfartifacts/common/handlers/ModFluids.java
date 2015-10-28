package noelflantier.sfartifacts.common.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.common.blocks.BlockLiquefiedAsgardite;
import noelflantier.sfartifacts.common.helpers.RegisterHelper;

public class ModFluids {

	public static Fluid fluidLiquefiedAsgardite;
	public static Block blockLiquefiedAsgardite;
    public static Block[] fluidBlocks;
	
	public static void loadFluids() {
		int nbf = 0;
		
		fluidLiquefiedAsgardite = new Fluid("liquefiedAsgardite").setDensity(3000).setViscosity(6000).setTemperature(2000);
		nbf+=1;
		FluidRegistry.registerFluid(fluidLiquefiedAsgardite);
        if (FluidContainerRegistry.fillFluidContainer(new FluidStack(fluidLiquefiedAsgardite, 1000), new ItemStack(Items.bucket)) == null)
        	FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(fluidLiquefiedAsgardite, 1000), new ItemStack(ModItems.itemFilledBucket, 1, 0), new ItemStack(Items.bucket)));

        blockLiquefiedAsgardite = new BlockLiquefiedAsgardite(fluidLiquefiedAsgardite, Material.water);
    	RegisterHelper.registerBlock(blockLiquefiedAsgardite);
    	
		fluidBlocks = new Block[nbf];
		fluidBlocks[fluidBlocks.length-1] = blockLiquefiedAsgardite;
	}
}
