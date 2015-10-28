package noelflantier.sfartifacts.common.hammerstandrecipe;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;

public class ThrowToMoveRecipe  extends RecipeOnHammerStand {
	
	public ThrowToMoveRecipe(List<ItemStack> recipe){
		super(recipe);
	}

	@Override
	public void end(ItemStack hammer){
		ItemNBTHelper.setBoolean(hammer, "CanThrowToMove",true);
		super.end(hammer);
		//this.item.getEntityItem().stackSize = this.item.getEntityItem().stackSize-quantity;
	}

	@Override
	public boolean canCraft(List<EntityItem> items, ItemStack stack){
		return super.canCraft(items, stack) && !ItemNBTHelper.getBoolean(stack, "CanThrowToMove",false);
		//return !ItemNBTHelper.getBoolean(stack, "CanMagnet", false) && item.getEntityItem().getItem()==ModItems.itemEnergyModule && item.getEntityItem().stackSize >= quantity;
	}
	
}