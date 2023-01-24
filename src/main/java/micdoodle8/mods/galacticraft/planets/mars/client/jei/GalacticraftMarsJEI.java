package micdoodle8.mods.galacticraft.planets.mars.client.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.cargorocket.CargoRocketRecipeCategory;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.cargorocket.CargoRocketRecipeMaker;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.gasliquefier.GasLiquefierRecipeCategory;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.gasliquefier.GasLiquefierRecipeMaker;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.methanesynth.MethaneSynthRecipeCategory;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.methanesynth.MethaneSynthRecipeMaker;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.tier2rocket.Tier2RocketRecipeCategory;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.tier2rocket.Tier2RocketRecipeMaker;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@JeiPlugin
public class GalacticraftMarsJEI implements IModPlugin
{
    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registry)
    {

        registry.addRecipes(Tier2RocketRecipeMaker.getRecipesList(), RecipeCategories.ROCKET_T2_ID);
        registry.addRecipes(GasLiquefierRecipeMaker.getRecipesList(), RecipeCategories.GAS_LIQUEFIER_ID);
        registry.addRecipes(CargoRocketRecipeMaker.getRecipesList(), RecipeCategories.ROCKET_CARGO_ID);
        registry.addRecipes(MethaneSynthRecipeMaker.getRecipesList(), RecipeCategories.METHANE_SYNTHESIZER_ID);


    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.nasaWorkbench), RecipeCategories.ROCKET_T2_ID, RecipeCategories.ROCKET_CARGO_ID);
        registry.addRecipeCatalyst(new ItemStack(MarsBlocks.gasLiquefier), RecipeCategories.GAS_LIQUEFIER_ID);
        registry.addRecipeCatalyst(new ItemStack(MarsBlocks.methaneSynthesizer), RecipeCategories.METHANE_SYNTHESIZER_ID);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Constants.MOD_ID_PLANETS, "mars_jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new Tier2RocketRecipeCategory(guiHelper),
                new GasLiquefierRecipeCategory(guiHelper),
                new CargoRocketRecipeCategory(guiHelper),
                new MethaneSynthRecipeCategory(guiHelper));
    }
}
