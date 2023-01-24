package micdoodle8.mods.galacticraft.core.data;

import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class GCReciperGenerator extends RecipeProvider {
    public GCReciperGenerator(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(GCItems.canvas).key('X', Tags.Items.STRING).key('Y', Tags.Items.RODS_WOODEN)
                .patternLine(" XY")
                .patternLine("XXX")
                .patternLine("YX ")
                .build(consumer);
    }
}
