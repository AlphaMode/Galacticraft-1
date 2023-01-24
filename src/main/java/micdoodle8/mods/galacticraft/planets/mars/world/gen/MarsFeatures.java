package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.world.gen.GCFeatures;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MarsFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Constants.MOD_ID_PLANETS);

    public static final MapGenDungeonMars MARS_DUNGEON = new MapGenDungeonMars(DungeonConfiguration::deserialize);
    public static IStructurePieceType CMARS_DUNGEON_BOSS = RoomBossMars::new;
    public static IStructurePieceType CMARS_DUNGEON_TREASURE = RoomTreasureMars::new;

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
    {
//        Registry.register(Registry.STRUCTURE_PIECE, "MarsDungeonBossRoom", CMARS_DUNGEON_BOSS);
//        Registry.register(Registry.STRUCTURE_PIECE, "MarsDungeonTreasureRoom", CMARS_DUNGEON_TREASURE);
        GCBlocks.register(event.getRegistry(), MARS_DUNGEON, "mars_dungeon");
    }
}
