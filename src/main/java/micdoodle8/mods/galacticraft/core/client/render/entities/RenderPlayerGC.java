package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerOxygenTanks;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockCryoChamber;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityCryogenicChamber;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.ArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.lang.reflect.Field;

/**
 * This renders the thermal armor (unless RenderPlayerAPI is installed).
 * The thermal armor render is done after the corresponding body part of the player is drawn.
 * This ALSO patches RenderPlayer so that it uses ModelPlayerGC in place of ModelPlayer to draw the player.
 * <p>
 * Finally, this also adds a hook into applyRotations so as to fire a RotatePlayerEvent - used by the Cryogenic Chamber
 *
 * @author User
 */
public class RenderPlayerGC extends PlayerRenderer
{
    public static final ResourceLocation OXYGEN_MASK_TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/oxygen.png");
    public static final ResourceLocation PLAYER_TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/player.png");
    public static ResourceLocation thermalPaddingTexture0;
    public static ResourceLocation thermalPaddingTexture1;
    public static ResourceLocation thermalPaddingTexture1_T2;
    public static ResourceLocation heatShieldTexture;
    public static boolean flagThermalOverride = false;
    private static final Boolean isSmartRenderLoaded = null;

    public RenderPlayerGC()
    {
        this(false);
    }

    public RenderPlayerGC(boolean smallArms)
    {
        super(Minecraft.getInstance().getRenderManager(), smallArms);
        this.entityModel = new PlayerModel<>(0.0F, smallArms);
        this.addGCLayers();
    }

    private void addGCLayers()
    {
        Field f1 = null;
        Field f2 = null;
        Field f3 = null;
        try
        {
            f1 = ArmorLayer.class.getDeclaredField(GCCoreUtil.isDeobfuscated() ? "renderer" : "field_177190_a");
            f1.setAccessible(true);
        }
        catch (Exception ignore)
        {
        }
        // The following code removes the vanilla skull and item layer renderers and replaces them with the Galacticraft ones
        // Also updates all armor layers (including layers added by other mods) to reflect GC model limb positioning
        int itemLayerIndex = -1;
        int skullLayerIndex = -1;
        for (int i = 0; i < this.layerRenderers.size(); i++)
        {
            LayerRenderer layer = this.layerRenderers.get(i);
            if (layer instanceof HeldItemLayer)
            {
                itemLayerIndex = i;
            }
            else if (layer instanceof ArmorLayer)
            {
                if (f1 != null)
                {
                    try
                    {
                        f1.set(layer, this);
                    }
                    catch (Exception ignore)
                    {
                    }
                }
            }
            else if (layer instanceof HeadLayer)
            {
                skullLayerIndex = i;
            }
        }
        if (skullLayerIndex >= 0)
        {
            this.setLayer(skullLayerIndex, new HeadLayer<>(this));
        }
        if (itemLayerIndex >= 0 && !ConfigManagerCore.disableVehicleCameraChanges.get())
        {
//            this.setLayer(itemLayerIndex, new LayerHeldItemGC(this));
        }

        this.addLayer(new LayerOxygenTanks(this));
//        this.addLayer(new LayerOxygenGear(this));
//        this.addLayer(new LayerOxygenMask(this));
//        this.addLayer(new LayerOxygenParachute(this));
//        this.addLayer(new LayerFrequencyModule(this));

        if (GalacticraftCore.isPlanetsLoaded)
        {
//            this.addLayer(new LayerThermalPadding(this));

            RenderPlayerGC.thermalPaddingTexture0 = new ResourceLocation("galacticraftplanets", "textures/misc/thermal_padding_0.png");
            RenderPlayerGC.thermalPaddingTexture1 = new ResourceLocation("galacticraftplanets", "textures/misc/thermal_padding_1.png");
            RenderPlayerGC.thermalPaddingTexture1_T2 = new ResourceLocation("galacticraftplanets", "textures/misc/thermal_padding_t2_1.png");
            RenderPlayerGC.heatShieldTexture = new ResourceLocation("galacticraftplanets", "textures/misc/shield.png");

//            this.addLayer(new LayerShield(this));
        }
    }

    private void setLayer(int index, LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> layer)
    {
        this.layerRenderers.set(index, layer);
    }

    public RenderPlayerGC(PlayerRenderer old, boolean smallArms)
    {
        super(Minecraft.getInstance().getRenderManager(), smallArms);

        //Preserve any layers added by other mods, for example WearableBackpacks
//        Class clazz = old.getClass().getSuperclass();
//        Field f = null;
//        do {
//            try
//            {
//                f = clazz.getDeclaredField(GCCoreUtil.isDeobfuscated() ? "layerRenderers" : "field_177097_h");
//                f.setAccessible(true);
//            } catch (Exception ignore) { }
//            clazz = clazz.getSuperclass();
//        } while (f == null && clazz != null);
//        if (f != null) try
//        {
//            List<LayerRenderer<?>> layers = (List<LayerRenderer<?>>) f.get(old);
//            if(layers.size() == 0)
//            {
//                //Specifically fix for compatibility with MetaMorph's non-standard "RenderSubPlayer" class
//                try {
//                Field g = old.getClass().getDeclaredField("original");
//                old = (PlayerRenderer) g.get(old);
//                layers = (List<LayerRenderer<?>>) f.get(old);
//                } catch (Exception ignore) { }
//            }
//            if (layers.size() > 0)
//            {
//                for (LayerRenderer<?> oldLayer : layers)
//                {
//                    if (this.hasNoLayer(oldLayer.getClass()))
//                    {
//                        LayerRenderer newInstance = null;
//                        try {
//                            newInstance = oldLayer.getClass().getConstructor(LivingRenderer.class).newInstance(this);
//                        }
//                        catch (Exception ignore) { }
//                        if (newInstance == null)
//                        {
//                            try {
//                                newInstance = oldLayer.getClass().getConstructor(PlayerRenderer.class).newInstance(this);
//                            }
//                            catch (Exception ignore) { }
//                        }
//                        if (newInstance == null)
//                        {
//                            try {
//                                newInstance = oldLayer.getClass().getConstructor(boolean.class, ModelPlayer.class).newInstance(smallArms, this.mainModel);
//                            }
//                            catch (Exception ignore) { }
//                        }
//                        if (newInstance != null)
//                        {
//                            oldLayer = newInstance;
//                        }
//                        this.addLayer(oldLayer);
//                    }
//                }
//            }
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        } TODO Is this still needed?

