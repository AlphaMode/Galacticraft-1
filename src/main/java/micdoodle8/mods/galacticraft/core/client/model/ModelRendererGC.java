package micdoodle8.mods.galacticraft.core.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelRendererGC extends ModelRenderer
{
    public ModelRendererGC(Model par1ModelBase, int par2, int par3)
    {
        super(par1ModelBase, par2, par3);
    }

    @Override
    public void translateRotate(MatrixStack matrixStackIn) {
        matrixStackIn.translate(this.rotationPointX / 16.0F, this.rotationPointY / 16.0F, this.rotationPointZ / 16.0F);
        if (this.rotateAngleY != 0.0F) {
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(this.rotateAngleY * Constants.RADIANS_TO_DEGREES));
        }

        if (this.rotateAngleZ != 0.0F) {
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(this.rotateAngleZ * Constants.RADIANS_TO_DEGREES));
        }

        if (this.rotateAngleX != 0.0F) {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(this.rotateAngleX * Constants.RADIANS_TO_DEGREES));
        }

    }
}
