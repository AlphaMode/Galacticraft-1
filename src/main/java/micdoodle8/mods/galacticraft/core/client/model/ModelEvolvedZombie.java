package micdoodle8.mods.galacticraft.core.client.model;

import com.google.common.collect.ImmutableList;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModelEvolvedZombie extends ZombieModel<EntityEvolvedZombie>
{
    ModelRenderer leftOxygenTank;
    ModelRenderer rightOxygenTank;
    ModelRenderer tubeRight2;
    ModelRenderer tubeLeft1;
    ModelRenderer tubeRight3;
    ModelRenderer tubeRight4;
    ModelRenderer tubeRight5;
    ModelRenderer tubeLeft6;
    ModelRenderer tubeRight7;
    ModelRenderer tubeRight1;
    ModelRenderer tubeLeft2;
    ModelRenderer tubeLeft3;
    ModelRenderer tubeLeft4;
    ModelRenderer tubeLeft5;
    ModelRenderer tubeLeft7;
    ModelRenderer tubeRight6;
    ModelRenderer tubeLeft8;
    ModelRenderer oxygenMask;
    private float saveGravity;
    private final boolean renderGear;

    public ModelEvolvedZombie(boolean renderGear)
    {
        this(0.0F, false, renderGear);
    }

    public ModelEvolvedZombie(float par1, boolean halfSizeTexture, boolean renderGear)
    {
        super(par1, 0.0F, halfSizeTexture ? 64 : 128, halfSizeTexture ? 32 : 64);
//        this.textureWidth = halfSizeTexture ? 64 : 128;
//        this.textureHeight = halfSizeTexture ? 32 : 64;
        this.renderGear = renderGear;
        final int par2 = 0;
        this.leftOxygenTank = new ModelRenderer(this, 56, 20);
        this.leftOxygenTank.addBox(-1.5F, 0F, -1.5F, 3, 7, 3, par1);
        this.leftOxygenTank.setRotationPoint(2F, 2F, 3.8F);
        this.leftOxygenTank.mirror = true;
        this.setRotation(this.leftOxygenTank, 0F, 0F, 0F);
        this.rightOxygenTank = new ModelRenderer(this, 56, 20);
        this.rightOxygenTank.addBox(-1.5F, 0F, -1.5F, 3, 7, 3, par1);
        this.rightOxygenTank.setRotationPoint(-2F, 2F, 3.8F);
        this.rightOxygenTank.mirror = true;
        this.setRotation(this.rightOxygenTank, 0F, 0F, 0F);
        this.tubeRight2 = new ModelRenderer(this, 56, 30);
        this.tubeRight2.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight2.setRotationPoint(-2F, 2F, 6.8F);
        this.tubeRight2.mirror = true;
        this.setRotation(this.tubeRight2, 0F, 0F, 0F);
        this.tubeLeft1 = new ModelRenderer(this, 56, 30);
        this.tubeLeft1.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft1.setRotationPoint(2F, 3F, 5.8F);
        this.tubeLeft1.mirror = true;
        this.setRotation(this.tubeLeft1, 0F, 0F, 0F);
        this.tubeRight3 = new ModelRenderer(this, 56, 30);
        this.tubeRight3.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight3.setRotationPoint(-2F, 1F, 6.8F);
        this.tubeRight3.mirror = true;
        this.setRotation(this.tubeRight3, 0F, 0F, 0F);
        this.tubeRight4 = new ModelRenderer(this, 56, 30);
        this.tubeRight4.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight4.setRotationPoint(-2F, 0F, 6.8F);
        this.tubeRight4.mirror = true;
        this.setRotation(this.tubeRight4, 0F, 0F, 0F);
        this.tubeRight5 = new ModelRenderer(this, 56, 30);
        this.tubeRight5.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight5.setRotationPoint(-2F, -1F, 6.8F);
        this.tubeRight5.mirror = true;
        this.setRotation(this.tubeRight5, 0F, 0F, 0F);
        this.tubeLeft6 = new ModelRenderer(this, 56, 30);
        this.tubeLeft6.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft6.setRotationPoint(2F, -2F, 5.8F);
        this.tubeLeft6.mirror = true;
        this.setRotation(this.tubeLeft6, 0F, 0F, 0F);
        this.tubeRight7 = new ModelRenderer(this, 56, 30);
        this.tubeRight7.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight7.setRotationPoint(-2F, -3F, 4.8F);
        this.tubeRight7.mirror = true;
        this.setRotation(this.tubeRight7, 0F, 0F, 0F);
        this.tubeRight1 = new ModelRenderer(this, 56, 30);
        this.tubeRight1.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight1.setRotationPoint(-2F, 3F, 5.8F);
        this.tubeRight1.mirror = true;
        this.setRotation(this.tubeRight1, 0F, 0F, 0F);
        this.tubeLeft2 = new ModelRenderer(this, 56, 30);
        this.tubeLeft2.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft2.setRotationPoint(2F, 2F, 6.8F);
        this.tubeLeft2.mirror = true;
        this.setRotation(this.tubeLeft2, 0F, 0F, 0F);
        this.tubeLeft3 = new ModelRenderer(this, 56, 30);
        this.tubeLeft3.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft3.setRotationPoint(2F, 1F, 6.8F);
        this.tubeLeft3.mirror = true;
        this.setRotation(this.tubeLeft3, 0F, 0F, 0F);
        this.tubeLeft4 = new ModelRenderer(this, 56, 30);
        this.tubeLeft4.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft4.setRotationPoint(2F, 0F, 6.8F);
        this.tubeLeft4.mirror = true;
        this.setRotation(this.tubeLeft4, 0F, 0F, 0F);
        this.tubeLeft5 = new ModelRenderer(this, 56, 30);
        this.tubeLeft5.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft5.setRotationPoint(2F, -1F, 6.8F);
        this.tubeLeft5.mirror = true;
        this.setRotation(this.tubeLeft5, 0F, 0F, 0F);
        this.tubeLeft7 = new ModelRenderer(this, 56, 30);
        this.tubeLeft7.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft7.setRotationPoint(2F, -3F, 4.8F);
        this.tubeLeft7.mirror = true;
        this.setRotation(this.tubeLeft7, 0F, 0F, 0F);
        this.tubeRight6 = new ModelRenderer(this, 56, 30);
        this.tubeRight6.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight6.setRotationPoint(-2F, -2F, 5.8F);
        this.tubeRight6.mirror = true;
        this.setRotation(this.tubeRight6, 0F, 0F, 0F);
        this.tubeLeft8 = new ModelRenderer(this, 56, 30);
        this.tubeLeft8.addBox(0F, 0F, 0F, 1, 1, 1, par1);
        this.tubeLeft8.setRotationPoint(0F, -5F, 0F);
        this.tubeLeft8.mirror = true;
        this.setRotation(this.tubeLeft8, 0F, 0F, 0F);
        this.oxygenMask = new ModelRenderer(this, 56, 0);
        this.oxygenMask.addBox(-5F, -9F, -5F, 10, 10, 10, par1);
        this.oxygenMask.setRotationPoint(0F, 0F, 0F);
        this.oxygenMask.mirror = true;
        this.setRotation(this.oxygenMask, 0F, 0F, 0F);
//        this.bipedCloak = new ModelRenderer(this, 0, 0);
//        this.bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, par1);
//        this.bipedEars = new ModelRenderer(this, 24, 0);
//        this.bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, par1);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1);
        this.bipedHead.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1 + 0.5F);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, par1);
        this.bipedBody.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, par1);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + par2, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, par1);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + par2, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, par1);
        this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F + par2, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, par1);
        this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F + par2, 0.0F);
    }

    protected Iterable<ModelRenderer> getHeadParts() {
        return ImmutableList.of(this.bipedHead, oxygenMask);
    }

    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.bipedBody, this.bipedRightArm, this.bipedLeftArm, this.bipedRightLeg, this.bipedLeftLeg, this.bipedHeadwear, leftOxygenTank, rightOxygenTank, tubeLeft1, tubeLeft2, tubeLeft3, tubeLeft4, tubeLeft5, tubeLeft6, tubeLeft7, tubeRight1, tubeRight2, tubeRight3, tubeRight4, tubeRight5, tubeRight6, tubeRight7);
    }

    public boolean isAggressive(EntityEvolvedZombie entityIn) {
        return entityIn.isAggressive();
    }

