package micdoodle8.mods.galacticraft.planets.venus.world.gen;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import micdoodle8.mods.galacticraft.planets.venus.dimension.VenusGenSettings;
import net.minecraft.block.BlockState;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawJunction;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;

public class VenusChunkGenerator extends NoiseChunkGenerator<VenusGenSettings>
{
    private static final float[] BIOME_WEIGHTS = Util.make(new float[25], (weights) ->
    {
        for (int xw = -2; xw <= 2; ++xw)
        {
            for (int zw = -2; zw <= 2; ++zw)
            {
                float weight = 10.0F / (float) Math.sqrt((float) (xw * xw + zw * zw) + 0.2F);
                weights[xw + 2 + (zw + 2) * 5] = weight;
            }
        }
    });

//    public static final BlockState BLOCK_FILL = VenusBlocks.venusBlock.getDefaultState().with(BlockVenusRock.BASIC_TYPE_VENUS, BlockVenusRock.EnumBlockBasicVenus.ROCK_HARD);
//
//    private final BiomeDecoratorVenus biomeDecoratorVenus = new BiomeDecoratorVenus();
//    private Random rand;
//    private OctavesNoiseGenerator noiseGen1;
//    private OctavesNoiseGenerator noiseGen2;
//    private OctavesNoiseGenerator noiseGen3;
//    private PerlinNoiseGenerator noiseGen4;
//    private OctavesNoiseGenerator noiseGen5;
//    private OctavesNoiseGenerator noiseGen6;
//    private OctavesNoiseGenerator mobSpawnerNoise;
//    private final Gradient noiseGenSmooth1;
//    private World world;
//    private WorldType worldType;
//    private final double[] terrainCalcs;
//    private final float[] parabolicField;
//    private double[] stoneNoise = new double[256];
//    private MapGenBaseMeta lavaCaveGenerator = new MapGenLavaVenus();
//    private Biome[] biomesForGeneration;
//    private double[] octaves1;
//    private double[] octaves2;
//    private double[] octaves3;
//    private double[] octaves4;
//    private final MapGenDungeonVenus dungeonGenerator = new MapGenDungeonVenus(new DungeonConfigurationVenus(VenusBlocks.venusBlock.getDefaultState().with(BlockVenusRock.BASIC_TYPE_VENUS, BlockVenusRock.EnumBlockBasicVenus.DUNGEON_BRICK_1),
//            VenusBlocks.venusBlock.getDefaultState().with(BlockVenusRock.BASIC_TYPE_VENUS, BlockVenusRock.EnumBlockBasicVenus.DUNGEON_BRICK_2),
//            30, 8, 16, 7, 7, RoomBossVenus.class, RoomTreasureVenus.class));

    private final OctavesNoiseGenerator depthNoise;

