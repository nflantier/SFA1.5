package noelflantier.sfartifacts.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.util.IIcon;

public class ItemHammerStand extends ItemMultiTexture{

	protected IIcon[] metaIcons;
	public static String[] typeHammerStand = new String[] {"normal", "broken"};
	String name;
	
	public ItemHammerStand(Block b) {
		super(b,b, typeHammerStand);
		this.name = "Hammer Stand";
	}
	
	@Override
    public int getMetadata(int meta){
		return meta<<2;
    }
}
