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

public class StructureComponentVillageTorch extends StructureComponentVillage
{
    private int averageGroundLevel = -1;

    public StructureComponentVillageTorch(TemplateManager manager, CompoundNBT nbt)
    {
        super(GCFeatures.CMOON_VILLAGE_TORCH, nbt);
        this.averageGroundLevel = nbt.getInt("AvgGroundLevel");
    }

    public StructureComponentVillageTorch(StructureComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, MutableBoundingBox par4StructureBoundingBox, Direction par5)
    {
        super(GCFeatures.CMOON_VILLAGE_TORCH, par1ComponentVillageStartPiece, par2);
        this.setCoordBaseMode(par5);
        this.boundingBox = par4StructureBoundingBox;
    }

    @Override
    protected void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);

        nbt.putInt("AvgGroundLevel", this.averageGroundLevel);
    }

    public static MutableBoundingBox func_74904_a(StructureComponentVillageStartPiece par0ComponentVillageStartPiece, List<StructurePiece> par1List, Random par2Random, int par3, int par4, int par5, Direction par6)
    {
        final MutableBoundingBox var7 = MutableBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 3, 4, 2, par6);
        return StructurePiece.findIntersecting(par1List, var7) != null ? null : var7;
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs,
     * Mob Spawners, it closes Mineshafts at the end, it adds Fences...
     */
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

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 4 - 1, 0);
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 2, 3, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.setBlockState(par1World, Blocks.DARK_OAK_FENCE.getDefaultState(), 1, 0, 0, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.DARK_OAK_FENCE.getDefaultState(), 1, 1, 0, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.DARK_OAK_FENCE.getDefaultState(), 1, 2, 0, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.BLACK_WOOL.getDefaultState(), 1, 3, 0, par3StructureBoundingBox);
        boolean flag = this.getCoordBaseMode() == Direction.EAST || this.getCoordBaseMode() == Direction.NORTH;
        this.setBlockState(par1World, GCBlocks.glowstoneTorchWall.getDefaultState().with(WallTorchBlock.HORIZONTAL_FACING, this.getCoordBaseMode().rotateY()), flag ? 2 : 0, 3, 0, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.glowstoneTorchWall.getDefaultState().with(WallTorchBlock.HORIZONTAL_FACING, this.getCoordBaseMode()), 1, 3, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.glowstoneTorchWall.getDefaultState().with(WallTorchBlock.HORIZONTAL_FACING, this.getCoordBaseMode().rotateYCCW()), flag ? 0 : 2, 3, 0, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.glowstoneTorchWall.getDefaultState().with(WallTorchBlock.HORIZONTAL_FACING, this.getCoordBaseMode().getOpposite()), 1, 3, -1, par3StructureBoundingBox);
        return true;
    }
}