    public VenusChunkGenerator(IWorld worldIn, BiomeProvider biomeProvider, VenusGenSettings settingsIn)
    {
        super(worldIn, biomeProvider, 4, 8, 128, settingsIn, true);
        depthNoise = new OctavesNoiseGenerator(randomSeed, 15, 0);
    }

//    public VenusChunkGenerator(World worldIn, long seed, boolean mapFeaturesEnabled)
//    {
//        this.world = worldIn;
//        this.worldType = worldIn.getWorldInfo().getGenerator();
//        this.rand = new Random(seed);
//        this.noiseGen1 = new OctavesNoiseGenerator(this.rand, 16);
//        this.noiseGen2 = new OctavesNoiseGenerator(this.rand, 16);
//        this.noiseGen3 = new OctavesNoiseGenerator(this.rand, 8);
//        this.noiseGen4 = new PerlinNoiseGenerator(this.rand, 4);
//        this.noiseGen5 = new OctavesNoiseGenerator(this.rand, 10);
//        this.noiseGen6 = new OctavesNoiseGenerator(this.rand, 16);
//        this.mobSpawnerNoise = new OctavesNoiseGenerator(this.rand, 8);
//        this.noiseGenSmooth1 = new Gradient(this.rand.nextLong(), 4, 0.25F);
//        this.terrainCalcs = new double[825];
//        this.parabolicField = new float[25];
//
//        for (int i = -2; i <= 2; ++i)
//        {
//            for (int j = -2; j <= 2; ++j)
//            {
//                float f = 10.0F / MathHelper.sqrt((float) (i * i + j * j) + 0.2F);
//                this.parabolicField[i + 2 + (j + 2) * 5] = f;
//            }
//        }
//
//        NoiseGenerator[] noiseGens = {noiseGen1, noiseGen2, noiseGen3, noiseGen4, noiseGen5, noiseGen6, mobSpawnerNoise};
//        this.noiseGen1 = (OctavesNoiseGenerator) noiseGens[0];
//        this.noiseGen2 = (OctavesNoiseGenerator) noiseGens[1];
//        this.noiseGen3 = (OctavesNoiseGenerator) noiseGens[2];
//        this.noiseGen4 = (PerlinNoiseGenerator) noiseGens[3];
//        this.noiseGen5 = (OctavesNoiseGenerator) noiseGens[4];
//        this.noiseGen6 = (OctavesNoiseGenerator) noiseGens[5];
//        this.mobSpawnerNoise = (OctavesNoiseGenerator) noiseGens[6];
//    }

//    private void setBlocksInChunk(int chunkX, int chunkZ, ChunkPrimer primer)
//    {
//        this.noiseGenSmooth1.setFrequency(0.015F);
//        this.biomesForGeneration = this.world.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration, chunkX * 4 - 2, chunkZ * 4 - 2, 10, 10);
//        this.createLandPerBiome(chunkX * 4, chunkZ * 4);
//
//        for (int i = 0; i < 4; ++i)
//        {
//            int j = i * 5;
//            int k = (i + 1) * 5;
//
//            for (int l = 0; l < 4; ++l)
//            {
//                int i1 = (j + l) * 33;
//                int j1 = (j + l + 1) * 33;
//                int k1 = (k + l) * 33;
//                int l1 = (k + l + 1) * 33;
//
//                for (int i2 = 0; i2 < 32; ++i2)
//                {
//                    double d0 = 0.125D;
//                    double d1 = this.terrainCalcs[i1 + i2];
//                    double d2 = this.terrainCalcs[j1 + i2];
//                    double d3 = this.terrainCalcs[k1 + i2];
//                    double d4 = this.terrainCalcs[l1 + i2];
//                    double d5 = (this.terrainCalcs[i1 + i2 + 1] - d1) * d0;
//                    double d6 = (this.terrainCalcs[j1 + i2 + 1] - d2) * d0;
//                    double d7 = (this.terrainCalcs[k1 + i2 + 1] - d3) * d0;
//                    double d8 = (this.terrainCalcs[l1 + i2 + 1] - d4) * d0;
//
//                    for (int j2 = 0; j2 < 8; ++j2)
//                    {
//                        double d9 = 0.25D;
//                        double d10 = d1;
//                        double d11 = d2;
//                        double d12 = (d3 - d1) * d9;
//                        double d13 = (d4 - d2) * d9;
//
//                        for (int k2 = 0; k2 < 4; ++k2)
//                        {
//                            double d14 = 0.25D;
//                            double d16 = (d11 - d10) * d14;
//                            double lvt_45_1_ = d10 - d16;
//
//                            for (int l2 = 0; l2 < 4; ++l2)
//                            {
//                                if ((lvt_45_1_ += d16) > this.noiseGenSmooth1.getNoise(chunkX * 16 + (i * 4 + k2), chunkZ * 16 + (l * 4 + l2)) * 20.0)
//                                {
//                                    primer.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, BLOCK_FILL);
//                                }
//                            }
//
//                            d10 += d12;
//                            d11 += d13;
//                        }
//
//                        d1 += d5;
//                        d2 += d6;
//                        d3 += d7;
//                        d4 += d8;
//                    }
//                }
//            }
//        }
//    }
//
//    private void replaceBlocksForBiome(int p_180517_1_, int p_180517_2_, ChunkPrimer p_180517_3_, Biome[] p_180517_4_)
//    {
//        double d0 = 0.03125D;
//        this.stoneNoise = this.noiseGen4.getRegion(this.stoneNoise, (double) (p_180517_1_ * 16), (double) (p_180517_2_ * 16), 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);
//
//        for (int i = 0; i < 16; ++i)
//        {
//            for (int j = 0; j < 16; ++j)
//            {
//                Biome biomegenbase = p_180517_4_[j + i * 16];
//                biomegenbase.genTerrainBlocks(this.world, this.rand, p_180517_3_, p_180517_1_ * 16 + i, p_180517_2_ * 16 + j, this.stoneNoise[j + i * 16]);
//            }
//        }
//    }
//
//    @Override
//    public void generateSurface(WorldGenRegion region, IChunk chunk)
//    {
//        ChunkPos chunkpos = chunk.getPos();
//        int chunkX = chunkpos.x;
//        int chunkZ = chunkpos.z;
//        SharedSeedRandom chunkSeed = new SharedSeedRandom();
//        chunkSeed.setBaseChunkSeed(chunkX, chunkZ);
//        this.setBlocksInChunk(x, z, chunkprimer);
//        this.biomesForGeneration = this.world.getBiomeProvider().getBiomes(this.biomesForGeneration, x * 16, z * 16, 16, 16);
//
//        this.caveGenerator.generate(this.world, x, z, chunkprimer);
//        this.lavaCaveGenerator.generate(this.world, x, z, chunkprimer);
//
//        this.replaceBlocksForBiome(x, z, chunkprimer, this.biomesForGeneration);
//
//        this.dungeonGenerator.generate(this.world, x, z, chunkprimer);
//
//        Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
//        byte[] abyte = chunk.getBiomeArray();
//
//        for (int i = 0; i < abyte.length; ++i)
//        {
//            abyte[i] = (byte) Biome.getIdForBiome(this.biomesForGeneration[i]);
//        }
//
//        chunk.generateSkylightMap();
//        return chunk;
//    }


//
//    @Override
//    public void populate(int x, int z)
//    {
//        FallingBlock.fallInstantly = true;
//        int i = x * 16;
//        int j = z * 16;
//        BlockPos blockpos = new BlockPos(i, 0, j);
//        Biome biomegenbase = this.world.getBiome(blockpos.add(16, 0, 16));
//        this.rand.setSeed(this.world.getSeed());
//        long k = this.rand.nextLong() / 2L * 2L + 1L;
//        long l = this.rand.nextLong() / 2L * 2L + 1L;
//        this.rand.setSeed((long) x * k + (long) z * l ^ this.world.getSeed());
//        boolean isValley = biomegenbase instanceof BiomeAdaptive && ((BiomeAdaptive)biomegenbase).isInstance(BiomeGenVenusValley.class);
//
//        if (this.rand.nextInt(isValley ? 3 : 10) == 0)
//        {
//            int i2 = this.rand.nextInt(16) + 8;
//            int l2 = this.rand.nextInt(this.rand.nextInt(248) + 8);
//            int k3 = this.rand.nextInt(16) + 8;
//
//            (new WorldGenLakesVenus()).generate(this.world, this.rand, blockpos.add(i2, l2, k3));
//        }
//
//
//        if (isValley)
//        {
//            if (this.rand.nextInt(5) == 0)
//            {
//                int i2 = this.rand.nextInt(16) + 8;
//                int k3 = this.rand.nextInt(16) + 8;
//                int l2 = this.world.getTopSolidOrLiquidBlock(blockpos.add(i2, 0, k3)).getY() - 10 - this.rand.nextInt(5);
//
//                (new WorldGenVaporPool()).generate(this.world, this.rand, blockpos.add(i2, l2, k3));
//            }
//            else if (this.rand.nextInt(190) == 0)
//            {
//                int i2 = this.rand.nextInt(16) + 8;
//                int k3 = this.rand.nextInt(16) + 8;
//                int l2 = this.world.getTopSolidOrLiquidBlock(blockpos.add(i2, 0, k3)).getY();
//
//                (new WorldGenCrashedProbe()).generate(this.world, this.rand, blockpos.add(i2, l2, k3));
//            }
//        }
//
//        this.dungeonGenerator.generateStructure(this.world, this.rand, new ChunkPos(x, z));
//
//        biomegenbase.decorate(this.world, this.rand, new BlockPos(i, 0, j));
//        WorldEntitySpawner.performWorldGenSpawning(this.world, biomegenbase, i + 8, j + 8, 16, 16, this.rand);
//
//        FallingBlock.fallInstantly = false;
//    }
//
//    @Override
//    public List<Biome.SpawnListEntry> getPossibleCreatures(EntityClassification creatureType, BlockPos pos)
//    {
//        Biome biomegenbase = this.world.getBiome(pos);
//
//        return biomegenbase.getSpawnableList(creatureType);
//    }
//
//    @Override
//    public void recreateStructures(Chunk chunk, int x, int z)
//    {
//        this.dungeonGenerator.generate(this.world, x, z, null);
//    }

