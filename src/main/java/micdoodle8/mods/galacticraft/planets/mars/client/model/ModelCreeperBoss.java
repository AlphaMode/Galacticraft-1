package micdoodle8.mods.galacticraft.planets.mars.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCreeperBoss;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModelCreeperBoss extends EntityModel<EntityCreeperBoss>
{
    public int headsRemaining = 3;
    ModelRenderer headMain;
    ModelRenderer bodyMain;
    ModelRenderer rightLegFront;
    ModelRenderer leftLegFront;
    ModelRenderer rightLeg;
    ModelRenderer leftLeg;
    ModelRenderer oxygenTank;
    ModelRenderer headLeft;
    ModelRenderer headRight;
    ModelRenderer neckRight;
    ModelRenderer neckLeft;

    public ModelCreeperBoss()
    {
        this(0.0F);
    }

    public ModelCreeperBoss(float scale)
    {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.neckRight = new ModelRenderer(this, 16, 20);
        this.neckRight.mirror = true;
        this.neckRight.addBox(-2.5F, -9F, -1.5F, 5, 9, 3, scale);
        this.neckRight.setRotationPoint(-3F, 10F, 0F);
        this.neckRight.setTextureSize(128, 64);
        this.neckRight.mirror = true;
        this.setRotation(this.neckRight, 0F, 0F, -1.169371F);
        this.neckRight.mirror = false;
        this.neckLeft = new ModelRenderer(this, 16, 20);
        this.neckLeft.addBox(-2.5F, -9F, -1.5F, 5, 9, 3, scale);
        this.neckLeft.setRotationPoint(3F, 10F, 0F);
        this.neckLeft.setTextureSize(128, 64);
        this.neckLeft.mirror = true;
        this.setRotation(this.neckLeft, 0F, 0F, 1.169371F);
        this.headMain = new ModelRenderer(this, 0, 0);
        this.headMain.addBox(-4F, -8F, -4F, 8, 8, 8, scale);
        this.headMain.setRotationPoint(0F, 6F, 0F);
        this.headMain.setTextureSize(128, 64);
        this.headMain.mirror = true;
        this.setRotation(this.headMain, 0F, 0F, 0F);
        this.bodyMain = new ModelRenderer(this, 16, 16);
        this.bodyMain.addBox(-4F, 0F, -2F, 8, 12, 4, scale);
        this.bodyMain.setRotationPoint(0F, 6F, 0F);
        this.bodyMain.setTextureSize(128, 64);
        this.bodyMain.mirror = true;
        this.setRotation(this.bodyMain, 0F, 0F, 0F);
        this.rightLegFront = new ModelRenderer(this, 0, 16);
        this.rightLegFront.addBox(-2F, 0F, -2F, 4, 6, 4, scale);
        this.rightLegFront.setRotationPoint(-2F, 18F, -4F);
        this.rightLegFront.setTextureSize(128, 64);
        this.rightLegFront.mirror = true;
        this.setRotation(this.rightLegFront, 0F, 0F, 0F);
        this.leftLegFront = new ModelRenderer(this, 0, 16);
        this.leftLegFront.addBox(-2F, 0F, -2F, 4, 6, 4, scale);
        this.leftLegFront.setRotationPoint(2F, 18F, -4F);
        this.leftLegFront.setTextureSize(128, 64);
        this.leftLegFront.mirror = true;
        this.setRotation(this.leftLegFront, 0F, 0F, 0F);
        this.rightLeg = new ModelRenderer(this, 0, 16);
        this.rightLeg.addBox(0F, 0F, -2F, 4, 6, 4, scale);
        this.rightLeg.setRotationPoint(-4F, 18F, 4F);
        this.rightLeg.setTextureSize(128, 64);
        this.rightLeg.mirror = true;
        this.setRotation(this.rightLeg, 0F, 0F, 0F);
        this.leftLeg = new ModelRenderer(this, 0, 16);
        this.leftLeg.addBox(-2F, 0F, -2F, 4, 6, 4, scale);
        this.leftLeg.setRotationPoint(2F, 18F, 4F);
        this.leftLeg.setTextureSize(128, 64);
        this.leftLeg.mirror = true;
        this.setRotation(this.leftLeg, 0F, 0F, 0F);
        this.oxygenTank = new ModelRenderer(this, 40, 0);
        this.oxygenTank.addBox(-5F, -9F, -5F, 10, 10, 10, scale);
        this.oxygenTank.setRotationPoint(0F, 6F, 0F);
        this.oxygenTank.setTextureSize(128, 64);
        this.oxygenTank.mirror = true;
        this.setRotation(this.oxygenTank, 0F, 0F, 0F);
        this.headLeft = new ModelRenderer(this, 0, 0);
        this.headLeft.addBox(1F, -9F, -4F, 8, 8, 8, scale);
        this.headLeft.setRotationPoint(3F, 6F, 0.1F);
        this.headLeft.setTextureSize(128, 64);
        this.headLeft.mirror = true;
        this.setRotation(this.headLeft, 0F, 0F, 0.7853982F);
        this.headRight = new ModelRenderer(this, 0, 0);
        this.headRight.addBox(-9F, -9F, -4F, 8, 8, 8, scale);
        this.headRight.setRotationPoint(-3F, 6F, -0.1F);
        this.headRight.setTextureSize(128, 64);
        this.headRight.mirror = true;
        this.setRotation(this.headRight, 0F, 0F, -0.7853982F);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        if (this.headsRemaining > 2)
        {
            this.headLeft.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            this.neckLeft.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            this.headRight.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            this.neckRight.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            this.headMain.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            this.oxygenTank.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
        else if (this.headsRemaining > 1)
        {
            this.headRight.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            this.neckRight.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            this.headMain.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            this.oxygenTank.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
        else if (this.headsRemaining > 0)
        {
            this.headMain.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            this.oxygenTank.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }

        this.bodyMain.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightLegFront.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftLegFront.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightLeg.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftLeg.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(EntityCreeperBoss entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.headMain.rotateAngleY = netHeadYaw / Constants.RADIANS_TO_DEGREES;
        this.headMain.rotateAngleX = headPitch / Constants.RADIANS_TO_DEGREES;
        this.oxygenTank.rotateAngleY = netHeadYaw / Constants.RADIANS_TO_DEGREES;
        this.oxygenTank.rotateAngleX = headPitch / Constants.RADIANS_TO_DEGREES;
        this.rightLegFront.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLegFront.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2F * limbSwingAmount;
        this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2F * limbSwingAmount;
        this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2F * limbSwingAmount;
    }
}
