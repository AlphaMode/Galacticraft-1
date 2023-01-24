package micdoodle8.mods.galacticraft.planets.venus.dimension;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import static micdoodle8.mods.galacticraft.core.GCBlocks.register;

public class VenusBiomeProviderTypes
{
    public static final DeferredRegister<BiomeProviderType<?, ?>> BIOME_PROVIDER_TYPES = new DeferredRegister<>(ForgeRegistries.BIOME_PROVIDER_TYPES, Constants.MOD_ID_PLANETS);

    public static final BiomeProviderType<VenusBiomeProviderSettings, VenusBiomeProvider> VENUS_TYPE = new BiomeProviderType<>(VenusBiomeProvider::new, VenusBiomeProviderSettings::new);


//    @SubscribeEvent
//    public static void registerBiomeTypes(RegistryEvent.Register<BiomeProviderType<?, ?>> evt)
//    {
//        IForgeRegistry<BiomeProviderType<?, ?>> r = evt.getRegistry();
//        register(r, "venus_biome_provider", VENUS_TYPE);
//    }

//    public static final RegistryObject<BiomeProviderType<VenusBiomeProviderSettings, VenusBiomeProvider>> VENUS = register(
//            "venus_biome_provider_type", () -> {
//
//            });
}
