package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.dimension.DimensionSpaceStation;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityThruster;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class BlockSpinThruster extends BlockAdvanced implements IShiftDescription, ISortable
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty ORIENTATION = BooleanProperty.create("rev");

    protected static final VoxelShape NORTH_AABB = VoxelShapes.create(0.2F, 0.2F, 0.4F, 0.8F, 0.8F, 1.0F);
    protected static final VoxelShape SOUTH_AABB = VoxelShapes.create(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.6F);
    protected static final VoxelShape WEST_AABB = VoxelShapes.create(0.4F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F);
    protected static final VoxelShape EAST_AABB = VoxelShapes.create(0.0F, 0.2F, 0.2F, 0.6F, 0.8F, 0.8F);

    public BlockSpinThruster(Properties builder)
    {
        super(builder);
        this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH).with(ORIENTATION, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        switch (state.get(FACING))
        {
        case EAST:
            return EAST_AABB;
        case WEST:
            return WEST_AABB;
        case SOUTH:
            return SOUTH_AABB;
        default:
        case NORTH:
            return NORTH_AABB;
        }
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos)
    {
        return hasEnoughSolidSide(world, pos.west(), Direction.EAST) || hasEnoughSolidSide(world, pos.east(), Direction.WEST) || hasEnoughSolidSide(world, pos.north(), Direction.SOUTH) || hasEnoughSolidSide(world, pos.south(), Direction.NORTH);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        if (context.getPlacementHorizontalFacing().getAxis().isHorizontal() && this.canBlockStay(context.getWorld(), context.getPos(), context.getPlacementHorizontalFacing()))
        {
            return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
        }
        else
        {
            for (Direction enumfacing : Direction.Plane.HORIZONTAL)
            {
                if (this.canBlockStay(context.getWorld(), context.getPos(), enumfacing))
                {
                    return this.getDefaultState().with(FACING, enumfacing);
                }
            }
            return this.getDefaultState();
        }
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
    {
//        if (this.getMetaFromState(state) == 0)
//        {
//            this.onBlockAdded(worldIn, pos, state);
//        }
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        int metadata = 0;//this.getMetaFromState(state);

        BlockPos baseBlock;
        switch (metadata)
        {
        case 1:
            baseBlock = pos.offset(Direction.WEST);
            break;
        case 2:
            baseBlock = pos.offset(Direction.EAST);
            break;
        case 3:
            baseBlock = pos.offset(Direction.NORTH);
            break;
        case 4:
            baseBlock = pos.offset(Direction.SOUTH);
            break;
        default:
            return;
        }

        if (!worldIn.isRemote)
        {
            if (worldIn.getDimension() instanceof DimensionSpaceStation)
            {
                ((DimensionSpaceStation) worldIn.getDimension()).getSpinManager().refresh(baseBlock, true);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        Direction enumfacing = state.get(FACING);

        if (!this.canBlockStay(worldIn, pos, enumfacing))
        {
//            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.removeBlock(pos, false);
        }
        if (!worldIn.isRemote)
        {
            if (worldIn.getDimension() instanceof DimensionSpaceStation)
            {
                ((DimensionSpaceStation) worldIn.getDimension()).getSpinManager().refresh(pos, true);
            }
        }
    }

    protected boolean canBlockStay(World world, BlockPos pos, Direction facing)
    {
        return hasEnoughSolidSide(world, pos.offset(facing.getOpposite()), facing);
    }

//    @Override
//    public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end)
//    {
//        float var8 = 0.3F;
//
//        EnumFacing facing = worldIn.getBlockState(pos).getValue(BlockMachine.FACING);
//
//        switch (facing)
//        {
//        case NORTH:
//            this.setBlockBounds(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
//            break;
//        case EAST:
//            this.setBlockBounds(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
//            break;
//        case SOUTH:
//            this.setBlockBounds(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
//            break;
//        case WEST:
//            this.setBlockBounds(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
//            break;
//        }
//
//        return super.collisionRayTrace(worldIn, pos, start, end);
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        //TODO this is torch code as a placeholder, still need to adjust positioning and particle type
        //Also make small thrust sounds
        if (worldIn.getDimension() instanceof DimensionSpaceStation)
        {
            if (((DimensionSpaceStation) worldIn.getDimension()).getSpinManager().thrustersFiring || rand.nextInt(80) == 0)
            {
                final int var6 = /*this.getMetaFromState(stateIn) & 7*/0;
                final double var7 = pos.getX() + 0.5F;
                final double var9 = pos.getY() + 0.7F;
                final double var11 = pos.getZ() + 0.5F;
                final double var13 = 0.2199999988079071D;
                final double var15 = 0.27000001072883606D;

                if (var6 == 1)
                {
                    worldIn.addParticle(ParticleTypes.SMOKE, var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
                }
                else if (var6 == 2)
                {
                    worldIn.addParticle(ParticleTypes.SMOKE, var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
                }
                else if (var6 == 3)
                {
                    worldIn.addParticle(ParticleTypes.SMOKE, var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
                }
                else if (var6 == 0)
                {
                    worldIn.addParticle(ParticleTypes.SMOKE, var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public ActionResultType onUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        BlockState state = world.getBlockState(pos);
        boolean orientation = state.get(ORIENTATION);

        Direction currentFacing = state.get(FACING);
//        if (this.canBlockStay(world, pos.offset(currentFacing.getOpposite()), currentFacing))
//        {
            world.setBlockState(pos, state.with(ORIENTATION, !orientation), 2);
//        }
        //TODO  else

        if (world.getDimension() instanceof DimensionSpaceStation && !world.isRemote)
        {
            DimensionSpaceStation worldOrbital = (DimensionSpaceStation) world.getDimension();
            worldOrbital.getSpinManager().refresh(pos, true);
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityThruster();
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (!worldIn.isRemote)
        {
            final int facing = 0;//this.getMetaFromState(state) & 8;
            if (worldIn.getDimension() instanceof DimensionSpaceStation)
            {
                DimensionSpaceStation worldOrbital = (DimensionSpaceStation) worldIn.getDimension();
                worldOrbital.getSpinManager().removeThruster(pos, facing == 0);
                worldOrbital.getSpinManager().updateSpinSpeed();
            }
        }
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

//    @Override
    public BlockState getStateFromMeta(int meta)
    {
        Direction enumfacing = Direction.byHorizontalIndex(meta % 4);
        return this.getDefaultState().with(FACING, enumfacing).with(ORIENTATION, meta >= 8);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, ORIENTATION);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }
}
