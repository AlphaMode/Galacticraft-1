package micdoodle8.mods.galacticraft.core.client.jei.refinery;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RefineryRecipeCategory implements IRecipeCategory<RefineryRecipeWrapper>
{
    private static final ResourceLocation refineryGuiTex = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/refinery_recipe.png");

    @Nonnull
    private final IDrawable icon;
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;
    @Nonnull
    private final IDrawableAnimated oilBar;
    @Nonnull
    private final IDrawableAnimated fuelBar;

    public RefineryRecipeCategory(IGuiHelper guiHelper)
    {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(GCBlocks.refinery));
        this.background = guiHelper.createDrawable(refineryGuiTex, 3, 4, 168, 64);
        this.localizedName = GCCoreUtil.translate("tile.refinery");

        IDrawableStatic progressBarDrawableOil = guiHelper.createDrawable(refineryGuiTex, 176, 0, 16, 38);
        this.oilBar = guiHelper.createAnimatedDrawable(progressBarDrawableOil, 70, IDrawableAnimated.StartDirection.TOP, true);
        IDrawableStatic progressBarDrawableFuel = guiHelper.createDrawable(refineryGuiTex, 192, 0, 16, 38);
        this.fuelBar = guiHelper.createAnimatedDrawable(progressBarDrawableFuel, 70, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Nonnull
    @Override
    public ResourceLocation getUid()
    {
        return RecipeCategories.REFINERY_ID;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return this.localizedName;
    }

    @Override
    public Class<RefineryRecipeWrapper> getRecipeClass() {
        return RefineryRecipeWrapper.class;
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
    public void draw(RefineryRecipeWrapper recipe, double mouseX, double mouseY)
    {
        this.oilBar.draw(40, 24);
        this.fuelBar.draw(114, 24);
    }

    @Override
    public void setIngredients(RefineryRecipeWrapper recipe, IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, recipe.input);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.output);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RefineryRecipeWrapper recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();

        itemstacks.init(0, true, 39, 2);
        itemstacks.init(1, false, 113, 2);

        itemstacks.set(ingredients);
    }
}
