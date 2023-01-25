package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelBalloonParachute;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityLandingBalloons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderLandingBalloons extends EntityRenderer<EntityLandingBalloons>
{
    private static IBakedModel balloonModel;
    protected ModelBalloonParachute parachuteModel = new ModelBalloonParachute();

    public RenderLandingBalloons(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 1.2F;
        GCModelCache.INSTANCE.reloadCallback(RenderLandingBalloons::updateModels);
    }

    public static void updateModels()
    {
        balloonModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/landing_balloon.obj"), ImmutableList.of("Sphere"));
    }

    @Override
    public ResourceLocation getEntityTexture(EntityLandingBalloons entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void render(EntityLandingBalloons entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        RenderSystem.disableRescaleNormal();
        matrixStackIn.push();
        matrixStackIn.translate(0.0F, 0.8F, 0.0F);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entityYaw));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(pitch));

        this.renderManager.textureManager.bindTexture(getEntityTexture(entity));

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            RenderSystem.shadeModel(GL11.GL_FLAT);
        }

        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.scale(0.5F, 0.5F, 0.5F);
        ClientUtil.drawBakedModel(balloonModel, bufferIn, matrixStackIn, packedLightIn);
        matrixStackIn.pop();

        if (entity.getPosY() >= 500.0F)
        {
            matrixStackIn.push();
            matrixStackIn.translate(1.25F, 0.93F, 0.3F);
            matrixStackIn.scale(2.5F, 3.0F, 2.5F);
            this.parachuteModel.render(matrixStackIn, bufferIn.getBuffer(parachuteModel.getRenderType(getEntityTexture(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.pop();
        }
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public boolean shouldRender(EntityLandingBalloons lander, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = lander.getBoundingBox().grow(2D, 1D, 2D);
        return lander.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
