package micdoodle8.mods.galacticraft.core.client.jei.buggy;

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
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class BuggyRecipeCategory implements IRecipeCategory<INasaWorkbenchRecipe>
{
    private static final ResourceLocation buggyGuiTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/buggybench.png");

    @Nonnull
    private final IDrawable icon;
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;

    public BuggyRecipeCategory(IGuiHelper guiHelper)
    {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(GCBlocks.nasaWorkbench));
        this.background = guiHelper.createDrawable(buggyGuiTexture, 3, 4, 168, 130);
        this.localizedName = GCCoreUtil.translate("tile.rocket_workbench");

    }

    @Nonnull
    @Override
    public ResourceLocation getUid()
    {
        return RecipeCategories.BUGGY_ID;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return this.localizedName;
    }

    @Override
    public Class<INasaWorkbenchRecipe> getRecipeClass() {
        return INasaWorkbenchRecipe.class;
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
    public void setIngredients(INasaWorkbenchRecipe recipe, IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, Lists.newArrayList(recipe.getRecipeInput().values()));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, INasaWorkbenchRecipe recipe, IIngredients ingredients)
    {
        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();

        itemstacks.init(0, true, 35, 36);
        itemstacks.init(1, true, 53, 36);
        itemstacks.init(2, true, 71, 36);
        itemstacks.init(3, true, 35, 54);
        itemstacks.init(4, true, 71, 54);
        itemstacks.init(5, true, 53, 54);
        itemstacks.init(6, true, 35, 72);
        itemstacks.init(7, true, 53, 72);
        itemstacks.init(8, true, 71, 72);
        itemstacks.init(9, true, 35, 90);
        itemstacks.init(10, true, 53, 90);
        itemstacks.init(11, true, 71, 90);

        itemstacks.init(12, true, 17, 36);
        itemstacks.init(13, true, 17, 90);
        itemstacks.init(14, true, 89, 36);
        itemstacks.init(15, true, 89, 90);
        itemstacks.init(16, true, 89, 7);
        itemstacks.init(17, true, 115, 7);
        itemstacks.init(18, true, 141, 7);
        itemstacks.init(19, false, 138, 102);

        itemstacks.set(ingredients);
    }
}
