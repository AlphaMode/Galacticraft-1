package micdoodle8.mods.galacticraft.core.network;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCrafting;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketDynamicInventory extends PacketBase
{
    private int type;
    private Object identifier;
    private ItemStack[] stacks;

    public PacketDynamicInventory()
    {
        super();
    }

    public PacketDynamicInventory(Entity entity)
    {
        super(GCCoreUtil.getDimensionType(entity.world));
        assert entity instanceof IInventory : "Entity does not implement " + IInventory.class.getSimpleName();
        this.type = 0;
        this.identifier = new Integer(entity.getEntityId());
        this.stacks = new ItemStack[((IInventory) entity).getSizeInventory()];

        for (int i = 0; i < this.stacks.length; i++)
        {
            this.stacks[i] = ((IInventory) entity).getStackInSlot(i);
        }
    }

    public PacketDynamicInventory(TileEntity tile)
    {
        super(GCCoreUtil.getDimensionType(tile.getWorld()));
        assert tile instanceof IInventory : "Tile does not implement " + IInventory.class.getSimpleName();
        IInventory chest = ((IInventory) tile);
        this.type = 1;
        this.identifier = tile.getPos();
        int size = chest.getSizeInventory();
        if (chest instanceof TileEntityCrafting)
        {
            this.stacks = new ItemStack[size + 1];
            this.stacks[size] = ((TileEntityCrafting) chest).getMemoryHeld();
        }
        else
        {
            this.stacks = new ItemStack[size];
        }

        for (int i = 0; i < size; i++)
        {
            this.stacks[i] = chest.getStackInSlot(i);
        }
    }

    public static void encode(final PacketDynamicInventory message, final PacketBuffer buf)
    {
        message.encodeInto(buf);
    }

    public static PacketDynamicInventory decode(PacketBuffer buf)
    {
        PacketDynamicInventory packet = new PacketDynamicInventory();
        packet.decodeInto(buf);
        return packet;
    }

    public static void handle(final PacketDynamicInventory message, Supplier<NetworkEvent.Context> ctx)
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

    @Override
    public void encodeInto(PacketBuffer buffer)
    {
        super.encodeInto(buffer);
        buffer.writeInt(this.type);

        switch (this.type)
        {
        case 0:
            buffer.writeInt((Integer) this.identifier);
            break;
        case 1:
            BlockPos pos = (BlockPos) this.identifier;
            buffer.writeInt(pos.getX());
            buffer.writeInt(pos.getY());
            buffer.writeInt(pos.getZ());
            break;
        }

        buffer.writeInt(this.stacks.length);

        for (int i = 0; i < this.stacks.length; i++)
        {
            buffer.writeItemStack(this.stacks[i]);
        }
    }

    @Override
    public void decodeInto(PacketBuffer buffer)
    {
        super.decodeInto(buffer);
        this.type = buffer.readInt();

        switch (this.type)
        {
        case 0:
            this.identifier = buffer.readInt();
            break;
        case 1:
            this.identifier = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
            break;
        }

        this.stacks = new ItemStack[buffer.readInt()];

        for (int i = 0; i < this.stacks.length; i++)
        {
            this.stacks[i] = buffer.readItemStack();
        }
    }

    @Override
    public void handleClientSide(PlayerEntity player)
    {
        if (player.world == null)
        {
            return;
        }

        switch (this.type)
        {
        case 0:
            Entity entity = player.world.getEntityByID((Integer) this.identifier);

            if (entity instanceof IInventorySettable)
            {
                this.setInventoryStacks((IInventorySettable) entity);
            }

            break;
        case 1:
            TileEntity tile = player.world.getTileEntity((BlockPos) this.identifier);

            if (tile instanceof TileEntityCrafting)
            {
                ((TileEntityCrafting) tile).setStacksClientSide(this.stacks);
            }
            else if (tile instanceof IInventorySettable)
            {
                this.setInventoryStacks((IInventorySettable) tile);
            }

            break;
        }
    }

    @Override
    public void handleServerSide(PlayerEntity player)
    {
        switch (this.type)
        {
        case 0:
            Entity entity = player.world.getEntityByID((Integer) this.identifier);

            if (entity instanceof IInventorySettable)
            {
                GalacticraftCore.packetPipeline.sendTo(new PacketDynamicInventory(entity), (ServerPlayerEntity) player);
            }

            break;
        case 1:
            TileEntity tile = player.world.getTileEntity((BlockPos) this.identifier);

            if (tile instanceof IInventorySettable)
            {
                GalacticraftCore.packetPipeline.sendTo(new PacketDynamicInventory(tile), (ServerPlayerEntity) player);
            }

            break;
        }
    }

    private void setInventoryStacks(IInventorySettable inv)
    {
        inv.setSizeInventory(this.stacks.length);

        for (int i = 0; i < this.stacks.length; i++)
        {
            inv.setInventorySlotContents(i, this.stacks[i]);
        }
    }
}
