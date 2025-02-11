package micdoodle8.mods.galacticraft.api.client;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface IGameScreen
{
    /**
     * Set up the frame edge size for this screen.  This
     * part must not be drawn when the screen is rendered.
     * <p>
     * Typical size: 0.1F for a screen which can be 1.0F x 1.0F or larger
     * <p>
     * (This will be called only once during screen initialisation.)
     *
     * @param frameSize
     */
    void setFrameSize(float frameSize);

    /**
     * Draw a screen in the XY plane with z == 0.
     * Must fit in the coordinate range (0, 0, 0) - (scaleX, scaleY, 0)
     * <p>
     * Register your IGameScreen by calling DrawGameScreen.registerScreen();
     * from your @Mod class during Forge postInit() phase.
     *
     * @param type   Type of screen to draw, as registered
     * @param ticks  The worldtime in ticks (including partial ticks)
     * @param scaleX Dimension of the screen in X direction
     * @param scaleY Dimension of the screen in Y direction
     * @param screen The screen driver (DrawGameScreen) for this particular screen
     *               <p>
     *               NOTE 1: It is not necessary to enclose your code with {@link MatrixStack#push()} and
     *               {@link MatrixStack#pop()}, as this will be done by the calling method.
     *               You can also change the lighting in your render as the calling
     *               method will finish by calling RenderHelper.enableStandardItemLighting().
     *               <p>
     *               NOTE 2: if disabling GL11.GL_TEXTURE_2D in your code it is very
     *               IMPORTANT to re-enable it again.
     */
    void render(MatrixStack matrixStackIn, int type, float ticks, float scaleX, float scaleY, IScreenManager screen);
}
