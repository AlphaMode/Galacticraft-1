package micdoodle8.mods.galacticraft.core.world.gen;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class MoonVillageConfiguration implements IFeatureConfig {
    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic<>(ops);
    }

    public static <T> MoonVillageConfiguration deserialize(Dynamic<T> dynamic) {
        return new MoonVillageConfiguration();
    }
}
