package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.planets.mars.client.fx.MarsParticles;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.Random;

public abstract class SulphuricAcidFluid extends ForgeFlowingFluid
{
    public SulphuricAcidFluid(Properties builder)
    {
        super(builder);
//        this.setQuantaPerBlock(9);
//        this.setLightLevel(0.1F);
//        this.needsRandomTick = true;
//        this.setUnlocalizedName(assetName);
    }

    @Override
    public boolean canDisplace(IFluidState state, IBlockReader world, BlockPos pos, Fluid fluidIn, Direction direction)
    {
        if (world.getBlockState(pos).getMaterial().isLiquid())
        {
            return false;
        }
        return super.canDisplace(state, world, pos, fluidIn, direction);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(World worldIn, BlockPos pos, IFluidState fluidState, Random rand)
    {
        super.animateTick(worldIn, pos, fluidState, rand);

        if (rand.nextInt(1200) == 0)
        {
            worldIn.playSound(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, rand.nextFloat() * 0.25F + 0.75F, 0.00001F + rand.nextFloat() * 0.5F);
        }
        if (rand.nextInt(10) == 0)
        {
            if (!worldIn.getBlockState(pos.down(2)).getMaterial().blocksMovement())
            {
                worldIn.addParticle(MarsParticles.DRIP, pos.getX() + rand.nextFloat(), pos.getY() - 1.05D, pos.getZ() + rand.nextFloat(), 0, 0, 0);
            }
        }
    }

    public static class Flowing extends SulphuricAcidFluid
    {
        public Flowing(Properties properties)
        {
            super(properties);
            setDefaultState(getStateContainer().getBaseState().with(LEVEL_1_8, 7));
        }

        protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        public int getLevel(IFluidState state) {
            return state.get(LEVEL_1_8);
        }

        public boolean isSource(IFluidState state) {
            return false;
        }
    }

    public static class Source extends SulphuricAcidFluid
    {
        public Source(Properties properties)
        {
            super(properties);
        }

        public int getLevel(IFluidState state) {
            return 8;
        }

        public boolean isSource(IFluidState state) {
            return true;
        }
    }
}
