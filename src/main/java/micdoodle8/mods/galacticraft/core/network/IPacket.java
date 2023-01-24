package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.dimension.DimensionType;

public interface IPacket
{
    void encodeInto(PacketBuffer buffer);

    void decodeInto(PacketBuffer buffer);

    void handleClientSide(PlayerEntity player);

    void handleServerSide(PlayerEntity player);

    DimensionType getDimensionID();
}
