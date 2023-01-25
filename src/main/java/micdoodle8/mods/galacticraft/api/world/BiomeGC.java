package micdoodle8.mods.galacticraft.api.world;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;

import java.util.Map;

/**
 * This extension of BiomeGenBase contains the default initialiseMobLists()
 * called on CelestialBody registration to register mob spawn data
 */
public abstract class BiomeGC extends Biome implements IMobSpawnBiome
{
    public final boolean isAdaptiveBiome;

    public static Biome.Builder defaultBuilder() {
        return new Builder()
                .depth(0.1F).scale(0.2F)
                .temperature(0.5F).downfall(0.0F)
                .waterColor(16777215).precipitation(RainType.NONE);
    }

    protected BiomeGC(Biome.Builder biomeBuilder)
    {
        super(biomeBuilder);
        GalacticraftCore.biomesList.add(this);
        this.isAdaptiveBiome = false;
    }

    protected BiomeGC(Biome.Builder biomeBuilder, boolean adaptive)
    {
        super(biomeBuilder);
        this.isAdaptiveBiome = adaptive;
    }

    /**
     * Override this in your biomes
     * <br>
     * (Note: if adaptive biomes, only the FIRST to register the adaptive biome will have its
     * types registered in the BiomeDictionary - sorry, that's a Forge limitation.)
     */
    public void registerTypes(Biome registering)
    {
    }

    /**
     * The default implementation in BiomeGenBaseGC will attempt to allocate each
     * SpawnListEntry in the CelestialBody's mobInfo to this biome's
     * Water, Cave, Monster or Creature lists according to whether the
     * spawnable entity's class is a subclass of EntityWaterMob, EntityAmbientCreature,
     * EntityMob or anything else (passive mobs or plain old EntityLiving).
     * <p>
     * Override this if different behaviour is required.
     */
    @Override
    public void initialiseMobLists(Map<SpawnListEntry, EntityClassification> mobInfo)
    {
        for (EntityClassification classification : EntityClassification.values())
        {
            this.getSpawns(classification).clear();
        }
        for (Map.Entry<SpawnListEntry, EntityClassification> entry : mobInfo.entrySet())
        {
            getSpawns(entry.getValue()).add(entry.getKey());
        }
    }
}
