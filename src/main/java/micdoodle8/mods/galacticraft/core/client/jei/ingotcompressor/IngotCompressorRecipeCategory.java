package micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor;

import java.util.List;

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
import micdoodle8.mods.galacticraft.core.client.jei.GalacticraftJEI;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class IngotCompressorRecipeCategory implements IRecipeCategory<IRecipe>
{
    private static final ResourceLocation compressorTex = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/ingot_compressor.png");
    private static final ResourceLocation compressorTexBlank = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/ingot_compressor_blank.png");

    @Nonnull
    private final IDrawable icon;
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final IDrawable backgroundBlank;
    @Nonnull
    private final String localizedName;
    @Nonnull
    private final IDrawableAnimated progressBar;

    private boolean drawNothing = false;

    public IngotCompressorRecipeCategory(IGuiHelper guiHelper)
    {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(GCBlocks.ingotCompressor));
        this.background = guiHelper.createDrawable(compressorTex, 18, 17, 137, 78);
        this.backgroundBlank = guiHelper.createDrawable(compressorTexBlank, 18, 17, 137, 78);
        this.localizedName = GCCoreUtil.translate("tile.machine.3");

        IDrawableStatic progressBarDrawable = guiHelper.createDrawable(compressorTex, 176, 13, 52, 17);
        this.progressBar = guiHelper.createAnimatedDrawable(progressBarDrawable, 70, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Nonnull
    @Override
    public ResourceLocation getUid()
    {
        return RecipeCategories.INGOT_COMPRESSOR_ID;
    }

    @Override
    public Class<? extends IRecipe> getRecipeClass() {
        return IRecipe.class;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return this.localizedName;
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        if (this.drawNothing)
        {
            return this.backgroundBlank;
        }
        return this.background;
    }

    @Override
    public void draw(@Nonnull IRecipe recipe, double mouseX, double mouseY)
    {
        if (!this.drawNothing) this.progressBar.draw(59, 19);
    }

    @Override
    public void setIngredients(IRecipe recipe, IIngredients ingredients) {

    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipe recipeWrapper, IIngredients ingredients)
    {
//        this.drawNothing = GalacticraftJEI.hidden.contains(recipeWrapper);
        if (this.drawNothing) return;

        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();

        for (int j = 0; j < 9; j++)
        {
            itemstacks.init(j, true, j % 3 * 18, j / 3 * 18);
        }

        itemstacks.init(9, false, 119, 20);

        if (ConfigManagerCore.quickMode.get())
        {
            List<ItemStack> output = ingredients.getOutputs(VanillaTypes.ITEM).get(0);
            ItemStack stackOutput = output.get(0);
            if (stackOutput.getTranslationKey().contains("compressed"))
            {
                ItemStack stackDoubled = stackOutput.copy();
                stackDoubled.setCount(stackOutput.getCount() * 2);
                output.set(0, stackDoubled);
            }
        }

        itemstacks.set(ingredients);
    }
}