        this.addGCLayers();
    }

    private boolean hasNoLayer(Class<? extends LayerRenderer> test)
    {
        for (LayerRenderer<?, ?> oldLayer : this.layerRenderers)
        {
            if (oldLayer.getClass() == test)
            {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void preRenderCallback(AbstractClientPlayerEntity entitylivingbaseIn, MatrixStack matrixStack, float partialTickTime)
    {
        super.preRenderCallback(entitylivingbaseIn, matrixStack, partialTickTime);

        if (entitylivingbaseIn.isAlive() && entitylivingbaseIn.isSleeping())
        {
            RotatePlayerEvent event = new RotatePlayerEvent(entitylivingbaseIn);
            MinecraftForge.EVENT_BUS.post(event);

            if (!event.vanillaOverride)
            {
                super.preRenderCallback(entitylivingbaseIn, matrixStack, partialTickTime);
            }
            else if (event.shouldRotate == null || event.shouldRotate)
            {
                entitylivingbaseIn.rotationYawHead = 0;
                entitylivingbaseIn.prevRotationYawHead = 0;
                matrixStack.translate(0.0F, 0.3F, 0.0F);
            }
        }
    }

    private static float getFacingAngle(Direction facingIn) {
        switch(facingIn) {
            case SOUTH:
                return 90.0F;
            case WEST:
                return 0.0F;
            case NORTH:
                return 270.0F;
            case EAST:
                return 180.0F;
            default:
                return 0.0F;
        }
    }

    @Override
    protected void applyRotations(AbstractClientPlayerEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks)
    {
        // TODO Cryo chamber
        if (entityLiving.isAlive() && entityLiving.isSleeping())
        {
            RotatePlayerEvent event = new RotatePlayerEvent(entityLiving);
            MinecraftForge.EVENT_BUS.post(event);

            if (!event.vanillaOverride)
            {
                super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
            }
            else if (event.shouldRotate == null)
            {
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(getFacingAngle(entityLiving.getBedDirection())));
            }
            else if (event.shouldRotate)
            {
                float rotation = 0.0F;

                if (entityLiving.getBedLocation() != null)
                {
                    BlockState bed = entityLiving.world.getBlockState(entityLiving.getBedLocation());

                    if (bed.getBlock().isBed(bed, entityLiving.world, entityLiving.getBedLocation(), entityLiving))
                    {
                        if (bed.getBlock() == GCBlocks.fakeBlock && bed.get(BlockMulti.MULTI_TYPE) == BlockMulti.EnumBlockMultiType.CRYO_CHAMBER)
                        {
                            TileEntity tile = event.getPlayer().world.getTileEntity(entityLiving.getBedLocation());
                            if (tile instanceof TileEntityCryogenicChamber)
                            {
                                bed = event.getPlayer().world.getBlockState(((TileEntityCryogenicChamber) tile).mainBlockPosition);
                            }
                        }

                        if (bed.getBlock() instanceof BlockCryoChamber)
                        {
                            switch (bed.get(BlockCryoChamber.FACING))
                            {
                            case NORTH:
                                rotation = 0.0F;
                                break;
                            case EAST:
                                rotation = 270.0F;
                                break;
                            case SOUTH:
                                rotation = 180.0F;
                                break;
                            case WEST:
                                rotation = 90.0F;
                                break;
                            }
                        }
                    }
                }

                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
            }
        }
        else
        {
            if (Minecraft.getInstance().gameSettings.thirdPersonView != 0)
            {
                final PlayerEntity player = (PlayerEntity) entityLiving;

                if (player.getRidingEntity() instanceof ICameraZoomEntity)
                {
                    Entity rocket = player.getRidingEntity();
                    float rotateOffset = ((ICameraZoomEntity)rocket).getRotateOffset();
                    if (rotateOffset > -10F)
                    {
                        matrixStackIn.translate(0, -rotateOffset, 0);
                        float anglePitch = rocket.prevRotationPitch;
                        float angleYaw = rocket.prevRotationYaw;
                        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-angleYaw));
                        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(anglePitch));
                        matrixStackIn.translate(0, rotateOffset, 0);
                    }
                }
            }
            super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        }
    }

    public static class RotatePlayerEvent extends PlayerEvent
    {
        public Boolean shouldRotate = null;
        public boolean vanillaOverride = false;

        public RotatePlayerEvent(AbstractClientPlayerEntity player)
        {
            super(player);
        }
    }
}
