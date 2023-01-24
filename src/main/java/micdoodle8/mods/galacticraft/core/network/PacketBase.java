package micdoodle8.mods.galacticraft.core.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.world.dimension.DimensionType;

public abstract class PacketBase implements IPacket
{
    private DimensionType dimensionID;

    public PacketBase()
    {
        this.dimensionID = null;
    }

    public PacketBase(DimensionType dimensionID)
    {
        this.dimensionID = dimensionID;
    }

    @Override
    public void encodeInto(PacketBuffer buffer)
    {
        if (dimensionID == null)
        {
            throw new IllegalStateException("Invalid Dimension ID! [GC]");
        }
        buffer.writeResourceLocation(this.dimensionID.getRegistryName());
//        buffer.writeInt(this.dimensionID);
    }

    @Override
    public void decodeInto(PacketBuffer buffer)
    {
        this.dimensionID = DimensionType.byName(buffer.readResourceLocation());
    }

    @Override
    public DimensionType getDimensionID()
    {
        return dimensionID;
    }
}
