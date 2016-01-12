package noelflantier.sfartifacts.common.items;

import net.minecraft.item.Item;
import noelflantier.sfartifacts.SFArtifacts;

public class ItemSFA extends Item{
	
	public String name;
	
	public ItemSFA() {
		this.setCreativeTab(SFArtifacts.sfTabs);
	}
	public ItemSFA(String name) {
		this();
		this.name = name;
	}
}
