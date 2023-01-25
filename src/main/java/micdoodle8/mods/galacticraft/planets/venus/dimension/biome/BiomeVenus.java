package micdoodle8.mods.galacticraft.planets.venus.dimension.biome;

import micdoodle8.mods.galacticraft.api.world.BiomeGC;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeVenus extends BiomeGC
{

    public BiomeVenus(Biome.Builder biomeBuilder)
    {
        super(biomeBuilder, true);
        addCarver(GenerationStage.Carving.AIR, Biome.createCarver(VenusBiomes.VENUS_CAVE_CARVER, new ProbabilityConfig(0.14285715F)));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, VenusFeatures.VENUS_LAKES.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
    }

    @Override
    public void registerTypes(Biome b)
    {
        BiomeDictionary.addTypes(b, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);
    }

//    @Override
//    public BiomeDecorator createBiomeDecorator()
//    {
//        return new BiomeDecoratorVenus();
//    }
//
    @Override
    public float getSpawningChance()
    {
        return 0.01F;
    }
}
