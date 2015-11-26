package noelflantier.sfartifacts.common.hammerstandrecipe;

import java.util.Hashtable;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;

/*public class LightningRecipe  extends RecipeOnHammerStand {
	
	public LightningRecipe(List<ItemStack> recipe){
		super(recipe);
	}

	@Override
	public void end(ItemStack hammer){
		ItemNBTHelper.setBoolean(hammer, "CanThrowLightning", true);
		super.end(hammer);
	}

	@Override
	public boolean canCraft(List<EntityItem> items, ItemStack stack){
		return super.canCraft(items, stack) && !ItemNBTHelper.getBoolean(stack, "CanThrowLightning", false);
		//return !ItemNBTHelper.getBoolean(stack, "CanThrowLightning", false) && item.getEntityItem().getItem() == ModItems.itemLightningRod && item.getEntityItem().getItemDamage()==3 && item.getEntityItem().stackSize >= quantity;
	}
}*/