    // get depth / scale
    @Override
    protected double[] getBiomeNoiseColumn(int x, int z)
    {
        double[] depthAndScale = new double[2];
        float scaleF1 = 0.0F;
        float depthF1 = 0.0F;
        float divisor = 0.0F;
        int j = this.getSeaLevel();
        float baseDepth = this.biomeProvider.getNoiseBiome(x, j, z).getDepth();

        for (int xMod = -2; xMod <= 2; ++xMod)
        {
            for (int zMod = -2; zMod <= 2; ++zMod)
            {
                Biome biomeAt = this.biomeProvider.getNoiseBiome(x + xMod, j, z + zMod);
                float biomeDepth = biomeAt.getDepth();
                float biomeScale = biomeAt.getScale();

                float weight = BIOME_WEIGHTS[xMod + 2 + (zMod + 2) * 5] / (biomeDepth + 2.0F);
                if (biomeAt.getDepth() > baseDepth)
                {
                    weight /= 2.0F;
                }

                scaleF1 += biomeScale * weight;
                depthF1 += biomeDepth * weight;
                divisor += weight;
            }
        }

        scaleF1 /= divisor;
        depthF1 /= divisor;
        scaleF1 = scaleF1 * 0.9F + 0.1F;
        depthF1 = (depthF1 * 4.0F - 1.0F) / 8.0F;
        depthAndScale[0] = (double) depthF1 + this.getSpecialDepth(x, z);
        depthAndScale[1] = scaleF1;
        return depthAndScale;
    }

