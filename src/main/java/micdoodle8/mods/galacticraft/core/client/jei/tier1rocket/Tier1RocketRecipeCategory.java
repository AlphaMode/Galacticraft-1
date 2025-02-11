package micdoodle8.mods.galacticraft.core.client.jei.tier1rocket;

import com.google.common.collect.Lists;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class Tier1RocketRecipeCategory implements IRecipeCategory<INasaWorkbenchRecipe>
{
    private static final ResourceLocation rocketGuiTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/rocketbench.png");

    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;

    public Tier1RocketRecipeCategory(IGuiHelper guiHelper)
    {
        this.background = guiHelper.createDrawable(rocketGuiTexture, 3, 4, 168, 130);
        this.localizedName = GCCoreUtil.translate("tile.rocket_workbench");

    }

    @Nonnull
    @Override
    public ResourceLocation getUid()
    {
        return RecipeCategories.ROCKET_T1_ID;
    }

    @Override
    public Class<INasaWorkbenchRecipe> getRecipeClass() {
        return INasaWorkbenchRecipe.class;
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
        return null;
    }

    @Override
    public void setIngredients(INasaWorkbenchRecipe recipe, IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, Lists.newArrayList(recipe.getRecipeInput().values()));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, INasaWorkbenchRecipe recipe, IIngredients ingredients)
    {
        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();

        itemstacks.init(0, true, 44, 14);
        itemstacks.init(1, true, 35, 32);
        itemstacks.init(2, true, 35, 50);
        itemstacks.init(3, true, 35, 68);
        itemstacks.init(4, true, 35, 86);
        itemstacks.init(5, true, 53, 32);
        itemstacks.init(6, true, 53, 50);
        itemstacks.init(7, true, 53, 68);
        itemstacks.init(8, true, 53, 86);
        itemstacks.init(9, true, 17, 104);
        itemstacks.init(10, true, 17, 86);
        itemstacks.init(11, true, 44, 104);
        itemstacks.init(12, true, 71, 86);
        itemstacks.init(13, true, 71, 104);
        itemstacks.init(14, true, 89, 7);
        itemstacks.init(15, true, 115, 7);
        itemstacks.init(16, true, 141, 7);
        itemstacks.init(17, false, 138, 91);

        itemstacks.set(ingredients);
    }
}
