package micdoodle8.mods.galacticraft.planets.venus.world.gen;

import com.mojang.datafixers.Dynamic;
import micdoodle8.mods.galacticraft.planets.PlanetFluids;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Function;

public class WorldGenVaporPool extends Feature<NoFeatureConfig>
{
    // TODO support LiquidConfig?
    public WorldGenVaporPool(Function<Dynamic<?>, NoFeatureConfig> configFactoryIn)
    {
        super(configFactoryIn);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos position, NoFeatureConfig config)
    {
        if (position.getY() <= 4)
        {
            return false;
        }

        BlockState venusSoft = VenusBlocks.rockSoft.getDefaultState();

        int radius = 5 + rand.nextInt(4);
        int radiusSq = radius * radius;
        for (int poolX = -radius; poolX <= radius; poolX++)
        {
            for (int poolY = -radius; poolY <= radius; poolY++)
            {
                for (int poolZ = -radius; poolZ <= radius; poolZ++)
                {
                    int distance = poolX * poolX + poolY * poolY + poolZ * poolZ;

                    if (distance <= radiusSq)
                    {
                        BlockPos pos = new BlockPos(poolX + position.getX(), poolY + position.getY(), poolZ + position.getZ());
                        worldIn.setBlockState(pos, distance >= radiusSq - 16 ? venusSoft : (poolY <= 0 ? PlanetFluids.LIQUID_SULPHURIC_ACID.getBlock().getDefaultState() : Blocks.AIR.getDefaultState()), distance == radiusSq ? 3 : 2);
                    }
                }
            }
        }

        boolean firstSet = false;
        for (int i = 255; i >= position.getY() + 1; --i)
        {
            BlockPos pos = new BlockPos(position.getX(), i, position.getZ());
            if (worldIn.getBlockState(pos).getBlock() != Blocks.AIR)
            {
                if (!firstSet)
                {
                    worldIn.setBlockState(pos, VenusBlocks.spout.getDefaultState(), 3);
                    firstSet = true;
                }
                else
                {
                    worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                }
            }
        }

        return true;
    }
}