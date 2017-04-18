package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import java.util.List;
import java.util.Random;

public class EntitySkeletonBoss extends EntityBossBase implements IEntityBreathable, IRangedAttackMob, IIgnoreShift
{
    protected long ticks = 0;
    private static final ItemStack defaultHeldItem = new ItemStack(Items.BOW, 1);

    public int throwTimer;
    public int postThrowDelay = 20;
    public Entity thrownEntity;
    public Entity targetEntity;

    public EntitySkeletonBoss(World par1World)
    {
        super(par1World);
        this.setSize(1.5F, 4.0F);
        this.isImmuneToFire = true;
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 25, 10.0F));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, false, true));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150.0F * ConfigManagerCore.dungeonBossHealthMod);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(ConfigManagerCore.hardMode ? 0.4F : 0.25F);
    }

    @Override
    protected void onDeathUpdate()
    {
        super.onDeathUpdate();

        if (!this.worldObj.isRemote)
        {
            if (this.deathTicks == 100)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(PacketSimple.EnumSimplePacket.C_PLAY_SOUND_BOSS_DEATH, GCCoreUtil.getDimensionID(this.worldObj), new Object[] { 1.5F }), new NetworkRegistry.TargetPoint(GCCoreUtil.getDimensionID(this.worldObj), this.posX, this.posY, this.posZ, 40.0D));
            }
        }
    }

    @Override
    public boolean isInWater()
    {
        return false;
    }

    @Override
    public boolean handleWaterMovement()
    {
        return false;
    }

    @Override
    public void updatePassenger(Entity passenger)
    {
        if (this.isPassenger(passenger))
        {
            final double offsetX = Math.sin(-this.rotationYawHead * (Math.PI / 180.0D));
            final double offsetZ = Math.cos(this.rotationYawHead * (Math.PI / 180.0D));
            final double offsetY = 2 * Math.cos((this.throwTimer + this.postThrowDelay) * 0.05F);

            passenger.setPosition(this.posX + offsetX, this.posY + this.getMountedYOffset() + passenger.getYOffset() + offsetY, this.posZ + offsetZ);
        }
    }

    @Override
    public void knockBack(Entity par1Entity, float par2, double par3, double par5)
    {
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        if (!this.isAIDisabled() && this.getPassengers().isEmpty() && this.postThrowDelay == 0 && this.throwTimer == 0 && par1EntityPlayer.equals(this.targetEntity) && this.deathTicks == 0)
        {
            if (!this.worldObj.isRemote)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_PLAY_SOUND_BOSS_LAUGH, GCCoreUtil.getDimensionID(this.worldObj), new Object[] {}), new TargetPoint(GCCoreUtil.getDimensionID(this.worldObj), this.posX, this.posY, this.posZ, 40.0D));
                par1EntityPlayer.startRiding(this);
            }

            this.throwTimer = 40;
        }

        super.onCollideWithPlayer(par1EntityPlayer);
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    protected SoundEvent getHurtSound()
    {
        this.playSound(GCSounds.bossOoh, this.getSoundVolume(), this.getSoundPitch() + 1.0F);
        return null;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return null;
    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public ItemStack getHeldItem()
//    {
//        return EntitySkeletonBoss.defaultHeldItem;
//    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public void onLivingUpdate()
    {
        this.ticks++;

        if (!this.worldObj.isRemote && this.getHealth() <= 150.0F * ConfigManagerCore.dungeonBossHealthMod / 2)
        {
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        }

        final EntityPlayer player = this.worldObj.getClosestPlayer(this.posX, this.posY, this.posZ, 20.0, false);

        if (player != null && !player.equals(this.targetEntity))
        {
            if (this.getDistanceSqToEntity(player) < 400.0D)
            {
                this.getNavigator().getPathToEntityLiving(player);
                this.targetEntity = player;
            }
        }
        else
        {
            this.targetEntity = null;
        }

        if (this.throwTimer > 0)
        {
            this.throwTimer--;
        }

        if (this.postThrowDelay > 0)
        {
            this.postThrowDelay--;
        }

        if (!this.getPassengers().isEmpty() && this.throwTimer == 0)
        {
            this.postThrowDelay = 20;

            this.thrownEntity = this.getPassengers().get(0);

            if (!this.worldObj.isRemote)
            {
                this.removePassengers();
            }
        }

        if (this.thrownEntity != null && this.postThrowDelay == 18)
        {
            double d0 = this.posX - this.thrownEntity.posX;
            double d1;

            for (d1 = this.posZ - this.thrownEntity.posZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D)
            {
                d0 = (Math.random() - Math.random()) * 0.01D;
            }


            if (!this.worldObj.isRemote)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_PLAY_SOUND_BOW, GCCoreUtil.getDimensionID(this.worldObj), new Object[] {}), new TargetPoint(GCCoreUtil.getDimensionID(this.worldObj), this.posX, this.posY, this.posZ, 40.0D));
            }
            ((EntityPlayer) this.thrownEntity).attackedAtYaw = (float) (Math.atan2(d1, d0) * 180.0D / Math.PI) - this.rotationYaw;

            this.thrownEntity.isAirBorne = true;
            final float f = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
            final float f1 = 2.4F;
            this.thrownEntity.motionX /= 2.0D;
            this.thrownEntity.motionY /= 2.0D;
            this.thrownEntity.motionZ /= 2.0D;
            this.thrownEntity.motionX -= d0 / f * f1;
            this.thrownEntity.motionY += (double) f1 / 5;
            this.thrownEntity.motionZ -= d1 / f * f1;

            if (this.thrownEntity.motionY > 0.4000000059604645D)
            {
                this.thrownEntity.motionY = 0.4000000059604645D;
            }
        }

        super.onLivingUpdate();
    }

    @Override
    public void onDeath(DamageSource par1DamageSource)
    {
        super.onDeath(par1DamageSource);

        if (par1DamageSource.getSourceOfDamage() instanceof EntityArrow && par1DamageSource.getEntity() instanceof EntityPlayer)
        {
            final EntityPlayer entityPlayer = (EntityPlayer) par1DamageSource.getEntity();
            final double var3 = entityPlayer.posX - this.posX;
            final double var5 = entityPlayer.posZ - this.posZ;

            if (var3 * var3 + var5 * var5 >= 2500.0D)
            {
                entityPlayer.addStat(AchievementList.SNIPE_SKELETON);
            }
        }
    }

    @Override
    protected Item getDropItem()
    {
        return Items.ARROW;
    }

    @Override
    public EntityItem entityDropItem(ItemStack par1ItemStack, float par2)
    {
        final EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY + par2, this.posZ, par1ItemStack);
        entityitem.motionY = -2.0D;
        entityitem.setDefaultPickupDelay();
        if (this.captureDrops)
        {
            this.capturedDrops.add(entityitem);
        }
        else
        {
            this.worldObj.spawnEntityInWorld(entityitem);
        }
        return entityitem;
    }

    @Override
    protected void dropFewItems(boolean b, int i)
    {
        if (this.rand.nextInt(200) - i >= 5)
        {
            return;
        }

        if (i > 0)
        {
            final ItemStack var2 = new ItemStack(Items.BOW);
            EnchantmentHelper.addRandomEnchantment(this.rand, var2, 5, false);
            this.entityDropItem(var2, 0.0F);
        }
        else
        {
            this.dropItem(Items.BOW, 1);
        }
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float f)
    {
        if (!this.getPassengers().isEmpty())
        {
            return;
        }

        EntityTippedArrow arrow = new EntityTippedArrow(this.worldObj, this);
        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - arrow.posY;
        double d2 = target.posZ - this.posZ;
        double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        arrow.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.worldObj.getDifficulty().getDifficultyId() * 4));

        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(arrow);
    }

    @Override
    public boolean shouldIgnoreShiftExit()
    {
        return true;
    }

    @Override
    public ItemStack getGuaranteedLoot(Random rand)
    {
        List<ItemStack> stackList = GalacticraftRegistry.getDungeonLoot(1);
        return stackList.get(rand.nextInt(stackList.size())).copy();
    }

    @Override
    public int getChestTier()
    {
        return 1;
    }

    @Override
    public void dropKey()
    {
        this.entityDropItem(new ItemStack(GCItems.key, 1, 0), 0.5F);
    }

    @Override
    public BossInfo.Color getHealthBarColor()
    {
        return BossInfo.Color.GREEN;
    }
}
