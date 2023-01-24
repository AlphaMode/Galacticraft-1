package micdoodle8.mods.galacticraft.api.event.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;

public abstract class CelestialBodyRenderEvent extends Event
{
    public final CelestialBody celestialBody;
    @Nullable
    public final MatrixStack matrixStackIn;

    public CelestialBodyRenderEvent(CelestialBody celestialBody, @Nullable MatrixStack matrixStackIn)
    {
        this.celestialBody = celestialBody;
        this.matrixStackIn = matrixStackIn;
    }

    public static class CelestialRingRenderEvent extends CelestialBodyRenderEvent
    {
        public CelestialRingRenderEvent(CelestialBody celestialBody)
        {
            this(celestialBody, null);
        }

        public CelestialRingRenderEvent(CelestialBody celestialBody, @Nullable MatrixStack matrixStackIn)
        {
            super(celestialBody, matrixStackIn);
        }


        @Cancelable
        public static class Pre extends CelestialRingRenderEvent
        {
            public final Vector3 parentOffset;

            public Pre(CelestialBody celestialBody, Vector3 parentOffset)
            {
                this(celestialBody, parentOffset, null);
            }

            public Pre(CelestialBody celestialBody, Vector3 parentOffset, @Nullable MatrixStack matrixStackIn)
            {
                super(celestialBody, matrixStackIn);
                this.parentOffset = parentOffset;
            }
        }

        public static class Post extends CelestialBodyRenderEvent
        {
            public Post(CelestialBody celestialBody)
            {
                this(celestialBody, null);
            }

            public Post(CelestialBody celestialBody, @Nullable MatrixStack matrixStackIn)
            {
                super(celestialBody, matrixStackIn);
            }
        }
    }

    @Cancelable
    public static class Pre extends CelestialBodyRenderEvent
    {
        public ResourceLocation celestialBodyTexture;
        public int textureSize;

        public Pre(CelestialBody celestialBody, ResourceLocation celestialBodyTexture, int textureSize)
        {
            this(celestialBody, celestialBodyTexture, textureSize, null);
        }

        public Pre(CelestialBody celestialBody, ResourceLocation celestialBodyTexture, int textureSize, @Nullable MatrixStack matrixStackIn)
        {
            super(celestialBody, matrixStackIn);
            this.celestialBodyTexture = celestialBodyTexture;
            this.textureSize = textureSize;
        }
    }

    public static class Post extends CelestialBodyRenderEvent
    {
        public Post(CelestialBody celestialBody)
        {
            this(celestialBody, null);
        }

        public Post(CelestialBody celestialBody, @Nullable MatrixStack matrixStackIn)
        {
            super(celestialBody, matrixStackIn);
        }
    }
}
