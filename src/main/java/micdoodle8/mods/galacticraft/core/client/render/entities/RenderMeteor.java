package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelMeteor;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderMeteor extends EntityRenderer<EntityMeteor>
{
    private static final ResourceLocation meteorTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/meteor.png");

    private final ModelMeteor modelMeteor;

    public RenderMeteor(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 1F;
        this.modelMeteor = new ModelMeteor();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityMeteor par1Entity)
    {
        return RenderMeteor.meteorTexture;
    }

    @Override
    public void render(EntityMeteor meteor, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entityYaw));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(entityYaw));
        final float f = meteor.getSize();
        matrixStackIn.scale(f / 2, f / 2, f / 2);
        this.renderManager.textureManager.bindTexture(getEntityTexture(meteor));
        this.modelMeteor.render(matrixStackIn, bufferIn.getBuffer(modelMeteor.getRenderType(getEntityTexture(meteor))), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
    }
}
