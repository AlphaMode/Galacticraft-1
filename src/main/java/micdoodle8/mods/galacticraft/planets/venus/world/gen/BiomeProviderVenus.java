//package micdoodle8.mods.galacticraft.planets.venus.world.gen;
//
//import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
//import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeAdaptive;
//import micdoodle8.mods.galacticraft.planets.venus.VenusModule;
//import micdoodle8.mods.galacticraft.planets.venus.world.gen.layer.GenLayerVenus;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraft.world.WorldType;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.biome.BiomeCache;
//import net.minecraft.world.biome.provider.BiomeProvider;
//import net.minecraft.world.gen.layer.Layer;
//import net.minecraft.world.gen.layer.IntCache;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import javax.annotation.Nullable;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class BiomeProviderVenus extends BiomeProvider
//{
//    private Layer unzoomedBiomes;
//    private Layer zoomedBiomes;
//    private BiomeCache biomeCache;
//    private List<Biome> biomesToSpawnIn;
//    private CelestialBody body;
//
//    protected BiomeProviderVenus()
//    {
//        body = VenusModule.planetVenus;
//        biomeCache = new BiomeCache(this);
//        biomesToSpawnIn = new ArrayList<>();
////        biomesToSpawnIn.add()
//    }
//
//    public BiomeProviderVenus(long seed, WorldType type)
//    {
//        this();
//        Layer[] genLayers = GenLayerVenus.createWorld(seed);
//        this.unzoomedBiomes = genLayers[0];
//        this.zoomedBiomes = genLayers[1];
//    }
//
//    public BiomeProviderVenus(World world)
//    {
//        this(world.getSeed(), world.getWorldInfo().getGenerator());
//    }
//
//    @Override
//    public List<Biome> getBiomesToSpawnIn()
//    {
//        return this.biomesToSpawnIn;
//    }
//
//    @Override
//    public Biome getBiome(BlockPos pos, Biome defaultBiome)
//    {
//        BiomeAdaptive.setBodyMultiBiome(body);
//        return this.biomeCache.getBiome(pos.getX(), pos.getZ(), BiomeAdaptive.biomeDefault);
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public float getTemperatureAtHeight(float par1, int par2)
//    {
//        return par1;
//    }
//
//    @Override
//    public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int length, int width)
//    {
//        IntCache.resetIntCache();
//        BiomeAdaptive.setBodyMultiBiome(body);
//
//        if (biomes == null || biomes.length < length * width)
//        {
//            biomes = new Biome[length * width];
//        }
//
//        int[] intArray = unzoomedBiomes.getInts(x, z, length, width);
//
//        for (int i = 0; i < length * width; ++i)
//        {
//            if (intArray[i] >= 0)
//            {
//                biomes[i] = Biome.getBiome(intArray[i]);
//            }
//            else
//            {
//                biomes[i] = BiomeAdaptive.biomeDefault;
//            }
//        }
//
//        return biomes;
//    }
//
//    @Override
//    public Biome[] getBiomes(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth)
//    {
//        return getBiomes(oldBiomeList, x, z, width, depth, true);
//    }
//
//    @Override
//    public Biome[] getBiomes(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
//    {
//        IntCache.resetIntCache();
//        BiomeAdaptive.setBodyMultiBiome(body);
//
//        if (listToReuse == null || listToReuse.length < length * width)
//        {
//            listToReuse = new Biome[width * length];
//        }
//
//        if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0)
//        {
//            Biome[] cached = this.biomeCache.getCachedBiomes(x, z);
//            System.arraycopy(cached, 0, listToReuse, 0, width * length);
//            return listToReuse;
//        }
//
//        int[] zoomed = zoomedBiomes.getInts(x, z, width, length);
//
//        for (int i = 0; i < width * length; ++i)
//        {
//            if (zoomed[i] >= 0)
//            {
//                listToReuse[i] = Biome.getBiome(zoomed[i]);
//            }
//            else
//            {
//                listToReuse[i] = BiomeAdaptive.biomeDefault;
//            }
//        }
//
//        return listToReuse;
//    }
//
//    @Override
//    public boolean areBiomesViable(int x, int z, int range, List<Biome> viables)
//    {
//        int i = x - range >> 2;
//        int j = z - range >> 2;
//        int k = x + range >> 2;
//        int l = z + range >> 2;
//        int diffX = (k - i) + 1;
//        int diffZ = (l - j) + 1;
//        int[] unzoomed = this.unzoomedBiomes.getInts(i, j, diffX, diffZ);
//
//        for (int a = 0; a < diffX * diffZ; ++a)
//        {
//            Biome biome = Biome.getBiome(unzoomed[a]);
//
//            if (!viables.contains(biome))
//            {
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    @Override
//    public BlockPos func_225531_a_(int xIn, int yIn, int zIn, int radiusIn, List<Biome> biomes, Random random)
//    {
//        int i = xIn - radiusIn >> 2;
//        int j = zIn - radiusIn >> 2;
//        int k = xIn + radiusIn >> 2;
//        int l = zIn + radiusIn >> 2;
//        int diffX = k - i + 1;
//        int diffZ = l - j + 1;
//        int k1 = yIn >> 2;
//        BlockPos blockpos = null;
//        int l1 = 0;
//
//        for(int i2 = 0; i2 < diffZ; ++i2) {
//            for(int j2 = 0; j2 < diffX; ++j2) {
//                int k2 = i + j2;
//                int l2 = j + i2;
//                if (biomes.contains(this.getNoiseBiome(k2, k1, l2))) {
//                    if (blockpos == null || random.nextInt(l1 + 1) == 0) {
//                        blockpos = new BlockPos(k2 << 2, yIn, l2 << 2);
//                    }
//
//                    ++l1;
//                }
//            }
//        }
//
//        return blockpos;
//    }
//
//    @Override
//    public void cleanupCache()
//    {
//        this.biomeCache.cleanupCache();
//    }
//}
