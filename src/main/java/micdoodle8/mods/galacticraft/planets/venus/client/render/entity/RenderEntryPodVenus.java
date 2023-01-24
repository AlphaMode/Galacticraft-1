package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelBalloonParachute;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityEntryPodVenus;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class RenderEntryPodVenus extends EntityRenderer<EntityEntryPodVenus>
{
    public static IBakedModel modelEntryPod;
    public static IBakedModel modelFlame;
    protected ModelBalloonParachute parachuteModel = new ModelBalloonParachute();

    public RenderEntryPodVenus(EntityRendererManager manager)
    {
        super(manager);
        GCModelCache.INSTANCE.reloadCallback(this::updateModel);
    }

    private void updateModel() {
        RenderEntryPodVenus.modelEntryPod = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/pod_flame.obj"), ImmutableList.of("PodBody"));
        RenderEntryPodVenus.modelFlame = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/pod_flame.obj"), ImmutableList.of("Flame_Sphere"));
    }

    @Override
    public void render(EntityEntryPodVenus entityEntryPod, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        RenderSystem.disableRescaleNormal();
        matrixStackIn.push();
        final float var24 = entityEntryPod.prevRotationPitch + (entityEntryPod.rotationPitch - entityEntryPod.prevRotationPitch) * partialTicks;
        final float var25 = entityEntryPod.prevRotationYaw + (entityEntryPod.rotationYaw - entityEntryPod.prevRotationYaw) * partialTicks;

        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180.0F - var24));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-var25));

        this.renderManager.textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.scale(0.65F, 0.6F, 0.65F);
        ClientUtil.drawBakedModel(modelEntryPod, bufferIn, matrixStackIn, packedLightIn);

        if (entityEntryPod.getPosY() > 382.0F)
        {
            RenderHelper.disableStandardItemLighting();
            RenderSystem.glMultiTexCoord2f(GL15.GL_TEXTURE1, 240.0F, 240.0F);
            RenderSystem.disableLighting();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL15.glCullFace(GL15.GL_FRONT);

            matrixStackIn.push();
            float val = (float) (Math.sin(entityEntryPod.ticksExisted) / 20.0F + 0.5F);
            matrixStackIn.scale(1.0F, 1.0F + val, 1.0F);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entityEntryPod.ticksExisted * 20.0F));
            ClientUtil.drawBakedModelColored(modelFlame, bufferIn, matrixStackIn, packedLightIn, 1.0F, 1.0F, 1.0F, entityEntryPod.getPosY() >= 790.0F ? 255 : (int) Math.max(Math.min(255, -(entityEntryPod.getMotion().y + 0.6F) * 100.0F), 0));
            matrixStackIn.pop();

            matrixStackIn.scale(1.0F, 1.0F + val / 6.0F, 1.0F);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entityEntryPod.ticksExisted * 5.0F));
            ClientUtil.drawBakedModelColored(modelFlame, bufferIn, matrixStackIn, packedLightIn, 1.0F, 1.0F, 1.0F, entityEntryPod.getPosY() >= 790.0F ? 255 : (int) Math.max(Math.min(255, -(entityEntryPod.getMotion().y + 0.6F) * 100.0F), 0));

            GL15.glCullFace(GL15.GL_BACK);
            RenderSystem.enableCull();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelper.enableStandardItemLighting();
        }

        if (entityEntryPod.getGroundPosY() != null && entityEntryPod.getPosY() - entityEntryPod.getGroundPosY() > 5.0F && entityEntryPod.getPosY() <= 242.0F)
        {
            matrixStackIn.push();
            matrixStackIn.translate(-1.4F, 1.5F, -0.3F);
            matrixStackIn.scale(2.5F, 3.0F, 2.5F);
            this.parachuteModel.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntitySolid(ModelBalloonParachute.grayParachuteTexture)), packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            matrixStackIn.pop();
        }
        matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityEntryPodVenus entityEntryPod)
    {
        return new ResourceLocation("missing");
    }

    @Override
    public boolean shouldRender(EntityEntryPodVenus lander, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = lander.getBoundingBox().grow(1D, 2D, 1D);
        return lander.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
