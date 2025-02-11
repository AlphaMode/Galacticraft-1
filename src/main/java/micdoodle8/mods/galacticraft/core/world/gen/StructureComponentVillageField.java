package micdoodle8.mods.galacticraft.core.world.gen;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class StructureComponentVillageField extends StructureComponentVillage
{
    private int averageGroundLevel = -1;

    public StructureComponentVillageField(TemplateManager manager, CompoundNBT nbt)
    {
        super(GCFeatures.CMOON_VILLAGE_FIELD, nbt);
        this.averageGroundLevel = nbt.getInt("AvgGroundLevel");
    }

    public StructureComponentVillageField(StructureComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, MutableBoundingBox par4StructureBoundingBox, Direction par5)
    {
        super(GCFeatures.CMOON_VILLAGE_FIELD, par1ComponentVillageStartPiece, par2);
        this.setCoordBaseMode(par5);
        this.boundingBox = par4StructureBoundingBox;
    }

    @Override
    protected void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);

        nbt.putInt("AvgGroundLevel", this.averageGroundLevel);
    }

    public static StructureComponentVillageField func_74900_a(StructureComponentVillageStartPiece par0ComponentVillageStartPiece, List<StructurePiece> par1List, Random par2Random, int par3, int par4, int par5, Direction par6, int par7)
    {
        final MutableBoundingBox var8 = MutableBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 13, 4, 9, par6);
        return StructurePiece.findIntersecting(par1List, var8) == null ? new StructureComponentVillageField(par0ComponentVillageStartPiece, par7, par2Random, var8, par6) : null;
    }

    @Override
    public boolean create(IWorld par1World, ChunkGenerator<?> chunkGenerator, Random par2Random, MutableBoundingBox par3StructureBoundingBox, ChunkPos chunkPos)
    {
        if (this.averageGroundLevel < 0)
        {
            this.averageGroundLevel = this.getAverageGroundLevel(par1World, par3StructureBoundingBox);

            if (this.averageGroundLevel < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 7 - 1, 0);
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 0, 12, 4, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 1, 2, 0, 7, Blocks.DIRT.getDefaultState(), Blocks.DIRT.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 0, 1, 5, 0, 7, Blocks.DIRT.getDefaultState(), Blocks.DIRT.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 7, 0, 1, 8, 0, 7, Blocks.DIRT.getDefaultState(), Blocks.DIRT.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 10, 0, 1, 11, 0, 7, Blocks.DIRT.getDefaultState(), Blocks.DIRT.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 0, 0, 8, Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LOG.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 6, 0, 0, 6, 0, 8, Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LOG.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 12, 0, 0, 12, 0, 8, Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LOG.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 0, 11, 0, 0, Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LOG.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 8, 11, 0, 8, Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LOG.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 3, 0, 1, 3, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 9, 0, 1, 9, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);
        int var4;

        for (var4 = 1; var4 <= 7; ++var4)
        {
            for (int i = 1; i < 12; i++)
            {
                if (i % 3 != 0)
                {
                    if (par2Random.nextInt(3) == 0)
                    {
//                        this.setBlockState(par1World, Blocks.SAPLING.getStateFromMeta(MathHelper.nextInt(par2Random, 0, 2)), i, 1, var4, par3StructureBoundingBox);
                    }
                }
            }
        }

        for (var4 = 0; var4 < 9; ++var4)
        {
            for (int var5 = 0; var5 < 13; ++var5)
            {
                this.clearCurrentPositionBlocksUpwards(par1World, var5, 4, var4, par3StructureBoundingBox);
                this.replaceAirAndLiquidDownwards(par1World, Blocks.DIRT.getDefaultState(), var5, -1, var4, par3StructureBoundingBox);
            }
        }

        return true;
    }
}
