package noelflantier.sfartifacts.common.blocks.tiles;

import java.util.List;

import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;

public interface ISFAFluid extends IFluidHandler{
	
	List<FluidTank> getFluidTanks();
}
