package micdoodle8.mods.galacticraft.planets.mars.client.jei.tier2rocket;

import com.google.common.collect.Lists;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class Tier2RocketRecipeCategory implements IRecipeCategory<INasaWorkbenchRecipe>
{
    private static final ResourceLocation rocketGuiTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/schematic_rocket_t2_recipe.png");

    @Nonnull
    private final IDrawable icon;
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;

    public Tier2RocketRecipeCategory(IGuiHelper guiHelper)
    {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(GCBlocks.nasaWorkbench));
        this.background = guiHelper.createDrawable(rocketGuiTexture, 0, 0, 168, 126);
        this.localizedName = GCCoreUtil.translate("tile.rocket_workbench");

    }

    @Nonnull
    @Override
    public ResourceLocation getUid()
    {
        return RecipeCategories.ROCKET_T2_ID;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return this.localizedName;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return this.background;
    }

    @Override
    public Class<INasaWorkbenchRecipe> getRecipeClass() {
        return INasaWorkbenchRecipe.class;
    }

    @Override
    public void setIngredients(INasaWorkbenchRecipe recipe, IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, Lists.newArrayList(recipe.getRecipeInput().values()));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, INasaWorkbenchRecipe recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();

        itemstacks.init(0, true, 44, 0);
        itemstacks.init(1, true, 35, 18);
        itemstacks.init(2, true, 35, 36);
        itemstacks.init(3, true, 35, 54);
        itemstacks.init(4, true, 35, 72);
        itemstacks.init(5, true, 35, 90);
        itemstacks.init(6, true, 53, 18);
        itemstacks.init(7, true, 53, 36);
        itemstacks.init(8, true, 53, 54);
        itemstacks.init(9, true, 53, 72);
        itemstacks.init(10, true, 53, 90);
        itemstacks.init(11, true, 17, 72); // Booster left
        itemstacks.init(12, true, 17, 90);
        itemstacks.init(13, true, 71, 90);
        itemstacks.init(14, true, 44, 108); // Rocket
        itemstacks.init(15, true, 71, 72); // Booster right
        itemstacks.init(16, true, 17, 108);
        itemstacks.init(17, true, 71, 108);
        itemstacks.init(18, true, 89, 7);
        itemstacks.init(19, true, 115, 7);
        itemstacks.init(20, true, 141, 7);
        itemstacks.init(21, false, 138, 95);

        itemstacks.set(ingredients);
    }
}
