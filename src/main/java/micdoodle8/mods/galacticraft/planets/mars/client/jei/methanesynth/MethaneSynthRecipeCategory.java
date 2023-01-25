package micdoodle8.mods.galacticraft.planets.mars.client.jei.methanesynth;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class MethaneSynthRecipeCategory implements IRecipeCategory<MethaneSynthRecipeWrapper>
{
    private static final ResourceLocation refineryGuiTex = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/methane_synthesizer_recipe.png");
    private static final ResourceLocation gasesTex = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/gases_methane_oxygen_nitrogen.png");

    @Nonnull
    private final IDrawable background, icon;
    @Nonnull
    private final String localizedName;
    @Nonnull
    private final IDrawableAnimated hydrogenBarInput;
    @Nonnull
    private final IDrawableAnimated carbonDioxideBarInput;
    @Nonnull
    private final IDrawableAnimated methaneBarOutput;

    boolean fillAtmos = false;

    public MethaneSynthRecipeCategory(IGuiHelper guiHelper)
    {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(MarsBlocks.methaneSynthesizer));
        this.background = guiHelper.createDrawable(refineryGuiTex, 3, 4, 168, 66);
        this.localizedName = GCCoreUtil.translate("tile.mars_machine.5");

        IDrawableStatic hydrogenBar = guiHelper.createDrawable(gasesTex, 35, 0, 16, 38);
        this.hydrogenBarInput = guiHelper.createAnimatedDrawable(hydrogenBar, 70, IDrawableAnimated.StartDirection.TOP, true);
        IDrawableStatic carbonDioxideBar = guiHelper.createDrawable(gasesTex, 35, 0, 16, 20);
        this.carbonDioxideBarInput = guiHelper.createAnimatedDrawable(carbonDioxideBar, 70, IDrawableAnimated.StartDirection.TOP, true);
        IDrawableStatic methaneBar = guiHelper.createDrawable(gasesTex, 1, 0, 16, 38);
        this.methaneBarOutput = guiHelper.createAnimatedDrawable(methaneBar, 70, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Nonnull
    @Override
    public ResourceLocation getUid()
    {
        return RecipeCategories.METHANE_SYNTHESIZER_ID;
    }

    @Override
    public Class<MethaneSynthRecipeWrapper> getRecipeClass() {
        return MethaneSynthRecipeWrapper.class;
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
    public void draw(MethaneSynthRecipeWrapper recipe, double mouseX, double mouseY) {
        if (this.fillAtmos)
        {
            this.hydrogenBarInput.draw(29, 24);
            this.carbonDioxideBarInput.draw(50, 24);
        }
        else
        {
            this.hydrogenBarInput.draw(29, 24);
        }
        this.methaneBarOutput.draw(114, 24);
    }

    @Override
    public void setIngredients(MethaneSynthRecipeWrapper recipe, IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, recipe.input);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.output);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MethaneSynthRecipeWrapper recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();

        itemstacks.init(0, true, 28, 2);
        itemstacks.init(1, true, 49, 2);
        itemstacks.init(2, true, 49, 48);
        itemstacks.init(3, false, 113, 2);

        if (recipeWrapper instanceof MethaneSynthRecipeWrapper)
        {
            MethaneSynthRecipeWrapper gasLiquefierRecipeWrapper = (MethaneSynthRecipeWrapper) recipeWrapper;
            List<ItemStack> input = ingredients.getInputs(VanillaTypes.ITEM).get(0);

            Item inputItem = input.get(0).getItem();
            if (inputItem == AsteroidsItems.atmosphericValve)
            {
                this.fillAtmos = true;
                itemstacks.set(1, input);
            }
            else
            {
                this.fillAtmos = false;
                itemstacks.set(2, input);
            }

            itemstacks.set(3, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
        }
    }
}
