package noelflantier.sfartifacts.common.items.baseclasses;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;

import com.google.common.collect.Sets;

public class ToolHammerBase extends RFHammerBase{

	private Set blockOverrides;
	protected float efficiencyOnProperMaterial = 4.0F;
    private float damageVsEntity;
    protected Item.ToolMaterial toolMaterial;
    private static final Set SHOVEL_OVERRIDES = Sets.newHashSet(Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel, Blocks.snow_layer, Blocks.snow, Blocks.clay, Blocks.farmland, Blocks.soul_sand, Blocks.mycelium);
	private static final Set PICKAXE_OVERRIDES = Sets.newHashSet(Blocks.cobblestone, Blocks.double_stone_slab, Blocks.stone_slab, Blocks.stone, Blocks.sandstone, Blocks.mossy_cobblestone, Blocks.iron_ore, Blocks.iron_block, Blocks.coal_ore, Blocks.gold_block, Blocks.gold_ore, Blocks.diamond_ore, Blocks.diamond_block, Blocks.ice, Blocks.netherrack, Blocks.lapis_ore, Blocks.lapis_block, Blocks.redstone_ore, Blocks.lit_redstone_ore, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.activator_rail, Material.iron, Material.anvil, Material.rock, Material.glass, Material.ice, Material.packedIce);
	private static final Set AXE_OVERRIDES = Sets.newHashSet(Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Material.wood, Material.leaves, Material.coral, Material.cactus, Material.plants, Material.vine);
	public int energyMining;
	
	protected ToolHammerBase(Item.ToolMaterial material, int energyMining)
	{
		this.blockOverrides = new HashSet();
		this.energyMining = energyMining;
		this.toolMaterial = material;
		this.setMaxDamage(material.getMaxUses());
		this.efficiencyOnProperMaterial = material.getEfficiencyOnProperMaterial();
		this.damageVsEntity =  material.getDamageVsEntity();
	}
	
	public boolean isEnchantValid(Enchantment enchant){
		return false;
	}
	
	@Override
	public void setHarvestLevel(String toolClass, int level) {
		if (toolClass.equals("pickaxe")) blockOverrides.addAll(PICKAXE_OVERRIDES);
		if (toolClass.equals("shovel")) blockOverrides.addAll(SHOVEL_OVERRIDES);
		if (toolClass.equals("axe")) blockOverrides.addAll(AXE_OVERRIDES);
		super.setHarvestLevel(toolClass, level);
	}
    
	/**
	 * Get strength vs block
	 */
	@Override
	public float func_150893_a(ItemStack stack, Block block)
	{
		return blockOverrides.contains(block) || blockOverrides.contains(block.getMaterial()) ? efficiencyOnProperMaterial : 1.0F;
	}	
	
	/**
	 * Can Harvest Block
	 */
	@Override
	public boolean func_150897_b(Block block)
	{
		if (getToolClasses(null).contains("pickaxe")) return true;
		return blockOverrides.contains(block);
	}
	
	@Override
	public float getDigSpeed(ItemStack stack, Block block, int meta)
	{
		float speed;
		if (ForgeHooks.isToolEffective(stack, block, meta))
		{
			speed = efficiencyOnProperMaterial;
		}
		else
		{
			speed = super.getDigSpeed(stack, block, meta);
		}
		
		if (getEnergyStored(stack) >= this.energyMining && !ItemNBTHelper.getBoolean(stack, "IsThrown", false))
		{
			float f = 1;
			if (speed > 50f) f *= f;
			return f * speed;
		}
		else
		{
			return 0.5f;
		}
	}
	
	public float getDamageVsEntity(){
		return this.damageVsEntity;
	}
}
