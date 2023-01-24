package micdoodle8.mods.galacticraft.core.data;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GCDataGenerators {
    @SubscribeEvent
    public static void generateData(GatherDataEvent event) {
        ExistingFileHelper helper = event.getExistingFileHelper();
        DataGenerator gen = event.getGenerator();

        gen.addProvider(new GCBlockStateGenerator(gen, helper));
    }
}
