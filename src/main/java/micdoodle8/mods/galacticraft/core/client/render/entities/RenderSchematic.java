package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.entities.EntityHangingSchematic;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderSchematic extends EntityRenderer<EntityHangingSchematic>
{
    public RenderSchematic(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityHangingSchematic entity)
    {
        return SchematicRegistry.getSchematicTexture(entity.schematic);
    }

    @Override
    public void render(EntityHangingSchematic entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        float f = 0.0625F;
        matrixStackIn.scale(f, f, f);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntitySolid(this.getEntityTexture(entity)));
        this.renderPainting(entity, entity.getWidthPixels(), entity.getHeightPixels(), matrixStackIn, ivertexbuilder);
        matrixStackIn.pop();
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private void renderPainting(EntityHangingSchematic painting, int width, int height, MatrixStack matrixStackIn, IVertexBuilder ivertexbuilder)
    {
        MatrixStack.Entry last = matrixStackIn.getLast();
        Matrix4f matrix = last.getMatrix();
        Matrix3f normal = last.getNormal();
        float f = (float) (-width) / 2.0F;
        float f1 = (float) (-height) / 2.0F;
        float f2 = 0.5F;
        float f3 = 0.75F;
        float f4 = 0.8125F;
        float f5 = 0.0F;
        float f6 = 0.0625F;
        float f7 = 0.75F;
        float f8 = 0.8125F;
        float f9 = 0.001953125F;
        float f10 = 0.001953125F;
        float f11 = 0.7519531F;
        float f12 = 0.7519531F;
        float f13 = 0.0F;
        float f14 = 0.0625F;

        for (int i = 0; i < width / 16; ++i)
        {
            for (int j = 0; j < height / 16; ++j)
            {
                float a = f + (float) ((i + 1) * 16);
                float b = f + (float) (i * 16);
                float c = f1 + (float) ((j + 1) * 16);
                float d = f1 + (float) (j * 16);
                int lightmapUV = this.getLightmap(painting, (a + b) / 2.0F, (c + d) / 2.0F);
                float f19 = (float) (width - i * 16) / 32.0F;
                float f20 = (float) (width - (i + 1) * 16) / 32.0F;
                float f21 = (float) (height - j * 16) / 32.0F;
                float f22 = (float) (height - (j + 1) * 16) / 32.0F;
                ivertexbuilder.pos(matrix, a, d, (-f2)).color(255, 255, 255, 255).tex(f20, f21).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 0.0F, 0.0F, -1.0F).endVertex();
                ivertexbuilder.pos(matrix, b, d, (-f2)).color(255, 255, 255, 255).tex(f19, f21).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 0.0F, 0.0F, -1.0F).endVertex();
                ivertexbuilder.pos(matrix, b, c, (-f2)).color(255, 255, 255, 255).tex(f19, f22).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 0.0F, 0.0F, -1.0F).endVertex();
                ivertexbuilder.pos(matrix, a, c, (-f2)).color(255, 255, 255, 255).tex(f20, f22).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 0.0F, 0.0F, -1.0F).endVertex();
                ivertexbuilder.pos(matrix, a, c, f2).color(255, 255, 255, 255).tex(f3, f5).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 0.0F, 0.0F, 1.0F).endVertex();
                ivertexbuilder.pos(matrix, b, c, f2).color(255, 255, 255, 255).tex(f4, f5).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 0.0F, 0.0F, 1.0F).endVertex();
                ivertexbuilder.pos(matrix, b, d, f2).color(255, 255, 255, 255).tex(f4, f6).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 0.0F, 0.0F, 1.0F).endVertex();
                ivertexbuilder.pos(matrix, a, d, f2).color(255, 255, 255, 255).tex(f3, f6).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 0.0F, 0.0F, 1.0F).endVertex();
                ivertexbuilder.pos(matrix, a, c, (-f2)).color(255, 255, 255, 255).tex(f7, f9).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                ivertexbuilder.pos(matrix, b, c, (-f2)).color(255, 255, 255, 255).tex(f8, f9).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                ivertexbuilder.pos(matrix, b, c, f2).color(255, 255, 255, 255).tex(f8, f10).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                ivertexbuilder.pos(matrix, a, c, f2).color(255, 255, 255, 255).tex(f7, f10).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                ivertexbuilder.pos(matrix, a, d, f2).color(255, 255, 255, 255).tex(f7, f9).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 0.0F, -1.0F, 0.0F).endVertex();
                ivertexbuilder.pos(matrix, b, d, f2).color(255, 255, 255, 255).tex(f8, f9).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 0.0F, -1.0F, 0.0F).endVertex();
                ivertexbuilder.pos(matrix, b, d, (-f2)).color(255, 255, 255, 255).tex(f8, f10).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 0.0F, -1.0F, 0.0F).endVertex();
                ivertexbuilder.pos(matrix, a, d, (-f2)).color(255, 255, 255, 255).tex(f7, f10).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 0.0F, -1.0F, 0.0F).endVertex();
                ivertexbuilder.pos(matrix, a, c, f2).color(255, 255, 255, 255).tex(f12, f13).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, -1.0F, 0.0F, 0.0F).endVertex();
                ivertexbuilder.pos(matrix, a, d, f2).color(255, 255, 255, 255).tex(f12, f14).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, -1.0F, 0.0F, 0.0F).endVertex();
                ivertexbuilder.pos(matrix, a, d, (-f2)).color(255, 255, 255, 255).tex(f11, f14).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, -1.0F, 0.0F, 0.0F).endVertex();
                ivertexbuilder.pos(matrix, a, c, (-f2)).color(255, 255, 255, 255).tex(f11, f13).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, -1.0F, 0.0F, 0.0F).endVertex();
                ivertexbuilder.pos(matrix, b, c, (-f2)).color(255, 255, 255, 255).tex(f12, f13).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
                ivertexbuilder.pos(matrix, b, d, (-f2)).color(255, 255, 255, 255).tex(f12, f14).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
                ivertexbuilder.pos(matrix, b, d, f2).color(255, 255, 255, 255).tex(f11, f14).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
                ivertexbuilder.pos(matrix, b, c, f2).color(255, 255, 255, 255).tex(f11, f13).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
            }
        }
    }

    private void makeVertex(Matrix4f last, Matrix3f lastNormal, IVertexBuilder ivertexbuilder, float x, float y, float u, float v, float z, int normalX, int normalY, int normalZ, int lightmapUV) {
        ivertexbuilder.pos(last, x, y, z).color(255, 255, 255, 255).tex(u, v).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(lastNormal, (float)normalX, (float)normalY, (float)normalZ).endVertex();
    }

    private int getLightmap(HangingEntity painting, double p_77008_2_, double p_77008_3_)
    {
        int x = MathHelper.floor(painting.getPosX());
        int y = MathHelper.floor(painting.getPosY() + (p_77008_3_ / 16.0F));
        int z = MathHelper.floor(painting.getPosZ());
        Direction facing = painting.getHorizontalFacing();

        if (facing == Direction.NORTH)
        {
            x = MathHelper.floor(painting.getPosX() + (p_77008_2_ / 16.0F));
        }

        if (facing == Direction.WEST)
        {
            z = MathHelper.floor(painting.getPosZ() - (p_77008_2_ / 16.0F));
        }

        if (facing == Direction.SOUTH)
        {
            x = MathHelper.floor(painting.getPosX() - (p_77008_2_ / 16.0F));
        }

        if (facing == Direction.EAST)
        {
            z = MathHelper.floor(painting.getPosZ() + (p_77008_2_ / 16.0F));
        }

        return WorldRenderer.getCombinedLight(painting.world, new BlockPos(x, y, z));
    }
}
