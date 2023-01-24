package micdoodle8.mods.galacticraft.core.world.gen;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public abstract class StructureComponentVillageRoadPiece extends StructureComponentVillage
{
    protected StructureComponentVillageRoadPiece(IStructurePieceType structurePierceTypeIn, CompoundNBT nbt)
    {
        super(structurePierceTypeIn, nbt);
    }

    protected StructureComponentVillageRoadPiece(IStructurePieceType structurePierceTypeIn, StructureComponentVillageStartPiece par1ComponentVillageStartPiece, int par2)
    {
        super(structurePierceTypeIn, par1ComponentVillageStartPiece, par2);
    }
}
