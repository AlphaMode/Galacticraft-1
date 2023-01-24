package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityEntryPod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderEntryPod extends EntityRenderer<EntityEntryPod>
{
    private static IBakedModel modelEntryPod;

    public RenderEntryPod(EntityRendererManager manager)
    {
        super(manager);
        GCModelCache.INSTANCE.reloadCallback(RenderEntryPod::updateModels);
    }

    public static void updateModels()
    {
        modelEntryPod = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/pod.obj"), ImmutableList.of("PodBody"));
    }

    @Override
    public void render(EntityEntryPod entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        RenderSystem.disableRescaleNormal();
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180.0F - pitch));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-yaw));

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
        matrixStackIn.scale(0.65F, 0.6F, 0.65F);
        ClientUtil.drawBakedModel(modelEntryPod, bufferIn, matrixStackIn, packedLightIn);
        matrixStackIn.pop();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityEntryPod entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public boolean shouldRender(EntityEntryPod lander, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = lander.getBoundingBox().grow(1D, 2D, 1D);
        return lander.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
