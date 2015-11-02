package noelflantier.sfartifacts.common.helpers;

import baubles.api.IBauble;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;

public class BaublesHelper {

	public static boolean hasItemClass(Class<? extends IBauble> cla, IInventory bi){
		int size = bi.getSizeInventory();
		for(int i = 0 ; i < size ; i++){
			if(bi.getStackInSlot(i)!=null && bi.getStackInSlot(i).getItem()!=null && bi.getStackInSlot(i).getItem().getClass()==cla)
				return true;
		}
		return false;
	}
	
}
