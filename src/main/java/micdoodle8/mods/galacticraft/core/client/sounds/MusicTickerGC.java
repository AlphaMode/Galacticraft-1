package micdoodle8.mods.galacticraft.core.client.sounds;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IExtensibleEnum;

import java.util.function.Supplier;

public class MusicTickerGC extends MusicTicker
{
    public MusicTickerGC(Minecraft client)
    {
        super(client);
    }

    @Override
    public void tick()
    {
        if (Minecraft.getInstance().world != null && Minecraft.getInstance().world.getDimension() instanceof IGalacticraftDimension) {
            GCMusicType musictype = ClientProxyCore.MUSIC_TYPE_MARS;

            if (this.currentMusic != null) {
                if (!musictype.getSound().getName().equals(this.currentMusic.getSoundLocation())) {
                    this.client.getSoundHandler().stop(this.currentMusic);
                    this.timeUntilNextMusic = MathHelper.nextInt(this.random, 0, musictype.getMinDelay() / 2);
                }

                if (!this.client.getSoundHandler().isPlaying(this.currentMusic)) {
                    this.currentMusic = null;
                    this.timeUntilNextMusic = Math.min(MathHelper.nextInt(this.random, musictype.getMinDelay(), musictype.getMaxDelay()), this.timeUntilNextMusic);
                }
            }

            this.timeUntilNextMusic = Math.min(this.timeUntilNextMusic, musictype.getMaxDelay());

            if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0) {
                this.playGC(musictype);
            }

            return;
        }
        super.tick();
    }

    /**
     * Plays a music track for the maximum allowable period of time
     */
    public void playGC(GCMusicType type) {
        this.currentMusic = SimpleSound.music(type.getSound());
        this.client.getSoundHandler().play(this.currentMusic);
        this.timeUntilNextMusic = Integer.MAX_VALUE;
    }

    @OnlyIn(Dist.CLIENT)
    public enum GCMusicType implements IExtensibleEnum {
        MARS_JC(GCSounds.music, 12000, 24000);

        private final Supplier<SoundEvent> sound;
        private final int minDelay;
        private final int maxDelay;

        GCMusicType(Supplier<SoundEvent> sound, int minDelayIn, int maxDelayIn) {
            this.sound = sound;
            this.minDelay = minDelayIn;
            this.maxDelay = maxDelayIn;
        }

        /**
         * Gets the {@link SoundEvent} containing the current music track's location
         */
        public SoundEvent getSound() {
            return this.sound.get();
        }

        /**
         * Returns the minimum delay between playing music of this type.
         */
        public int getMinDelay() {
            return this.minDelay;
        }

        /**
         * Returns the maximum delay between playing music of this type.
         */
        public int getMaxDelay() {
            return this.maxDelay;
        }

        public static GCMusicType create(String name, Supplier<SoundEvent> sound, int minDelayIn, int maxDelayIn)
        {
            throw new IllegalStateException("Enum not extended");
        }
    }
}
