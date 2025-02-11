package micdoodle8.mods.galacticraft.core.network;

import com.google.common.collect.Sets;
import micdoodle8.mods.galacticraft.api.transmission.tile.IBufferTransmitter;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.function.Supplier;

public class PacketFluidNetworkUpdate extends PacketBase
{
    private PacketType type;
    private BlockPos pos;

    private FluidStack stack;
    private Fluid fluidType;
    private boolean didTransfer;

    private boolean newNetwork;
    private Collection<IBufferTransmitter<FluidStack>> transmittersAdded;
    private Collection<BlockPos> transmittersCoords;

    public PacketFluidNetworkUpdate()
    {
    }

    public static PacketFluidNetworkUpdate getFluidUpdate(DimensionType dimensionID, BlockPos pos, FluidStack stack, boolean didTransfer)
    {
        return new PacketFluidNetworkUpdate(PacketType.FLUID, dimensionID, pos, stack, didTransfer);
    }

    public static PacketFluidNetworkUpdate getAddTransmitterUpdate(DimensionType dimensionID, BlockPos pos, boolean newNetwork, Collection<IBufferTransmitter<FluidStack>> transmittersAdded)
    {
        return new PacketFluidNetworkUpdate(PacketType.ADD_TRANSMITTER, dimensionID, pos, newNetwork, transmittersAdded);
    }

    public static void encode(final PacketFluidNetworkUpdate message, final PacketBuffer buf)
    {
        message.encodeInto(buf);
    }

    public static PacketFluidNetworkUpdate decode(PacketBuffer buf)
    {
        PacketFluidNetworkUpdate packet = new PacketFluidNetworkUpdate();
        packet.decodeInto(buf);
        return packet;
    }

    public static void handle(final PacketFluidNetworkUpdate message, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            if (GCCoreUtil.getEffectiveSide() == LogicalSide.CLIENT)
            {
                message.handleClientSide(Minecraft.getInstance().player);
            }
            else
            {
                message.handleServerSide(ctx.get().getSender());
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private PacketFluidNetworkUpdate(PacketType type, DimensionType dimensionID, BlockPos pos, FluidStack stack, boolean didTransfer)
    {
        super(dimensionID);
        this.type = type;
        this.pos = pos;
        this.stack = stack;
        this.didTransfer = didTransfer;
    }

    private PacketFluidNetworkUpdate(PacketType type, DimensionType dimensionID, BlockPos pos, boolean newNetwork, Collection<IBufferTransmitter<FluidStack>> transmittersAdded)
    {
        super(dimensionID);
        this.type = type;
        this.pos = pos;
        this.newNetwork = newNetwork;
        this.transmittersAdded = transmittersAdded;
    }

    @Override
    public void encodeInto(PacketBuffer buffer)
    {
        super.encodeInto(buffer);
        buffer.writeInt(this.pos.getX());
        buffer.writeInt(this.pos.getY());
        buffer.writeInt(this.pos.getZ());
        buffer.writeInt(this.type.ordinal());

        switch (this.type)
        {
        case ADD_TRANSMITTER:
            buffer.writeBoolean(this.newNetwork);
            buffer.writeInt(this.transmittersAdded.size());

            for (IBufferTransmitter transmitter : this.transmittersAdded)
            {
                TileEntity tile = (TileEntity) transmitter;
                buffer.writeInt(tile.getPos().getX());
                buffer.writeInt(tile.getPos().getY());
                buffer.writeInt(tile.getPos().getZ());
            }
            break;
        case FLUID:
            if (this.stack != null)
            {
                buffer.writeBoolean(true);
                buffer.writeResourceLocation(this.stack.getFluid().getRegistryName());
                buffer.writeInt(this.stack.getAmount());
            }
            else
            {
                buffer.writeBoolean(false);
            }

            buffer.writeBoolean(this.didTransfer);
            break;
        }
    }

    @Override
    public void decodeInto(PacketBuffer buffer)
    {
        super.decodeInto(buffer);
        this.pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
        this.type = PacketType.values()[buffer.readInt()];

        switch (this.type)
        {
        case ADD_TRANSMITTER:
            this.newNetwork = buffer.readBoolean();
            this.transmittersCoords = Sets.newHashSet();
            int transmitterCount = buffer.readInt();

            for (int i = 0; i < transmitterCount; ++i)
            {
                this.transmittersCoords.add(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()));
            }
            break;
        case FLUID:
            if (buffer.readBoolean())
            {
                this.fluidType = ForgeRegistries.FLUIDS.getValue(buffer.readResourceLocation());
                if (this.fluidType != null)
                {
                    this.stack = new FluidStack(this.fluidType, buffer.readInt());
                }
            }
            else
            {
                this.fluidType = null;
                this.stack = null;
            }

            this.didTransfer = buffer.readBoolean();
            break;
        }
    }

    @Override
    public void handleClientSide(PlayerEntity player)
    {
        TileEntity tile = player.world.getTileEntity(this.pos);

        if (tile instanceof IBufferTransmitter)
        {
            IBufferTransmitter<FluidStack> transmitter = (IBufferTransmitter<FluidStack>) tile;

            switch (this.type)
            {
            case ADD_TRANSMITTER:
            {
                FluidNetwork network = transmitter.hasNetwork() && !this.newNetwork ? (FluidNetwork) transmitter.getNetwork() : new FluidNetwork();
                network.register();
                transmitter.setNetwork(network);

                for (BlockPos pos : this.transmittersCoords)
                {
                    TileEntity transmitterTile = player.world.getTileEntity(pos);

                    if (transmitterTile instanceof IBufferTransmitter)
                    {
                        ((IBufferTransmitter) transmitterTile).setNetwork(network);
                    }
                }

                network.updateCapacity();
            }
            break;
            case FLUID:
                if (transmitter.getNetwork() != null)
                {
                    FluidNetwork network = (FluidNetwork) transmitter.getNetwork();

                    if (this.fluidType != null)
                    {
                        network.refFluid = this.fluidType;
                    }

                    network.buffer = this.stack;
                    network.didTransfer = this.didTransfer;
                }
                break;
            }
        }
    }

    @Override
    public void handleServerSide(PlayerEntity player)
    {

    }

    public enum PacketType
    {
        ADD_TRANSMITTER,
        FLUID,
    }
}
