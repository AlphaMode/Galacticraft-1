package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.entity.ICargoEntity;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.entity.IFuelable;
import micdoodle8.mods.galacticraft.api.entity.ILandable;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlockNames;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TileEntityLandingPad extends TileEntityFake implements IMultiBlock, IFuelable, IFuelDock, ICargoEntity
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.landingPadFull)
    public static TileEntityType<TileEntityLandingPad> TYPE;

    public TileEntityLandingPad()
    {
        super(TYPE);
    }

    private IDockable dockedEntity;
    private boolean initialised;

    @Override
    public void tick()
    {
        if (!this.initialised)
        {
            if (!this.world.isRemote)
            {
                this.onCreate(this.world, this.getPos());
            }
            this.initialiseMultiTiles(this.getPos(), this.world);
            this.initialised = true;
        }

        if (!this.world.isRemote)
        {
            final List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.getPos().getX() - 0.5D, this.getPos().getY(), this.getPos().getZ() - 0.5D, this.getPos().getX() + 0.5D, this.getPos().getY() + 1.0D, this.getPos().getZ() + 0.5D));

            boolean docked = false;

            for (final Object o : list)
            {
                if (o instanceof IDockable && ((Entity) o).isAlive())
                {
                    final IDockable fuelable = (IDockable) o;

                    if (!fuelable.inFlight())
                    {
                        docked = true;

                        if (fuelable != this.dockedEntity && fuelable.isDockValid(this))
                        {
                            if (fuelable instanceof ILandable)
                            {
                                ((ILandable) fuelable).landEntity(this.getPos());
                            }
                            else
                            {
                                fuelable.setPad(this);
                            }
                        }

                        break;
                    }
                }
            }

            if (!docked)
            {
                this.dockedEntity = null;
            }
        }
    }

//    @Override
//    public boolean canUpdate()
//    {
//        return true;
//    }

    @Override
    public ActionResultType onActivated(PlayerEntity entityPlayer)
    {
        return ActionResultType.PASS;
    }

    @Override
    public void onCreate(World world, BlockPos placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.markDirty();

        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(placedPosition, positions);
        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions, placedPosition, this.getMultiType());
    }

    @Override
    public BlockMulti.EnumBlockMultiType getMultiType()
    {
        return EnumBlockMultiType.ROCKET_PAD;
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int y = placedPosition.getY();
        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                if (x == 0 && z == 0)
                {
                    continue;
                }
                positions.add(new BlockPos(placedPosition.getX() + x, y, placedPosition.getZ() + z));
            }
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final BlockPos thisBlock = getPos();
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            BlockState stateAt = this.world.getBlockState(pos);

            if (stateAt.getBlock() == GCBlocks.fakeBlock && stateAt.get(BlockMulti.MULTI_TYPE) == EnumBlockMultiType.ROCKET_PAD)
            {
                if (this.world.isRemote && this.world.rand.nextDouble() < 0.1D)
                {
                    Minecraft.getInstance().particles.addBlockDestroyEffects(pos, this.world.getBlockState(pos));
                }
                this.world.destroyBlock(pos, false);
            }
        }
        this.world.destroyBlock(thisBlock, true);

        if (this.dockedEntity != null)
        {
            this.dockedEntity.onPadDestroyed();
            this.dockedEntity = null;
        }

    }

    @Override
    public int addFuel(FluidStack liquid, IFluidHandler.FluidAction action)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.addFuel(liquid, action);
        }

        return 0;
    }

    @Override
    public FluidStack removeFuel(int amount)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.removeFuel(amount);
        }

        return null;
    }

    @Override
    public HashSet<ILandingPadAttachable> getConnectedTiles()
    {
        HashSet<ILandingPadAttachable> connectedTiles = new HashSet<ILandingPadAttachable>();

        for (int x = this.getPos().getX() - 1; x < this.getPos().getX() + 2; x++)
        {
            this.testConnectedTile(x, this.getPos().getZ() - 2, connectedTiles);
            this.testConnectedTile(x, this.getPos().getZ() + 2, connectedTiles);
        }

        for (int z = this.getPos().getZ() - 1; z < this.getPos().getZ() + 2; z++)
        {
            this.testConnectedTile(this.getPos().getX() - 2, z, connectedTiles);
            this.testConnectedTile(this.getPos().getX() + 2, z, connectedTiles);
        }

        return connectedTiles;
    }

    private void testConnectedTile(int x, int z, HashSet<ILandingPadAttachable> connectedTiles)
    {
        BlockPos testPos = new BlockPos(x, this.getPos().getY(), z);
        if (!this.world.isBlockLoaded(testPos))
        {
            return;
        }

        final TileEntity tile = this.world.getTileEntity(testPos);

        if (tile instanceof ILandingPadAttachable && ((ILandingPadAttachable) tile).canAttachToLandingPad(this.world, this.getPos()))
        {
            connectedTiles.add((ILandingPadAttachable) tile);
//            if (GalacticraftCore.isPlanetsLoaded && tile instanceof TileEntityLaunchController)
//            {
//                ((TileEntityLaunchController) tile).setAttachedPad(this);
//            } TODO Planets
        }
    }

    @Override
    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.addCargo(stack, doAdd);
        }

        return EnumCargoLoadingState.NOTARGET;
    }

    @Override
    public RemovalResult removeCargo(boolean doRemove)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.removeCargo(doRemove);
        }

        return new RemovalResult(EnumCargoLoadingState.NOTARGET, ItemStack.EMPTY);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(getPos().getX() - 1, getPos().getY(), getPos().getZ() - 1, getPos().getX() + 2, getPos().getY() + 0.4D, getPos().getZ() + 2);
    }

    @Override
    public boolean isBlockAttachable(IWorldReader world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile instanceof ILandingPadAttachable)
        {
            return ((ILandingPadAttachable) tile).canAttachToLandingPad(world, this.getPos());
        }

        return false;
    }

    @Override
    public IDockable getDockedEntity()
    {
        return this.dockedEntity;
    }

    @Override
    public void dockEntity(IDockable entity)
    {
        this.dockedEntity = entity;
    }
}
