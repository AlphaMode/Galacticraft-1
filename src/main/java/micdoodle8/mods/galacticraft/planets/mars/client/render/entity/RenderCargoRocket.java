package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.ItemModelCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderCargoRocket extends EntityRenderer<EntityCargoRocket>
{
    private ItemModelCargoRocket rocketModel;

    public RenderCargoRocket(EntityRendererManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 0.5F;
    }

    private void updateModel()
    {
        if (this.rocketModel == null)
        {
            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "rocket_cargo1", "inventory");
            this.rocketModel = (ItemModelCargoRocket) Minecraft.getInstance().getItemRenderer().getItemModelMesher().getModelManager().getModel(modelResourceLocation);
        }
    }

    @Override
    public ResourceLocation getEntityTexture(EntityCargoRocket entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void render(EntityCargoRocket entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        RenderSystem.disableRescaleNormal();
        matrixStackIn.push();
        matrixStackIn.translate(0.0F, entity.getRenderOffsetY(), 0.0F);
        matrixStackIn.scale(0.4F, 0.4F, 0.4F);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-pitch));
        this.renderManager.textureManager.bindTexture(getEntityTexture(entity));

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            RenderSystem.shadeModel(GL11.GL_FLAT);
        }

        this.updateModel();
        ClientUtil.drawBakedModel(this.rocketModel, bufferIn, matrixStackIn, packedLightIn);
        matrixStackIn.pop();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public boolean shouldRender(EntityCargoRocket rocket, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = rocket.getBoundingBox();
        return rocket.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
