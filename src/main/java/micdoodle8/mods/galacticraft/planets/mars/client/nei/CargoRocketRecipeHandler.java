//package micdoodle8.mods.galacticraft.planets.mars.client.nei;
//
//import codechicken.lib.gui.GuiDraw;
//import codechicken.nei.api.stack.PositionedStack;
//import codechicken.nei.recipe.TemplateRecipeHandler;
//import codechicken.nei.util.NEIServerUtils;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ResourceLocation;
//import org.lwjgl.opengl.GL11;
//
//import java.util.ArrayList;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
//public class CargoRocketRecipeHandler extends TemplateRecipeHandler
//{
//    private static final ResourceLocation cargoRocketTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/schematic_rocket_cargo.png");
//
//    public String getRecipeId()
//    {
//        return "galacticraft.cargoRocket";
//    }
//
//    @Override
//    public int recipiesPerPage()
//    {
//        return 1;
//    }
//
//    public Set<Entry<ArrayList<PositionedStack>, PositionedStack>> getRecipes()
//    {
//        return NEIGalacticraftMarsConfig.getCargoBenchRecipes();
//    }
//
//    @Override
//    public void drawBackground(int recipe)
//    {
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GuiDraw.changeTexture(CargoRocketRecipeHandler.cargoRocketTexture);
//        GuiDraw.blit(0, 0, 3, 4, 168, 125);
//    }
//
//    @Override
//    public void loadTransferRects()
//    {
//    }
//
//    @Override
//    public void loadCraftingRecipes(String outputId, Object... results)
//    {
//        if (outputId.equals(this.getRecipeId()))
//        {
//            for (final Map.Entry<ArrayList<PositionedStack>, PositionedStack> irecipe : this.getRecipes())
//            {
//                this.arecipes.add(new CachedRocketRecipe(irecipe));
//            }
//        }
//        else
//        {
//            super.loadCraftingRecipes(outputId, results);
//        }
//    }
//
//    @Override
//    public void loadCraftingRecipes(ItemStack result)
//    {
//        for (final Map.Entry<ArrayList<PositionedStack>, PositionedStack> irecipe : this.getRecipes())
//        {
//            if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getValue().item, result))
//            {
//                this.arecipes.add(new CachedRocketRecipe(irecipe));
//            }
//        }
//    }
//
//    @Override
//    public void loadUsageRecipes(ItemStack ingredient)
//    {
//        for (final Map.Entry<ArrayList<PositionedStack>, PositionedStack> irecipe : this.getRecipes())
//        {
//            for (final PositionedStack pstack : irecipe.getKey())
//            {
//                if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, pstack.item))
//                {
//                    this.arecipes.add(new CachedRocketRecipe(irecipe));
//                    break;
//                }
//            }
//        }
//    }
//
//    public class CachedRocketRecipe extends TemplateRecipeHandler.CachedRecipe
//    {
//        public ArrayList<PositionedStack> input;
//        public PositionedStack output;
//
//        @Override
//        public ArrayList<PositionedStack> getIngredients()
//        {
//            return this.input;
//        }
//
//        @Override
//        public PositionedStack getResult()
//        {
//            return this.output;
//        }
//
//        public CachedRocketRecipe(ArrayList<PositionedStack> pstack1, PositionedStack pstack2)
//        {
//            super();
//            this.input = pstack1;
//            this.output = pstack2;
//        }
//
//        public CachedRocketRecipe(Map.Entry<ArrayList<PositionedStack>, PositionedStack> recipe)
//        {
//            this(recipe.getKey(), recipe.getValue());
//        }
//    }
//
//    @Override
//    public String getRecipeName()
//    {
//        return GCCoreUtil.translate("tile.rocket_workbench");
//    }
//
//    @Override
//    public String getGuiTexture()
//    {
//        return GalacticraftPlanets.TEXTURE_PREFIX + "textures/gui/schematic_rocket_cargo.png";
//    }
//
//    @Override
//    public void drawForeground(int recipe)
//    {
//    }
//}
