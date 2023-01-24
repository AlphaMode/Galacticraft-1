package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.ArrayList;
import java.util.Iterator;

public class StructureVillageStartMoon extends StructureStart
{
    private final int terrainType;

    public StructureVillageStartMoon(Structure<?> structure, int p_i225876_2_, int p_i225876_3_, MutableBoundingBox p_i225876_4_, int p_i225876_5_, long p_i225876_6_, int terrainType) {
        super(structure, p_i225876_2_, p_i225876_3_, p_i225876_4_, p_i225876_5_, p_i225876_6_);
        this.terrainType = terrainType;
    }

    @Override
    public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
        GCLog.debug("Generating Moon Village at x" + chunkX * 16 + " z" + chunkZ * 16);
        final ArrayList<StructureVillagePieceWeightMoon> var6 = StructureVillagePiecesMoon.getStructureVillageWeightedPieceList(this.rand, terrainType);
        final StructureComponentVillageStartPiece var7 = new StructureComponentVillageStartPiece(0, this.rand, (chunkX << 4) + 2, (chunkZ << 4) + 2, var6, terrainType);
        this.components.add(var7);
        var7.buildComponent(var7, this.components, this.rand);
        final ArrayList<Object> var8 = var7.field_74930_j;
        final ArrayList<Object> var9 = var7.field_74932_i;
        int var10;

        while (!var8.isEmpty() || !var9.isEmpty())
        {
            StructurePiece var11;

            if (var8.isEmpty())
            {
                var10 = this.rand.nextInt(var9.size());
                var11 = (StructurePiece) var9.remove(var10);
                var11.buildComponent(var7, this.components, this.rand);
            }
            else
            {
                var10 = this.rand.nextInt(var8.size());
                var11 = (StructurePiece) var8.remove(var10);
                var11.buildComponent(var7, this.components, this.rand);
            }
        }

        this.recalculateStructureSize();
        var10 = 0;
        final Iterator<StructurePiece> var13 = this.components.iterator();

        while (var13.hasNext())
        {
            final StructurePiece var12 = var13.next();

            if (!(var12 instanceof StructureComponentVillageRoadPiece))
            {
                ++var10;
            }
        }
    }

    /**
     * currently only defined for Villages, returns true if Village has more
     * than 2 non-road components
     */
    @Override
    public boolean isValid()
    {
        return true;
    }
}
