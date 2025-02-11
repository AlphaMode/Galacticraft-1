package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.*;
import micdoodle8.mods.galacticraft.core.world.gen.placement.SapphirePlacement;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.feature.ReplaceBlockFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GCFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = new DeferredRegister<>(ForgeRegistries.FEATURES, Constants.MOD_ID_CORE);

    public static final MapGenVillageMoon MOON_VILLAGE = new MapGenVillageMoon(MoonVillageConfiguration::deserialize);
    public static final StructureDungeon MOON_DUNGEON = new StructureDungeon(DungeonConfiguration::deserialize);
    public static final CraterFeature MOON_CRATER = new CraterFeature(NoFeatureConfig::deserialize);
//    public static final CraterConfig SMALL_CRATERS = new CraterConfig(false);
    public static final CraterConfig LARGE_AND_SMALL_CRATERS = new CraterConfig(true);
    public static final Feature<ReplaceBlockConfig> SAPPHIRE_ORE = new ReplaceBlockFeature(ReplaceBlockConfig::deserialize);
    public static IStructurePieceType CMOON_DUNGEON_START = DungeonStart::new;
    public static IStructurePieceType CMOON_DUNGEON_CORRIDOR = Corridor::new;
    public static IStructurePieceType CMOON_DUNGEON_EMPTY = RoomEmpty::new;
    public static IStructurePieceType CMOON_DUNGEON_BOSS = RoomBoss::new;
    public static IStructurePieceType CMOON_DUNGEON_TREASURE = RoomTreasure::new;
    public static IStructurePieceType CMOON_DUNGEON_SPAWNER = RoomSpawner::new;
    public static IStructurePieceType CMOON_DUNGEON_CHEST = RoomChest::new;
    public static IStructurePieceType CMOON_DUNGEON_ENTRANCE = RoomEntrance::new;
    public static IStructurePieceType CMOON_VILLAGE_START = StructureComponentVillageStartPiece::new;
    public static IStructurePieceType CMOON_VILLAGE_FIELD = StructureComponentVillageField::new;
    public static IStructurePieceType CMOON_VILLAGE_FIELD_TWO = StructureComponentVillageField2::new;
    public static IStructurePieceType CMOON_VILLAGE_HOUSE = StructureComponentVillageHouse::new;
    public static IStructurePieceType CMOON_VILLAGE_PATH = StructureComponentVillagePathGen::new;
    public static IStructurePieceType CMOON_VILLAGE_TORCH = StructureComponentVillageTorch::new;
    public static IStructurePieceType CMOON_VILLAGE_WELL = StructureComponentVillageWell::new;
    public static IStructurePieceType CMOON_VILLAGE_WOOD_HUT = StructureComponentVillageWoodHut::new;

    public static final Placement<NoPlacementConfig> SAPPHIRE_ORE_PLACEMENT = new SapphirePlacement(NoPlacementConfig::deserialize);

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
    {
        GCBlocks.register(event.getRegistry(), MOON_CRATER, "moon_crater");
        GCBlocks.register(event.getRegistry(), MOON_VILLAGE, "moon_village");
        GCBlocks.register(event.getRegistry(), MOON_DUNGEON, "gc_dungeon");
        GCBlocks.register(event.getRegistry(), SAPPHIRE_ORE, "sapphire_ore");
        Registry.register(Registry.STRUCTURE_PIECE, "moon_dungeon_start", CMOON_DUNGEON_START);
        Registry.register(Registry.STRUCTURE_PIECE, "moon_dungeon_corridor", CMOON_DUNGEON_CORRIDOR);
        Registry.register(Registry.STRUCTURE_PIECE, "moon_dungeon_empty_room", CMOON_DUNGEON_EMPTY);
        Registry.register(Registry.STRUCTURE_PIECE, "moon_dungeon_boss_room", CMOON_DUNGEON_BOSS);
        Registry.register(Registry.STRUCTURE_PIECE, "moon_dungeon_treasure_room", CMOON_DUNGEON_TREASURE);
        Registry.register(Registry.STRUCTURE_PIECE, "moon_dungeon_spawner_room", CMOON_DUNGEON_SPAWNER);
        Registry.register(Registry.STRUCTURE_PIECE, "moon_dungeon_chest_room", CMOON_DUNGEON_CHEST);
        Registry.register(Registry.STRUCTURE_PIECE, "moon_dungeon_entrance_room", CMOON_DUNGEON_ENTRANCE);
        Registry.register(Registry.STRUCTURE_PIECE, GalacticraftCore.rl("moon_village_start"), CMOON_VILLAGE_START);
        Registry.register(Registry.STRUCTURE_PIECE, GalacticraftCore.rl("moon_village_field"), CMOON_VILLAGE_FIELD);
        Registry.register(Registry.STRUCTURE_PIECE, GalacticraftCore.rl("moon_village_field_two"), CMOON_VILLAGE_FIELD_TWO);
        Registry.register(Registry.STRUCTURE_PIECE, GalacticraftCore.rl("moon_village_house"), CMOON_VILLAGE_HOUSE);
        Registry.register(Registry.STRUCTURE_PIECE, GalacticraftCore.rl("moon_village_path"), CMOON_VILLAGE_PATH);
        Registry.register(Registry.STRUCTURE_PIECE, GalacticraftCore.rl("moon_village_torch"), CMOON_VILLAGE_TORCH);
        Registry.register(Registry.STRUCTURE_PIECE, GalacticraftCore.rl("moon_village_well"), CMOON_VILLAGE_WELL);
        Registry.register(Registry.STRUCTURE_PIECE, GalacticraftCore.rl("moon_village_wood_hut"), CMOON_VILLAGE_WOOD_HUT);
    }

    @SubscribeEvent
    public static void registerPlacements(RegistryEvent.Register<Placement<?>> event)
    {
        GCBlocks.register(event.getRegistry(), SAPPHIRE_ORE_PLACEMENT, "sapphire_ore");
    }

    public static <T extends Feature<?>> RegistryObject<T> register(final String name, final Supplier<T> sup)
    {
        return FEATURES.register(name, sup);
    }
}
