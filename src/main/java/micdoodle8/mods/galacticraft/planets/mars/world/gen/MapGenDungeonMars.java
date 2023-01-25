package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import com.mojang.datafixers.Dynamic;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonStart;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.StructureDungeon;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.function.Function;

public class MapGenDungeonMars extends StructureDungeon
{
    public MapGenDungeonMars(Function<Dynamic<?>, ? extends DungeonConfiguration> func)
    {
        super(func);
    }

    @Override
    public String getStructureName()
    {
        return "GC_Dungeon_Mars";
    }

    @Override
    public IStartFactory getStartFactory() {
        return MapGenDungeonMars.Start::new;
    }

    public static class Start extends StructureStart
    {
        //        private DungeonConfiguration configuration;
        DungeonStart startPiece;

        public Start(Structure<?> structure, int chunkX, int chunkZ, MutableBoundingBox boundsIn, int referenceIn, long seed)
        {
            super(structure, chunkX, chunkZ, boundsIn, referenceIn, seed);
//            this.configuration = configuration;
        }

        @Override
        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
        {
            DungeonConfiguration dungeonConfig = generator.getStructureConfig(biomeIn, MarsFeatures.MARS_DUNGEON);
            startPiece = new DungeonStart((World) generator.world, dungeonConfig, rand, (chunkX << 4) + 2, (chunkZ << 4) + 2);
            startPiece.buildComponent(startPiece, this.components, rand);
            List<StructurePiece> list = startPiece.attachedComponents;

            while (!list.isEmpty())
            {
                int i = rand.nextInt(list.size());
                StructurePiece structurecomponent = list.remove(i);
                structurecomponent.buildComponent(startPiece, this.components, rand);
            }

            this.recalculateStructureSize();
        }

        public boolean isValid() {
            return true;
        }
    }
}
