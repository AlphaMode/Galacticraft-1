package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityIngotCompressor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerIngotCompressor extends Container
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.INGOT_COMPRESSOR)
    public static ContainerType<ContainerIngotCompressor> TYPE;

    private final TileEntityIngotCompressor compressor;

    public ContainerIngotCompressor(int containerId, PlayerInventory playerInv, TileEntityIngotCompressor compressor)
    {
        super(TYPE, containerId);
        this.compressor = compressor;
        compressor.compressingCraftMatrix.eventHandler = this;

        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 3; y++)
            {
                this.addSlot(new Slot(compressor.compressingCraftMatrix, y + x * 3, 19 + y * 18, 18 + x * 18));
            }
        }

        // Coal slot
        this.addSlot(new Slot(compressor, 0, 55, 75));

        // Smelting result
        this.addSlot(new FurnaceResultSlot(playerInv.player, compressor, 1, 138, 38));
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(playerInv, var4 + var3 * 9 + 9, 8 + var4 * 18, 110 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlot(new Slot(playerInv, var3, 8 + var3 * 18, 168));
        }

        compressor.playersUsing.add(playerInv.player);
    }

    public TileEntityIngotCompressor getCompressor()
    {
        return compressor;
    }

    @Override
    public void onContainerClosed(PlayerEntity entityplayer)
    {
        super.onContainerClosed(entityplayer);
        this.compressor.playersUsing.remove(entityplayer);
    }

    @Override
    public boolean canInteractWith(PlayerEntity par1EntityPlayer)
    {
        return this.compressor.isUsableByPlayer(par1EntityPlayer);
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        super.onCraftMatrixChanged(par1IInventory);
        this.compressor.updateInput();
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(par1);

        if (slot != null && slot.getHasStack())
        {
            ItemStack var4 = slot.getStack();
            var2 = var4.copy();

            if (par1 <= 10)
            {
                if (!this.mergeItemStack(var4, 11, 47, true))
                {
                    return ItemStack.EMPTY;
                }

                if (par1 == 1)
                {
                    slot.onSlotChange(var4, var2);
                }
            }
            else
            {
                if (ForgeHooks.getBurnTime(var4) > 0)
                {
                    if (!this.mergeItemStack(var4, 9, 10, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par1 < 38)
                {
                    if (!this.mergeItemStack(var4, 0, 9, false) && !this.mergeItemStack(var4, 38, 47, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.mergeItemStack(var4, 0, 9, false) && !this.mergeItemStack(var4, 11, 38, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (var4.getCount() == 0)
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (var4.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, var4);
        }

        return var2;
    }

    //Can only split-drag into the crafting table slots
    @Override
    public boolean canDragIntoSlot(Slot par1Slot)
    {
        return par1Slot.slotNumber < 9;
    }

}
