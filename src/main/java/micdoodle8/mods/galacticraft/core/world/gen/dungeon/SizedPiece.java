package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;

import java.util.List;
import java.util.Random;

public abstract class SizedPiece extends DirectionalPiece
{
    protected int sizeX;
    protected int sizeY;
    protected int sizeZ;

    public SizedPiece(IStructurePieceType type, CompoundNBT nbt)
    {
        super(type, nbt);
    }

    public SizedPiece(IStructurePieceType type, DungeonConfiguration configuration, int sizeX, int sizeY, int sizeZ, Direction direction)
    {
        super(type, configuration, direction);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    @Override
    protected void writeStructureToNBT(CompoundNBT tagCompound)
    {
        super.writeStructureToNBT(tagCompound);

        tagCompound.putInt("sizeX", this.sizeX);
        tagCompound.putInt("sizeY", this.sizeY);
        tagCompound.putInt("sizeZ", this.sizeZ);
    }

    @Override
    protected void readStructureFromNBT(CompoundNBT nbt)
    {
        super.readStructureFromNBT(nbt);

        this.sizeX = nbt.getInt("sizeX");
        this.sizeY = nbt.getInt("sizeY");
        this.sizeZ = nbt.getInt("sizeZ");
    }

    @Override
    public void buildComponent(StructurePiece p_74861_1_, List<StructurePiece> p_74861_2_, Random p_74861_3_) {
        super.buildComponent(p_74861_1_, p_74861_2_, p_74861_3_);
    }

    public int getSizeX()
    {
        return sizeX;
    }

    public int getSizeY()
    {
        return sizeY;
    }

    public int getSizeZ()
    {
        return sizeZ;
    }
}