//    @Override
//    public void render(EntityEvolvedZombie entity, float f, float f1, float f2, float f3, float f4, float f5)
//    {
//        //		super.render(entity, f, f1, f2, f3, f4, f5);
//        this.saveGravity = WorldUtil.getGravityFactor(entity);
//        this.setRotationAngles(entity, f, f1, f2, f3, f4, f5);
//
//        if (this.isChild)
//        {
//            float f6 = 2.0F;
//            GL11.glPushMatrix();
//            GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
//            GL11.glTranslatef(0.0F, 16.0F * f5, 0.0F);
//            this.bipedHead.render(f5);
//            if (this.renderGear)
//            {
//                this.oxygenMask.render(f5);
//            }
//            GL11.glPopMatrix();
//            GL11.glPushMatrix();
//            GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
//            GL11.glTranslatef(0.0F, 24.0F * f5, 0.0F);
//            if (this.renderGear)
//            {
//                this.leftOxygenTank.render(f5);
//                this.rightOxygenTank.render(f5);
//                this.tubeRight2.render(f5);
//                this.tubeLeft1.render(f5);
//                this.tubeRight3.render(f5);
//                this.tubeRight4.render(f5);
//                this.tubeRight5.render(f5);
//                this.tubeLeft6.render(f5);
//                this.tubeRight7.render(f5);
//                this.tubeRight1.render(f5);
//                this.tubeLeft2.render(f5);
//                this.tubeLeft3.render(f5);
//                this.tubeLeft4.render(f5);
//                this.tubeLeft5.render(f5);
//                this.tubeLeft7.render(f5);
//                this.tubeRight6.render(f5);
//                this.tubeLeft8.render(f5);
//            }
//            this.bipedBody.render(f5);
//            this.bipedRightArm.render(f5);
//            this.bipedLeftArm.render(f5);
//            this.bipedRightLeg.render(f5);
//            this.bipedLeftLeg.render(f5);
//            this.bipedHeadwear.render(f5);
//            GL11.glPopMatrix();
//        }
//        else
//        {
//            if (this.renderGear)
//            {
//                this.leftOxygenTank.render(f5);
//                this.rightOxygenTank.render(f5);
//                this.tubeRight2.render(f5);
//                this.tubeLeft1.render(f5);
//                this.tubeRight3.render(f5);
//                this.tubeRight4.render(f5);
//                this.tubeRight5.render(f5);
//                this.tubeLeft6.render(f5);
//                this.tubeRight7.render(f5);
//                this.tubeRight1.render(f5);
//                this.tubeLeft2.render(f5);
//                this.tubeLeft3.render(f5);
//                this.tubeLeft4.render(f5);
//                this.tubeLeft5.render(f5);
//                this.tubeLeft7.render(f5);
//                this.tubeRight6.render(f5);
//                this.tubeLeft8.render(f5);
//                this.oxygenMask.render(f5);
//            }
//            this.bipedHead.render(f5);
//            this.bipedBody.render(f5);
//            this.bipedRightArm.render(f5);
//            this.bipedLeftArm.render(f5);
//            this.bipedRightLeg.render(f5);
//            this.bipedLeftLeg.render(f5);
//            this.bipedHeadwear.render(f5);
//        }
//    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(EntityEvolvedZombie entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float f = MathHelper.sin(this.swingProgress * (float) Math.PI);
        float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;
        this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
        this.bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
        this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F);
        this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F);
        this.bipedRightArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
        this.bipedLeftArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
        this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

        ClientUtil.copyModelAngles(this.bipedHead, this.oxygenMask);
    }

