package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.ItemOilCanister;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.IReputationTracking;
import net.minecraft.entity.merchant.IReputationType;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.village.GossipManager;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class EntityAlienVillager extends AbstractVillagerEntity implements IEntityBreathable, IReputationTracking
{
    private static final DataParameter<VillagerData> VILLAGER_DATA = EntityDataManager.createKey(EntityAlienVillager.class, DataSerializers.VILLAGER_DATA);
    private boolean leveledUp;
    @Nullable
    private PlayerEntity previousCustomer;
    private int timeUntilReset;
    private final GossipManager gossip = new GossipManager();
    private int xp;
    private boolean needsInitilization;
    private int wealth;
    private static final VillagerTrades.ITrade[] DEFAULT_TRADE_LIST_MAP = new VillagerTrades.ITrade[] {
            new EntityAlienVillager.ListItemForEmeralds(new ItemStack(GCItems.oxMask), new EntityAlienVillager.PriceInfo(1, 2)),
//            new EntityAlienVillager.ListItemForEmeralds(new ItemStack(GCItems.oxTankLight, 1, 235), new EntityAlienVillager.PriceInfo(3, 4)),
            new EntityAlienVillager.ListItemForEmeralds(new ItemStack(GCItems.oxygenGear), new EntityAlienVillager.PriceInfo(3, 4)),
//            new EntityAlienVillager.ListItemForEmeralds(new ItemStack(GCItems.fuelCanister, 1, 317), new EntityAlienVillager.PriceInfo(3, 4)),
            new EntityAlienVillager.ListItemForEmeralds(new ItemStack(GCItems.parachutePlain), new EntityAlienVillager.PriceInfo(1, 2)),
//            new EntityAlienVillager.ListItemForEmeralds(new ItemStack(GCItems.battery, 1, 58), new EntityAlienVillager.PriceInfo(2, 4)),
            new EntityAlienVillager.ItemAndEmeraldToItem(ItemOilCanister.createEmptyCanister(1), new EntityAlienVillager.PriceInfo(1, 1), new ItemStack(GCItems.dehydratedCarrot)), //carrots = also yields a tin!
            new EntityAlienVillager.ListItemForEmeralds(new ItemStack(GCItems.compressedWaferBasic), new EntityAlienVillager.PriceInfo(3, 4)),
//            new EntityAlienVillager.ItemAndEmeraldToItem(new ItemStack(GCItems.schematic, 1, 0), new EntityAlienVillager.PriceInfo(3, 5), new ItemStack(GCItems.schematic, 1, 1)), //Exchange buggy and rocket schematics
//            new EntityAlienVillager.ItemAndEmeraldToItem(new ItemStack(GCItems.schematic, 1, 1), new EntityAlienVillager.PriceInfo(3, 5), new ItemStack(GCItems.schematic, 1, 0)), //Exchange buggy and rocket schematics
//            new EntityAlienVillager.ItemAndEmeraldToItem(new ItemStack(GCItems.basicItem, 2, 3), new EntityAlienVillager.PriceInfo(1, 1), new ItemStack(GCItems.basicItem, 1, 6)), //Compressed Tin - needed to craft a Fuel Loader
//            new EntityAlienVillager.ItemAndEmeraldToItem(new ItemStack(GCItems.basicItem, 2, 4), new EntityAlienVillager.PriceInfo(1, 1), new ItemStack(GCItems.basicItem, 1, 7)), //Compressed Copper - needed to craft a Fuel Loader
//            new EntityAlienVillager.EmeraldForItems(new ItemStack(Blocks.SAPLING, 1, 3), new EntityAlienVillager.PriceInfo(11, 39)) //The one thing Alien Villagers don't have and can't get is jungle trees...
            };

    public EntityAlienVillager(EntityType<? extends EntityAlienVillager> type, World worldIn)
    {
        super(type, worldIn);
        ((GroundPathNavigator) this.getNavigator()).setBreakDoors(true);
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, ZombieEntity.class, 8.0F, 0.6D, 0.6D));
        this.goalSelector.addGoal(1, new EntityAITradePlayerGC(this));
        this.goalSelector.addGoal(1, new EntityAILookAtTradePlayerGC(this));
