package noelflantier.sfartifacts.common.hammerstandrecipe;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;

public class MagnetRecipe extends RecipeOnHammerStand {
	
	public MagnetRecipe(List<ItemStack> recipe){
		super(recipe);
	}

	@Override
	public void end(ItemStack hammer){
		ItemNBTHelper.setBoolean(hammer, "CanMagnet", true);
		ItemNBTHelper.setBoolean(hammer, "IsMagnetOn", true);
		super.end(hammer);
		//this.item.getEntityItem().stackSize = this.item.getEntityItem().stackSize-quantity;
	}

	@Override
	public boolean canCraft(List<EntityItem> items, ItemStack stack){
		return super.canCraft(items, stack) && !ItemNBTHelper.getBoolean(stack, "CanMagnet", false);
		//return !ItemNBTHelper.getBoolean(stack, "CanMagnet", false) && item.getEntityItem().getItem()==ModItems.itemEnergyModule && item.getEntityItem().stackSize >= quantity;
	}
	
}
