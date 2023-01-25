package micdoodle8.mods.galacticraft.planets.asteroids.inventory;

import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiAstroMinerDock;
import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiSchematicAstroMinerDock;
import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiSchematicTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.IForgeRegistry;

import static micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks.register;

public class AsteroidsContainers
{
    @SubscribeEvent
    public static void initContainers(RegistryEvent.Register<ContainerType<?>> evt)
    {
        IForgeRegistry<ContainerType<?>> r = evt.getRegistry();

        ContainerType<ContainerAstroMinerDock> minerDock = IForgeContainerType.create((windowId, inv, data) -> new ContainerAstroMinerDock(windowId, inv, (TileEntityMinerBase) inv.player.world.getTileEntity(data.readBlockPos())));
        ContainerType<ContainerSchematicAstroMiner> schematicAstroMiner = IForgeContainerType.create((windowId, inv, data) -> new ContainerSchematicAstroMiner(windowId, inv));
        ContainerType<ContainerSchematicTier3Rocket> schematicTier3Rocket = IForgeContainerType.create((windowId, inv, data) -> new ContainerSchematicTier3Rocket(windowId, inv));
        ContainerType<ContainerShortRangeTelepad> telepad = IForgeContainerType.create((windowId, inv, data) -> new ContainerShortRangeTelepad(windowId, inv, (TileEntityShortRangeTelepad) inv.player.world.getTileEntity(data.readBlockPos())));

        register(r, minerDock, AsteroidsContainerNames.ASTRO_MINER_DOCK);
        register(r, schematicAstroMiner, AsteroidsContainerNames.SCHEMATIC_ASTRO_MINER);
        register(r, schematicTier3Rocket, AsteroidsContainerNames.SCHEMATIC_TIER_3_ROCKET);
        register(r, telepad, AsteroidsContainerNames.SHORT_RANGE_TELEPAD);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
        {
            ScreenManager.registerFactory(minerDock, GuiAstroMinerDock::new);
            ScreenManager.registerFactory(schematicAstroMiner, GuiSchematicAstroMinerDock::new);
            ScreenManager.registerFactory(schematicTier3Rocket, GuiSchematicTier3Rocket::new);
            ScreenManager.registerFactory(telepad, GuiShortRangeTelepad::new);
        });
    }
}
