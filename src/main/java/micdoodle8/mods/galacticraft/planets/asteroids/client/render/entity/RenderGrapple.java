package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.ItemModelGrapple;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityGrapple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class RenderGrapple extends EntityRenderer<EntityGrapple>
{
    private static ItemModelGrapple grappleModel;

    public RenderGrapple(EntityRendererManager manager)
    {
        super(manager);
    }

    public static void updateModel()
    {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "grapple", "inventory");
        grappleModel = (ItemModelGrapple) Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);
    }

    @Override
    public void render(EntityGrapple grapple, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        RenderSystem.disableRescaleNormal();
        matrixStackIn.push();

        Vec3d vec3 = new Vec3d(0.0D, -0.2D, 0.0D);
        PlayerEntity shootingEntity = grapple.getShootingEntity();

        if (shootingEntity != null/* && grapple.getPullingEntity()*/)
        {
            double d3 = MathHelper.lerp(partialTicks, shootingEntity.prevPosX, shootingEntity.getPosX()) + vec3.getX();
            double d4 = MathHelper.lerp(partialTicks, shootingEntity.prevPosY, shootingEntity.getPosY()) + vec3.getY();
            double d5 = MathHelper.lerp(partialTicks, shootingEntity.prevPosZ, shootingEntity.getPosZ()) + vec3.getZ();

            Tessellator tessellator = Tessellator.getInstance();
            RenderSystem.disableTexture();
            RenderSystem.disableLighting();
            tessellator.getBuffer().begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            byte b2 = 16;

            double d14 = MathHelper.lerp(partialTicks, grapple.prevPosX, grapple.getPosX());
            double d8 = MathHelper.lerp(partialTicks, grapple.prevPosY, grapple.getPosY()) + 0.25D;
            double d10 = MathHelper.lerp(partialTicks, grapple.prevPosZ, grapple.getPosZ());
            float d11 = (float) (d3 - d14);
            float d12 = (float) (d4 - d8);
            float d13 = (float) (d5 - d10);
            matrixStackIn.translate(0, -0.2F, 0);

            Matrix4f last = matrixStackIn.getLast().getMatrix();
            for (int i = 0; i <= b2; ++i)
            {
                float f12 = (float) i / (float) b2;
                tessellator.getBuffer().pos(last, d11 * f12, d12 * (f12 * f12 + f12) * 0.5F + 0.15F, d13 * f12).color(203.0F / 255.0F, 203.0F / 255.0F, 192.0F / 255.0F, 1.0F).endVertex();
            }

            tessellator.draw();
            matrixStackIn.translate(0, 0, 0);
            RenderSystem.enableLighting();
            RenderSystem.enableTexture();
        }

        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, grapple.prevRotationYaw, grapple.rotationYaw) - 90.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, grapple.prevRotationPitch, grapple.rotationPitch) - 180));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(MathHelper.lerp(partialTicks, grapple.prevRotationRoll, grapple.rotationRoll)));

        updateModel();

        this.renderManager.textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            RenderSystem.shadeModel(GL11.GL_FLAT);
        }

        ClientUtil.drawBakedModel(grappleModel, bufferIn, matrixStackIn, packedLightIn);

//        this.bindEntityTexture(grapple);
//        ItemRendererGrappleHook.modelGrapple.renderAll(); TODO

        matrixStackIn.pop();
    }

//    protected ResourceLocation getEntityTexture(EntityGrapple grapple)
//    {
////        return ItemRendererGrappleHook.grappleTexture;
//    }

    @Override
    public ResourceLocation getEntityTexture(EntityGrapple entity)
    {
        return new ResourceLocation("missing");
    }

    @Override
    public boolean shouldRender(EntityGrapple entity, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        return entity.isInRangeToRender3d(camX, camY, camZ);
    }
}
