package micdoodle8.mods.galacticraft.core.client.sounds;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class GCSounds
{
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Constants.MOD_ID_CORE);
    public static final RegistryObject<SoundEvent> bossDeath = registerSound("entity.bossdeath");
    public static final RegistryObject<SoundEvent> bossLaugh = registerSound("entity.bosslaugh");
    public static final RegistryObject<SoundEvent> bossOoh = registerSound("entity.ooh");
    public static final RegistryObject<SoundEvent> bossOuch = registerSound("entity.ouch");
    public static final RegistryObject<SoundEvent> slimeDeath = registerSound("entity.slime_death");
    public static final RegistryObject<SoundEvent> astroMiner = registerSound("entity.astrominer");
    public static final RegistryObject<SoundEvent> shuttle = registerSound("shuttle.shuttle");
    public static final RegistryObject<SoundEvent> parachute = registerSound("player.parachute");
    public static final RegistryObject<SoundEvent> openAirLock = registerSound("player.openairlock");
    public static final RegistryObject<SoundEvent> closeAirLock = registerSound("player.closeairlock");
    public static final RegistryObject<SoundEvent> singleDrip = registerSound("ambience.singledrip");
    public static final RegistryObject<SoundEvent> scaryScape = registerSound("ambience.scaryscape");
    public static final RegistryObject<SoundEvent> deconstructor = registerSound("block.deconstructor");
    public static final RegistryObject<SoundEvent> advanced_compressor = registerSound("player.unlockchest");
    public static final RegistryObject<SoundEvent> laserCharge = registerSound("laser.charge");
    public static final RegistryObject<SoundEvent> laserShoot = registerSound("laser.shoot");

    public static final RegistryObject<SoundEvent> music = registerSound("galacticraft.music_space");

    private static RegistryObject<SoundEvent> registerSound(String soundName)
    {
        return SOUNDS.register(soundName, () -> new SoundEvent(new ResourceLocation(Constants.MOD_ID_CORE, soundName)));
    }
}
