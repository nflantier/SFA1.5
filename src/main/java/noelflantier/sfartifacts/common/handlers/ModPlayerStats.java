package noelflantier.sfartifacts.common.handlers;

import java.lang.ref.WeakReference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ModPlayerStats implements IExtendedEntityProperties{

    public static final String PROP_NAME = "SFARTIFACTS";
    public EntityPlayer player;
    public boolean startManual;
	public boolean isMovingWithHammer = false;
	public int tickMovingWithHammer = 0;
	public int tickToUpdate = 0;
	public boolean justStopMoving = false;
	public boolean justStartMoving = false;
	public int justBlockedAttack = 0;
    
    public ModPlayerStats(){
    }

    public ModPlayerStats(EntityPlayer entityplayer) {
        this.player = entityplayer; 
    }
    
	@Override
	public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound tTag = new NBTTagCompound();
        tTag.setBoolean("startManual", this.startManual);
        tTag.setBoolean("isMovingWithHammer", this.isMovingWithHammer);
        tTag.setInteger("tickMovingWithHammer", this.tickMovingWithHammer);
        tTag.setInteger("tickToUpdate", this.tickToUpdate);
        tTag.setBoolean("justStopMoving", this.justStopMoving);
        tTag.setBoolean("justStartMoving", this.justStartMoving);
        tTag.setInteger("justBlockedAttack", this.justBlockedAttack);
        compound.setTag(PROP_NAME, tTag);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = (NBTTagCompound) compound.getTag(PROP_NAME);
        this.startManual = properties.getBoolean("startManual");
        this.isMovingWithHammer = properties.getBoolean("isMovingWithHammer");
        this.tickMovingWithHammer = properties.getInteger("tickMovingWithHammer");
        this.tickToUpdate = properties.getInteger("tickToUpdate");
        this.justStopMoving = properties.getBoolean("justStopMoving");
        this.justStartMoving = properties.getBoolean("justStartMoving");
        this.justBlockedAttack = properties.getInteger("justBlockedAttack");
	}

	@Override
	public void init(Entity entity, World world) {
	}

    public static final void register (EntityPlayer player){
        player.registerExtendedProperties(ModPlayerStats.PROP_NAME, new ModPlayerStats(player));
    }

    public static final ModPlayerStats get (EntityPlayer player){
        return (ModPlayerStats) player.getExtendedProperties(PROP_NAME);
    }
}
