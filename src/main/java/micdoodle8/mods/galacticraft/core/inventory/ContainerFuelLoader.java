package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerFuelLoader extends Container
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.FUEL_LOADER)
    public static ContainerType<ContainerFuelLoader> TYPE;

    private final TileEntityFuelLoader fuelLoader;

    public ContainerFuelLoader(int containerId, PlayerInventory playerInv, TileEntityFuelLoader fuelLoader)
    {
        super(TYPE, containerId);
        this.fuelLoader = fuelLoader;
        this.addSlot(new SlotSpecific(fuelLoader, 0, 51, 55, IItemElectric.class));
        this.addSlot(new Slot(fuelLoader, 1, 7, 12));

        int var6;
        int var7;

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlot(new Slot(playerInv, var7 + var6 * 9 + 9, 8 + var7 * 18, 31 + 58 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlot(new Slot(playerInv, var6, 8 + var6 * 18, 31 + 116));
        }
    }

    public TileEntityFuelLoader getFuelLoader()
    {
        return fuelLoader;
    }

    @Override
    public boolean canInteractWith(PlayerEntity var1)
    {
        return this.fuelLoader.isUsableByPlayer(var1);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity par1EntityPlayer, int par2)
    {
        ItemStack var3 = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            final ItemStack var5 = slot.getStack();
            var3 = var5.copy();
            boolean movedToMachineSlot = false;

            if (par2 < 2)
            {
                if (!this.mergeItemStack(var5, 2, 38, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (EnergyUtil.isElectricItem(var5.getItem()))
                {
                    if (!this.mergeItemStack(var5, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                    movedToMachineSlot = true;
                }
                else
                {
                    if (FluidUtil.isFuelContainerAny(var5))
                    {
                        if (!this.mergeItemStack(var5, 1, 2, false))
                        {
                            return ItemStack.EMPTY;
                        }
                        movedToMachineSlot = true;
                    }
                    else if (par2 < 29)
                    {
                        if (!this.mergeItemStack(var5, 29, 38, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.mergeItemStack(var5, 2, 29, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (var5.getCount() == 0)
            {
                // Needed where tile has inventoryStackLimit of 1
                if (movedToMachineSlot && var3.getCount() > 1)
                {
                    ItemStack remainder = var3.copy();
                    remainder.shrink(1);
                    slot.putStack(remainder);
                }
                else
                {
                    slot.putStack(ItemStack.EMPTY);
                }
            }
            else
            {
                slot.onSlotChanged();
            }

            if (var5.getCount() == var3.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, var5);
        }

        return var3;
    }
}
