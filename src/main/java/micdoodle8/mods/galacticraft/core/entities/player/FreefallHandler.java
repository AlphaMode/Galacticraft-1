package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.api.event.ZeroGravityEvent;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.TransformerHooks;
import micdoodle8.mods.galacticraft.core.dimension.DimensionSpaceStation;
import micdoodle8.mods.galacticraft.core.dimension.SpinManager;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.util.function.Function;

public class FreefallHandler
{
    private Vec3d pPrevMotion = Vec3d.ZERO;
    private float jetpackBoost;
    private double pPrevdY;
    public boolean sneakLast;
    public boolean onWall;

    public int pjumpticks = 0;
    private GCPlayerStatsClient stats;

    public FreefallHandler(GCPlayerStatsClient statsClientCapability)
    {
        stats = statsClientCapability;
    }

    public boolean testFreefall(PlayerEntity player)
    {
        ZeroGravityEvent zeroGEvent = new ZeroGravityEvent.InFreefall(player);
        MinecraftForge.EVENT_BUS.post(zeroGEvent);
        if (zeroGEvent.isCanceled())
        {
            return false;
        }

        //Test whether feet are on a block, also stops the login glitch
        int playerFeetOnY = (int) (player.getBoundingBox().minY - 0.01D);
        int xx = MathHelper.floor(player.getPosX());
        int zz = MathHelper.floor(player.getPosZ());
        BlockPos pos = new BlockPos(xx, playerFeetOnY, zz);
        BlockState state = player.world.getBlockState(pos);
        Block b = state.getBlock();
        if (b.getMaterial(state) != Material.AIR && !(b instanceof FlowingFluidBlock))
        {
            double blockYmax;
            if (b == GCBlocks.platform)
                blockYmax = playerFeetOnY + 1.0D;
            else
                blockYmax = playerFeetOnY + b.getShape(state, player.world, pos, ISelectionContext.forEntity(player)).getBoundingBox().maxY;
            if (player.getBoundingBox().minY - blockYmax < 0.01D && player.getBoundingBox().minY - blockYmax > -0.5D)
            {
                player.onGround = true;
                if (player.getBoundingBox().minY - blockYmax > 0D)
                {
                    player.setRawPosition(player.getPosX(), player.getPosY() - player.getBoundingBox().minY - blockYmax, player.getPosZ());
                    player.setBoundingBox(player.getBoundingBox().offset(0, blockYmax - player.getBoundingBox().minY, 0));
                }
                else/* if (b.canCollideCheck(player.world.getBlockState(new BlockPos(xx, playerFeetOnY, zz)), false))*/
                {
                    BlockPos offsetPos = new BlockPos(xx, playerFeetOnY, zz);
                    AxisAlignedBB collisionBox = b.getCollisionShape(player.world.getBlockState(offsetPos), player.world, offsetPos, ISelectionContext.forEntity(player)).getBoundingBox();
                    if (collisionBox != null && collisionBox.intersects(player.getBoundingBox()))
                    {
                        player.setRawPosition(player.getPosX(), player.getPosY() - player.getBoundingBox().minY - blockYmax, player.getPosZ());
                        player.setBoundingBox(player.getBoundingBox().offset(0, blockYmax - player.getBoundingBox().minY, 0));
                    }
                }
                return false;
            }
        }
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private boolean testFreefall(ClientPlayerEntity p, boolean flag)
    {
        World world = p.world;
        Dimension worldProvider = world.getDimension();
        if (!(worldProvider instanceof IZeroGDimension))
        {
            return false;
        }
        ZeroGravityEvent zeroGEvent = new ZeroGravityEvent.InFreefall(p);
        MinecraftForge.EVENT_BUS.post(zeroGEvent);
        if (zeroGEvent.isCanceled())
        {
            return false;
        }
        if (this.pjumpticks > 0 || (stats.isSsOnGroundLast() && p.movementInput.jump))
        {
            return false;
        }

        if (p.getRidingEntity() != null)
        {
            Entity e = p.getRidingEntity();
            if (e instanceof EntitySpaceshipBase)
            {
                return ((EntitySpaceshipBase) e).getLaunched();
            }
            if (e instanceof EntityLanderBase)
            {
                return false;
            }
            //TODO: should check whether lander has landed (whatever that means)
            //TODO: could check other ridden entities - every entity should have its own freefall check :(
        }

        //This is an "on the ground" check
        if (!flag)
        {
            return false;
        }
        else
        {
            float rY = p.rotationYaw % 360F;
            double zreach = 0D;
            double xreach = 0D;
            if (rY < 80F || rY > 280F)
            {
                zreach = 0.2D;
            }
            if (rY < 170F && rY > 10F)
            {
                xreach = 0.2D;
            }
            if (rY < 260F && rY > 100F)
            {
                zreach = -0.2D;
            }
            if (rY < 350F && rY > 190F)
            {
                xreach = -0.2D;
            }
            AxisAlignedBB playerReach = p.getBoundingBox().expand(xreach, 0, zreach);

            boolean checkBlockWithinReach;
            if (worldProvider instanceof DimensionSpaceStation)
            {
                SpinManager spinManager = ((DimensionSpaceStation) worldProvider).getSpinManager();
                checkBlockWithinReach = playerReach.maxX >= spinManager.ssBoundsMinX && playerReach.minX <= spinManager.ssBoundsMaxX && playerReach.maxY >= spinManager.ssBoundsMinY && playerReach.minY <= spinManager.ssBoundsMaxY && playerReach.maxZ >= spinManager.ssBoundsMinZ && playerReach.minZ <= spinManager.ssBoundsMaxZ;
                //Player is somewhere within the space station boundaries
            }
            else
            {
                checkBlockWithinReach = true;
            }

            if (checkBlockWithinReach)
            {
                //Check if the player's bounding box is in the same block coordinates as any non-vacuum block (including torches etc)
                //If so, it's assumed the player has something close enough to grab onto, so is not in freefall
                //Note: breatheable air here means the player is definitely not in freefall
                int xm = MathHelper.floor(playerReach.minX);
                int xx = MathHelper.floor(playerReach.maxX);
                int ym = MathHelper.floor(playerReach.minY);
                int yy = MathHelper.floor(playerReach.maxY);
                int zm = MathHelper.floor(playerReach.minZ);
                int zz = MathHelper.floor(playerReach.maxZ);
                for (int x = xm; x <= xx; x++)
                {
                    for (int y = ym; y <= yy; y++)
                    {
                        for (int z = zm; z <= zz; z++)
                        {
                            //Blocks.AIR is hard vacuum - we want to check for that, here
                            Block b = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                            if (Blocks.AIR != b && GCBlocks.brightAir != b)
                            {
                                this.onWall = true;
                                return false;
                            }
                        }
                    }
                }
            }
        }

        /*
        if (freefall)
		{
			//If that check didn't produce a result, see if the player is inside the walls
			//TODO: could apply special weightless movement here like Coriolis force - the player is inside the walls,  not touching them, and in a vacuum
			int quadrant = 0;
			double xd = p.posX - this.spinCentreX;
			double zd = p.posZ - this.spinCentreZ;
			if (xd<0)
			{
				if (xd<-Math.abs(zd))
				{
					quadrant = 2;
				} else
					quadrant = (zd<0) ? 3 : 1;
			} else
				if (xd>Math.abs(zd))
				{
					quadrant = 0;
				} else
					quadrant = (zd<0) ? 3 : 1;

			int ymin = MathHelper.floor(p.boundingBox.minY)-1;
			int ymax = MathHelper.floor(p.boundingBox.maxY);
			int xmin, xmax, zmin, zmax;

			switch (quadrant)
			{
			case 0:
				xmin = MathHelper.floor(p.boundingBox.maxX);
				xmax = this.ssBoundsMaxX - 1;
				zmin = MathHelper.floor(p.boundingBox.minZ)-1;
				zmax = MathHelper.floor(p.boundingBox.maxZ)+1;
				break;
			case 1:
				xmin = MathHelper.floor(p.boundingBox.minX)-1;
				xmax = MathHelper.floor(p.boundingBox.maxX)+1;
				zmin = MathHelper.floor(p.boundingBox.maxZ);
				zmax = this.ssBoundsMaxZ - 1;
				break;
			case 2:
				zmin = MathHelper.floor(p.boundingBox.minZ)-1;
				zmax = MathHelper.floor(p.boundingBox.maxZ)+1;
				xmin = this.ssBoundsMinX;
				xmax = MathHelper.floor(p.boundingBox.minX);
				break;
			case 3:
			default:
				xmin = MathHelper.floor(p.boundingBox.minX)-1;
				xmax = MathHelper.floor(p.boundingBox.maxX)+1;
				zmin = this.ssBoundsMinZ;
				zmax = MathHelper.floor(p.boundingBox.minZ);
				break;
			}

			//This block search could cost a lot of CPU (but client LogicalSide) - maybe optimise later
			BLOCKCHECK0:
			for(int x = xmin; x <= xmax; x++)
				for (int z = zmin; z <= zmax; z++)
					for (int y = ymin; y <= ymax; y++)
						if (Blocks.AIR != this.worldProvider.getWorld().getBlock(x, y, z))
						{
							freefall = false;
							break BLOCKCHECK0;
						}
		}*/

        this.onWall = false;
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void setupFreefallPre(ClientPlayerEntity p)
    {
        double dY = p.getMotion().getY() - pPrevMotion.getY();
        jetpackBoost = 0F;
        pPrevdY = dY;
        pPrevMotion = p.getMotion();
    }

    @OnlyIn(Dist.CLIENT)
    public void freefallMotion(ClientPlayerEntity p)
    {
        boolean jetpackUsed = false;
        double dX = p.getMotion().getX() - pPrevMotion.getX();
        double dY = p.getMotion().getY() - pPrevMotion.getY();
        double dZ = p.getMotion().getZ() - pPrevMotion.getZ();

        double posOffsetX = -p.getMotion().getX();
        double posOffsetY = - p.getMotion().getY();
        if (posOffsetY == - TransformerHooks.getGravityForEntity(p)) posOffsetY = 0;
        double posOffsetZ = -p.getMotion().getZ();
        //if (p.capabilities.isFlying)

        GCPlayerStatsClient stats = GCPlayerStatsClient.get(p);
        ///Undo whatever vanilla tried to do to our y motion
        if (dY < 0D && p.getMotion().getY() != 0.0D)
        {
            Vec3d mot = p.getMotion();
            p.setMotion(mot.getX(), pPrevMotion.getY(), mot.getZ());
        }
        else if (dY > 0.01D && stats.isInFreefallLast())
        {
            //Impulse upwards - it's probably a jetpack from another mod
            if (dX < 0.01D && dZ < 0.01D)
            {
                float pitch = p.rotationPitch / Constants.RADIANS_TO_DEGREES;
                jetpackBoost = (float) dY * MathHelper.cos(pitch) * 0.1F;
                float factor = 1 + MathHelper.sin(pitch) / 5;
                setMotionY(p, y -> y - dY * factor);
                jetpackUsed = true;
            }
            else
            {
                setMotionY(p, y -> y - dY / 2);
            }
        }

        setMotionX(p, x -> x - dX);
//        p.motionY -= dY;    //Enabling this will disable jetpacks
        setMotionZ(p, z -> z - dZ);

        if (p.movementInput.moveForward != 0)
        {
            setMotionX(p, motionX -> motionX - p.movementInput.moveForward * MathHelper.sin(p.rotationYaw / Constants.RADIANS_TO_DEGREES) / (ConfigManagerCore.hardMode.get() ? 600F : 200F));
            setMotionZ(p, motionZ -> motionZ + p.movementInput.moveForward * MathHelper.cos(p.rotationYaw / Constants.RADIANS_TO_DEGREES) / (ConfigManagerCore.hardMode.get() ? 600F : 200F));
        }

        if (jetpackBoost != 0)
        {
            setMotionX(p, motionX  -> motionX - jetpackBoost * MathHelper.sin(p.rotationYaw / Constants.RADIANS_TO_DEGREES));
            setMotionZ(p, motionZ -> motionZ + jetpackBoost * MathHelper.cos(p.rotationYaw / Constants.RADIANS_TO_DEGREES));
        }

        if (p.movementInput.sneaking)
        {
            if (!sneakLast)
            {
//              posOffsetY += 0.0268;
                sneakLast = true;
            }
            setMotionY(p, y -> y - (ConfigManagerCore.hardMode.get() ? 0.002D : 0.0032D));
        }
        else if (sneakLast)
        {
            sneakLast = false;
//          posOffsetY -= 0.0268;
        }

        if (!jetpackUsed && p.movementInput.jump)
        {
            setMotionY(p, y -> y + (ConfigManagerCore.hardMode.get() ? 0.002D : 0.0032D));
        }

        float speedLimit = ConfigManagerCore.hardMode.get() ? 0.9F : 0.7F;

        if (p.getMotion().getX() > speedLimit)
        {
            setMotionX(p, speedLimit);
        }
        if (p.getMotion().getX() < -speedLimit)
        {
            setMotionX(p, -speedLimit);
        }
        if (p.getMotion().getY() > speedLimit)
        {
            setMotionY(p, speedLimit);
        }
        if (p.getMotion().getY() < -speedLimit)
        {
            setMotionY(p, -speedLimit);
        }
        if (p.getMotion().getZ() > speedLimit)
        {
            setMotionZ(p, speedLimit);
        }
        if (p.getMotion().getZ() < -speedLimit)
        {
            setMotionZ(p, -speedLimit);
        }
        pPrevMotion = p.getMotion();
        p.move(MoverType.SELF, p.getMotion().add(posOffsetX, posOffsetY, posOffsetZ));
    }

    /*              double dyaw = p.rotationYaw - p.prevRotationYaw;
    p.rotationYaw -= dyaw * 0.8D;
    double dyawh = p.rotationYawHead - p.prevRotationYawHead;
    p.rotationYawHead -= dyawh * 0.8D;
    while (p.rotationYaw > 360F)
    {
        p.rotationYaw -= 360F;
    }
    while (p.rotationYaw < 0F)
    {
        p.rotationYaw += 360F;
    }
    while (p.rotationYawHead > 360F)
    {
        p.rotationYawHead -= 360F;
    }
    while (p.rotationYawHead < 0F)
    {
        p.rotationYawHead += 360F;
    }
*/

    private static void setMotionX(Entity p, Function<Double, Double> x)
    {
        Vec3d mot = p.getMotion();
        p.setMotion(x.apply(mot.getX()), mot.getY(), mot.getZ());
    }

    private static void setMotionX(Entity p, double x)
    {
        Vec3d mot = p.getMotion();
        p.setMotion(x, mot.getY(), mot.getZ());
    }

    private static void setMotionY(Entity p, Function<Double, Double> y)
    {
        Vec3d mot = p.getMotion();
        p.setMotion(mot.getX(), y.apply(mot.getY()), mot.getZ());
    }

    private static void setMotionY(Entity p, double y)
    {
        Vec3d mot = p.getMotion();
        p.setMotion(mot.getX(), y, mot.getZ());
    }

    private static void setMotionZ(Entity p, Function<Double, Double> z)
    {
        Vec3d mot = p.getMotion();
        p.setMotion(mot.getX(), mot.getY(), z.apply(mot.getZ()));
    }

    private static void setMotionZ(Entity p, double z)
    {
        Vec3d mot = p.getMotion();
        p.setMotion(mot.getX(), mot.getY(), z);
    }

    public void updateFreefall(PlayerEntity p)
    {
        pPrevMotion = p.getMotion();
    }

    @OnlyIn(Dist.CLIENT)
    public void preVanillaMotion(ClientPlayerEntity p)
    {
        this.setupFreefallPre(p);
        stats.setSsOnGroundLast(p.onGround);
    }

    @OnlyIn(Dist.CLIENT)
    public void postVanillaMotion(ClientPlayerEntity p)
    {
        World world = p.world;
        Dimension worldProvider = world.getDimension();
        if (!(worldProvider instanceof IZeroGDimension))
        {
            return;
        }
        ZeroGravityEvent zeroGEvent = new ZeroGravityEvent.Motion(p);
        MinecraftForge.EVENT_BUS.post(zeroGEvent);
        if (zeroGEvent.isCanceled())
        {
            return;
        }

        boolean freefall = stats.isInFreefall();
        freefall = this.testFreefall(p, freefall);
        stats.setInFreefall(freefall);
        stats.setInFreefallFirstCheck(true);

        SpinManager spinManager = null;
        if (worldProvider instanceof DimensionSpaceStation && !stats.getPlatformControlled())
        {
            spinManager = ((DimensionSpaceStation) worldProvider).getSpinManager();
        }
        boolean doCentrifugal = spinManager != null;

        if (freefall)
        {
            this.pjumpticks = 0;
            //Reverse effects of deceleration
            setMotionX(p, motionX -> motionX / 0.91F);
            setMotionZ(p, motionZ -> motionZ / 0.91F);
            setMotionY(p, motionY -> motionY / 0.9800000190734863D);

            if (spinManager != null)
            {
                doCentrifugal = spinManager.updatePlayerForSpin(p, 1F);
            }

            //Do freefall motion
            if (!p.abilities.isCreativeMode)
            {
                this.freefallMotion(p);
            }
            else
            {
                p.abilities.isFlying = true;
                //Half the normal acceleration in Creative mode
                double dx = p.getMotion().getX() - this.pPrevMotion.getX();
                double dy = p.getMotion().getY() - this.pPrevMotion.getY();
                double dz = p.getMotion().getZ() - this.pPrevMotion.getZ();
                setMotionX(p, motionX -> motionX - dx / 2);
                setMotionY(p, motionY -> motionY - dy / 2);
                setMotionZ(p, motionZ -> motionZ - dz / 2);

                if (p.getMotion().getX() > 1.2F)
                {
                    setMotionX(p, 1.2F);
                }
                if (p.getMotion().getX() < -1.2F)
                {
                    setMotionX(p, -1.2F);
                }
                if (p.getMotion().getY() > 0.7F)
                {
                    setMotionY(p, 0.7F);
                }
                if (p.getMotion().getY() < -0.7F)
                {
                    setMotionY(p, -0.7F);
                }
                if (p.getMotion().getZ() > 1.2F)
                {
                    setMotionZ(p, 1.2F);
                }
                if (p.getMotion().getZ() < -1.2F)
                {
                    setMotionZ(p, -1.2F);
                }
            }
            //TODO: Think about endless drift?
            //Player may run out of oxygen - that will kill the player eventually if can't get back to SS
            //Could auto-kill + respawn the player if floats too far away (config option whether to lose items or not)
            //But we want players to be able to enjoy the view of the spinning space station from the outside
            //Arm and leg movements could start tumbling the player?
        }
        else
        //Not freefall - within arm's length of something or jumping
        {
            double dy = p.getMotion().getY() - this.pPrevMotion.getY();
            //if (p.motionY < 0 && this.pPrevMotionY >= 0) p.posY -= p.motionY;
            //if (p.motionY != 0) p.motionY = this.pPrevMotionY;
            if (p.movementInput.jump)
            {
                if ((p.onGround || stats.isSsOnGroundLast()) && !p.abilities.isCreativeMode)
                {
                    if (this.pjumpticks < 25) this.pjumpticks++;
                    setMotionY(p, motionY -> motionY - dy);
                }
                else
                {
                    setMotionY(p, motionY -> motionY + 0.015D);
                    if (this.pjumpticks == 0)
                    {
                        setMotionY(p, motionY -> - dy);
                    }
                }
            }
            else if (this.pjumpticks > 0)
            {
                setMotionY(p, motionY -> motionY + 0.0145D * this.pjumpticks);
                this.pjumpticks = 0;
            }
            else if (p.movementInput.sneaking)
            {
                if (!p.onGround)
                {
                    setMotionY(p, motionY -> - 0.015D);
                }
                this.pjumpticks = 0;
            }
        }

        //Artificial gravity
        if (doCentrifugal && !p.onGround)
        {
            spinManager.applyCentrifugalForce(p);
        }

        this.pPrevMotion = p.getMotion();
    }

    /**
     * Used for non-player entities in ZeroGDimensions
     */
    public static boolean testEntityFreefall(World worldObj, AxisAlignedBB entityBoundingBox)
    {
        //Check if the entity's bounding box is in the same block coordinates as any non-vacuum block (including torches etc)
        //If so, it's assumed the entity has something close enough to catch onto, so is not in freefall
        //Note: breatheable air here means the entity is definitely not in freefall
        int xmx = MathHelper.floor(entityBoundingBox.maxX + 0.2D);
        int ym = MathHelper.floor(entityBoundingBox.minY - 0.1D);
        int yy = MathHelper.floor(entityBoundingBox.maxY + 0.1D);
        int zm = MathHelper.floor(entityBoundingBox.minZ - 0.2D);
        int zz = MathHelper.floor(entityBoundingBox.maxZ + 0.2D);
        if (ym < 0) ym = 0;
        if (yy > 255) yy = 255;

        for (int x = MathHelper.floor(entityBoundingBox.minX - 0.2D); x <= xmx; x++)
        {
            for (int z = zm; z <= zz; z++)
            {
                if (!worldObj.isBlockLoaded(new BlockPos(x, 0, z)) || worldObj.isRemote)
                    continue;

                for (int y = ym; y <= yy; y++)
                {
                    if (Blocks.AIR != worldObj.getBlockState(new BlockPos(x, y, z)).getBlock())
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Call this on every freefalling non-player entity in a dimension
     * either at the end of the world tick (ideal) or else during the
     * start of the next world tick (e.g. during updateWeather())
     *
     * May require iteration through the world's loadedEntityList
     * See SpinManager.updateSpin() for an example
     * @param e
     */
    public static void tickFreefallEntity(Entity e)
    {
        if (e.world.getDimension() instanceof IZeroGDimension) ((IZeroGDimension)e.world.getDimension()).setInFreefall(e);

        //Undo deceleration applied at the end of the previous tick
        boolean warnLog = false;
        if (e instanceof LivingEntity)
        {
            ZeroGravityEvent zeroGEvent = new ZeroGravityEvent.InFreefall((LivingEntity) e);
            MinecraftForge.EVENT_BUS.post(zeroGEvent);
            if (!zeroGEvent.isCanceled())
            {
                setMotionX(e, motionX -> motionX / (double)0.91F); //0.91F;
                setMotionZ(e, motionZ -> motionZ / (double)0.91F); //0.91F;
                setMotionY(e, motionY -> motionY / ((e instanceof FlyingEntity) ?  0.91F : 0.9800000190734863D));

                if (e.getMotion().getX() > 10D)
                {
                    warnLog = true;
                    setMotionX(e, 10D);
                }
                else if (e.getMotion().getX() < -10D)
                {
                    warnLog = true;
                    setMotionX(e, -10D);
                }
                if (e.getMotion().getY() > 10D)
                {
                    warnLog = true;
                    setMotionY(e, 10D);
                }
                else if (e.getMotion().getY() < -10D)
                {
                    warnLog = true;
                    setMotionY(e, -10D);
                }
                if (e.getMotion().getZ() > 10D)
                {
                    warnLog = true;
                    setMotionZ(e, 10D);
                }
                else if (e.getMotion().getZ() < -10D)
                {
                    warnLog = true;
                    setMotionZ(e, -10D);
                }
            }
        }
        else if (e instanceof FallingBlockEntity)
        {
            setMotionY(e, motionY -> motionY / 0.9800000190734863D);
            //e.motionY += 0.03999999910593033D;
            //e.posY += 0.03999999910593033D;
            //e.lastTickPosY += 0.03999999910593033D;
            if (e.getMotion().getY() > 10D)
            {
                warnLog = true;
                setMotionY(e, 10D);
            }
            else if (e.getMotion().getY() < -10D)
            {
                warnLog = true;
               setMotionY(e, -10D);
            }
        }
        else
        {
            setMotionX(e, motionX -> motionX / 0.9800000190734863D);
            setMotionY(e, motionY -> motionY / 0.9800000190734863D);
            setMotionZ(e, motionZ -> motionZ / 0.9800000190734863D);

            if (e.getMotion().getX() > 10D)
            {
                warnLog = true;
                setMotionX(e, 10D);
            }
            else if (e.getMotion().getX() < -10D)
            {
                warnLog = true;
                setMotionX(e, -10D);
            }
            if (e.getMotion().getY() > 10D)
            {
                warnLog = true;
                setMotionY(e, 10D);
            }
            else if (e.getMotion().getY() < -10D)
            {
                warnLog = true;
                setMotionY(e, -10D);
            }
            if (e.getMotion().getZ() > 10D)
            {
                warnLog = true;
                setMotionZ(e, 10D);
            }
            else if (e.getMotion().getZ() < -10D)
            {
                warnLog = true;
                e.setMotion(e.getMotion().getX(), e.getMotion().getY(), -10D);
            }
        }

        if (warnLog)
            GCLog.debug(e.getName() + " moving too fast");
    }
}
