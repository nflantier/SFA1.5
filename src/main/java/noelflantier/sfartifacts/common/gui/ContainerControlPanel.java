package noelflantier.sfartifacts.common.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import noelflantier.sfartifacts.common.blocks.tiles.TileControlPannel;

public class ContainerControlPanel extends Container {

	private TileControlPannel tile;
	//private int lastEnergy, lastMaxEnergy, lastLightningRodEnergy, passiveEnergy;
	
	public ContainerControlPanel(InventoryPlayer inventory, TileControlPannel tile) {
		this.tile = tile;
	}

	@Override
	public void onContainerClosed(EntityPlayer entity){
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public void addCraftingToCrafters(ICrafting icraft){
		super.addCraftingToCrafters(icraft);
		/*icraft.sendProgressBarUpdate(this, 0, this.tile.storage.getEnergyStored());
		icraft.sendProgressBarUpdate(this, 1, this.tile.storage.getMaxEnergyStored());
		icraft.sendProgressBarUpdate(this, 2, this.tile.lightningRodEnergy);
		icraft.sendProgressBarUpdate(this, 3, this.tile.passiveEnergy);*/
	}

	@Override
	public void detectAndSendChanges(){
		super.detectAndSendChanges();
		/*for(int i = 0 ; i<this.crafters.size();i++){
			ICrafting icraft = (ICrafting)this.crafters.get(i);
			if(this.lastEnergy!=this.tile.storage.getEnergyStored()){
				icraft.sendProgressBarUpdate(this,0, this.tile.storage.getEnergyStored());
			}
			if(this.lastMaxEnergy!=this.tile.storage.getMaxEnergyStored()){
				icraft.sendProgressBarUpdate(this,1, this.tile.storage.getMaxEnergyStored());
			}
			if(this.lastLightningRodEnergy!=this.tile.lightningRodEnergy){
				icraft.sendProgressBarUpdate(this,2, this.tile.lightningRodEnergy);
			}
			if(this.passiveEnergy!=this.tile.passiveEnergy){
				icraft.sendProgressBarUpdate(this,3, this.tile.passiveEnergy);
			}
		}
		this.lastEnergy = this.tile.storage.getEnergyStored();
		this.lastMaxEnergy = this.tile.storage.getMaxEnergyStored();
		this.lastLightningRodEnergy = this.tile.lightningRodEnergy;
		this.passiveEnergy = this.tile.passiveEnergy;*/
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int value){
		/*if(id == 0){
			this.tile.storage.setEnergyStored(value);
		}
		if(id == 1){
			this.tile.storage.setCapacity(value);
		}
		if(id == 2){
			this.tile.lightningRodEnergy = value;
		}
		if(id == 3){
			this.tile.passiveEnergy = value;
		}*/

	}
}
