package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelLander;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderLander extends EntityRenderer<EntityLander>
{
    private static final ResourceLocation landerTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/lander.png");

    protected ModelLander landerModel;

    public RenderLander(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 2F;
        this.landerModel = new ModelLander();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityLander par1Entity)
    {
        return RenderLander.landerTexture;
    }

    @Override
    public void render(EntityLander lander, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        final float var24 = lander.prevRotationPitch + (lander.rotationPitch - lander.prevRotationPitch) * partialTicks;
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-var24));

        float f6 = lander.timeSinceHit - partialTicks;
        float f7 = lander.currentDamage - partialTicks;

        if (f7 < 0.0F)
        {
            f7 = 0.0F;
        }

        if (f6 > 0.0F)
        {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees((float) Math.sin(f6) * 0.2F * f6 * f7 / 25.0F));
        }

        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        this.landerModel.setRotationAngles(lander, 0.0F, 0.0F, -0.1F, 0.0F, MathHelper.lerp(partialTicks, lander.prevRotationPitch, lander.rotationPitch));
        this.landerModel.render(matrixStackIn, bufferIn.getBuffer(this.landerModel.getRenderType(getEntityTexture(lander))), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
    }

    @Override
    public boolean shouldRender(EntityLander lander, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = lander.getBoundingBox().grow(2D, 1D, 2D);
        return lander.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
