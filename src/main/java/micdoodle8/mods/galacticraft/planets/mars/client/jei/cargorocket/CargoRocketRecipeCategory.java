package micdoodle8.mods.galacticraft.planets.mars.client.jei.cargorocket;

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

public class CargoRocketRecipeCategory implements IRecipeCategory<INasaWorkbenchRecipe>
{
    private static final ResourceLocation rocketGuiTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/schematic_rocket_cargo.png");

    @Nonnull
    private final IDrawable background, icon;
    @Nonnull
    private final String localizedName;

    public CargoRocketRecipeCategory(IGuiHelper guiHelper)
    {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(GCBlocks.nasaWorkbench));
        this.background = guiHelper.createDrawable(rocketGuiTexture, 3, 4, 168, 125);
        this.localizedName = GCCoreUtil.translate("tile.rocket_workbench");

    }

    @Nonnull
    @Override
    public ResourceLocation getUid()
    {
        return RecipeCategories.ROCKET_CARGO_ID;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return this.localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
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

        itemstacks.init(0, true, 44, 13);
        itemstacks.init(1, true, 44, 31);
        itemstacks.init(2, true, 35, 49);
        itemstacks.init(3, true, 35, 67);
        itemstacks.init(4, true, 35, 85);
        itemstacks.init(5, true, 53, 49);
        itemstacks.init(6, true, 53, 67);
        itemstacks.init(7, true, 53, 85);
        itemstacks.init(8, true, 17, 85);
        itemstacks.init(9, true, 71, 85);
        itemstacks.init(10, true, 44, 103);
        itemstacks.init(11, true, 17, 103);
        itemstacks.init(12, true, 71, 103);
        itemstacks.init(13, true, 89, 7);
        itemstacks.init(14, true, 115, 7);
        itemstacks.init(15, true, 141, 7);
        itemstacks.init(16, false, 138, 91);

        itemstacks.set(ingredients);
    }
}
