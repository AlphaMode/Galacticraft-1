//package micdoodle8.mods.galacticraft.api.prefab.world.gen;
//
//import com.google.common.collect.Sets;
//import net.minecraft.block.BlockState;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.biome.provider.BiomeProvider;
//import net.minecraft.world.gen.feature.structure.Structure;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.Set;
//
///**
// * Do not include this prefab class in your released mod download.
// * <p/>
// * This chunk manager is used for single-biome dimensions, which is common on basic planets.
// */
//public abstract class BiomeProviderSpace extends BiomeProvider
//{
//    private final List<Biome> biomesToSpawnIn;
//
//    public BiomeProviderSpace()
//    {
//        this.biomesToSpawnIn = new ArrayList<Biome>();
//        this.biomesToSpawnIn.add(this.getBiome());
//    }
//
//    @Override
//    public List<Biome> getBiomesToSpawnIn()
//    {
//        return this.biomesToSpawnIn;
//    }
//
//    @Override
//    public Biome getBiome(BlockPos pos)
//    {
//        return this.getBiome();
//    }
//
//    @Override
//    public Biome getBiome(int x, int y)
//    {
//        return this.getBiome();
//    }
//
//    @Override
//    public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag)
//    {
//        Biome[] abiome = new Biome[width * length];
//
//        for(int i = 0; i < length; ++i) {
//            for(int j = 0; j < width; ++j) {
//                abiome[j + i * width] = getBiome();
//            }
//        }
//
//        return abiome;
//    }
//
//    @Override
//    public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength)
//    {
//        int i = centerX - sideLength >> 2;
//        int j = centerZ - sideLength >> 2;
//        int k = centerX + sideLength >> 2;
//        int l = centerZ + sideLength >> 2;
//        int i1 = k - i + 1;
//        int j1 = l - j + 1;
//        return Sets.newHashSet(this.getBiomeBlock(i, j, i1, j1));
//    }
//
//    @Override
//    public boolean hasStructure(Structure<?> structureIn)
//    {
//        return this.hasStructureCache.computeIfAbsent(structureIn, (structure) -> this.getBiome().hasStructure(structure));
//    }
//
//    @Override
//    public Set<BlockState> getSurfaceBlocks()
//    {
//        if (this.topBlocksCache.isEmpty()) {
//            this.topBlocksCache.add(getBiome().getSurfaceBuilderConfig().getTop());
//        }
//
//        return this.topBlocksCache;
//    }
//
//    //    @Override
////    public Biome getBiome(BlockPos pos, Biome defaultBiome)
////    {
////        return this.getBiome();
////    }
////
////    @Override
////    public float getTemperatureAtHeight(float par1, int par2)
////    {
////        return par1;
////    }
////
////    @Override
////    public Biome[] getBiomesForGeneration(Biome[] par1ArrayOfBiome, int par2, int par3, int par4, int par5)
////    {
////        IntCache.resetIntCache();
////
////        if (par1ArrayOfBiome == null || par1ArrayOfBiome.length < par4 * par5)
////        {
////            par1ArrayOfBiome = new Biome[par4 * par5];
////        }
////
////        for (int var7 = 0; var7 < par4 * par5; ++var7)
////        {
////            par1ArrayOfBiome[var7] = this.getBiome();
////        }
////
////        return par1ArrayOfBiome;
////    }
////
////    @Override
////    public Biome[] getBiomes(Biome[] par1ArrayOfBiome, int par2, int par3, int par4, int par5)
////    {
////        return this.getBiomes(par1ArrayOfBiome, par2, par3, par4, par5, true);
////    }
////
////    @Override
////    public Biome[] getBiomes(Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
////    {
////        IntCache.resetIntCache();
////
////        if (listToReuse == null || listToReuse.length < width * length)
////        {
////            listToReuse = new Biome[width * length];
////        }
////
////        if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0)
////        {
////            final Biome[] var9 = this.biomeCache.getCachedBiomes(x, z);
////            System.arraycopy(var9, 0, listToReuse, 0, width * length);
////            return listToReuse;
////        }
////        else
////        {
////            for (int var8 = 0; var8 < width * length; ++var8)
////            {
////                listToReuse[var8] = this.getBiome();
////            }
////
////            return listToReuse;
////        }
////    }
////
////    @Override
////    public boolean areBiomesViable(int par1, int par2, int par3, List par4List)
////    {
////        return par4List.contains(this.getBiome());
////    }
//
//    @Override
//    public BlockPos findBiomePosition(int par1, int par2, int par3, List par4List, Random par5Random)
//    {
//        final int var6 = par1 - par3 >> 2;
//        final int var7 = par2 - par3 >> 2;
//        final int var8 = par1 + par3 >> 2;
//        final int var10 = var8 - var6 + 1;
//
//        final int var16 = var6 + 0 % var10 << 2;
//        final int var17 = var7 + 0 / var10 << 2;
//
//        return new BlockPos(var16, 0, var17);
//    }
//
//    public abstract Biome getBiome();
//}
