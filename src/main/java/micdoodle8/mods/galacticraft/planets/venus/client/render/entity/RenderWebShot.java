package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityWebShot;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderWebShot extends EntityRenderer<EntityWebShot>
{
    public RenderWebShot(EntityRendererManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 0.5F;
    }

    @Override
    public void render(EntityWebShot entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        RenderHelper.disableStandardItemLighting();
        matrixStackIn.push();
        matrixStackIn.translate(0, 0.5F, 0);
        this.renderManager.textureManager.bindTexture(getEntityTexture(entity));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees((entity.ticksExisted + partialTicks) * 50.0F));
        matrixStackIn.scale(0.5F, 0.5F, 0.5F);
        matrixStackIn.translate(-0.5F, -1F, 0.5F);
        blockrendererdispatcher.renderBlock(Blocks.COBWEB.getDefaultState(), matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
        matrixStackIn.translate(0.0F, 0.0F, 1.0F);
        matrixStackIn.pop();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityWebShot entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}
