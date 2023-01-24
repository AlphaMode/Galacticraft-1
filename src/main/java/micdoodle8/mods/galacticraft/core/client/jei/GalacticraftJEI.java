package micdoodle8.mods.galacticraft.core.client.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.jei.buggy.BuggyRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.buggy.BuggyRecipeMaker;
import micdoodle8.mods.galacticraft.core.client.jei.circuitfabricator.CircuitFabricatorRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.circuitfabricator.CircuitFabricatorRecipeMaker;
import micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor.IngotCompressorRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.refinery.RefineryRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.refinery.RefineryRecipeMaker;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeMaker;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class GalacticraftJEI implements IModPlugin {
    private static IRecipeRegistration registryCached = null;
    private static IRecipeManager recipesCached = null;

    private static boolean hiddenSteel = false;
    private static boolean hiddenAdventure = false;
    public static Map<?, ResourceLocation> hidden = new LinkedHashMap<>();
    private static IRecipeCategory ingotCompressorCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return GalacticraftCore.rl("jei");
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registry)
    {
        registryCached = registry;
        IStackHelper stackHelper = registry.getJeiHelpers().getStackHelper();

//        registry.handleRecipes(INasaWorkbenchRecipe.class, BuggyRecipeWrapper::new, RecipeCategories.BUGGY_ID);
//        registry.handleRecipes(CircuitFabricatorRecipeWrapper.class, recipe -> recipe, RecipeCategories.CIRCUIT_FABRICATOR_ID);
//        registry.handleRecipes(ShapedRecipesGC.class, IngotCompressorShapedRecipeWrapper::new, RecipeCategories.INGOT_COMPRESSOR_ID);
//        registry.handleRecipes(ShapelessOreRecipeGC.class, new IRecipeWrapperFactory<ShapelessOreRecipeGC>() {
//        	@Override public IRecipeWrapper getRecipeWrapper(ShapelessOreRecipeGC recipe) { return new IngotCompressorShapelessRecipeWrapper(stackHelper, recipe); }
//        		}, RecipeCategories.INGOT_COMPRESSOR_ID);
//        registry.handleRecipes(RefineryRecipeWrapper.class, recipe -> recipe, RecipeCategories.REFINERY_ID);
//        registry.handleRecipes(OxygenCompressorRecipeWrapper.class, recipe -> recipe, RecipeCategories.OXYGEN_COMPRESSOR_ID);
//        registry.handleRecipes(ShapedRecipeNBT.class, NBTSensitiveShapedRecipeWrapper::new, VanillaRecipeCategoryUid.CRAFTING);

        registry.addRecipes(Tier1RocketRecipeMaker.getRecipesList(), RecipeCategories.ROCKET_T1_ID);
        registry.addRecipes(BuggyRecipeMaker.getRecipesList(), RecipeCategories.BUGGY_ID);
        registry.addRecipes(CircuitFabricatorRecipeMaker.getRecipesList(), RecipeCategories.CIRCUIT_FABRICATOR_ID);
        registry.addRecipes(CompressorRecipes.getRecipeListAll(), RecipeCategories.INGOT_COMPRESSOR_ID);
//        registry.addRecipes(OxygenCompressorRecipeMaker.getRecipesList(), RecipeCategories.OXYGEN_COMPRESSOR_ID);
        registry.addRecipes(RefineryRecipeMaker.getRecipesList(), RecipeCategories.REFINERY_ID);

        this.addInformationPages(registry);
//        GCItems.hideItemsJEI(registry.getJeiHelpers().getIngredientBlacklist());
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registry) {
        registry.addRecipeTransferHandler(new MagneticCraftingTransferInfo());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.nasaWorkbench), RecipeCategories.ROCKET_T1_ID, RecipeCategories.BUGGY_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.circuitFabricator), RecipeCategories.CIRCUIT_FABRICATOR_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.ingotCompressor), RecipeCategories.INGOT_COMPRESSOR_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.ingotCompressorElectric), RecipeCategories.INGOT_COMPRESSOR_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.oxygenCompressor), RecipeCategories.OXYGEN_COMPRESSOR_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.refinery), RecipeCategories.REFINERY_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.crafting), VanillaRecipeCategoryUid.CRAFTING);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        ingotCompressorCategory = new IngotCompressorRecipeCategory(guiHelper);
        registry.addRecipeCategories(new Tier1RocketRecipeCategory(guiHelper),
                new BuggyRecipeCategory(guiHelper),
                new CircuitFabricatorRecipeCategory(guiHelper),
                ingotCompressorCategory,
//                new OxygenCompressorRecipeCategory(guiHelper),
                new RefineryRecipeCategory(guiHelper));
    }

    private void addInformationPages(IRecipeRegistration registry)
    {
//        registry.addIngredientInfo(new ItemStack(GCBlocks.oxygenPipe), VanillaTypes.ITEM, GCCoreUtil.translate("jei.fluid_pipe.info"));
        registry.addIngredientInfo(new ItemStack(GCBlocks.fuelLoader), VanillaTypes.ITEM, GCCoreUtil.translate("jei.fuel_loader.info"));
        registry.addIngredientInfo(new ItemStack(GCBlocks.oxygenCollector), VanillaTypes.ITEM, GCCoreUtil.translate("jei.oxygen_collector.info"));
        registry.addIngredientInfo(new ItemStack(GCBlocks.oxygenDistributor), VanillaTypes.ITEM, GCCoreUtil.translate("jei.oxygen_distributor.info"));
        registry.addIngredientInfo(new ItemStack(GCBlocks.oxygenSealer), VanillaTypes.ITEM, GCCoreUtil.translate("jei.oxygen_sealer.info"));
        if (CompatibilityManager.isAppEngLoaded())
        {
//            registry.addIngredientInfo(new ItemStack(GCBlocks.machineBase2), ItemStack.class, new String [] { GCCoreUtil.translate("jei.electric_compressor.info"), GCCoreUtil.translate("jei.electric_compressor.appeng.info") });
        }
        else
        {
//            registry.addIngredientInfo(new ItemStack(GCBlocks.machineBase2), ItemStack.class, GCCoreUtil.translate("jei.electric_compressor.info"));
        }
        registry.addIngredientInfo(new ItemStack(GCBlocks.crafting), VanillaTypes.ITEM, GCCoreUtil.translate("jei.magnetic_crafting.info"));
//        registry.addIngredientInfo(new ItemStack(GCBlocks.brightLamp), ItemStack.class, GCCoreUtil.translate("jei.arc_lamp.info"));
        registry.addIngredientInfo(new ItemStack(GCItems.wrench), VanillaTypes.ITEM, GCCoreUtil.translate("jei.wrench.info"));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime rt)
    {
        recipesCached = rt.getRecipeManager();
    }

    public static void updateHidden(boolean hideSteel, boolean hideAdventure)
    {
        boolean changeHidden = false;
        if (hideSteel != hiddenSteel)
        {
            hiddenSteel = hideSteel;
            changeHidden = true;
        }
        if (hideAdventure != hiddenAdventure)
        {
            hiddenAdventure = hideAdventure;
            changeHidden = true;
        }
        if (changeHidden && recipesCached != null)
        {
            unhide();
            List<IRecipe> toHide = CompressorRecipes.getRecipeListHidden(hideSteel, hideAdventure);
            hidden.clear();
            List<?> allRW = recipesCached.getRecipes(ingotCompressorCategory);
            for (IRecipe recipe : toHide)
            {
//                hidden.add(recipesCached.getRecipeWrapper(recipe, RecipeCategories.INGOT_COMPRESSOR_ID));
            }
            hide();
        }
    }

    private static void hide()
    {
        for (Map.Entry<?, ResourceLocation> wrapper : hidden.entrySet())
        {
            recipesCached.hideRecipe(wrapper.getKey(), wrapper.getValue());
        }
    }

    private static void unhide()
    {
        for (Map.Entry<?, ResourceLocation> wrapper : hidden.entrySet())
        {
            recipesCached.unhideRecipe(wrapper.getKey(), wrapper.getValue());
        }
    }
}
