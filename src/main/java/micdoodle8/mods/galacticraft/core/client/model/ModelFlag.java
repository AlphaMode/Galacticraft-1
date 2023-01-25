package micdoodle8.mods.galacticraft.core.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class ModelFlag extends EntityModel<EntityFlag>
{
    ModelRenderer base;
    ModelRenderer pole;

    public ModelFlag()
    {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.base = new ModelRenderer(this, 4, 0);
        this.base.addBox(-1.5F, 0F, -1.5F, 3, 1, 3);
        this.base.setRotationPoint(0F, 23F, 0F);
        this.base.setTextureSize(128, 64);
        this.base.mirror = true;
        this.setRotation(this.base, 0F, 0F, 0F);
        this.pole = new ModelRenderer(this, 0, 0);
        this.pole.addBox(-0.5F, -40F, -0.5F, 1, 40, 1);
        this.pole.setRotationPoint(0F, 23F, 0F);
        this.pole.setTextureSize(128, 64);
        this.pole.mirror = true;
        this.setRotation(this.pole, 0F, 0F, 0F);
    }

    @Override
    public void setRotationAngles(EntityFlag entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.renderPole(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void render(EntityFlag flag, MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.renderPole(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.renderFlag(flag, flag.ticksExisted, matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void renderPole(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        this.base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.pole.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void renderFlag(EntityFlag entity, float ticks, MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        if (entity.flagData != null)
        {
            matrixStackIn.push();

            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0F, -1.1F, 0.0F);

            RenderSystem.disableTexture();
            RenderSystem.disableCull();

            float windLevel = 1.0F;

            if (entity.world.getDimension() instanceof IGalacticraftDimension)
            {
                windLevel = ((IGalacticraftDimension) entity.world.getDimension()).getWindLevel();
            }

            for (int i = 0; i < entity.flagData.getWidth(); i++)
            {
                for (int j = 0; j < entity.flagData.getHeight(); j++)
                {
                    matrixStackIn.push();
                    matrixStackIn.translate(0, -1.0F, 0);
                    float offset = 0.0F;
                    float offsetAhead = 0.0F;

                    if (windLevel > 0)
                    {
                        offset = (float) (Math.sin(ticks / 2.0F + i * 50 + 3) / 25.0F) * i / 30.0F;
                        offsetAhead = (float) (Math.sin(ticks / 2.0F + (i + 1) * 50 + 3) / 25.0F) * (i + 1) / 30.0F;
                        offset *= windLevel;
                        offsetAhead *= windLevel;
                    }

                    Vector3 col = entity.flagData.getColorAt(i, j);
                    RenderSystem.color3f(col.floatX(), col.floatY(), col.floatZ());

                    Tessellator tess = Tessellator.getInstance();
                    BufferBuilder worldRenderer = tess.getBuffer();
                    Matrix4f last = matrixStackIn.getLast().getMatrix();
                    worldRenderer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);

                    worldRenderer.pos(last, i / 24.0F + 0.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offset, offset).endVertex();
                    worldRenderer.pos(last, i / 24.0F + 0.0F / 24.0F, j / 24.0F + 1.0F / 24.0F + offset, offset).endVertex();
                    worldRenderer.pos(last, i / 24.0F + 1.0F / 24.0F, j / 24.0F + 1.0F / 24.0F + offsetAhead, offsetAhead).endVertex();

                    worldRenderer.pos(last, i / 24.0F + 0.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offset, offset).endVertex();
                    worldRenderer.pos(last, i / 24.0F + 1.0F / 24.0F, j / 24.0F + 1.0F / 24.0F + offsetAhead, offsetAhead).endVertex();
                    worldRenderer.pos(last, i / 24.0F + 1.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offsetAhead, offsetAhead).endVertex();

                    tess.draw();

                    RenderSystem.color3f(1, 1, 1);
                    matrixStackIn.pop();
                }
            }

            RenderSystem.enableTexture();
            RenderSystem.enableCull();

            matrixStackIn.pop();
        }
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
