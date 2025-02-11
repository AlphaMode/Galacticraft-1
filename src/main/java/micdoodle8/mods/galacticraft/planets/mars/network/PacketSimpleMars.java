package micdoodle8.mods.galacticraft.planets.mars.network;

import micdoodle8.mods.galacticraft.core.event.EventWakePlayer;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.network.PacketBase;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityCryogenicChamber;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class PacketSimpleMars extends PacketBase
{
    public enum EnumSimplePacketMars
    {
        // SERVER
        S_UPDATE_SLIMELING_DATA(LogicalSide.SERVER, Integer.class, Integer.class, String.class),
        S_WAKE_PLAYER(LogicalSide.SERVER),
        S_UPDATE_ADVANCED_GUI(LogicalSide.SERVER, Integer.class, BlockPos.class, Integer.class),
        S_UPDATE_CARGO_ROCKET_STATUS(LogicalSide.SERVER, Integer.class, Integer.class),
        S_SWITCH_LAUNCH_CONTROLLER_GUI(LogicalSide.SERVER, BlockPos.class, Integer.class),
        // CLIENT
//        C_OPEN_CUSTOM_GUI(LogicalSide.CLIENT, Integer.class, Integer.class, Integer.class),
//        C_OPEN_CUSTOM_GUI_TILE(LogicalSide.CLIENT, Integer.class, Integer.class, BlockPos.class),
        C_BEGIN_CRYOGENIC_SLEEP(LogicalSide.CLIENT, BlockPos.class);

        private final LogicalSide targetSide;
        private final Class<?>[] decodeAs;

        EnumSimplePacketMars(LogicalSide targetSide, Class<?>... decodeAs)
        {
            this.targetSide = targetSide;
            this.decodeAs = decodeAs;
        }

        public LogicalSide getTargetSide()
        {
            return this.targetSide;
        }

        public Class<?>[] getDecodeClasses()
        {
            return this.decodeAs;
        }
    }

    private EnumSimplePacketMars type;
    private List<Object> data;

    public PacketSimpleMars()
    {
        super();
    }

    public PacketSimpleMars(EnumSimplePacketMars packetType, DimensionType dimID, Object[] data)
    {
        this(packetType, dimID, Arrays.asList(data));
    }

    public PacketSimpleMars(EnumSimplePacketMars packetType, DimensionType dimID, List<Object> data)
    {
        super(dimID);

        if (packetType.getDecodeClasses().length != data.size())
        {
            GCLog.info("Mars Simple Packet found data length different than packet type: " + packetType.name());
        }

        this.type = packetType;
        this.data = data;
    }

    public static void encode(final PacketSimpleMars message, final PacketBuffer buf)
    {
        buf.writeInt(message.type.ordinal());
        buf.writeResourceLocation(message.getDimensionID().getRegistryName());

        try
        {
            NetworkUtil.encodeData(buf, message.data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static PacketSimpleMars decode(PacketBuffer buf)
    {
        PacketSimpleMars.EnumSimplePacketMars type = PacketSimpleMars.EnumSimplePacketMars.values()[buf.readInt()];
        DimensionType dim = DimensionType.byName(buf.readResourceLocation());
        ArrayList<Object> data = null;

        try
        {
            if (type.getDecodeClasses().length > 0)
            {
                data = NetworkUtil.decodeData(type.getDecodeClasses(), buf);
            }
            if (buf.readableBytes() > 0 && buf.writerIndex() < 0xfff00)
            {
                GCLog.severe("Galacticraft packet length problem for packet type " + type.toString());
            }
        }
        catch (Exception e)
        {
            System.err.println("[Galacticraft] Error handling simple packet type: " + type.toString() + " " + buf.toString());
            e.printStackTrace();
            throw e;
        }
        return new PacketSimpleMars(type, dim, data);
    }

    public static void handle(final PacketSimpleMars message, Supplier<NetworkEvent.Context> ctx)
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
        buffer.writeInt(this.type.ordinal());

        try
        {
            NetworkUtil.encodeData(buffer, this.data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void decodeInto(PacketBuffer buffer)
    {
        super.decodeInto(buffer);
        this.type = EnumSimplePacketMars.values()[buffer.readInt()];

        if (this.type.getDecodeClasses().length > 0)
        {
            this.data = NetworkUtil.decodeData(this.type.getDecodeClasses(), buffer);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClientSide(PlayerEntity player)
    {
        ClientPlayerEntity playerBaseClient = null;

        if (player instanceof ClientPlayerEntity)
        {
            playerBaseClient = (ClientPlayerEntity) player;
        }

        switch (this.type)
        {
//        case C_OPEN_CUSTOM_GUI:
//            int entityID = 0;
//            Entity entity = null;
//
//            switch ((Integer) this.data.get(1))
//            {
//            case 0:
//                entityID = (Integer) this.data.get(2);
//                entity = player.world.getEntityByID(entityID);
//
//                if (entity instanceof EntitySlimeling)
//                {
//                    Minecraft.getInstance().displayGuiScreen(new GuiSlimelingInventory(player, (EntitySlimeling) entity));
//                }
//
//                player.openContainer.windowId = (Integer) this.data.get(0);
//                break;
//            case 1:
//                entityID = (Integer) this.data.get(2);
//                entity = player.world.getEntityByID(entityID);
//
//                if (entity != null && entity instanceof EntityCargoRocket)
//                {
//                    Minecraft.getInstance().displayGuiScreen(new GuiCargoRocket(player.inventory, (EntityCargoRocket) entity));
//                }
//
//                player.openContainer.windowId = (Integer) this.data.get(0);
//                break;
//            }
//
//            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_CONTAINER_SLOT_REFRESH, GCCoreUtil.getDimensionID(player.world), new Object[] { player.openContainer.windowId }));
//            break;
//        case C_OPEN_CUSTOM_GUI_TILE:
//            BlockPos pos;
//            TileEntity tile;
//
//            switch ((Integer) this.data.get(1))
//            {
//            case 0:
//                pos = (BlockPos) this.data.get(2);
//                tile = player.world.getTileEntity(pos);
//
//                if (tile != null && tile instanceof TileEntityLaunchController)
//                {
//                    Minecraft.getInstance().displayGuiScreen(new GuiLaunchControllerAdvanced(player.inventory, (TileEntityLaunchController) tile));
//                }
//
//                player.openContainer.windowId = (Integer) this.data.get(0);
//                break;
//            }
//            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_CONTAINER_SLOT_REFRESH, GCCoreUtil.getDimensionID(player.world), new Object[] { player.openContainer.windowId }));
//            break;
        case C_BEGIN_CRYOGENIC_SLEEP:
            BlockPos pos = (BlockPos) this.data.get(0);
            TileEntity tile = player.world.getTileEntity(pos);

            if (tile instanceof TileEntityCryogenicChamber)
            {
                ((TileEntityCryogenicChamber) tile).sleepInBedAt(player, pos.getX(), pos.getY(), pos.getZ());
            }
        default:
            break;
        }
    }

    @Override
    public void handleServerSide(PlayerEntity player)
    {
        ServerPlayerEntity playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

        switch (this.type)
        {
        case S_UPDATE_SLIMELING_DATA:
            Entity entity = player.world.getEntityByID((Integer) this.data.get(0));

            if (entity instanceof EntitySlimeling)
            {
                EntitySlimeling slimeling = (EntitySlimeling) entity;

                int subType = (Integer) this.data.get(1);

                switch (subType)
                {
                case 0:
                    if (player == slimeling.getOwner() && !slimeling.world.isRemote)
                    {
                        slimeling.setSittingAI(!slimeling.isSitting());
                        slimeling.setJumping(false);
                        slimeling.getNavigator().clearPath();
                        slimeling.setAttackTarget(null);
                    }
                    break;
                case 1:
                    if (player == slimeling.getOwner() && !slimeling.world.isRemote)
                    {
                        slimeling.slimelingName = (String) this.data.get(2);
                        slimeling.setSlimelingName(slimeling.slimelingName);
                    }
                    break;
                case 2:
                    if (player == slimeling.getOwner() && !slimeling.world.isRemote)
                    {
                        slimeling.age += 5000;
                    }
                    break;
                case 3:
                    if (!slimeling.isInLove() && player == slimeling.getOwner() && !slimeling.world.isRemote)
                    {
                        slimeling.setInLove(playerBase);
                    }
                    break;
                case 4:
                    if (player == slimeling.getOwner() && !slimeling.world.isRemote)
                    {
                        slimeling.attackDamage = Math.min(slimeling.attackDamage + 0.1F, 1.0F);
                    }
                    break;
                case 5:
                    if (player == slimeling.getOwner() && !slimeling.world.isRemote)
                    {
                        slimeling.setHealth(slimeling.getHealth() + 5.0F);
                    }
                    break;
                case 6:
                    if (player == slimeling.getOwner() && !slimeling.world.isRemote)
                    {
//                        MarsUtil.openSlimelingInventory(playerBase, slimeling);  TODO guis
                    }
                    break;
                }
            }
            break;
        case S_WAKE_PLAYER:
            BlockPos c = playerBase.getBedLocation(playerBase.dimension);

            if (c != null)
            {
                EventWakePlayer event = new EventWakePlayer(playerBase, c, true, true, true);
                MinecraftForge.EVENT_BUS.post(event);
                playerBase.stopSleepInBed(true, true);
            }
            break;
        case S_UPDATE_ADVANCED_GUI:
            TileEntity tile = player.world.getTileEntity((BlockPos) this.data.get(1));

            switch ((Integer) this.data.get(0))
            {
            case 0:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.setFrequency((Integer) this.data.get(2));
                }
                break;
            case 1:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.setLaunchDropdownSelection((Integer) this.data.get(2));
                }
                break;
            case 2:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.setDestinationFrequency((Integer) this.data.get(2));
                }
                break;
            case 3:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.launchPadRemovalDisabled = (Integer) this.data.get(2) == 1;
                }
                break;
            case 4:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.setLaunchSchedulingEnabled((Integer) this.data.get(2) == 1);
                }
                break;
            case 5:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.requiresClientUpdate = true;
                }
                break;
            default:
                break;
            }
            break;
        case S_UPDATE_CARGO_ROCKET_STATUS:
            Entity entity2 = player.world.getEntityByID((Integer) this.data.get(0));

            if (entity2 instanceof EntityCargoRocket)
            {
                EntityCargoRocket rocket = (EntityCargoRocket) entity2;

                int subType = (Integer) this.data.get(1);

                switch (subType)
                {
                default:
                    rocket.statusValid = rocket.checkLaunchValidity();
                    break;
                }
            }
            break;
        case S_SWITCH_LAUNCH_CONTROLLER_GUI:
            BlockPos pos = (BlockPos) this.data.get(0);
            TileEntity tile1 = player.world.getTileEntity(pos);
            if (tile1 instanceof TileEntityLaunchController)
            {
                TileEntityLaunchController launchController = (TileEntityLaunchController) tile1;
                switch ((Integer) this.data.get(1))
                {
                case 0:
//                    MarsUtil.openAdvancedLaunchController(playerBase, launchController);  TODO guis
                    break;
                case 1:
//                    player.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_MARS, player.world, pos.getX(), pos.getY(), pos.getZ()); TODO guis
                    break;
                }
            }
            break;
        default:
            break;
        }
    }
}
