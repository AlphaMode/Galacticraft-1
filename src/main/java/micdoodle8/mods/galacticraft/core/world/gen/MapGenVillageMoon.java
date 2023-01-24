package micdoodle8.mods.galacticraft.core.world.gen;

import com.mojang.datafixers.Dynamic;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;

import java.util.Random;
import java.util.function.Function;

public class MapGenVillageMoon extends Structure<MoonVillageConfiguration>
{
//    public static List<Biome> villageSpawnBiomes = Arrays.asList(new Biome[] { BiomeAdaptive.biomeDefault });
    private final int terrainType;

    public MapGenVillageMoon(Function<Dynamic<?>, ? extends MoonVillageConfiguration> func)
    {
        super(func);
        this.terrainType = 0;
    }

    @Override
    public boolean canBeGenerated(BiomeManager biomeManagerIn, ChunkGenerator<?> generatorIn, Random randIn, int chunkX, int chunkZ, Biome biomeIn)
    {
        final byte numChunks = 32;
        final byte offsetChunks = 8;
        final int oldi = chunkX;
        final int oldj = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= numChunks - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= numChunks - 1;
        }

        int randX = chunkX / numChunks;
        int randZ = chunkZ / numChunks;
        ((SharedSeedRandom)randIn).setLargeFeatureSeedWithSalt(generatorIn.getSeed(), chunkX, chunkZ, 10387312);
        randX *= numChunks;
        randZ *= numChunks;
        randX += randIn.nextInt(numChunks - offsetChunks);
        randZ += randIn.nextInt(numChunks - offsetChunks);

        return oldi == randX && oldj == randZ;
    }

    @Override
    public IStartFactory getStartFactory() {
        return (structure, chunkX, chunkZ, boundsIn, referenceIn, seed) -> new StructureVillageStartMoon(structure, chunkX, chunkZ, boundsIn, referenceIn, seed, this.terrainType);
    }

    @Override
    public String getStructureName()
    {
        return Constants.MOD_ID_CORE + ":moon_village";
    }

    @Override
    public int getSize() {
        return 0;
    }
}
