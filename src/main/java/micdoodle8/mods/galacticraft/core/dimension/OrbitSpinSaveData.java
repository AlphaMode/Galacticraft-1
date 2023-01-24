package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

public class OrbitSpinSaveData extends WorldSavedData
{
    public static final String saveDataID = "GCSpinData";
    public CompoundNBT datacompound;
    private CompoundNBT alldata;
    private DimensionType dim = DimensionType.OVERWORLD;

    public OrbitSpinSaveData()
    {
        super(OrbitSpinSaveData.saveDataID);
        this.datacompound = new CompoundNBT();
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        this.alldata = nbt;
        //world.loadData calls this but can't extract from alldata until we know the dimension ID
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        if (this.dim != DimensionType.OVERWORLD)
        {
            nbt.put(this.dim.getRegistryName().toString(), this.datacompound);
        }

        return nbt;
    }

    public static OrbitSpinSaveData initWorldData(ServerWorld world)
    {
        OrbitSpinSaveData worldData = world.getSavedData().getOrCreate(() -> {
            OrbitSpinSaveData data = new OrbitSpinSaveData();
            if (world.getDimension() instanceof DimensionSpaceStation)
            {
                data.dim = GCCoreUtil.getDimensionType(world);
                ((DimensionSpaceStation) world.getDimension()).getSpinManager().writeToNBT(data.datacompound);
            }
            data.markDirty();
            return data;
        }, OrbitSpinSaveData.saveDataID);

        if (world.getDimension() instanceof DimensionSpaceStation)
        {
            worldData.dim = GCCoreUtil.getDimensionType(world);

            worldData.datacompound = null;
            if (worldData.alldata != null)
            {
                worldData.datacompound = worldData.alldata.getCompound(worldData.dim.getRegistryName().toString());
            }
            if (worldData.datacompound == null)
            {
                worldData.datacompound = new CompoundNBT();
            }
        }

        return worldData;
    }
}
