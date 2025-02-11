package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface IMultiBlock extends ITickableTileEntity
{
    /**
     * Called when activated
     */
    ActionResultType onActivated(PlayerEntity entityPlayer);

    /**
     * Called when this multiblock is created
     *
     * @param placedPosition - The position the block was placed at
     */
    void onCreate(World world, BlockPos placedPosition);

    /**
     * Called when one of the multiblocks of this block is destroyed
     *
     * @param callingBlock - The tile entity who called the onDestroy function
     */
    void onDestroy(TileEntity callingBlock);

    void getPositions(BlockPos placedPosition, List<BlockPos> positions);

    BlockMulti.EnumBlockMultiType getMultiType();
}
