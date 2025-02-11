package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
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

public class StructureComponentVillageHouse extends StructureComponentVillage
{
    private int averageGroundLevel = -1;

    public StructureComponentVillageHouse(TemplateManager manager, CompoundNBT nbt)
    {
        super(GCFeatures.CMOON_VILLAGE_HOUSE, nbt);
        this.averageGroundLevel = nbt.getInt("AvgGroundLevel");
    }

    public StructureComponentVillageHouse(StructureComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, MutableBoundingBox par4StructureBoundingBox, Direction par5)
    {
        super(GCFeatures.CMOON_VILLAGE_HOUSE, par1ComponentVillageStartPiece, par2);
        this.setCoordBaseMode(par5);
        this.boundingBox = par4StructureBoundingBox;
    }

    public static StructureComponentVillageHouse func_74921_a(StructureComponentVillageStartPiece par0ComponentVillageStartPiece, List<StructurePiece> par1List, Random par2Random, int par3, int par4, int par5, Direction par6, int par7)
    {
        final MutableBoundingBox var8 = MutableBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 17, 9, 17, par6);
        return StructurePiece.findIntersecting(par1List, var8) == null ? new StructureComponentVillageHouse(par0ComponentVillageStartPiece, par7, par2Random, var8, par6) : null;
    }

    @Override
    protected void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);

        nbt.putInt("AvgGroundLevel", this.averageGroundLevel);
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs,
     * Mob Spawners, it closes Mineshafts at the end, it adds Fences...
     */
    @Override
    public boolean create(IWorld par1World, ChunkGenerator<?> chunkGenerator, Random par2Random, MutableBoundingBox par3StructureBoundingBox, ChunkPos pos)
    {
        if (this.averageGroundLevel < 0)
        {
            this.averageGroundLevel = this.getAverageGroundLevel(par1World, par3StructureBoundingBox);

            if (this.averageGroundLevel < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 9 - 1, 0);
        }

        this.fillWithAir(par1World, par3StructureBoundingBox, 3, 0, 3, 13, 9, 13);
        this.fillWithAir(par1World, par3StructureBoundingBox, 5, 0, 2, 11, 9, 14);
        this.fillWithAir(par1World, par3StructureBoundingBox, 2, 0, 5, 14, 9, 11);

        for (int i = 3; i <= 13; i++)
        {
            for (int j = 3; j <= 13; j++)
            {
                this.setBlockState(par1World, GCBlocks.decoBlock0.getDefaultState(), i, 0, j, par3StructureBoundingBox);
            }
        }

        for (int i = 5; i <= 11; i++)
        {
            for (int j = 2; j <= 14; j++)
            {
                this.setBlockState(par1World, GCBlocks.decoBlock0.getDefaultState(), i, 0, j, par3StructureBoundingBox);
            }
        }

        for (int i = 2; i <= 14; i++)
        {
            for (int j = 5; j <= 11; j++)
            {
                this.setBlockState(par1World, GCBlocks.decoBlock0.getDefaultState(), i, 0, j, par3StructureBoundingBox);
            }
        }

        int yLevel = 0;

        for (yLevel = -8; yLevel < 4; yLevel++)
        {
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 2, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 3, yLevel, 2, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 3, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 4, par3StructureBoundingBox);

            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 5, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 6, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 7, par3StructureBoundingBox);
            this.setBlockState(par1World, yLevel <= 1 ? GCBlocks.decoBlock1.getDefaultState() : Blocks.AIR.getDefaultState(), 1, yLevel, 8, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 9, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 10, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 11, par3StructureBoundingBox);

            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 12, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 13, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 3, yLevel, 14, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 14, par3StructureBoundingBox);

            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 5, yLevel, 15, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 6, yLevel, 15, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 7, yLevel, 15, par3StructureBoundingBox);
            this.setBlockState(par1World, yLevel <= 1 ? GCBlocks.decoBlock1.getDefaultState() : Blocks.AIR.getDefaultState(), 8, yLevel, 15, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 9, yLevel, 15, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 10, yLevel, 15, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 11, yLevel, 15, par3StructureBoundingBox);

            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 14, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 13, yLevel, 14, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 13, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 12, par3StructureBoundingBox);

            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 11, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 10, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 9, par3StructureBoundingBox);
            this.setBlockState(par1World, yLevel <= 1 ? GCBlocks.decoBlock1.getDefaultState() : Blocks.AIR.getDefaultState(), 15, yLevel, 8, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 7, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 6, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 5, par3StructureBoundingBox);

            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 4, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 3, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 13, yLevel, 2, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 2, par3StructureBoundingBox);

            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 11, yLevel, 1, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 10, yLevel, 1, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 9, yLevel, 1, par3StructureBoundingBox);
            this.setBlockState(par1World, yLevel <= 1 ? GCBlocks.decoBlock1.getDefaultState() : Blocks.AIR.getDefaultState(), 8, yLevel, 1, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 7, yLevel, 1, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 6, yLevel, 1, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 5, yLevel, 1, par3StructureBoundingBox);
        }

        yLevel = 4;

        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 3, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 4, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 5, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 6, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 9, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 10, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 11, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 3, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 14, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 5, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 6, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 7, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 8, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 9, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 10, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 11, yLevel, 15, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 13, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 12, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 10, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 9, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 6, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 5, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 13, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 2, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 11, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 10, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 9, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 8, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 7, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 6, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 5, yLevel, 1, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.glowstoneTorchWall.getDefaultState().with(WallTorchBlock.HORIZONTAL_FACING, this.getCoordBaseMode()), 8, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.glowstoneTorchWall.getDefaultState().with(WallTorchBlock.HORIZONTAL_FACING, this.getCoordBaseMode().rotateY()), 14, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.glowstoneTorchWall.getDefaultState().with(WallTorchBlock.HORIZONTAL_FACING, this.getCoordBaseMode().getOpposite()), 8, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.glowstoneTorchWall.getDefaultState().with(WallTorchBlock.HORIZONTAL_FACING, this.getCoordBaseMode().rotateYCCW()), 2, yLevel, 8, par3StructureBoundingBox);

        yLevel = 5;

        // corner 1
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 5, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 3, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 5, par3StructureBoundingBox);

        // LogicalSide 1
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 6, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 9, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 1, yLevel, 10, par3StructureBoundingBox);

        // corner 2
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 3, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 5, yLevel, 14, par3StructureBoundingBox);

        // LogicalSide 2
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 6, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 7, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 8, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 9, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 10, yLevel, 15, par3StructureBoundingBox);

        // corner 3
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 11, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 13, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 11, par3StructureBoundingBox);

        // LogicalSide 3
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 10, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 9, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 15, yLevel, 6, par3StructureBoundingBox);

        // corner 4
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 5, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 13, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 11, yLevel, 2, par3StructureBoundingBox);

        // LogicalSide 4
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 10, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 9, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 8, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 7, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 6, yLevel, 1, par3StructureBoundingBox);

        yLevel = 6;

        // corner 1
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 3, yLevel, 4, par3StructureBoundingBox);

        // LogicalSide 1
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 5, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 6, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 9, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 10, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 11, par3StructureBoundingBox);

        // corner 2
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 3, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 13, par3StructureBoundingBox);

        // LogicalSide 2
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 5, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 6, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 7, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 8, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 9, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 10, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 11, yLevel, 14, par3StructureBoundingBox);

        // corner 3
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 13, yLevel, 12, par3StructureBoundingBox);

        // LogicalSide 3
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 10, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 9, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 6, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 5, par3StructureBoundingBox);

        // corner 4
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 13, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 3, par3StructureBoundingBox);

        // LogicalSide 4
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 11, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 10, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 9, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 8, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 7, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 6, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 5, yLevel, 2, par3StructureBoundingBox);

        yLevel = 7;

        // corner 1
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 6, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 5, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 3, yLevel, 5, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 3, yLevel, 6, par3StructureBoundingBox);

        // LogicalSide 1
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 2, yLevel, 9, par3StructureBoundingBox);

        // corner 2
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 3, yLevel, 10, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 3, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 5, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 6, yLevel, 13, par3StructureBoundingBox);

        // LogicalSide 2
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 7, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 8, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 9, yLevel, 14, par3StructureBoundingBox);

        // corner 3
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 10, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 11, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 13, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 13, yLevel, 10, par3StructureBoundingBox);

        // LogicalSide 3
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 9, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 14, yLevel, 7, par3StructureBoundingBox);

        // corner 4
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 13, yLevel, 6, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 13, yLevel, 5, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 11, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 10, yLevel, 3, par3StructureBoundingBox);

        // LogicalSide 4
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 9, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 8, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 7, yLevel, 2, par3StructureBoundingBox);

        yLevel = 8;

        // corner 1
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 6, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 5, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 5, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 6, par3StructureBoundingBox);

        // LogicalSide 1
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 3, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 3, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 3, yLevel, 9, par3StructureBoundingBox);

        // corner 2
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 10, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 5, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 6, yLevel, 12, par3StructureBoundingBox);

        // LogicalSide 2
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 7, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 8, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 9, yLevel, 13, par3StructureBoundingBox);

        // corner 3
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 10, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 11, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 10, par3StructureBoundingBox);

        // LogicalSide 3
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 13, yLevel, 9, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 13, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 13, yLevel, 7, par3StructureBoundingBox);

        // corner 4
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 6, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 5, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 11, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 10, yLevel, 4, par3StructureBoundingBox);

        // LogicalSide 4
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 9, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 8, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 7, yLevel, 3, par3StructureBoundingBox);

        // extras
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 5, yLevel, 5, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 5, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 11, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 11, yLevel, 5, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 4, yLevel, 9, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 7, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 8, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 9, yLevel, 12, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 9, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 8, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 7, yLevel, 4, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), 12, yLevel, 9, par3StructureBoundingBox);

        yLevel = 9;

        for (int i = 5; i <= 11; i++)
        {
            for (int j = 5; j <= 11; j++)
            {
                if (!(j == 5 && i == 5 || j == 5 && i == 11 || j == 11 && i == 5 || j == 11 && i == 11))
                {
                    if (i >= 7 && i <= 9 && j >= 7 && j <= 9)
                    {
                        this.setBlockState(par1World, Blocks.GLASS.getDefaultState(), i, yLevel, j, par3StructureBoundingBox);
                    }
                    else
                    {
                        this.setBlockState(par1World, GCBlocks.decoBlock1.getDefaultState(), i, yLevel, j, par3StructureBoundingBox);
                    }
                }
            }
        }

        this.spawnVillagers(par1World, par3StructureBoundingBox, 6, 5, 6, 4);
        return true;
    }
}
