package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.ShapedRecipesGC;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlockNames;
import micdoodle8.mods.galacticraft.core.inventory.ContainerIngotCompressor;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.inventory.PersistantInventoryCrafting;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TileEntityIngotCompressor extends TileEntityAdvanced implements IInventoryDefaults, ISidedInventory, INamedContainerProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.ingotCompressor)
    public static TileEntityType<TileEntityIngotCompressor> TYPE;

    public static final int PROCESS_TIME_REQUIRED = 200;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int processTicks = 0;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int furnaceBurnTime = 0;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int currentItemBurnTime = 0;
    private long ticks;

    private ItemStack producingStack = ItemStack.EMPTY;
    public PersistantInventoryCrafting compressingCraftMatrix = new PersistantInventoryCrafting();
    public final Set<PlayerEntity> playersUsing = new HashSet<PlayerEntity>();
    private static final Random random = new Random();

    public TileEntityIngotCompressor()
    {
        super(TYPE);
        inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.world.isRemote)
        {
            boolean updateInv = false;
            boolean flag = this.furnaceBurnTime > 0;

            if (this.furnaceBurnTime > 0)
            {
                --this.furnaceBurnTime;
            }

            if (this.furnaceBurnTime == 0 && this.canSmelt())
            {
                ItemStack fuel = this.getInventory().get(0);
                this.currentItemBurnTime = this.furnaceBurnTime = ForgeHooks.getBurnTime(fuel);

                if (this.furnaceBurnTime > 0)
                {
                    updateInv = true;

                    if (!fuel.isEmpty())
                    {
                        fuel.shrink(1);

                        if (fuel.getCount() == 0)
                        {
                            this.getInventory().set(0, fuel.getItem().getContainerItem(fuel));
                        }
                    }
                }
            }

            if (this.furnaceBurnTime > 0 && this.canSmelt())
            {
                ++this.processTicks;

                if (this.processTicks % 40 == 0 && this.processTicks > TileEntityIngotCompressor.PROCESS_TIME_REQUIRED / 2)
                {
                    this.world.playSound(null, this.getPos(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 0.3F, this.world.rand.nextFloat() * 0.1F + 0.9F);
                }

                if (this.processTicks == TileEntityIngotCompressor.PROCESS_TIME_REQUIRED)
                {
                    this.processTicks = 0;
                    this.smeltItem();
                    updateInv = true;
                }
            }
            else
            {
                this.processTicks = 0;
            }

            if (flag != this.furnaceBurnTime > 0)
            {
                updateInv = true;
            }

            if (updateInv)
            {
                this.markDirty();
            }
        }

        this.ticks++;
    }

    public void updateInput()
    {
        this.producingStack = CompressorRecipes.findMatchingRecipe(this.compressingCraftMatrix, this.world);
    }

    private boolean canSmelt()
    {
        ItemStack itemstack = this.producingStack;
        if (itemstack.isEmpty())
        {
            return false;
        }
        if (this.getInventory().get(1).isEmpty())
        {
            return true;
        }
        if (!this.getInventory().get(1).isItemEqual(itemstack))
        {
            return false;
        }
        int result = this.getInventory().get(1).getCount() + itemstack.getCount();
        return result <= this.getInventoryStackLimit() && result <= itemstack.getMaxStackSize();
    }

    public static boolean isItemCompressorInput(ItemStack stack)
    {
        for (IRecipe recipe : CompressorRecipes.getRecipeList())
        {
            if (recipe instanceof ShapedRecipesGC)
            {
                for (ItemStack itemstack1 : ((ShapedRecipesGC) recipe).recipeItems)
                {
                    if (stack.getItem() == itemstack1.getItem() && (itemstack1.getDamage() == 32767 || stack.getDamage() == itemstack1.getDamage()))
                    {
                        return true;
                    }
                }
            }
//            else if (recipe instanceof ShapelessOreRecipeGC)
//            {
//                ArrayList<Object> required = new ArrayList<Object>(((ShapelessOreRecipeGC) recipe).getInput());
//
//                Iterator<Object> req = required.iterator();
//
//                int match = 0;
//
//                while (req.hasNext())
//                {
//                    Object next = req.next();
//
//                    if (next instanceof ItemStack)
//                    {
//                        if (OreDictionary.itemMatches((ItemStack) next, stack, false))
//                        {
//                            match++;
//                        }
//                    }
//                    else if (next instanceof List)
//                    {
//                        for (ItemStack itemStack : ((List<ItemStack>) next))
//                        {
//                            if (OreDictionary.itemMatches(itemStack, stack, false))
//                            {
//                                match++;
//                                break;
//                            }
//                        }
//                    }
//                }
//
//                if (match == 0)
//                {
//                    continue;
//                }
//
//                if (match == 1)
//                {
//                    return true;
//                }
//
//                return random.nextInt(match) == 0;
//            } TODO OreDict recipes
        }

        return false;
    }

    private void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack resultItemStack = this.producingStack;
            if (ConfigManagerCore.quickMode.get())
            {
                if (resultItemStack.getItem().getTranslationKey(resultItemStack).contains("compressed"))
                {
                    resultItemStack.grow(resultItemStack.getCount());
                }
            }

            if (this.getInventory().get(1).isEmpty())
            {
                this.getInventory().set(1, resultItemStack.copy());
            }
            else if (this.getInventory().get(1).isItemEqual(resultItemStack))
            {
                if (this.getInventory().get(1).getCount() + resultItemStack.getCount() > 64)
                {
                    resultItemStack.grow(this.getInventory().get(1).getCount() - 64);
                    GCCoreUtil.spawnItem(this.world, this.getPos(), resultItemStack);
                    this.getInventory().get(1).setCount(64);
                }
                else
                {
                    this.getInventory().get(1).grow(resultItemStack.getCount());
                }
            }

            for (int i = 0; i < this.compressingCraftMatrix.getSizeInventory(); i++)
            {
                if (!this.compressingCraftMatrix.getStackInSlot(i).isEmpty() && this.compressingCraftMatrix.getStackInSlot(i).getItem() == Items.WATER_BUCKET)
                {
                    this.compressingCraftMatrix.setInventorySlotContentsNoUpdate(i, new ItemStack(Items.BUCKET));
                }
                else
                {
                    this.compressingCraftMatrix.decrStackSize(i, 1);
                }
            }
            this.updateInput();
        }
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        this.processTicks = nbt.getInt("smeltingTicks");
        ListNBT var2 = nbt.getList("Items", 10);

        this.inventory = NonNullList.withSize(this.getSizeInventory() - this.compressingCraftMatrix.getSizeInventory(), ItemStack.EMPTY);

        ListNBT nbttaglist = nbt.getList("Items", 10);

        for (int i = 0; i < nbttaglist.size(); ++i)
        {
            CompoundNBT nbttagcompound = nbttaglist.getCompound(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < this.getInventory().size())
            {
                this.getInventory().set(j, ItemStack.read(nbttagcompound));
            }
            else if (j < this.getInventory().size() + this.compressingCraftMatrix.getSizeInventory())
            {
                this.compressingCraftMatrix.setInventorySlotContents(j - this.getInventory().size(), ItemStack.read(nbttagcompound));
            }
        }

        this.updateInput();
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putInt("smeltingTicks", this.processTicks);
        ListNBT var2 = new ListNBT();
        int i;

        for (i = 0; i < this.getInventory().size(); ++i)
        {
            if (!this.getInventory().get(i).isEmpty())
            {
                CompoundNBT tagCompound = new CompoundNBT();
                tagCompound.putByte("Slot", (byte) i);
                this.getInventory().get(i).write(tagCompound);
                var2.add(tagCompound);
            }
        }

        for (i = 0; i < this.compressingCraftMatrix.getSizeInventory(); ++i)
        {
            if (!this.compressingCraftMatrix.getStackInSlot(i).isEmpty())
            {
                CompoundNBT tagCompound = new CompoundNBT();
                tagCompound.putByte("Slot", (byte) (i + this.getInventory().size()));
                this.compressingCraftMatrix.getStackInSlot(i).write(tagCompound);
                var2.add(tagCompound);
            }
        }

        nbt.put("Items", var2);
        return nbt;
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        return super.getSizeInventory() + this.compressingCraftMatrix.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        if (par1 >= this.getInventory().size())
        {
            return this.compressingCraftMatrix.getStackInSlot(par1 - this.getInventory().size());
        }

        return this.getInventory().get(par1);
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (par1 >= this.getInventory().size())
        {
            ItemStack result = this.compressingCraftMatrix.decrStackSize(par1 - this.getInventory().size(), par2);
            if (!result.isEmpty())
            {
                this.updateInput();
            }
            this.markDirty();
            return result;
        }

        if (!this.getInventory().get(par1).isEmpty())
        {
            ItemStack var3;

            if (this.getInventory().get(par1).getCount() <= par2)
            {
                var3 = this.getInventory().get(par1);
                this.getInventory().set(par1, ItemStack.EMPTY);
                this.markDirty();
                return var3;
            }
            else
            {
                var3 = this.getInventory().get(par1).split(par2);

                if (this.getInventory().get(par1).isEmpty())
                {
                    this.getInventory().set(par1, ItemStack.EMPTY);
                }

                this.markDirty();
                return var3;
            }
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int par1)
    {
        if (par1 >= this.getInventory().size())
        {
            this.markDirty();
            return this.compressingCraftMatrix.removeStackFromSlot(par1 - this.getInventory().size());
        }

        if (!this.getInventory().get(par1).isEmpty())
        {
            ItemStack var2 = this.getInventory().get(par1);
            this.getInventory().set(par1, ItemStack.EMPTY);
            this.markDirty();
            return var2;
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack stack)
    {
        if (par1 >= this.getInventory().size())
        {
            this.compressingCraftMatrix.setInventorySlotContents(par1 - this.getInventory().size(), stack);
            this.updateInput();
        }
        else
        {
            this.getInventory().set(par1, stack);

            if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
            {
                stack.setCount(this.getInventoryStackLimit());
            }
        }
        this.markDirty();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.getInventory())
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity par1EntityPlayer)
    {
        return this.world.getTileEntity(this.getPos()) == this && par1EntityPlayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
    {
        if (slotID == 0)
        {
            return ForgeHooks.getBurnTime(itemStack) > 0;
        }
        else if (slotID >= 2)
        {
            if (!this.producingStack.isEmpty())
            {
                ItemStack stackInSlot = this.getStackInSlot(slotID);
                return !stackInSlot.isEmpty() && stackInSlot.isItemEqual(itemStack);
            }
            return TileEntityIngotCompressor.isItemCompressorInput(itemStack);
        }

        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        if (side == Direction.DOWN)
        {
            return new int[]{1};
        }
        int[] slots = new int[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayList<Integer> removeSlots = new ArrayList<>();

        for (int i = 2; i < 11; i++)
        {
            if (removeSlots.contains(i))
            {
                continue;
            }
            ItemStack stack1 = this.getStackInSlot(i);
            if (stack1.isEmpty())
            {
                continue;
            }

            for (int j = i + 1; j < 11; j++)
            {
                if (removeSlots.contains(j))
                {
                    continue;
                }
                ItemStack stack2 = this.getStackInSlot(j);
                if (stack2.isEmpty())
                {
                    continue;
                }

                if (stack1.isItemEqual(stack2))
                {
                    if (stack2.getCount() >= stack1.getCount())
                    {
                        removeSlots.add(j);
                    }
                    else
                    {
                        removeSlots.add(i);
                    }
                    break;
                }
            }
        }

        if (removeSlots.size() > 0)
        {
            int[] returnSlots = new int[slots.length - removeSlots.size()];
            int j = 0;
            for (int i = 0; i < slots.length; i++)
            {
                if (i > 0 && removeSlots.contains(slots[i]))
                {
                    continue;
                }
                returnSlots[j] = slots[i];
                j++;
            }

            return returnSlots;
        }

        return slots;
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack par2ItemStack, Direction par3)
    {
        return this.isItemValidForSlot(slotID, par2ItemStack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack par2ItemStack, Direction par3)
    {
        return slotID == 1;
    }

    @Override
    public double getPacketRange()
    {
        return 12.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 3;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }

    @Override
    public Container createMenu(int containerId, PlayerInventory playerInv, PlayerEntity player)
    {
        return new ContainerIngotCompressor(containerId, playerInv, this);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("container.compressor");
    }
}
