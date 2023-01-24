package micdoodle8.mods.galacticraft.core.data;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockSpaceGlass;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;

public class GCBlockStateGenerator extends BlockStateProvider {

    public GCBlockStateGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Constants.MOD_ID_CORE, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        VariantBlockStateBuilder variantBlockStateBuilder = getVariantBuilder(GCBlocks.spaceGlassClear).forAllStates(blockState -> {
            ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
            BlockSpaceGlass.GlassRotation rotation = blockState.get(BlockSpaceGlass.ROTATION);
            BlockSpaceGlass.GlassModel model = blockState.get(BlockSpaceGlass.MODEL);

            builder.rotationX(rotation.getX());
            builder.rotationY(rotation.getY());


            switch (model) {
                case STANDARD_PANE:
                    builder.modelFile(models().getExistingFile(new ResourceLocation("galacticraftcore:block/glass/glass_ns")));
                    break;
                case STANDARD_S1:
                    builder.modelFile(models().getExistingFile(new ResourceLocation("galacticraftcore:block/glass/glass_ns_s1")));
                    break;
                case STANDARD_S1B:
                    builder.modelFile(models().getExistingFile(new ResourceLocation("galacticraftcore:block/glass/glass_ns_s1b")));
                    break;
                case STANDARD_S1BB:
                    builder.modelFile(models().getExistingFile(new ResourceLocation("galacticraftcore:block/glass/glass_ns_s1bb")));
                    break;
                case STANDARD_S2:
                    builder.modelFile(models().getExistingFile(new ResourceLocation("galacticraftcore:block/glass/glass_ns_s2")));
                    break;
                case CORNER:
                    builder.modelFile(models().getExistingFile(new ResourceLocation("galacticraftcore:block/glass/glass_ne")));
                    break;
                default:
                    builder.modelFile(models().getExistingFile(new ResourceLocation("galacticraftcore:block/glass/glass_ne")));
                    break;
            }
            builder.nextModel();
            return builder.build();
        });
    }
}
