package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.entities.EntityAlienVillager;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;

import java.util.List;
import java.util.Random;

public abstract class StructureComponentVillage extends StructurePiece
{ private int villagersSpawned;
    protected StructureComponentVillageStartPiece startPiece;

    public StructureComponentVillage(IStructurePieceType structurePierceTypeIn, CompoundNBT nbt)
    {
        super(structurePierceTypeIn, nbt);
        this.villagersSpawned = nbt.getInt("VCount");
    }

    protected StructureComponentVillage(IStructurePieceType structurePierceTypeIn, StructureComponentVillageStartPiece par1ComponentVillageStartPiece, int par2)
    {
        super(structurePierceTypeIn, par2);
        this.startPiece = par1ComponentVillageStartPiece;
    }

    @Override
    protected void readAdditional(CompoundNBT nbt)
    {
        nbt.putInt("VCount", this.villagersSpawned);
    }

    protected StructurePiece getNextComponentNN(StructureComponentVillageStartPiece par1ComponentVillageStartPiece, List<StructurePiece> par2List, Random par3Random, int par4, int par5)
    {
        switch (this.getCoordBaseMode().getHorizontalIndex())
        {
        case 0:
            return StructureVillagePiecesMoon.getNextStructureComponent(par1ComponentVillageStartPiece, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY + par4, this.boundingBox.minZ + par5, Direction.byHorizontalIndex(1), this.getComponentType());
        case 1:
            return StructureVillagePiecesMoon.getNextStructureComponent(par1ComponentVillageStartPiece, par2List, par3Random, this.boundingBox.minX + par5, this.boundingBox.minY + par4, this.boundingBox.minZ - 1, Direction.byHorizontalIndex(2), this.getComponentType());
        case 2:
            return StructureVillagePiecesMoon.getNextStructureComponent(par1ComponentVillageStartPiece, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY + par4, this.boundingBox.minZ + par5, Direction.byHorizontalIndex(1), this.getComponentType());
        case 3:
            return StructureVillagePiecesMoon.getNextStructureComponent(par1ComponentVillageStartPiece, par2List, par3Random, this.boundingBox.minX + par5, this.boundingBox.minY + par4, this.boundingBox.minZ - 1, Direction.byHorizontalIndex(2), this.getComponentType());
        default:
            return null;
        }
    }

    protected StructurePiece getNextComponentPP(StructureComponentVillageStartPiece par1ComponentVillageStartPiece, List<StructurePiece> par2List, Random par3Random, int par4, int par5)
    {
        switch (this.getCoordBaseMode().getHorizontalIndex())
        {
        case 0:
            return StructureVillagePiecesMoon.getNextStructureComponent(par1ComponentVillageStartPiece, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY + par4, this.boundingBox.minZ + par5, Direction.byHorizontalIndex(3), this.getComponentType());
        case 1:
            return StructureVillagePiecesMoon.getNextStructureComponent(par1ComponentVillageStartPiece, par2List, par3Random, this.boundingBox.minX + par5, this.boundingBox.minY + par4, this.boundingBox.maxZ + 1, Direction.byHorizontalIndex(0), this.getComponentType());
        case 2:
            return StructureVillagePiecesMoon.getNextStructureComponent(par1ComponentVillageStartPiece, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY + par4, this.boundingBox.minZ + par5, Direction.byHorizontalIndex(3), this.getComponentType());
        case 3:
            return StructureVillagePiecesMoon.getNextStructureComponent(par1ComponentVillageStartPiece, par2List, par3Random, this.boundingBox.minX + par5, this.boundingBox.minY + par4, this.boundingBox.maxZ + 1, Direction.byHorizontalIndex(0), this.getComponentType());
        default:
            return null;
        }
    }

    protected int getAverageGroundLevel(IWorld world, MutableBoundingBox boundingBox)
    {
        int i = 0;
        int j = 0;
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();

        for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k)
        {
            for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l)
            {
                mutableBlockPos.setPos(l, 64, k);

                if (boundingBox.isVecInside(mutableBlockPos))
                {
                    i += world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, mutableBlockPos).getY();
                    ++j;
                }
            }
        }

        if (j == 0)
        {
            return -1;
        }
        else
        {
            return i / j;
        }
    }

    protected static boolean canVillageGoDeeper(MutableBoundingBox par0StructureBoundingBox)
    {
        return par0StructureBoundingBox != null && par0StructureBoundingBox.minY > 10;
    }

    protected void spawnVillagers(IWorld par1World, MutableBoundingBox par2StructureBoundingBox, int par3, int par4, int par5, int par6)
    {
        if (this.villagersSpawned < par6)
        {
            for (int var7 = this.villagersSpawned; var7 < par6; ++var7)
            {
                int var8 = this.getXWithOffset(par3 + var7, par5);
                final int var9 = this.getYWithOffset(par4);
                int var10 = this.getZWithOffset(par3 + var7, par5);

                var8 += par1World.getRandom().nextInt(3) - 1;
                var10 += par1World.getRandom().nextInt(3) - 1;

                if (!par2StructureBoundingBox.isVecInside(new BlockPos(var8, var9, var10)))
                {
                    break;
                }

                ++this.villagersSpawned;
                final EntityAlienVillager alienVillager;
                if (par1World instanceof WorldGenRegion)
                    alienVillager = GCEntities.ALIEN_VILLAGER.create(((WorldGenRegion) par1World).getWorld());
                else
                    alienVillager = GCEntities.ALIEN_VILLAGER.create((World) par1World);
                alienVillager.setLocationAndAngles(var8 + 0.5D, var9, var10 + 0.5D, 0.0F, 0.0F);
                par1World.addEntity(alienVillager);
            }
        }
    }

    protected void clearCurrentPositionBlocksUpwards(IWorld worldIn, int x, int y, int z, MutableBoundingBox structurebb)
    {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));

        if (structurebb.isVecInside(blockpos))
        {
            while (!worldIn.isAirBlock(blockpos) && blockpos.getY() < 255)
            {
                worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
                blockpos = blockpos.up();
            }
        }
    }
}
