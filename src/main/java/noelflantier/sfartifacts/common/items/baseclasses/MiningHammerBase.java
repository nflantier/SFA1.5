package noelflantier.sfartifacts.common.items.baseclasses;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.common.helpers.HammerHelper;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;

public class MiningHammerBase extends ToolHammerBase{
	
	public int energyMining;
		
	public MiningHammerBase(ToolMaterial material, int energyMining){
		super(material, energyMining);
		this.energyMining = energyMining;
	}
		
	@Override
	public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
		int radius = ItemNBTHelper.getInteger(stack, "Radius", 0);
		int depth = 0;
		boolean mine = HammerHelper.breakOnMinning(stack, x, y, z, radius, depth, player);
		return mine ? true : super.onBlockStartBreak(stack, x, y, z, player);
		}
	
}