    private double getSpecialDepth(int x, int z)
    {
        double sDepth = this.depthNoise.getValue(x * 200, 10.0D, z * 200, 1.0D, 0.0D, true) / 4000.0D;
        if (sDepth < 0.0D)
        {
            sDepth = -sDepth * 0.3D;
        }

        sDepth = sDepth * 3.0D - 2.0D;
        if (sDepth < 0.0D)
        {
            sDepth /= 28.0D;
        }
        else
        {
            if (sDepth > 1.0D)
            {
                sDepth = 1.0D;
            }

            sDepth /= 40.0D;
        }

        return sDepth;
    }

    // yoffset
    @Override
    protected double func_222545_a(double depth, double scale, int yy)
    {
        // The higher this value is, the higher the terrain is!
        final double baseSize = 8.5D;
        double yOffsets = ((double) yy - (baseSize + depth * baseSize / 8.0D * 4.0D)) * 12.0D * 128.0D / 256.0D / scale;
        if (yOffsets < 0.0D)
        {
            yOffsets *= 4.0D;
        }

        return yOffsets;
    }

    private double[] func_222572_j() {
        double[] adouble = new double[this.noiseSizeY()];

        for(int i = 0; i < this.noiseSizeY(); ++i) {
            adouble[i] = Math.cos((double)i * Math.PI * 6.0D / (double)this.noiseSizeY()) * 2.0D;
            double d0 = (double)i;
            if (i > this.noiseSizeY() / 2) {
                d0 = (double)(this.noiseSizeY() - 1 - i);
            }

            if (d0 < 4.0D) {
                d0 = 4.0D - d0;
                adouble[i] -= d0 * d0 * d0 * 10.0D;
            }
        }

        return adouble;
    }

    // populate noise
    @Override
    protected void fillNoiseColumn(double[] noiseColumn, int x, int z)
    {
        double xzScale = 684.4119873046875D;
        double yScale = 684.4119873046875D;
        double xzOtherScale = 8.555149841308594D;
        double yOtherScale = 4.277574920654297D;

        final int topSlideMax = 0;
        final int topSlideScale = 3;

        calcNoiseColumn(noiseColumn, x, z, xzScale, yScale, xzOtherScale, yOtherScale, topSlideScale, topSlideMax);
    }

    @Override
    public int getGroundHeight()
    {
        return 69;
    }

    @Override
    public int getSeaLevel()
    {
        return super.getSeaLevel();
    }
}