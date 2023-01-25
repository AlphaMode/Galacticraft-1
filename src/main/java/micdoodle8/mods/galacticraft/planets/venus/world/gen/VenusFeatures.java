package micdoodle8.mods.galacticraft.planets.venus.world.gen;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon.*;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VenusFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Constants.MOD_ID_PLANETS);

    public static final RegistryObject<MapGenDungeonVenus> VENUS_DUNGEON = register("venus_dungeon", () -> new MapGenDungeonVenus(DungeonConfigurationVenus::deserialize));
    public static final WorldGenVaporPool VAPOR_POOL = new WorldGenVaporPool(NoFeatureConfig::deserialize);
    public static final WorldGenLakesVenus VENUS_LAKES = new WorldGenLakesVenus(NoFeatureConfig::deserialize);
    public static IStructurePieceType CVENUS_DUNGEON_START = DungeonStartVenus::new;
    public static IStructurePieceType CVENUS_DUNGEON_CORRIDOR = CorridorVenus::new;
    public static IStructurePieceType CVENUS_DUNGEON_EMPTY = RoomEmptyVenus::new;
    public static IStructurePieceType CVENUS_DUNGEON_BOSS = RoomBossVenus::new;
    public static IStructurePieceType CVENUS_DUNGEON_TREASURE = RoomTreasureVenus::new;
    public static IStructurePieceType CVENUS_DUNGEON_SPAWNER = RoomSpawnerVenus::new;
    public static IStructurePieceType CVENUS_DUNGEON_CHEST = RoomChestVenus::new;
    public static IStructurePieceType CVENUS_DUNGEON_ENTRANCE = RoomEntranceVenus::new;

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
    {
//        Registry.register(Registry.STRUCTURE_PIECE, "venus_dungeon", CVENUS_DUNGEON_START);
//        Registry.register(Registry.STRUCTURE_PIECE, "venus_dungeon_corridor", CVENUS_DUNGEON_CORRIDOR);
//        Registry.register(Registry.STRUCTURE_PIECE, "venus_dungeon_empty_room", CVENUS_DUNGEON_CORRIDOR);
//        Registry.register(Registry.STRUCTURE_PIECE, "venus_dungeon_boss_room", CVENUS_DUNGEON_CORRIDOR);
//        Registry.register(Registry.STRUCTURE_PIECE, "venus_dungeon_treasure_room", CVENUS_DUNGEON_CORRIDOR);
//        Registry.register(Registry.STRUCTURE_PIECE, "venus_dungeon_spawner_room", CVENUS_DUNGEON_CORRIDOR);
//        Registry.register(Registry.STRUCTURE_PIECE, "venus_dungeon_chest_room", CVENUS_DUNGEON_CORRIDOR);
//        Registry.register(Registry.STRUCTURE_PIECE, "venus_dungeon_entrance_room", CVENUS_DUNGEON_CORRIDOR);

        GCBlocks.register(event.getRegistry(), VAPOR_POOL, "vapor_pool");
        GCBlocks.register(event.getRegistry(), VENUS_LAKES, "venus_lakes");
    }

    public static <T extends Feature<?>> RegistryObject<T> register(final String name, final Supplier<T> sup)
    {
        return FEATURES.register(name, sup);
    }
}
