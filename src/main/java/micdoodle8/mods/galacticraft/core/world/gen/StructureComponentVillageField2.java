package micdoodle8.mods.galacticraft.core.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;

public class StructureComponentVillageField2 extends StructureComponentVillage
{
    private int averageGroundLevel = -1;

    private Block cropTypeA;
    private Block cropTypeB;
    private Block cropTypeC;
    private Block cropTypeD;

    public StructureComponentVillageField2(TemplateManager manager, CompoundNBT nbt)
    {
        super(GCFeatures.CMOON_VILLAGE_FIELD_TWO, nbt);

        this.averageGroundLevel = nbt.getInt("AvgGroundLevel");
        this.cropTypeA = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("CropTypeA")));
        this.cropTypeB = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("CropTypeB")));
        this.cropTypeC = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("CropTypeC")));
        this.cropTypeD = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("CropTypeD")));
    }

    public StructureComponentVillageField2(StructureComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, MutableBoundingBox par4StructureBoundingBox, Direction par5)
    {
        super(GCFeatures.CMOON_VILLAGE_FIELD_TWO, par1ComponentVillageStartPiece, par2);
        this.setCoordBaseMode(par5);
        this.boundingBox = par4StructureBoundingBox;
        this.cropTypeA = this.getRandomCrop(par3Random);
        this.cropTypeB = this.getRandomCrop(par3Random);
        this.cropTypeC = this.getRandomCrop(par3Random);
        this.cropTypeD = this.getRandomCrop(par3Random);
    }

    @Override
    protected void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);

        nbt.putInt("AvgGroundLevel", this.averageGroundLevel);
        nbt.putString("CropTypeA", ForgeRegistries.BLOCKS.getKey(this.cropTypeA).toString());
        nbt.putString("CropTypeB", ForgeRegistries.BLOCKS.getKey(this.cropTypeB).toString());
        nbt.putString("CropTypeC", ForgeRegistries.BLOCKS.getKey(this.cropTypeC).toString());
        nbt.putString("CropTypeD", ForgeRegistries.BLOCKS.getKey(this.cropTypeD).toString());
    }

    private Block getRandomCrop(Random par1Random)
    {
        switch (par1Random.nextInt(5))
        {
        case 0:
            return Blocks.CARROTS;
        case 1:
            return Blocks.POTATOES;
        default:
            return Blocks.WHEAT;
        }
    }

    public static StructureComponentVillageField2 func_74900_a(StructureComponentVillageStartPiece par0ComponentVillageStartPiece, List<StructurePiece> par1List, Random par2Random, int par3, int par4, int par5, Direction par6, int par7)
    {
        final MutableBoundingBox structureboundingbox = MutableBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 13, 4, 9, par6);
        return StructureComponentVillage.canVillageGoDeeper(structureboundingbox) && StructurePiece.findIntersecting(par1List, structureboundingbox) == null ? new StructureComponentVillageField2(par0ComponentVillageStartPiece, par7, par2Random, structureboundingbox, par6) : null;
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs,
     * Mob Spawners, it closes Mineshafts at the end, it adds Fences...
     */
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

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 4 - 1, 0);
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 0, 12, 4, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 1, 2, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 0, 1, 5, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 7, 0, 1, 8, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 10, 0, 1, 11, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 0, 0, 8, Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LOG.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 6, 0, 0, 6, 0, 8, Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LOG.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 12, 0, 0, 12, 0, 8, Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LOG.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 0, 11, 0, 0, Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LOG.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 8, 11, 0, 8, Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LOG.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 3, 0, 1, 3, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 9, 0, 1, 9, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);
        int i;

        for (i = 1; i <= 7; ++i)
        {
//            this.setBlockState(par1World, this.cropTypeA.getStateFromMeta(MathHelper.nextInt(par2Random, 2, 7)), 1, 1, i, par3StructureBoundingBox);
//            this.setBlockState(par1World, this.cropTypeA.getStateFromMeta(MathHelper.nextInt(par2Random, 2, 7)), 2, 1, i, par3StructureBoundingBox);
//            this.setBlockState(par1World, this.cropTypeB.getStateFromMeta(MathHelper.nextInt(par2Random, 2, 7)), 4, 1, i, par3StructureBoundingBox);
//            this.setBlockState(par1World, this.cropTypeB.getStateFromMeta(MathHelper.nextInt(par2Random, 2, 7)), 5, 1, i, par3StructureBoundingBox);
//            this.setBlockState(par1World, this.cropTypeC.getStateFromMeta(MathHelper.nextInt(par2Random, 2, 7)), 7, 1, i, par3StructureBoundingBox);
//            this.setBlockState(par1World, this.cropTypeC.getStateFromMeta(MathHelper.nextInt(par2Random, 2, 7)), 8, 1, i, par3StructureBoundingBox);
//            this.setBlockState(par1World, this.cropTypeD.getStateFromMeta(MathHelper.nextInt(par2Random, 2, 7)), 10, 1, i, par3StructureBoundingBox);
//            this.setBlockState(par1World, this.cropTypeD.getStateFromMeta(MathHelper.nextInt(par2Random, 2, 7)), 11, 1, i, par3StructureBoundingBox);
        }

        for (i = 0; i < 9; ++i)
        {
            for (int j = 0; j < 13; ++j)
            {
                this.clearCurrentPositionBlocksUpwards(par1World, j, 4, i, par3StructureBoundingBox);
                this.replaceAirAndLiquidDownwards(par1World, Blocks.DIRT.getDefaultState(), j, -1, i, par3StructureBoundingBox);
            }
        }

        return true;
    }
}
