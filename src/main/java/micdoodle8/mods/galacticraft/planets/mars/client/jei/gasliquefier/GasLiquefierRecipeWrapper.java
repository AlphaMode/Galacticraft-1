package micdoodle8.mods.galacticraft.planets.mars.client.jei.gasliquefier;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class GasLiquefierRecipeWrapper
{
    @Nonnull
    public final ItemStack input;
    @Nonnull
    public final ItemStack output;

    public GasLiquefierRecipeWrapper(@Nonnull ItemStack input, @Nonnull ItemStack output)
    {
        this.input = input;
        this.output = output;
    }
}