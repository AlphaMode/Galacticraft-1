package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityTier3Rocket;
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
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderTier3Rocket extends EntityRenderer<EntityTier3Rocket>
{
    private static IBakedModel rocketModel;
    private static IBakedModel coneModel;
    private static IBakedModel cubeModel;

    public RenderTier3Rocket(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 2F;
        GCModelCache.INSTANCE.reloadCallback(RenderTier3Rocket::updateModels);
    }

    public static void updateModels()
    {
        rocketModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/tier3rocket.obj"), ImmutableList.of("Boosters", "Rocket"));
        coneModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/tier3rocket.obj"), ImmutableList.of("NoseCone"));
        cubeModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/tier3rocket.obj"), ImmutableList.of("Cube"));
    }

    @Override
    public ResourceLocation getEntityTexture(EntityTier3Rocket entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void render(EntityTier3Rocket entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks + 180;
        RenderSystem.disableRescaleNormal();
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-pitch));
        matrixStackIn.translate(0.0F, entity.getRenderOffsetY(), 0.0F);
        float rollAmplitude = entity.rollAmplitude / 3 - partialTicks;

        if (rollAmplitude > 0.0F)
        {
            final float i = entity.getLaunched() ? (5 - MathHelper.floor(entity.timeUntilLaunch / 85)) / 10F : 0.3F;
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(MathHelper.sin(rollAmplitude) * rollAmplitude * i * partialTicks));
            matrixStackIn.rotate(new Vector3f(1.0F, 0.0F, 1.0F).rotationDegrees(MathHelper.sin(rollAmplitude) * rollAmplitude * i * partialTicks));
        }

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
        matrixStackIn.scale(0.8F, 0.8F, 0.8F);
        ClientUtil.drawBakedModel(rocketModel, bufferIn, matrixStackIn, packedLightIn);

        Vector3 teamColor = ClientUtil.updateTeamColor(PlayerUtil.getName(Minecraft.getInstance().player), true);

        if (teamColor != null)
        {
            RenderSystem.disableTexture();
            ClientUtil.drawBakedModelColored(coneModel, bufferIn, matrixStackIn, packedLightIn, teamColor.floatZ(), teamColor.floatY(), teamColor.floatX(), 1.0F);
        }
        else
        {
            ClientUtil.drawBakedModel(coneModel, bufferIn, matrixStackIn, packedLightIn);
            RenderSystem.disableTexture();
        }

        RenderSystem.disableLighting();

        boolean red = Minecraft.getInstance().player.ticksExisted / 10 % 2 < 1;
        ClientUtil.drawBakedModelColored(cubeModel, bufferIn, matrixStackIn, packedLightIn, 0F, red ? 0 : 1.0F, red ? 1.0F : 0, 1.0F);

        RenderSystem.enableTexture();
        RenderSystem.enableLighting();

        RenderSystem.color3f(1F, 1F, 1F);
        matrixStackIn.pop();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public boolean shouldRender(EntityTier3Rocket rocket, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = rocket.getBoundingBox().grow(0.5D, 0, 0.5D);
        return rocket.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
