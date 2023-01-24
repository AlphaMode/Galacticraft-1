package micdoodle8.mods.galacticraft.core.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureUtil;

import java.awt.image.BufferedImage;

public class DynamicTextureProper extends DynamicTexture
{
    private boolean updateFlag = false;
    private final int width;    //We could transform these in the base class to protected
    private final int height;    //but whatever.

    public DynamicTextureProper(NativeImage img)
    {
        this(img.getWidth(), img.getHeight(), false);
        this.update(img);
    }

    public DynamicTextureProper(int width, int height, boolean clearIn)
    {
        super(width, height, clearIn);
        this.width = width;
        this.height = height;
    }

    public void update(NativeImage img)
    {
        deleteGlTexture();
        try {
            setTextureData(img);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> {
                TextureUtil.prepareImage(this.getGlTextureId(), img.getWidth(), img.getHeight());
                this.updateDynamicTexture();
            });
        } else {
            TextureUtil.prepareImage(this.getGlTextureId(), img.getWidth(), img.getHeight());
            this.updateDynamicTexture();
        }
        this.updateFlag = true;
    }

    @Override
    public int getGlTextureId()
    {
        if (this.updateFlag)
        {
            this.updateFlag = false;
            this.updateDynamicTexture();
        }
        return super.getGlTextureId();
    }
}
