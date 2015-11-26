package noelflantier.sfartifacts.common.hammerstandrecipe;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;

/*public class CapacityRecipe  extends RecipeOnHammerStand {
	
	public CapacityRecipe(List<ItemStack> recipe){
		super(recipe);
	}

	@Override
	public void end(ItemStack hammer){
		ItemNBTHelper.setInteger(hammer, "AddedCapacityLevel", ItemNBTHelper.getInteger(hammer, "AddedCapacityLevel", 0)+1);
		super.end(hammer);
		//this.item.getEntityItem().stackSize = this.item.getEntityItem().stackSize-quantity;
	}

	public boolean canCraft(List<EntityItem> items, ItemStack stack){
		return super.canCraft(items, stack);
		//return !ItemNBTHelper.getBoolean(stack, "CanBeConfigByHand", false) && item.getEntityItem().getItem()==Item.getItemFromBlock(ModBlocks.blockControlPanel) && item.getEntityItem().stackSize >= quantity;
	}

}*/
