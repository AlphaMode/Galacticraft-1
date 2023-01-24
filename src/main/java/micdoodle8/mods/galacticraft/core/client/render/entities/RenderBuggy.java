package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderBuggy extends EntityRenderer<EntityBuggy>
{
    public static final ResourceLocation MODEL = new ResourceLocation(Constants.MOD_ID_CORE, "models/buggy.obj");
    public static IBakedModel mainModel;
    public static IBakedModel radarDish;
    public static IBakedModel wheelLeftCover;
    public static IBakedModel wheelRight;
    public static IBakedModel wheelLeft;
    public static IBakedModel wheelRightCover;
    public static IBakedModel cargoLeft;
    public static IBakedModel cargoMid;
    public static IBakedModel cargoRight;

    public static void updateModels()
    {
        RenderBuggy.mainModel = GCModelCache.INSTANCE.getModel(MODEL, ImmutableList.of("MainBody"));
        RenderBuggy.radarDish = GCModelCache.INSTANCE.getModel(MODEL, ImmutableList.of("RadarDish_Dish"));
        RenderBuggy.wheelLeftCover = GCModelCache.INSTANCE.getModel(MODEL, ImmutableList.of("Wheel_Left_Cover"));
        RenderBuggy.wheelRight = GCModelCache.INSTANCE.getModel(MODEL, ImmutableList.of("Wheel_Right"));
        RenderBuggy.wheelLeft = GCModelCache.INSTANCE.getModel(MODEL, ImmutableList.of("Wheel_Left"));
        RenderBuggy.wheelRightCover = GCModelCache.INSTANCE.getModel(MODEL, ImmutableList.of("Wheel_Right_Cover"));
        RenderBuggy.cargoLeft = GCModelCache.INSTANCE.getModel(MODEL, ImmutableList.of("CargoLeft"));
        RenderBuggy.cargoMid = GCModelCache.INSTANCE.getModel(MODEL, ImmutableList.of("CargoMid"));
        RenderBuggy.cargoRight = GCModelCache.INSTANCE.getModel(MODEL, ImmutableList.of("CargoRight"));
    }

    public RenderBuggy(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 1.0F;
        GCModelCache.INSTANCE.reloadCallback(RenderBuggy::updateModels);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityBuggy entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void render(EntityBuggy entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        RenderSystem.disableRescaleNormal();
        matrixStackIn.push();
        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-pitch));
        matrixStackIn.scale(0.41F, 0.41F, 0.41F);

        this.renderManager.textureManager.bindTexture(getEntityTexture(entity));

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            RenderSystem.shadeModel(GL11.GL_FLAT);
        }

        // Front wheels
        matrixStackIn.push();
        float dZ = -2.727F;
        float dY = 0.976F;
        float dX = 1.25F;
        float rotation = entity.wheelRotationX;
        matrixStackIn.translate(dX, dY, dZ);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.wheelRotationZ));
        ClientUtil.drawBakedModel(this.wheelRightCover, bufferIn, matrixStackIn, packedLightIn);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(rotation));
        ClientUtil.drawBakedModel(this.wheelRight, bufferIn, matrixStackIn, packedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(-dX, dY, dZ);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.wheelRotationZ));
        ClientUtil.drawBakedModel(this.wheelLeftCover, bufferIn, matrixStackIn, packedLightIn);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(rotation));
        ClientUtil.drawBakedModel(this.wheelLeft, bufferIn, matrixStackIn, packedLightIn);
        matrixStackIn.pop();

        // Back wheels
        matrixStackIn.push();
        dX = 1.9F;
        dZ = -dZ;
        matrixStackIn.translate(dX, dY, dZ);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-entity.wheelRotationZ));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(rotation));
        ClientUtil.drawBakedModel(this.wheelRight, bufferIn, matrixStackIn, packedLightIn);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.translate(-dX, dY, dZ);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-entity.wheelRotationZ));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(rotation));
        ClientUtil.drawBakedModel(this.wheelLeft, bufferIn, matrixStackIn, packedLightIn);
        matrixStackIn.pop();

        ClientUtil.drawBakedModel(this.mainModel, bufferIn, matrixStackIn, packedLightIn);

        // Radar Dish
        matrixStackIn.push();
        matrixStackIn.translate(-1.178F, 4.1F, -2.397F);
        int ticks = entity.ticksExisted + entity.getEntityId() * 10000;
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees((float) Math.sin(ticks * 0.05) * 50.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees((float) Math.cos(ticks * 0.1) * 50.0F));
        ClientUtil.drawBakedModel(this.radarDish, bufferIn, matrixStackIn, packedLightIn);
        matrixStackIn.pop();

        if (entity.getBuggyType() != null && entity.buggyType.ordinal() > 0)
        {
            ClientUtil.drawBakedModel(this.cargoLeft, bufferIn, matrixStackIn, packedLightIn);

            if (entity.buggyType.ordinal() > 1)
            {
                ClientUtil.drawBakedModel(this.cargoMid, bufferIn, matrixStackIn, packedLightIn);

                if (entity.buggyType.ordinal() > 2)
                {
                    ClientUtil.drawBakedModel(this.cargoRight, bufferIn, matrixStackIn, packedLightIn);
                }
            }
        }

        matrixStackIn.pop();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public boolean shouldRender(EntityBuggy buggy, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = buggy.getBoundingBox().grow(2D, 1D, 2D);
        return buggy.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
