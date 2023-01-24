package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class StructureComponentVillageWell extends StructureComponentVillage
{
    private int averageGroundLevel = -1;

    public StructureComponentVillageWell(TemplateManager manager, CompoundNBT nbt)
    {
        super(GCFeatures.CMOON_VILLAGE_WELL, nbt);
        this.averageGroundLevel = nbt.getInt("AvgGroundLevel");
    }

    public StructureComponentVillageWell(IStructurePieceType structurePierceTypeIn, CompoundNBT nbt)
    {
        super(structurePierceTypeIn, nbt);
        this.averageGroundLevel = nbt.getInt("AvgGroundLevel");
    }

    public StructureComponentVillageWell(StructureComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, int par4, int par5)
    {
        super(GCFeatures.CMOON_VILLAGE_WELL, par1ComponentVillageStartPiece, par2);
        this.setCoordBaseMode(Direction.byIndex(par3Random.nextInt(4)));

        switch (this.getCoordBaseMode().getHorizontalIndex())
        {
        case 0:
        case 2:
            this.boundingBox = new MutableBoundingBox(par4, 64, par5, par4 + 6 - 1, 78, par5 + 6 - 1);
            break;
        default:
            this.boundingBox = new MutableBoundingBox(par4, 64, par5, par4 + 6 - 1, 78, par5 + 6 - 1);
        }
    }

    @Override
    protected void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);

        nbt.putInt("AvgGroundLevel", this.averageGroundLevel);
    }

    @Override
    public void buildComponent(StructurePiece componentIn, List<StructurePiece> par2List, Random par3Random)
    {
        StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) componentIn, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, Direction.byHorizontalIndex(1), this.getComponentType());
        StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) componentIn, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, Direction.byHorizontalIndex(3), this.getComponentType());
        StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) componentIn, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, Direction.byHorizontalIndex(2), this.getComponentType());
        StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) componentIn, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, Direction.byHorizontalIndex(0), this.getComponentType());
    }

    @Override
    public boolean create(IWorld par1World, ChunkGenerator<?> chunkGeneratorIn, Random par2Random, MutableBoundingBox par3StructureBoundingBox, ChunkPos chunkPosIn)
    {
        if (this.averageGroundLevel < 0)
        {
            this.averageGroundLevel = this.getAverageGroundLevel(par1World, par3StructureBoundingBox);

            if (this.averageGroundLevel < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 3, 0);
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 1, 4, 12, 4, GCBlocks.decoBlock1.getDefaultState(), Blocks.WATER.getDefaultState(), false);
        this.setBlockState(par1World, Blocks.AIR.getDefaultState(), 2, 12, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.AIR.getDefaultState(), 3, 12, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.AIR.getDefaultState(), 2, 12, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.AIR.getDefaultState(), 3, 12, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.DARK_OAK_FENCE.getDefaultState(), 1, 13, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.DARK_OAK_FENCE.getDefaultState(), 1, 14, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.DARK_OAK_FENCE.getDefaultState(), 4, 13, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.DARK_OAK_FENCE.getDefaultState(), 4, 14, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.DARK_OAK_FENCE.getDefaultState(), 1, 13, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.DARK_OAK_FENCE.getDefaultState(), 1, 14, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.DARK_OAK_FENCE.getDefaultState(), 4, 13, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.DARK_OAK_FENCE.getDefaultState(), 4, 14, 4, par3StructureBoundingBox);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 15, 1, 4, 15, 4, GCBlocks.decoBlock1.getDefaultState(), GCBlocks.decoBlock1.getDefaultState(), false);

        for (int var4 = 0; var4 <= 5; ++var4)
        {
            for (int var5 = 0; var5 <= 5; ++var5)
            {
                if (var5 == 0 || var5 == 5 || var4 == 0 || var4 == 5)
                {
                    this.setBlockState(par1World, Blocks.SPRUCE_PLANKS.getDefaultState(), var5, 11, var4, par3StructureBoundingBox);
                    this.clearCurrentPositionBlocksUpwards(par1World, var5, 12, var4, par3StructureBoundingBox);
                }
            }
        }
        return true;
    }
}
