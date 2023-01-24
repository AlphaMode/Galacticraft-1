package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedEnderman;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderEvolvedEnderman;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedEnderman;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerEvolvedEndermanHeldBlock extends LayerRenderer<EntityEvolvedEnderman, ModelEvolvedEnderman> {
    public LayerEvolvedEndermanHeldBlock(RenderEvolvedEnderman render) {
        super(render);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityEvolvedEnderman entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        {
            BlockState blockstate = entity.getHeldBlockState();
            if (blockstate != null) {
                matrixStackIn.push();
                matrixStackIn.translate(0.0F, 0.6875F, -0.75F);
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(20.0F));
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(45.0F));
                matrixStackIn.translate(0.25F, 0.1875F, 0.25F);
                float f = 0.5F;
                matrixStackIn.scale(-0.5F, -0.5F, 0.5F);
                Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(blockstate, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
                matrixStackIn.pop();
            }
        }
    }
}