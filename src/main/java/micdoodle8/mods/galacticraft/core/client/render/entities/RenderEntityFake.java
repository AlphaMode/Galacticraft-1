package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.entities.EntityCelestialFake;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderEntityFake extends EntityRenderer<EntityCelestialFake>
{
    public RenderEntityFake(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityCelestialFake par1Entity)
    {
        return null;
    }

    @Override
    public void render(EntityCelestialFake entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
    }

    @Override
    public boolean shouldRender(EntityCelestialFake lander, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        return false;
    }
}
