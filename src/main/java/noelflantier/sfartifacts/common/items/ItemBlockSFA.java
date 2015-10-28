package noelflantier.sfartifacts.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockSFA extends ItemBlock{

	public String name;
	public ItemBlockSFA(Block block) {
		super(block);
	}
	public ItemBlockSFA(Block block, String name) {
		this(block);
		this.name = name;
	}

}
