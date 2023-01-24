package micdoodle8.mods.galacticraft.core.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelMeteor extends EntityModel<EntityMeteor>
{
    ModelRenderer[] shapes = new ModelRenderer[13];

    public ModelMeteor()
    {
        this.textureWidth = 128;
        this.textureHeight = 64;

        this.shapes[0] = new ModelRenderer(this, 0, 0);
        this.shapes[0].addBox(0F, -7F, -13F, 2, 4, 4);
        this.shapes[0].setRotationPoint(0F, 0F, 0F);
        this.shapes[0].setTextureSize(128, 64);
        this.shapes[0].mirror = true;
        this.setRotation(this.shapes[0], 0F, 0F, 0F);
        this.shapes[1] = new ModelRenderer(this, 0, 0);
        this.shapes[1].addBox(-10F, -10F, -10F, 20, 20, 20);
        this.shapes[1].setRotationPoint(0F, 0F, 0F);
        this.shapes[1].setTextureSize(128, 64);
        this.shapes[1].mirror = true;
        this.setRotation(this.shapes[1], 0F, 0F, 0F);
        this.shapes[2] = new ModelRenderer(this, 0, 0);
        this.shapes[2].addBox(-5F, -8F, -12F, 5, 9, 1);
        this.shapes[2].setRotationPoint(0F, 0F, 0F);
        this.shapes[2].setTextureSize(128, 64);
        this.shapes[2].mirror = true;
        this.setRotation(this.shapes[2], 0F, 0F, 0F);
        this.shapes[3] = new ModelRenderer(this, 0, 0);
        this.shapes[3].addBox(0F, -6F, 11F, 4, 13, 1);
        this.shapes[3].setRotationPoint(0F, 0F, 0F);
        this.shapes[3].setTextureSize(128, 64);
        this.shapes[3].mirror = true;
        this.setRotation(this.shapes[3], 0F, 0F, 0F);
        this.shapes[4] = new ModelRenderer(this, 0, 0);
        this.shapes[4].addBox(-9F, 10F, -9F, 18, 1, 18);
        this.shapes[4].setRotationPoint(0F, 0F, 0F);
        this.shapes[4].setTextureSize(128, 64);
        this.shapes[4].mirror = true;
        this.setRotation(this.shapes[4], 0F, 0F, 0F);
        this.shapes[5] = new ModelRenderer(this, 0, 0);
        this.shapes[5].addBox(11F, 3F, -8F, 1, 5, 5);
        this.shapes[5].setRotationPoint(0F, 0F, 0F);
        this.shapes[5].setTextureSize(128, 64);
        this.shapes[5].mirror = true;
        this.setRotation(this.shapes[5], 0F, 0F, 0F);
        this.shapes[6] = new ModelRenderer(this, 0, 0);
        this.shapes[6].addBox(-7F, -8F, 10F, 7, 12, 2);
        this.shapes[6].setRotationPoint(0F, 0F, 0F);
        this.shapes[6].setTextureSize(128, 64);
        this.shapes[6].mirror = true;
        this.setRotation(this.shapes[6], 0F, 0F, 0F);
        this.shapes[7] = new ModelRenderer(this, 0, 0);
        this.shapes[7].addBox(-9F, -9F, 10F, 18, 18, 1);
        this.shapes[7].setRotationPoint(0F, 0F, 0F);
        this.shapes[7].setTextureSize(128, 64);
        this.shapes[7].mirror = true;
        this.setRotation(this.shapes[7], 0F, 0F, 0F);
        this.shapes[8] = new ModelRenderer(this, 0, 0);
        this.shapes[8].addBox(-11F, -9F, -9F, 1, 18, 18);
        this.shapes[8].setRotationPoint(0F, 0F, 0F);
        this.shapes[8].setTextureSize(128, 64);
        this.shapes[8].mirror = true;
        this.setRotation(this.shapes[8], 0F, 0F, 0F);
        this.shapes[9] = new ModelRenderer(this, 0, 0);
        this.shapes[9].addBox(10F, -9F, -9F, 1, 18, 18);
        this.shapes[9].setRotationPoint(0F, 0F, 0F);
        this.shapes[9].setTextureSize(128, 64);
        this.shapes[9].mirror = true;
        this.setRotation(this.shapes[9], 0F, 0F, 0F);
        this.shapes[10] = new ModelRenderer(this, 0, 0);
        this.shapes[10].addBox(-9F, -9F, -11F, 18, 18, 1);
        this.shapes[10].setRotationPoint(0F, 0F, 0F);
        this.shapes[10].setTextureSize(128, 64);
        this.shapes[10].mirror = true;
        this.setRotation(this.shapes[10], 0F, 0F, 0F);
        this.shapes[11] = new ModelRenderer(this, 0, 0);
        this.shapes[11].addBox(-9F, -9F, -11F, 18, 18, 1);
        this.shapes[11].setRotationPoint(0F, 0F, 0F);
        this.shapes[11].setTextureSize(128, 64);
        this.shapes[11].mirror = true;
        this.setRotation(this.shapes[11], 0F, 0F, 0F);
        this.shapes[12] = new ModelRenderer(this, 0, 0);
        this.shapes[12].addBox(-9F, -11F, -9F, 18, 1, 18);
        this.shapes[12].setRotationPoint(0F, 0F, 0F);
        this.shapes[12].setTextureSize(128, 64);
        this.shapes[12].mirror = true;
        this.setRotation(this.shapes[12], 0F, 0F, 0F);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        for (final ModelRenderer shape : this.shapes)
        {
            shape.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }

    @Override
    public void setRotationAngles(EntityMeteor entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
