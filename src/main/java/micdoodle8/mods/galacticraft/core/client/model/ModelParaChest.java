package micdoodle8.mods.galacticraft.core.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderParaChest;
import micdoodle8.mods.galacticraft.core.entities.EntityParachest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class ModelParaChest extends EntityModel<EntityParachest>
{
    public ModelRenderer chestLid = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
    public ModelRenderer chestBelow;
    public ModelRenderer chestKnob;
    private static final ResourceLocation grayParachuteTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/gray.png");

    public ModelRenderer[] parachute = new ModelRenderer[3];
    public ModelRenderer[] parachuteStrings = new ModelRenderer[4];

    public ModelParaChest()
    {
        this(0.0F);
    }

    public ModelParaChest(float par1)
    {
        this.chestLid.addBox(0.0F, -5.0F, -14.0F, 14, 5, 14, 0.0F);
        this.chestLid.rotationPointX = 1.0F;
        this.chestLid.rotationPointY = 7.0F;
        this.chestLid.rotationPointZ = 15.0F;
        this.chestKnob = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        this.chestKnob.addBox(-1.0F, -2.0F, -15.0F, 2, 4, 1, 0.0F);
        this.chestKnob.rotationPointX = 8.0F;
        this.chestKnob.rotationPointY = 7.0F;
        this.chestKnob.rotationPointZ = 15.0F;
        this.chestBelow = (new ModelRenderer(this, 0, 19)).setTextureSize(64, 64);
        this.chestBelow.addBox(0.0F, 0.0F, 0.0F, 14, 10, 14, 0.0F);
        this.chestBelow.rotationPointX = 1.0F;
        this.chestBelow.rotationPointY = 6.0F;
        this.chestBelow.rotationPointZ = 1.0F;

        this.parachute[0] = new ModelRenderer(this, 0, 0).setTextureSize(256, 256);
        this.parachute[0].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, par1);
        this.parachute[0].setRotationPoint(15.0F, 4.0F, 0.0F);
        this.parachute[1] = new ModelRenderer(this, 0, 42).setTextureSize(256, 256);
        this.parachute[1].addBox(-20.0F, -45.0F, -20.0F, 40, 2, 40, par1);
        this.parachute[1].setRotationPoint(0.0F, 0.0F, 0.0F);
        this.parachute[2] = new ModelRenderer(this, 0, 0).setTextureSize(256, 256);
        this.parachute[2].addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40, par1);
        this.parachute[2].setRotationPoint(11F, -11, 0.0F);

        this.parachuteStrings[0] = new ModelRenderer(this, 100, 0).setTextureSize(256, 256);
        this.parachuteStrings[0].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, par1);
        this.parachuteStrings[0].setRotationPoint(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[1] = new ModelRenderer(this, 100, 0).setTextureSize(256, 256);
        this.parachuteStrings[1].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, par1);
        this.parachuteStrings[1].setRotationPoint(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[2] = new ModelRenderer(this, 100, 0).setTextureSize(256, 256);
        this.parachuteStrings[2].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, par1);
        this.parachuteStrings[2].setRotationPoint(0.0F, 0.0F, 0.0F);
        this.parachuteStrings[3] = new ModelRenderer(this, 100, 0).setTextureSize(256, 256);
        this.parachuteStrings[3].addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1, par1);
        this.parachuteStrings[3].setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    @Override
    public void setRotationAngles(EntityParachest entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
//        this.chestLid.rotateAngleX = (float) Math.PI;
//        this.chestBelow.rotateAngleX = (float) Math.PI;
//        this.chestKnob.rotateAngleX = (float) Math.PI;
//
//        this.chestLid.rotationPointX = 2.0F;
//        this.chestLid.rotationPointY = 7.0F;
//        this.chestLid.rotationPointZ = -6.0F;
//        this.chestKnob.rotationPointX = 9.0F;
//        this.chestKnob.rotationPointY = 7.0F;
//        this.chestKnob.rotationPointZ = -6.0F;
//        this.chestBelow.rotationPointX = 2.0F;
//        this.chestBelow.rotationPointY = 8.0F;
//        this.chestBelow.rotationPointZ = 8.0F;
        this.chestKnob.rotateAngleX = this.chestLid.rotateAngleX;
        this.chestLid.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.chestKnob.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.chestBelow.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);

        int i;

        for (i = 0; i < this.parachute.length; i++)
        {
            this.parachute[i].render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }

        for (i = 0; i < this.parachuteStrings.length; i++)
        {
            this.parachuteStrings[i].render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }

        this.parachute[0].rotateAngleY = 0 / Constants.RADIANS_TO_DEGREES;
        this.parachute[2].rotateAngleY = -(0 / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[0].rotateAngleY = 0 / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[1].rotateAngleY = 0 / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[2].rotateAngleY = -(0 / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[3].rotateAngleY = -(0 / Constants.RADIANS_TO_DEGREES);

        this.parachute[0].setRotationPoint(-5.85F, -11.0F, 2.0F);
        this.parachute[1].setRotationPoint(9F, -7F, 2.0F);
        this.parachute[2].setRotationPoint(-2.15F, 4.0F, 2.0F);
        this.parachute[0].rotateAngleZ = 210F / Constants.RADIANS_TO_DEGREES;
        this.parachute[1].rotateAngleZ = 180F / Constants.RADIANS_TO_DEGREES;
        this.parachute[2].rotateAngleZ = -(210F / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[0].rotateAngleZ = (155F + 180F) / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[0].rotateAngleX = 23F / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[0].setRotationPoint(9.0F, 3.0F, 2.0F);
        this.parachuteStrings[1].rotateAngleZ = (155F + 180F) / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[1].rotateAngleX = -(23F / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[1].setRotationPoint(9.0F, 3.0F, 2.0F);

        this.parachuteStrings[2].rotateAngleZ = -((155F + 180F) / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[2].rotateAngleX = 23F / Constants.RADIANS_TO_DEGREES;
        this.parachuteStrings[2].setRotationPoint(9.0F, 3.0F, 2.0F);
        this.parachuteStrings[3].rotateAngleZ = -((155F + 180F) / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[3].rotateAngleX = -(23F / Constants.RADIANS_TO_DEGREES);
        this.parachuteStrings[3].setRotationPoint(9.0F, 3.0F, 2.0F);

        Minecraft.getInstance().textureManager.bindTexture(RenderParaChest.PARACHEST_TEXTURE);
    }
}
