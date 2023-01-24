package micdoodle8.mods.galacticraft.planets.venus.dimension.biome;

import micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;

public class VenusVallyBiome extends BiomeVenus {
    public VenusVallyBiome(Builder biomeBuilder) {
        super(biomeBuilder);
//        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, VenusFeatures.VAPOR_POOL.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(50, 8, 8, 256))));
    }
}
