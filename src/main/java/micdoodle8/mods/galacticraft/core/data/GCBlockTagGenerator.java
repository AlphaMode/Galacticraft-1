package micdoodle8.mods.galacticraft.core.data;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class GCBlockTagGenerator extends BlockTagsProvider {
    public GCBlockTagGenerator(DataGenerator p_i48256_1_) {
        super(p_i48256_1_);
    }

    @Override
    protected void registerTags() {
        getBuilder(new Tag<>(new ResourceLocation("forge", "storage_blocks/aluminum"))).add(GCBlocks.decoBlockAluminum);
        getBuilder(new Tag<>(new ResourceLocation("forge", "storage_blocks/copper"))).add(GCBlocks.decoBlockCopper);
        getBuilder(new Tag<>(new ResourceLocation("forge", "storage_blocks/silicon"))).add(GCBlocks.decoBlockSilicon);
        getBuilder(new Tag<>(new ResourceLocation("forge", "storage_blocks/tin"))).add(GCBlocks.decoBlockTin);
    }
}
