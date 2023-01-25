package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelMeteorChunk;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteorChunk;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class RenderMeteorChunk extends EntityRenderer<EntityMeteorChunk>
{
    private static final ResourceLocation meteorChunkTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/blocks/meteor_chunk.png");
    private static final ResourceLocation meteorChunkHotTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/blocks/meteor_chunk_hot.png");
    private final ModelMeteorChunk modelMeteor;

    public RenderMeteorChunk(EntityRendererManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 0.1F;
        this.modelMeteor = new ModelMeteorChunk();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityMeteorChunk entity)
    {
        if (entity.isHot())
        {
            return RenderMeteorChunk.meteorChunkHotTexture;
        }
        else
        {
            return RenderMeteorChunk.meteorChunkTexture;
        }
    }

    @Override
    public void render(EntityMeteorChunk entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        final float pitch = entity.rotationPitch;
        final float yaw = entity.rotationYaw;
        matrixStackIn.scale(0.3F, 0.3F, 0.3F);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(yaw));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(pitch));
        this.modelMeteor.render(matrixStackIn, bufferIn.getBuffer(modelMeteor.getRenderType(getEntityTexture(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
    }
}
