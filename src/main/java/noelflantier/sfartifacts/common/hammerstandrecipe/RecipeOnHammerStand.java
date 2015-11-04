package noelflantier.sfartifacts.common.hammerstandrecipe;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public abstract class RecipeOnHammerStand {

	public int age;
	public int maxAge;
	public List<ItemStack> recipe;
	public List<EntityItem> itemOnSpot = new ArrayList<EntityItem>();

	public RecipeOnHammerStand(List<ItemStack> recipe){
		this.age = 0;
		this.maxAge = 10;
		this.recipe = recipe;
	}
	public RecipeOnHammerStand(){
		this.age = 0;
		this.maxAge = 10;
	}
	
	public boolean isDone(){
		return this.age>=this.maxAge;
	}
		
	public boolean itemStillHere(){
		boolean flag = true;
		for (EntityItem item : itemOnSpot){
			if(!item.isEntityAlive())
				flag = false;
		}
		if(!flag)
			itemOnSpot.clear();
		return flag;
	}

	public void end(ItemStack hammer) {		
		for (ItemStack st : recipe){
			for(EntityItem ei:itemOnSpot){
				if(st.getItem()==ei.getEntityItem().getItem() && st.getItemDamage()==ei.getEntityItem().getItemDamage()){
					if(st.stackSize<=ei.getEntityItem().stackSize){
						ei.getEntityItem().stackSize-=st.stackSize;
						break;
					}
				}
			}
		}
		itemOnSpot.clear();
		this.age = 0;
		//this.item.getEntityItem().stackSize = this.item.getEntityItem().stackSize-quantity;
	}
	
	public boolean canCraft(List<EntityItem> items, ItemStack stack){
		if(items.isEmpty())
			return false;
		for (ItemStack st : recipe){
			boolean flag = false;
			for(EntityItem it:items){
				if(st.getItem()==it.getEntityItem().getItem() && st.getItemDamage()==it.getEntityItem().getItemDamage()){
					if(st.stackSize<=it.getEntityItem().stackSize && !flag){
						itemOnSpot.add(it);
						flag = true;
						break;
					}
				}
			}
		}
		boolean eq = itemOnSpot.size()==recipe.size();
		if(!eq)
			itemOnSpot.clear();

		System.out.println(eq+"  "+this.recipe+"   "+this.itemOnSpot);
		return eq;
	}
}
