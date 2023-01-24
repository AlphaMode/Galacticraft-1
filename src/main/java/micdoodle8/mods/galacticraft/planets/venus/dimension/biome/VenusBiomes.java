package micdoodle8.mods.galacticraft.planets.venus.dimension.biome;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.MapGenCaveVenus;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusSurfaceBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VenusBiomes {
    public static final MapGenCaveVenus VENUS_CAVE_CARVER = new MapGenCaveVenus(ProbabilityConfig::deserialize, 256);
    public static final SurfaceBuilderConfig VENUS_SURFACE = new SurfaceBuilderConfig(VenusBlocks.rockHard.getDefaultState(), VenusBlocks.rockSoft.getDefaultState(), Blocks.AIR.getDefaultState());
    public static final SurfaceBuilderConfig VENUS_VALLY_SURFACE = new SurfaceBuilderConfig(VenusBlocks.rockHard.getDefaultState(), VenusBlocks.rockSoft.getDefaultState(), VenusBlocks.rockVolcanicDeposit.getDefaultState());
    public static final VenusSurfaceBuilder VENUS_SURFACE_BUILDER = new VenusSurfaceBuilder(SurfaceBuilderConfig::deserialize);

    public static final Biome VENUS_FLAT = new BiomeVenus((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, VenusBiomes.VENUS_SURFACE).precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.5F).scale(0.4F).temperature(4.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null));
    public static final Biome VENUS_MOUNTAIN = new BiomeVenus((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, VenusBiomes.VENUS_SURFACE).precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(2.0F).scale(1.0F).temperature(4.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null));
    public static final Biome VENUS_VALLEY = new VenusVallyBiome((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, VenusBiomes.VENUS_VALLY_SURFACE).precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(-0.4F).scale(0.2F).temperature(4.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null));

    @SubscribeEvent
    public static void registerSurfaceBuilders(RegistryEvent.Register<SurfaceBuilder<?>> event) {
        GCBlocks.register(event.getRegistry(), VENUS_SURFACE_BUILDER, "venus");
    }

    @SubscribeEvent
    public static void registerWorldCarvers(RegistryEvent.Register<WorldCarver<?>> event) {
        GCBlocks.register(event.getRegistry(), VENUS_CAVE_CARVER, "venus_caves");
    }
}
