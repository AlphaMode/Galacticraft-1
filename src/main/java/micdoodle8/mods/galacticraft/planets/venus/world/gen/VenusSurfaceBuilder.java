package micdoodle8.mods.galacticraft.planets.venus.world.gen;

import com.mojang.datafixers.Dynamic;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockDungeonBrick;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockVenusRock;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Random;
import java.util.function.Function;

public class VenusSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {
    public VenusSurfaceBuilder(Function<Dynamic<?>, SurfaceBuilderConfig> deserializer) {
        super(deserializer);
    }

    @Override
    public void buildSurface(Random rand, IChunk chunkIn, Biome biomeIn, int chunkX, int chunkZ, int startHeight, double noiseVal, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config) {
        BlockState topBlock = config.getTop();
        BlockState fillerBlock = config.getUnder();
        BlockState underFluidBlock = config.getUnderWaterMaterial();
        BlockState stoneBlock = VenusBlocks.rockHard.getDefaultState();
        int j = -1;
        int k = (int)(noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int x = chunkX & 15;
        int z = chunkZ & 15;

        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int y = startHeight; y >= 0; --y)
        {
           pos.setPos(x, y, z);
            BlockState iblockstate2 = chunkIn.getBlockState(pos);

            if (iblockstate2.isAir(chunkIn.getWorldForge(), pos))
            {
                j = -1;
            }
            else if (iblockstate2.getBlock() instanceof BlockVenusRock || iblockstate2.getBlock() instanceof BlockVenusRock || iblockstate2.getBlock() instanceof BlockDungeonBrick)
            {
                if (j == -1)
                {
                    if (k <= 0)
                    {
                        topBlock = Blocks.AIR.getDefaultState();
                        fillerBlock = stoneBlock;
                    }
                    else if (y >= seaLevel - 4 && y <= seaLevel + 1)
                    {
                        topBlock = config.getTop();
                        fillerBlock = config.getUnder();
                    }

                    j = k;

                    if (y >= seaLevel - 1)
                    {
                        chunkIn.setBlockState(pos, topBlock, false);
                    }
                    else if (y < seaLevel - 7 - k)
                    {
                        topBlock = Blocks.AIR.getDefaultState();
                        fillerBlock = stoneBlock;
                        if (!underFluidBlock.isAir(chunkIn.getWorldForge(), pos))
                            chunkIn.setBlockState(pos, underFluidBlock, false);
                    }
                    else
                    {
                        chunkIn.setBlockState(pos, fillerBlock, false);
                    }
                }
                else if (j > 0)
                {
                    --j;
                    chunkIn.setBlockState(pos, fillerBlock, false);
                }
            }
        }
    }
}
