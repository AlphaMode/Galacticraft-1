package micdoodle8.mods.galacticraft.planets.mars.dimension.biome;

import micdoodle8.mods.galacticraft.api.world.BiomeGC;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.MarsFeatures;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.RoomBossMars;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.RoomTreasureMars;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class BiomeMars extends BiomeGC
{
    public static final BiomeMars marsFlat = new BiomeMars();
    //    public static final Biome moonFlat = new BiomeFlatMoon(new BiomeProperties("Moon").setBaseHeight(1.5F).setHeightVariation(0.4F).setRainfall(0.0F));

    BiomeMars()
    {
        super(defaultBuilder().surfaceBuilder(SurfaceBuilder.DEFAULT, new SurfaceBuilderConfig(MarsBlocks.rockSurface.getDefaultState(), MarsBlocks.rockMiddle.getDefaultState(), MarsBlocks.MARS_STONE.getDefaultState())).precipitation(Biome.RainType.NONE).category(Category.NONE).depth(2.5F).scale(0.4F).temperature(0.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null), true);
        ConfiguredFeature<DungeonConfiguration, ? extends Structure<DungeonConfiguration>> dungeon = MarsFeatures.MARS_DUNGEON.withConfiguration(new DungeonConfiguration(MarsBlocks.dungeonBrick.getDefaultState(), 30, 8, 16, 7, 7,
                RoomBossMars.class, RoomTreasureMars.class));
        addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, dungeon);
        addStructure(dungeon);
    }

//    @Override
//    public BiomeDecorator createBiomeDecorator()
//    {
//        return getModdedBiomeDecorator(new BiomeDecoratorMoon());
//    }

    @Override
    public float getSpawningChance()
    {
        return 0.1F;
    }

//    @Override
//    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
//    {
//        this.fillerBlock = MoonChunkGenerator.BLOCK_LOWER;
//        this.topBlock = MoonChunkGenerator.BLOCK_TOP;
//        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
//    }
}

