package micdoodle8.mods.galacticraft.planets.mars.client.fx;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_PLANETS)
public class MarsParticles
{
    @ObjectHolder(MarsParticleTypeNames.drip)
    public static BasicParticleType DRIP;
    @ObjectHolder(MarsParticleTypeNames.cryo)
    public static BasicParticleType CRYO;

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name)
    {
        reg.register(thing.setRegistryName(name));
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name)
    {
        register(reg, thing, new ResourceLocation(Constants.MOD_ID_PLANETS, name));
    }

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> evt)
    {
        IForgeRegistry<ParticleType<?>> r = evt.getRegistry();

        register(r, new BasicParticleType(false), MarsParticleTypeNames.drip);
        register(r, new BasicParticleType(false), MarsParticleTypeNames.cryo);
    }
}
