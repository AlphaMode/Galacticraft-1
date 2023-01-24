package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelFlag;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderFlag extends EntityRenderer<EntityFlag>
{
    public static ResourceLocation flagTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/flag.png");

    protected ModelFlag modelFlag;

    public RenderFlag(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 1F;
        this.modelFlag = new ModelFlag();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityFlag entity)
    {
        return RenderFlag.flagTexture;
    }

    @Override
    public void render(EntityFlag entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        RenderSystem.disableRescaleNormal();
        matrixStackIn.push();
        long seed = entity.getEntityId() * 493286711L;
        seed = seed * seed * 4392167121L + seed * 98761L;
        float seedX = (((seed >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedY = (((seed >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedZ = (((seed >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        matrixStackIn.translate(seedX, seedY + 1.5F, seedZ);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entity.getFacingAngle()));
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        this.modelFlag.render(entity, matrixStackIn, bufferIn.getBuffer(this.modelFlag.getRenderType(getEntityTexture(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
    }

    @Override
    public boolean shouldRender(EntityFlag lander, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = lander.getBoundingBox().grow(1D, 2D, 1D);
        return lander.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
