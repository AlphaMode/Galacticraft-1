package micdoodle8.mods.galacticraft.core.client.jei.refinery;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class RefineryRecipeWrapper
{
    @Nonnull
    public final ItemStack input;
    @Nonnull
    public final ItemStack output;

    public RefineryRecipeWrapper(@Nonnull ItemStack input, @Nonnull ItemStack output)
    {
        this.input = input;
        this.output = output;
    }
}