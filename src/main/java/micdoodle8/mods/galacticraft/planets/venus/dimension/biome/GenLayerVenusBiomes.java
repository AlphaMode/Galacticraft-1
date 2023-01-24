package micdoodle8.mods.galacticraft.planets.venus.dimension.biome;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

public enum GenLayerVenusBiomes implements IAreaTransformer0
{
    INSTANCE;

    private static final Biome[] biomes = new Biome[] { VenusBiomes.VENUS_FLAT, VenusBiomes.VENUS_VALLEY, VenusBiomes.VENUS_MOUNTAIN };

    @Override
    public int apply(INoiseRandom noise, int x, int y)
    {
        return Registry.BIOME.getId(biomes[noise.random(biomes.length)]);
    }
}
