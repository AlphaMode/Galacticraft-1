package micdoodle8.mods.galacticraft.core.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.entities.EntityAlienVillager;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.entity.model.VillagerModel;
import net.minecraft.entity.Entity;

public class ModelAlienVillager extends VillagerModel<EntityAlienVillager>
{
    public ModelRenderer brain;

    public ModelAlienVillager(float scale)
    {
        this(scale, 0.0F, 64, 64);
    }

    public ModelAlienVillager(float scale, float par2, int par3, int par4)
    {
        super(scale, 0, 0);
        this.villagerHead = new ModelRenderer(this).setTextureSize(par3, par4);
        this.villagerHead.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        this.villagerHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, scale + 0.001F);
//        this.villagerNose = new ModelRenderer(this).setTextureSize(par3, par4);
//        this.villagerNose.setRotationPoint(0.0F, par2 - 2.0F, 0.0F);
//        this.villagerNose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, scale + 0.002F);
//        this.villagerHead.addChild(this.villagerNose);
//        this.villagerBody = new ModelRenderer(this).setTextureSize(par3, par4);
//        this.villagerBody.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
//        this.villagerBody.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, scale + 0.003F);
//        this.villagerBody.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, scale + 0.5F + 0.004F);
//        this.villagerArms = new ModelRenderer(this).setTextureSize(par3, par4);
//        this.villagerArms.setRotationPoint(0.0F, 0.0F + par2 + 2.0F, 0.0F);
//        this.villagerArms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, scale + 0.005F);
//        this.villagerArms.setTextureOffset(44, 22).addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, scale + 0.0001F);
//        this.villagerArms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, scale + 0.0004F);
//        this.rightVillagerLeg = new ModelRenderer(this, 0, 22).setTextureSize(par3, par4);
//        this.rightVillagerLeg.setRotationPoint(-2.0F, 12.0F + par2, 0.0F);
//        this.rightVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale + 0.0006F);
//        this.leftVillagerLeg = new ModelRenderer(this, 0, 22).setTextureSize(par3, par4);
//        this.leftVillagerLeg.mirror = true;
//        this.leftVillagerLeg.setRotationPoint(2.0F, 12.0F + par2, 0.0F);
//        this.leftVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale + 0.0002F);
        this.brain = new ModelRenderer(this).setTextureSize(par3, par4);
        this.brain.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        this.brain.setTextureOffset(32, 0).addBox(-4.0F, -16.0F, -4.0F, 8, 8, 8, scale + 0.5F);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);

        this.brain.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setRotationAngles(EntityAlienVillager par7Entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        super.setRotationAngles(par7Entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        this.brain.rotateAngleX = this.villagerHead.rotateAngleX;
        this.brain.rotateAngleY = this.villagerHead.rotateAngleY;
        this.brain.rotateAngleZ = this.villagerHead.rotateAngleZ;
    }
}
