package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;

public class BlockEmergencyBoxKit extends BlockEmergencyBox implements IShiftDescription, IPartialSealableBlock, ISortable
{
    public BlockEmergencyBoxKit(Properties builder)
    {
        super(builder);
    }
}
