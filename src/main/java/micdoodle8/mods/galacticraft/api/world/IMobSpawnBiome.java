package micdoodle8.mods.galacticraft.api.world;

import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome.SpawnListEntry;

import java.util.Map;

/**
 * Implement this on any Galacticraft World dimension biome registered for a Celestial Body
 */
public interface IMobSpawnBiome
{
    void initialiseMobLists(Map<SpawnListEntry, EntityClassification> mobInfo);
}