//    @Override
////    public void setRotationAngles(EntityEvolvedZombie entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
//    public void setRotationAngles(EntityEvolvedZombie entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
//    {
//        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
//        float f = MathHelper.sin(this.swingProgress * (float) Math.PI);
//        float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
//        this.bipedRightArm.rotateAngleZ = 0.0F;
//        this.bipedLeftArm.rotateAngleZ = 0.0F;
//        this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
//        this.bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
//        this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F);
//        this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F);
//        this.bipedRightArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
//        this.bipedLeftArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
//        this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
//        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
//        this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
//        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
//
//        ClientUtil.copyModelAngles(this.bipedHead, this.oxygenMask);
//    }

//    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6)
//    {
//        this.bipedHead.rotateAngleY = par4 / Constants.RADIANS_TO_DEGREES;
//        this.bipedHead.rotateAngleX = par5 / Constants.RADIANS_TO_DEGREES;
//        this.oxygenMask.rotateAngleY = par4 / Constants.RADIANS_TO_DEGREES;
//        this.oxygenMask.rotateAngleX = par5 / Constants.RADIANS_TO_DEGREES;
//        this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
//        this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
//        this.bipedRightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 2.0F * par2 * 0.5F;
//        this.bipedLeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
//        this.bipedRightArm.rotateAngleZ = 0.0F;
//        this.bipedLeftArm.rotateAngleZ = 0.0F;
//        this.bipedRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F / this.saveGravity) * (1.5F - this.saveGravity) * par2;
//        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F / this.saveGravity + (float) Math.PI) * (1.5F - this.saveGravity) * par2;
//        this.bipedRightLeg.rotateAngleY = 0.0F;
//        this.bipedLeftLeg.rotateAngleY = 0.0F;
//
//        if (this.isRiding)
//        {
//            this.bipedRightArm.rotateAngleX += -((float) Math.PI / 5F);
//            this.bipedLeftArm.rotateAngleX += -((float) Math.PI / 5F);
//            this.bipedRightLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
//            this.bipedLeftLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
//            this.bipedRightLeg.rotateAngleY = (float) Math.PI / 10F;
//            this.bipedLeftLeg.rotateAngleY = -((float) Math.PI / 10F);
//        }
//
//        this.bipedRightArm.rotateAngleY = 0.0F;
//        this.bipedLeftArm.rotateAngleY = 0.0F;
//        float var7;
//        float var8;
//
//        if (this.onGround > -9990.0F)
//        {
//            var7 = this.onGround;
//            this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(var7) * (float) Math.PI * 2.0F) * 0.2F;
//            this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
//            this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
//            this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
//            this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
//            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
//            this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
//            this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
//            var7 = 1.0F - this.onGround;
//            var7 *= var7;
//            var7 *= var7;
//            var7 = 1.0F - var7;
//            var8 = MathHelper.sin(var7 * (float) Math.PI);
//            final float var9 = MathHelper.sin(this.onGround * (float) Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
//            this.bipedRightArm.rotateAngleX = (float) (this.bipedRightArm.rotateAngleX - (var8 * 1.2D + var9));
//            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
//            this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * (float) Math.PI) * -0.4F;
//        }
//
//        this.bipedBody.rotateAngleX = 0.0F;
//        this.bipedRightLeg.rotationPointZ = 0.0F;
//        this.bipedLeftLeg.rotationPointZ = 0.0F;
//        this.bipedRightLeg.rotationPointY = 12.0F;
//        this.bipedLeftLeg.rotationPointY = 12.0F;
//        this.bipedHead.rotationPointY = 0.0F;
//
//        this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
//        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
//        this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
//        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
//
//        final float var7a = MathHelper.sin(this.onGround * (float) Math.PI);
//        final float var8a = MathHelper.sin((1.0F - (1.0F - this.onGround) * (1.0F - this.onGround)) * (float) Math.PI);
//
//        this.bipedRightArm.rotateAngleZ = 0.0F;
//        this.bipedLeftArm.rotateAngleZ = 0.0F;
//        this.bipedRightArm.rotateAngleY = -(0.1F - var7a * 0.6F);
//        this.bipedLeftArm.rotateAngleY = 0.1F - var7a * 0.6F;
//        this.bipedRightArm.rotateAngleX = -(Constants.halfPI);
//        this.bipedLeftArm.rotateAngleX = -(Constants.halfPI);
//        this.bipedRightArm.rotateAngleX -= var7a * 1.2F - var8a * 0.4F;
//        this.bipedLeftArm.rotateAngleX -= var7a * 1.2F - var8a * 0.4F;
//        this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
//        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
//        this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
//        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
//    }
}
