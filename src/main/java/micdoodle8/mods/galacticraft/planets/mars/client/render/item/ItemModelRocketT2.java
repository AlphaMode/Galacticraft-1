package micdoodle8.mods.galacticraft.planets.mars.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class ItemModelRocketT2 extends ModelTransformWrapper
{
    public ItemModelRocketT2(IBakedModel modelToWrap)
    {
        super(modelToWrap);
    }

    @Override
    protected boolean getTransformForPerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat)
    {
        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
        {
            float scale = 0.13F;
            mat.push();
            mat.translate(0.275F, -0.15F, 0.0F);
            mat.rotate(new Quaternion(0.0F, -45.0F, 0.0F, true));
            mat.rotate(new Quaternion(65.0F, 0.0F, 0.0F, true));
            mat.rotate(new Quaternion(0.0F, ClientUtil.getClientTimeTotal() / 1000.0F, 0.0F, false));
            mat.scale(scale, scale, scale);
            mat.translate(0.5D, 0.5D, 0.5D);
            return true;
        }

        if (cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND)
        {
            mat.push();
            mat.rotate(new Quaternion(0.0F, 45.0F, 0.0F, true));
            mat.scale(0.5F, 0.5F, 0.5F);
            mat.rotate(new Quaternion(Constants.halfPI, 0.0F, 0.0F, false));
            mat.rotate(new Quaternion(0.0F, 0.0F, -0.65F, false));
            mat.translate(0.5F, -3.2F, -2.6F);
            return true;
        }

        if (cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND || cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND)
        {
            mat.push();
            mat.rotate(new Quaternion(75.0F, 0.0F, 0.0F, true));
            mat.scale(0.5F, 0.5F, 0.5F);
            mat.rotate(new Quaternion(0.0F, 0.0F, -Constants.halfPI, false));
            mat.rotate(new Quaternion(0.0F, Constants.halfPI, 0.0F, false));
            mat.rotate(new Quaternion(0.2F, 0.0F, 0.0F, false));
            mat.rotate(new Quaternion(0.0F, 0.0F, 0.5F, false));
            mat.rotate(new Quaternion(0.0F, 0.0F, -0.65F, false));
            mat.translate(0.4F, -2.8F, 1.2F);
            return true;
        }

        if (cameraTransformType == ItemCameraTransforms.TransformType.GROUND)
        {
            mat.push();
            mat.scale(0.1F, 0.1F, 0.1F);
            mat.translate(0.5F, 0.0F, 0.5F);
            return true;
        }

        if (cameraTransformType == ItemCameraTransforms.TransformType.FIXED)
        {
            mat.push();
            mat.scale(0.135F, 0.135F, 0.135F);
            mat.translate(0.5F, -2.75F, 0.5F);
            return true;
        }

//        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
//        {
//            Vector3f trans = new Vector3f(-0.15F, 0.0F, -0.15F);
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(30, 225, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.5F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(-0.6F, -0.7F, 0.0F));
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY(Constants.halfPI);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotX((float) (Math.PI / 4.0F));
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(trans);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY(ClientUtil.getClientTimeTotal() / 1000.0F);
//            ret.mul(mul);
//            mul.setIdentity();
//            trans.scale(-1.0F);
//            mul.setTranslation(trans);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.3F);
//            ret.mul(mul);
//            return true;
//        }
//
//        if (cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND ||
//                cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND)
//        {
//            Vector3f trans = new Vector3f(0.5F, 4.2F, -3.6F);
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, 45, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.5F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotX(Constants.halfPI);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotZ((float) (-0.65F + Math.PI));
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(trans);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotX((float) (Math.PI));
//            ret.mul(mul);
//            return true;
//        }
//
//        if (cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND ||
//                cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND)
//        {
//            Vector3f trans = new Vector3f(0.0F, -3.9F, 1.45F);
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(75, 15, 5));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.5F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotX((float) (Math.PI / 2.0F));
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotZ((float) (-Math.PI / 2.0F));
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotX(0.3F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(trans);
//            ret.mul(mul);
//            return true;
//        }
//
//        if (cameraTransformType == ItemCameraTransforms.TransformType.GROUND)
//        {
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            mul.setScale(0.1F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.5F, 0.0F, 0.5F));
//            ret.mul(mul);
//            return true;
//        }
//        if (cameraTransformType == ItemCameraTransforms.TransformType.FIXED)
//        {
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            mul.setScale(0.135F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY(0.0F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.5F, -2.75F, 0.5F));
//            ret.mul(mul);
//            return true;
//        } TODO Item models

        return false;
    }
}
