package micdoodle8.mods.galacticraft.planets.mars.client.jei.gasliquefier;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GasLiquefierRecipeMaker
{
    public static List<GasLiquefierRecipeWrapper> getRecipesList()
    {
        List<GasLiquefierRecipeWrapper> recipes = new ArrayList<>();

        recipes.add(new GasLiquefierRecipeWrapper(new ItemStack(AsteroidsItems.methaneCanister), new ItemStack(GCItems.fuelCanister)));
        recipes.add(new GasLiquefierRecipeWrapper(new ItemStack(AsteroidsItems.atmosphericValve), new ItemStack(AsteroidsItems.canisterLN2)));
        recipes.add(new GasLiquefierRecipeWrapper(new ItemStack(AsteroidsItems.atmosphericValve), new ItemStack(AsteroidsItems.canisterLOX)));
        recipes.add(new GasLiquefierRecipeWrapper(new ItemStack(AsteroidsItems.canisterLN2), new ItemStack(AsteroidsItems.canisterLN2)));
        recipes.add(new GasLiquefierRecipeWrapper(new ItemStack(AsteroidsItems.canisterLOX), new ItemStack(AsteroidsItems.canisterLOX)));

        return recipes;
    }
}
