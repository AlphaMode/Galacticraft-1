package micdoodle8.mods.galacticraft.planets.mars.client.jei.methanesynth;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class MethaneSynthRecipeWrapper
{
    @Nonnull
    public final ItemStack input;
    @Nonnull
    public final ItemStack output;

    public MethaneSynthRecipeWrapper(@Nonnull ItemStack input, @Nonnull ItemStack output)
    {
        this.input = input;
        this.output = output;
    }
}