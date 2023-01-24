package micdoodle8.mods.galacticraft.planets.mars.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySludgeling;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModelSludgeling extends EntityModel<EntitySludgeling>
{
    ModelRenderer tail4;
    ModelRenderer body;
    ModelRenderer tail1;
    ModelRenderer tail2;
    ModelRenderer tail3;

    public ModelSludgeling()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;

        this.tail4 = new ModelRenderer(this, 0, 0);
        this.tail4.addBox(-0.5F, 0.3F, 4.5F, 1, 1, 1);
        this.tail4.setRotationPoint(0F, 23.5F, -2F);
        this.tail4.setTextureSize(64, 32);
        this.tail4.mirror = true;
        this.setRotation(this.tail4, 0F, 0F, 0F);
        this.body = new ModelRenderer(this, 4, 0);
        this.body.addBox(-1F, -0.5F, -1.5F, 2, 1, 3);
        this.body.setRotationPoint(0F, 23.5F, -2F);
        this.body.setTextureSize(64, 32);
        this.body.mirror = true;
        this.setRotation(this.body, 0F, 0F, 0F);
        this.tail1 = new ModelRenderer(this, 0, 0);
        this.tail1.addBox(-0.5F, -0.3F, 1.5F, 1, 1, 1);
        this.tail1.setRotationPoint(0F, 23.5F, -2F);
        this.tail1.setTextureSize(64, 32);
        this.tail1.mirror = true;
        this.setRotation(this.tail1, 0F, 0F, 0F);
        this.tail2 = new ModelRenderer(this, 0, 0);
        this.tail2.addBox(-0.5F, -0.1F, 2.5F, 1, 1, 1);
        this.tail2.setRotationPoint(0F, 23.5F, -2F);
        this.tail2.setTextureSize(64, 32);
        this.tail2.mirror = true;
        this.setRotation(this.tail2, 0F, 0F, 0F);
        this.tail3 = new ModelRenderer(this, 0, 0);
        this.tail3.addBox(-0.5F, 0.1F, 3.5F, 1, 1, 1);
        this.tail3.setRotationPoint(0F, 23.5F, -2F);
        this.tail3.setTextureSize(64, 32);
        this.tail3.mirror = true;
        this.setRotation(this.tail3, 0F, 0F, 0F);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tail1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tail2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tail3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tail4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(EntitySludgeling sludgeling, float f, float f1, float f2, float f3, float f4)
    {
        this.tail1.rotateAngleY = MathHelper.cos(f2 * 0.3F + 0 * 0.15F * (float) Math.PI) * (float) Math.PI * 0.025F * (1 + Math.abs(0 - 2));
        this.tail2.rotateAngleY = MathHelper.cos(f2 * 0.3F + 1 * 0.15F * (float) Math.PI) * (float) Math.PI * 0.025F * (1 + Math.abs(1 - 2));
        this.tail3.rotateAngleY = MathHelper.cos(f2 * 0.3F + 2 * 0.15F * (float) Math.PI) * (float) Math.PI * 0.025F * (1 + Math.abs(1 - 2));
        this.tail4.rotateAngleY = MathHelper.cos(f2 * 0.3F + 3 * 0.15F * (float) Math.PI) * (float) Math.PI * 0.025F * (1 + Math.abs(1 - 2));
    }
}
