package micdoodle8.mods.galacticraft.core.world.gen;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.ArrayList;
import java.util.Random;

public class StructureComponentVillageStartPiece extends StructureComponentVillageWell
{
    public int terrainType;
    public StructureVillagePieceWeightMoon structVillagePieceWeight;
    public ArrayList<StructureVillagePieceWeightMoon> structureVillageWeightedPieceList;
    public ArrayList<Object> field_74932_i = new ArrayList<Object>();
    public ArrayList<Object> field_74930_j = new ArrayList<Object>();

    public StructureComponentVillageStartPiece(TemplateManager manager, CompoundNBT nbt)
    {
        super(GCFeatures.CMOON_VILLAGE_START, nbt);
        this.terrainType = nbt.getInt("TerrainType");
    }

    public StructureComponentVillageStartPiece(int par2, Random par3Random, int par4, int par5, ArrayList<StructureVillagePieceWeightMoon> par6ArrayList, int par7)
    {
        super(null, 0, par3Random, par4, par5);
        this.structureVillageWeightedPieceList = par6ArrayList;
        this.terrainType = par7;
        this.startPiece = this;
    }

    @Override
    protected void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);

        nbt.putInt("TerrainType", this.terrainType);
    }
}
