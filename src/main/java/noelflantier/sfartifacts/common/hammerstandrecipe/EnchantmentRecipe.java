package noelflantier.sfartifacts.common.hammerstandrecipe;

import java.util.LinkedHashMap;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.baseclasses.ToolHammerBase;

/*public class EnchantmentRecipe extends RecipeOnHammerStand {
	

	public EnchantmentRecipe(List<ItemStack> recipe){
		super(recipe);
	}
	
	@Override
	public void end(ItemStack hammer) {
		/*NBTTagList list = this.item.getEntityItem().getTagCompound().getTagList("StoredEnchantments", 10);
		hammer.addEnchantment(Enchantment.enchantmentsList[list.getCompoundTagAt(0).getInteger("id")], list.getCompoundTagAt(0).getInteger("lvl"));
		
		if (!hammer.getTagCompound().hasKey("EnchStored", 9))
			hammer.getTagCompound().setTag("EnchStored", new NBTTagList());
		NBTTagList nbttaglist = hammer.getTagCompound().getTagList("EnchStored", 10);
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setShort("id", (short)Enchantment.enchantmentsList[list.getCompoundTagAt(0).getInteger("id")].effectId);
        nbttagcompound.setShort("lvl", (short)((byte)list.getCompoundTagAt(0).getInteger("lvl")));
        nbttagcompound.setBoolean("enable", true);
        nbttaglist.appendTag(nbttagcompound);
		this.item.getEntityItem().stackSize = this.item.getEntityItem().stackSize-quantity;*/

		/*for (ItemStack st : recipe){
			for(EntityItem ei:itemOnSpot){
				if(st.getItem()==ei.getEntityItem().getItem()){
					NBTTagList list = ei.getEntityItem().getTagCompound().getTagList("StoredEnchantments", 10);
					hammer.addEnchantment(Enchantment.enchantmentsList[list.getCompoundTagAt(0).getInteger("id")], list.getCompoundTagAt(0).getInteger("lvl"));
					
					if (!hammer.getTagCompound().hasKey("EnchStored", 9))
						hammer.getTagCompound().setTag("EnchStored", new NBTTagList());
					NBTTagList nbttaglist = hammer.getTagCompound().getTagList("EnchStored", 10);
			        NBTTagCompound nbttagcompound = new NBTTagCompound();
			        nbttagcompound.setShort("id", (short)Enchantment.enchantmentsList[list.getCompoundTagAt(0).getInteger("id")].effectId);
			        nbttagcompound.setShort("lvl", (short)((byte)list.getCompoundTagAt(0).getInteger("lvl")));
			        nbttagcompound.setBoolean("enable", true);
			        nbttaglist.appendTag(nbttagcompound);
					ei.getEntityItem().stackSize-=st.stackSize;
					break;
				}
			}
		}
		this.age = 0;*/
		//super.end(hammer);
	//}

	/*@Override
	public boolean canCraft(List<EntityItem> items, ItemStack stack){
		/*for (ItemStack st : recipe){
			for(EntityItem it:items){
				if(st.getItem()==it.getEntityItem().getItem() && st.stackSize<=it.getEntityItem().stackSize && it.getEntityItem().hasTagCompound()){
					NBTTagList list = it.getEntityItem().getTagCompound().getTagList("StoredEnchantments", 10);
					if (list.tagCount() != 1) continue;
					Enchantment enchant = Enchantment.enchantmentsList[list.getCompoundTagAt(0).getInteger("id")];
					if(!((ToolHammerBase)stack.getItem()).isEnchantValid(enchant))continue;
					NBTTagList tl = ItemNBTHelper.getTagList(stack, "EnchStored", 10);
			        LinkedHashMap linkedhashmap = new LinkedHashMap();
			        if (tl != null){
			    		for (int i = 0; i < tl.tagCount(); ++i)
			    	        {
			    	            short short1 = tl.getCompoundTagAt(i).getShort("id");
			    	            short short2 = tl.getCompoundTagAt(i).getShort("lvl");
			    	            linkedhashmap.put(Integer.valueOf(short1), Integer.valueOf(short2));
			    	        }
			            }
					if(linkedhashmap.containsKey(list.getCompoundTagAt(0).getInteger("id")))continue;
					itemOnSpot.add(it);
					break;
				}
			}
		}
		boolean eq = itemOnSpot.size()==recipe.size();
		if(!eq)
			itemOnSpot.clear();
		return eq;*/
		/*if(item.getEntityItem().stackSize < quantity || item.getEntityItem().getItem()!=Items.enchanted_book  || !item.getEntityItem().hasTagCompound())return false;
		NBTTagList list = item.getEntityItem().getTagCompound().getTagList("StoredEnchantments", 10);
		if (list.tagCount() != 1) return false;
		Enchantment enchant = Enchantment.enchantmentsList[list.getCompoundTagAt(0).getInteger("id")];
		if(!((ToolHammerBase)stack.getItem()).isEnchantValid(enchant))return false;
		
		NBTTagList tl = ItemNBTHelper.getTagList(stack, "EnchStored", 10);
        LinkedHashMap linkedhashmap = new LinkedHashMap();
        if (tl != null){
		for (int i = 0; i < tl.tagCount(); ++i)
	        {
	            short short1 = tl.getCompoundTagAt(i).getShort("id");
	            short short2 = tl.getCompoundTagAt(i).getShort("lvl");
	            linkedhashmap.put(Integer.valueOf(short1), Integer.valueOf(short2));
	        }
        }
		if(linkedhashmap.containsKey(list.getCompoundTagAt(0).getInteger("id")))return false;
		
		return true;*/
	//}
//}