//        this.goalSelector.addGoal(2, new EntityAIMoveIndoors(this));
//        this.goalSelector.addGoal(3, new EntityAIRestrictOpenDoor(this));
        this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 0.6D));
//        this.goalSelector.addGoal(9, new EntityAIWatchClosest2(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(9, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.setCanPickUpLoot(true);
    }

    @Override
    protected void onGrowingAdult()
    {
        super.onGrowingAdult();
    }
    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    private void levelUp() {
        this.setVillagerData(this.getVillagerData().withLevel(this.getVillagerData().getLevel() + 1));
        this.populateTradeData();
    }

    private boolean canLevelUp() {
        int i = this.getVillagerData().getLevel();
        return VillagerData.func_221128_d(i) && this.xp >= VillagerData.func_221127_c(i);
    }

    public int getPlayerReputation(PlayerEntity player) {
        return this.gossip.getReputation(player.getUniqueID(), (p_223103_0_) -> {
            return true;
        });
    }

    @Override
    protected void updateAITasks()
    {
//        this.world.getProfiler().startSection("brain");
//        this.getBrain().tick((ServerWorld)this.world, this);
//        this.world.getProfiler().endSection();
        if (!this.hasCustomer() && this.timeUntilReset > 0) {
            --this.timeUntilReset;
            if (this.timeUntilReset <= 0) {
                if (this.leveledUp) {
                    this.levelUp();
                    this.leveledUp = false;
                }

                this.addPotionEffect(new EffectInstance(Effects.REGENERATION, 200, 0));
            }
        }

        if (this.previousCustomer != null && this.world instanceof ServerWorld) {
            ((ServerWorld)this.world).updateReputation(IReputationType.TRADE, this.previousCustomer, this);
            this.world.setEntityState(this, (byte)14);
            this.previousCustomer = null;
        }

        if (!this.isAIDisabled() && this.rand.nextInt(100) == 0) {
            Raid raid = ((ServerWorld)this.world).findRaid(new BlockPos(this));
            if (raid != null && raid.isActive() && !raid.isOver()) {
                this.world.setEntityState(this, (byte)42);
            }
        }

        if (this.hasCustomer()) {
            this.resetCustomer();
        }

        super.updateAITasks();
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand)
    {
        ItemStack itemstack = player.inventory.getCurrentItem();
        boolean flag = itemstack != null && itemstack.getItem() == Items.VILLAGER_SPAWN_EGG;

        if (!flag && this.isAlive() && !this.hasCustomer() && !this.isChild() && !player.isSneaking())
        {
            PlayerGearData gearData = GalacticraftCore.proxy.getGearData(player);

            if (!this.world.isRemote && getOffers().isEmpty())
            {
                if (gearData != null && gearData.getFrequencyModule() != GCPlayerHandler.GEAR_NOT_PRESENT)
                {
                    this.displayMerchantGui(player);
                }
                else
                {
                    if (player instanceof ServerPlayerEntity)
                    {
                        ServerPlayerEntity playerMP = (ServerPlayerEntity) player;
                        GCPlayerStats stats = GCPlayerStats.get(playerMP);
                        if (stats.getChatCooldown() == 0)
                        {
                            player.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.village.warning.no_freq_mod")));
                            stats.setChatCooldown(20);
                        }
                    }
                }
            }

            player.addStat(Stats.TALKED_TO_VILLAGER);
            return true;
        }
        else
        {
            return super.processInteract(player, hand);
        }
    }

    private void displayMerchantGui(PlayerEntity player) {
        this.recalculateSpecialPricesFor(player);
        this.setCustomer(player);
        this.openMerchantContainer(player, this.getDisplayName(), this.getVillagerData().getLevel());
    }

    private void recalculateSpecialPricesFor(PlayerEntity playerIn) {
        int i = this.getPlayerReputation(playerIn);
        if (i != 0) {
            for(MerchantOffer merchantoffer : this.getOffers()) {
                merchantoffer.increaseSpecialPrice(-MathHelper.floor((float)i * merchantoffer.getPriceMultiplier()));
            }
        }

        if (playerIn.isPotionActive(Effects.HERO_OF_THE_VILLAGE)) {
            EffectInstance effectinstance = playerIn.getActivePotionEffect(Effects.HERO_OF_THE_VILLAGE);
            int k = effectinstance.getAmplifier();

            for(MerchantOffer merchantoffer1 : this.getOffers()) {
                double d0 = 0.3D + 0.0625D * (double)k;
                int j = (int)Math.floor(d0 * (double)merchantoffer1.getBuyingStackFirst().getCount());
                merchantoffer1.increaseSpecialPrice(-Math.max(j, 1));
            }
        }

    }

    @Override
    protected float getSoundPitch()
    {
        return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F;
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        if (this.isSleeping()) {
            return null;
        } else {
            return this.hasCustomer() ? SoundEvents.ENTITY_VILLAGER_TRADE : SoundEvents.ENTITY_VILLAGER_AMBIENT;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    public void setVillagerData(VillagerData p_213753_1_) {
        VillagerData villagerdata = this.getVillagerData();
        if (villagerdata.getProfession() != p_213753_1_.getProfession()) {
            this.offers = null;
        }

        this.dataManager.set(VILLAGER_DATA, p_213753_1_);
    }

    public VillagerData getVillagerData() {
        return this.dataManager.get(VILLAGER_DATA);
    }

    @Override
    public void setRevengeTarget(LivingEntity livingBase)
    {
        if (livingBase != null && this.world instanceof ServerWorld) {
            ((ServerWorld)this.world).updateReputation(IReputationType.VILLAGER_HURT, livingBase, this);
            if (this.isAlive() && livingBase instanceof PlayerEntity) {
                this.world.setEntityState(this, (byte)13);
            }
        }

        super.setRevengeTarget(livingBase);
    }

    @Override
    public void onDeath(DamageSource cause)
    {
        LOGGER.info("Alien Villager {} died, message: '{}'", this, cause.getDeathMessage(this).getString());
        Entity entity = cause.getTrueSource();
        if (entity != null) {
            this.func_223361_a(entity);
        }

        super.onDeath(cause);
    }

    private void func_223361_a(Entity p_223361_1_) {
        if (this.world instanceof ServerWorld) {
            Optional<List<LivingEntity>> optional = this.brain.getMemory(MemoryModuleType.VISIBLE_MOBS);
            if (optional.isPresent()) {
                ServerWorld serverworld = (ServerWorld)this.world;
                optional.get().stream().filter((p_223349_0_) -> {
                    return p_223349_0_ instanceof IReputationTracking;
                }).forEach((p_223342_2_) -> {
                    serverworld.updateReputation(IReputationType.VILLAGER_KILLED, p_223361_1_, (IReputationTracking)p_223342_2_);
                });
            }
        }
    }

    public boolean isTrading()
    {
        return this.getCustomer() != null;
    }

    @Override
    protected void onVillagerTrade(MerchantOffer offer) {
        int i = 3 + this.rand.nextInt(4);
        this.xp += offer.getGivenExp();
        this.previousCustomer = this.getCustomer();
        if (this.canLevelUp()) {
            this.timeUntilReset = 40;
            this.leveledUp = true;
            i += 5;
        }

        if (offer.getDoesRewardExp()) {
            this.world.addEntity(new ExperienceOrbEntity(this.world, this.getPosX(), this.getPosY() + 0.5D, this.getPosZ(), i));
        }
    }

    @Override
    protected void populateTradeData() {
        this.addTrades(this.getOffers(), DEFAULT_TRADE_LIST_MAP, 2);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setClientSideOffers(MerchantOffers recipeList)
    {
    }

    @Override
    public float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
    {
        return this.isChild() ? 0.81F : 1.62F;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 12) {
            this.spawnParticles(ParticleTypes.HEART);
        } else if (id == 13) {
            this.spawnParticles(ParticleTypes.ANGRY_VILLAGER);
        } else if (id == 14) {
            this.spawnParticles(ParticleTypes.HAPPY_VILLAGER);
        } else if (id == 42) {
            this.spawnParticles(ParticleTypes.SPLASH);
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @Override
    public EntityAlienVillager createChild(AgeableEntity ageable)
    {
        EntityAlienVillager entityvillager = GCEntities.ALIEN_VILLAGER.create(this.world);
        entityvillager.onInitialSpawn(this.world, this.world.getDifficultyForLocation(new BlockPos(entityvillager)), SpawnReason.BREEDING, null, null);
        return entityvillager;
    }

    @Override
    public World getWorld()
    {
        return this.world;
    }

    @Override
    public boolean canBeLeashedTo(PlayerEntity player)
    {
        return false;
    }

    @Override
    public void onStruckByLightning(LightningBoltEntity lightningBolt)
    {
        if (!this.world.isRemote && this.isAlive())
        {
            WitchEntity entitywitch = GCEntities.EVOLVED_WITCH.create(this.world);
            entitywitch.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch);
            entitywitch.onInitialSpawn(this.world, this.world.getDifficultyForLocation(new BlockPos(entitywitch)), SpawnReason.CONVERSION, null, null);
            entitywitch.setNoAI(this.isAIDisabled());

            if (this.hasCustomName())
            {
                entitywitch.setCustomName(this.getCustomName());
                entitywitch.setCustomNameVisible(this.getAlwaysRenderNameTagForRender());
            }

            this.world.addEntity(entitywitch);
            this.remove();
        }
    }

    @Override
    protected void updateEquipmentIfNeeded(ItemEntity itemEntity)
    {
        ItemStack itemstack = itemEntity.getItem();
        Item item = itemstack.getItem();

        if (this.canVillagerPickupItem(item))
        {
            ItemStack itemstack1 = this.getVillagerInventory().addItem(itemstack);

            if (itemstack1.isEmpty())
            {
                itemEntity.remove();
            }
            else
            {
                itemstack.setCount(itemstack1.getCount());
            }
        }
    }

    private boolean canVillagerPickupItem(Item itemIn)
    {
        return itemIn == Items.BREAD || itemIn == Items.POTATO || itemIn == Items.CARROT || itemIn == Items.WHEAT || itemIn == Items.WHEAT_SEEDS;
    }

    public boolean func_175553_cp()
    {
        return this.hasEnoughItems(1);
    }

    public boolean canAbondonItems()
    {
        return this.hasEnoughItems(2);
    }

    public boolean func_175557_cr()
    {
        return !this.hasEnoughItems(5);
    }

    private boolean hasEnoughItems(int multiplier)
    {
        for (int i = 0; i < this.getVillagerInventory().getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.getVillagerInventory().getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                if (itemstack.getItem() == Items.BREAD && itemstack.getCount() >= 3 * multiplier || itemstack.getItem() == Items.POTATO && itemstack.getCount() >= 12 * multiplier || itemstack.getItem() == Items.CARROT && itemstack.getCount() >= 12 * multiplier)
                {
                    return true;
                }

                if (itemstack.getItem() == Items.WHEAT && itemstack.getCount() >= 9 * multiplier)
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
    {
        if (super.replaceItemInInventory(inventorySlot, itemStackIn))
        {
            return true;
        }
        else
        {
            int i = inventorySlot - 300;

            if (i >= 0 && i < this.getVillagerInventory().getSizeInventory())
            {
                this.getVillagerInventory().setInventorySlotContents(i, itemStackIn);
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    @Override
    public void updateReputation(IReputationType type, Entity target) {

    }

    public static class EmeraldForItems implements VillagerTrades.ITrade
    {
        public ItemStack sellItem;
        public EntityAlienVillager.PriceInfo price;

        public EmeraldForItems(ItemStack itemStack, EntityAlienVillager.PriceInfo priceIn)
        {
            this.sellItem = itemStack;
            this.price = priceIn;
        }

        @Override
        public MerchantOffer getOffer(Entity trader, Random random)
        {
            int i = 1;

            if (this.price != null)
            {
                i = this.price.getPrice(random);
            }

            ItemStack tradeStack = this.sellItem.copy();
            tradeStack.setCount(i);

            return new MerchantOffer(tradeStack, new ItemStack(GCItems.lunarSapphire), 0, 7, 0);
        }
    }

    public static class ItemAndEmeraldToItem implements VillagerTrades.ITrade
    {
        public ItemStack field_179411_a;
        public EntityAlienVillager.PriceInfo field_179409_b;
        public ItemStack field_179410_c;

        public ItemAndEmeraldToItem(Item p_i45813_1_, EntityAlienVillager.PriceInfo p_i45813_2_, Item p_i45813_3_)
        {
            this.field_179411_a = new ItemStack(p_i45813_1_);
            this.field_179409_b = p_i45813_2_;
            this.field_179410_c = new ItemStack(p_i45813_3_);
        }

        public ItemAndEmeraldToItem(ItemStack p_i45813_1_, EntityAlienVillager.PriceInfo p_i45813_2_, ItemStack p_i45813_3_)
        {
            this.field_179411_a = p_i45813_1_;
            this.field_179409_b = p_i45813_2_;
            this.field_179410_c = p_i45813_3_;
        }


        @Override
        public MerchantOffer getOffer(Entity trader, Random random)
        {
            int i = 1;

            if (this.field_179409_b != null)
            {
                i = this.field_179409_b.getPrice(random);
            }

            return new MerchantOffer(this.field_179411_a.copy(), new ItemStack(GCItems.lunarSapphire), this.field_179410_c.copy(), 0, 7, 0);
        }
    }

    public static class ListItemForEmeralds implements VillagerTrades.ITrade
    {
        public ItemStack itemToBuy;
        public EntityAlienVillager.PriceInfo priceInfo;

        public ListItemForEmeralds(ItemStack stack, EntityAlienVillager.PriceInfo priceInfo)
        {
            this.itemToBuy = stack;
            this.priceInfo = priceInfo;
        }

        @Override
        public MerchantOffer getOffer(Entity trader, Random random)
        {
            int i = 1;

            if (this.priceInfo != null)
            {
                i = this.priceInfo.getPrice(random);
            }

            ItemStack itemstack;
            ItemStack itemstack1;

            if (i < 0)
            {
                itemstack = new ItemStack(GCItems.lunarSapphire);
                itemstack1 = new ItemStack(this.itemToBuy.getItem(), -i);
            }
            else
            {
                itemstack = new ItemStack(GCItems.lunarSapphire, i);
                itemstack1 = new ItemStack(this.itemToBuy.getItem());
            }

            return new MerchantOffer(itemstack, itemstack1, 0, 7, 0);
        }
    }

    public static class PriceInfo extends Tuple<Integer, Integer>
    {
        public PriceInfo(int p_i45810_1_, int p_i45810_2_)
        {
            super(Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_));
        }

        public int getPrice(Random rand)
        {
            return ((Integer) this.getA()).intValue() >= this.getB() ? this.getA() : this.getA() + rand.nextInt(this.getB() - this.getA() + 1);
        }
    }
}