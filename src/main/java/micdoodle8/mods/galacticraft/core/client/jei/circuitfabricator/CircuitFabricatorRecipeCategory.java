package micdoodle8.mods.galacticraft.core.client.jei.circuitfabricator;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.*;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class CircuitFabricatorRecipeCategory implements IRecipeCategory<CircuitFabricatorRecipeWrapper>
{
    private static final ResourceLocation circuitFabTex = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/circuit_fabricator.png");

    @Nonnull
    private final IDrawable icon;
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;
    @Nonnull
    private final IDrawableAnimated progressBar;

    public CircuitFabricatorRecipeCategory(IGuiHelper guiHelper)
    {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(GCBlocks.circuitFabricator));
        this.background = guiHelper.createDrawable(circuitFabTex, 3, 4, 168, 101);
        this.localizedName = GCCoreUtil.translate("tile.machine2.5");

        IDrawableStatic progressBarDrawable = guiHelper.createDrawable(circuitFabTex, 176, 17, 51, 10);
        this.progressBar = guiHelper.createAnimatedDrawable(progressBarDrawable, 70, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Nonnull
    @Override
    public ResourceLocation getUid()
    {
        return RecipeCategories.CIRCUIT_FABRICATOR_ID;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return this.localizedName;
    }

    @Override
    public Class<CircuitFabricatorRecipeWrapper> getRecipeClass() {
        return CircuitFabricatorRecipeWrapper.class;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(CircuitFabricatorRecipeWrapper recipe, IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, recipe.getInput());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.output);
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return this.background;
    }

    @Override
    public void draw(CircuitFabricatorRecipeWrapper wrapper, double mouseX, double mouseY)
    {
        this.progressBar.draw(85, 16);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CircuitFabricatorRecipeWrapper recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();

        itemstacks.init(0, true, 11, 12);
        itemstacks.init(1, true, 70, 41);
        itemstacks.init(2, true, 70, 59);
        itemstacks.init(3, true, 118, 41);
        itemstacks.init(4, true, 141, 15);
        itemstacks.init(5, false, 148, 81);
        itemstacks.set(ingredients);
    }
}
