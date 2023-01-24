package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.client.model.ModelSpiderQueen;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntitySpiderQueen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderSpiderQueen extends MobRenderer<EntitySpiderQueen, ModelSpiderQueen>
{
    private static final ResourceLocation spiderTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/model/spider_queen.png");
    private static IBakedModel webModel;

    public RenderSpiderQueen(EntityRendererManager renderManager)
    {
        super(renderManager, new ModelSpiderQueen(), 1.0F);
        GCModelCache.INSTANCE.reloadCallback(RenderSpiderQueen::updateModels);
    }

    public static void updateModels()
    {
        try
        {
            webModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/web.obj"), ImmutableList.of("Sphere"));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void preRenderCallback(EntitySpiderQueen entity, MatrixStack matrixStackIn, float partialTickTime)
    {
        if (entity.getBurrowedCount() >= 0)
        {
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStackIn.translate(0.0F, entity.getHealth(), 0.0F);
        }
        matrixStackIn.scale(1.5F, 1.5F, 1.5F);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees((float) (Math.pow(entity.deathTicks, 2) / 5.0F + (Math.pow(entity.deathTicks, 2) / 5.0F - Math.pow(entity.deathTicks - 1, 2) / 5.0F) * partialTickTime)));
        super.preRenderCallback(entity, matrixStackIn, partialTickTime);
    }

    @Override
    public void render(EntitySpiderQueen entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn){
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

        matrixStackIn.push();
        matrixStackIn.translate(0, 0.8F, 0);
        matrixStackIn.scale(1.4F, 1.5F, 1.4F);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        RenderHelper.disableStandardItemLighting();
        this.renderManager.textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            RenderSystem.shadeModel(GL11.GL_FLAT);
        }

        if (entity.getBurrowedCount() >= 0)
        {
            RenderSystem.disableCull();
            ClientUtil.drawBakedModel(webModel, bufferIn, matrixStackIn, packedLightIn);
            matrixStackIn.scale(1.05F, 1.1F, 1.05F);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(192.5F));
            ClientUtil.drawBakedModel(webModel, bufferIn, matrixStackIn, packedLightIn);
            RenderSystem.enableCull();
        }

        RenderHelper.enableStandardItemLighting();

        matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(EntitySpiderQueen juicer)
    {
        return spiderTexture;
    }
}
