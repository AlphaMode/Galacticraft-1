package micdoodle8.mods.galacticraft.core.energy.tile;

import micdoodle8.mods.galacticraft.api.item.ElectricItemHelper;
import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.tile.ReceiverMode;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

import java.util.EnumSet;


public abstract class TileBaseUniversalElectrical extends EnergyStorageTile
{
    protected boolean isAddedToEnergyNet;
    protected Object powerHandlerBC;

    //	@NetworkedField(targetSide = LogicalSide.CLIENT)
    //	public float energyStored = 0;
    private final float IC2surplusInGJ = 0F;

    public TileBaseUniversalElectrical(TileEntityType<?> type)
    {
        super(type);
    }

    @Override
    public double getPacketRange()
    {
        return 12.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 3;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }

    public EnumSet<Direction> getElectricalInputDirections()
    {
        return EnumSet.allOf(Direction.class);
    }

    public EnumSet<Direction> getElectricalOutputDirections()
    {
        return EnumSet.noneOf(Direction.class);
    }

    @Override
    public float getRequest(Direction direction)
    {
        if (this.getElectricalInputDirections().contains(direction) || direction == null)
        {
            return super.getRequest(direction);
        }

        return 0F;
    }

    @Override
    public float receiveElectricity(Direction from, float receive, int tier, boolean doReceive)
    {
        if (this.getElectricalInputDirections().contains(from) || from == null)
        {
            return super.receiveElectricity(from, receive, tier, doReceive);
        }

        return 0F;
    }

    //	@Override
    //	public float receiveElectricity(EnumFacing from, ElectricityPack receive, boolean doReceive)
    //	{
    //		if (from == EnumFacing.UNKNOWN || this.getElectricalInputDirections().contains(from))
    //		{
    //			if (!doReceive)
    //			{
    //				return this.getRequest(from);
    //			}
    //
    //			return this.receiveElectricity(receive, doReceive);
    //		}
    //
    //		return 0F;
    //	}

    /**
     * A non-LogicalSide specific version of receiveElectricity for you to optionally
     * use it internally.
     */
    //	public float receiveElectricity(ElectricityPack receive, boolean doReceive)
    //	{
    //		if (receive != null)
    //		{
    //			float prevEnergyStored = this.getEnergyStored();
    //			float newStoredEnergy = Math.min(this.getEnergyStored() + receive.getWatts(), this.getMaxEnergyStored());
    //
    //			if (doReceive)
    //			{
    //				this.setEnergyStored(newStoredEnergy);
    //			}
    //
    //			return Math.max(newStoredEnergy - prevEnergyStored, 0);
    //		}
    //
    //		return 0;
    //	}

    //	public float receiveElectricity(float energy, boolean doReceive)
    //	{
    //		return this.receiveElectricity(ElectricityPack.getFromWatts(energy, this.getVoltage()), doReceive);
    //	}

    //	@Override
    //	public void setEnergyStored(float energy)
    //	{
    //		this.energyStored = Math.max(Math.min(energy, this.getMaxEnergyStored()), 0);
    //	}

    //	@Override
    //	public float getEnergyStored()
    //	{
    //		return this.energyStored;
    //	}

    //	public boolean canConnect(EnumFacing direction, NetworkType type)
    //	{
    //		if (direction == null || direction.equals(EnumFacing.UNKNOWN) || type != NetworkType.POWER)
    //		{
    //			return false;
    //		}
    //
    //		return this.getElectricalInputDirections().contains(direction) || this.getElectricalOutputDirections().contains(direction);
    //	}

    //	@Override
    //	public float getVoltage()
    //	{
    //		return 0.120F;
    //	}
    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        //		this.energyStored = nbt.getFloat("energyStored");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        //		nbt.putFloat("energyStored", this.energyStored);
        return nbt;
    }

    public void discharge(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            Item item = itemStack.getItem();
            float energyToDischarge = this.getRequest(null);

            if (item instanceof IItemElectric)
            {
                this.storage.receiveEnergyGC(ElectricItemHelper.dischargeItem(itemStack, energyToDischarge));
                this.poweredByTierGC = ((IItemElectric) item).getTierGC(itemStack);
            }
//            else if (EnergyConfigHandler.isRFAPILoaded() && item instanceof IEnergyContainerItem)
//            {
//                this.storage.receiveEnergyGC(((IEnergyContainerItem)item).extractEnergy(itemStack, (int) (energyToDischarge / EnergyConfigHandler.RF_RATIO), false) * EnergyConfigHandler.RF_RATIO);
//            }
//            else if (EnergyConfigHandler.isMekanismLoaded() && item instanceof IEnergizedItem && ((IEnergizedItem) item).canSend(itemStack))
//            {
//                this.storage.receiveEnergyGC((float) EnergizedItemManager.discharge(itemStack, energyToDischarge / EnergyConfigHandler.MEKANISM_RATIO) * EnergyConfigHandler.MEKANISM_RATIO);
//            } TODO Mekanism
//            else if (EnergyConfigHandler.isIndustrialCraft2Loaded())
//            {
//                if (item instanceof ISpecialElectricItem && item instanceof IElectricItem)
//                {
//                    IElectricItem electricItem = (IElectricItem) item;
//                    ISpecialElectricItem specialElectricItem = (ISpecialElectricItem) item;
//                    if (electricItem.canProvideEnergy(itemStack))
//                    {
//                        double energyDischargeIC2 = energyToDischarge / EnergyConfigHandler.IC2_RATIO;
//                        double result = specialElectricItem.getManager(itemStack).discharge(itemStack, energyDischargeIC2, 4, false, false, false);
//                        float energyDischarged = (float) result * EnergyConfigHandler.IC2_RATIO;
//                        this.storage.receiveEnergyGC(energyDischarged);
//                    }
//                }
//                else if (item instanceof IElectricItem)
//                {
//                    IElectricItem electricItem = (IElectricItem) item;
//                    if (electricItem.canProvideEnergy(itemStack))
//                    {
//                        double energyDischargeIC2 = energyToDischarge / EnergyConfigHandler.IC2_RATIO;
//                        double result = ElectricItem.manager.discharge(itemStack, energyDischargeIC2, 4, false, false, false);
//                        float energyDischarged = (float) result * EnergyConfigHandler.IC2_RATIO;
//                        this.storage.receiveEnergyGC(energyDischarged);
//                    }
//                }
//
//            } TODO IC Support
            //			else if (GCCoreCompatibilityManager.isTELoaded() && itemStack.getItem() instanceof IEnergyContainerItem)
            //			{
            //				float given = ((IEnergyContainerItem) itemStack.getItem()).extractEnergy(itemStack, (int) Math.floor(this.getRequest(EnumFacing.UNKNOWN) * EnergyConfigHandler.TO_TE_RATIO), false);
            //				this.receiveElectricity(given * EnergyConfigHandler.TE_RATIO, true);
            //			}
        }
    }

    @Override
    public void initiate()
    {
        super.initiate();
//        if (EnergyConfigHandler.isBuildcraftLoaded())
//        {
//            this.initBuildCraft();
//        } TODO
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.world.isRemote)
        {
//            if (!this.isAddedToEnergyNet)
//            {
            // Register to the IC2 Network
//                this.initIC();
//            }

//            if (EnergyConfigHandler.isIndustrialCraft2Loaded() && this.IC2surplusInGJ >= 0.001F)
//            {
//                this.IC2surplusInGJ -= this.storage.receiveEnergyGC(this.IC2surplusInGJ);
//                if (this.IC2surplusInGJ < 0.001F)
//                {
//                    this.IC2surplusInGJ = 0;
//                }
//            }

//            if (EnergyConfigHandler.isBuildcraftLoaded())
//            {
//                if (this.powerHandlerBC == null)
//                {
//                    this.initBuildCraft();
//                }
//
//                PowerHandler handler = (PowerHandler) this.powerHandlerBC;
//
//                double energyBC = handler.getEnergyStored();
//                if (energyBC > 0D)
//                {
//                    float usedBC = this.storage.receiveEnergyGC((float) energyBC * EnergyConfigHandler.BC3_RATIO) / EnergyConfigHandler.BC3_RATIO;
//                    energyBC -= usedBC;
//                    if (energyBC < 0D)
//                    {
//                        energyBC = 0D;
//                    }
//                    handler.setEnergy(energyBC);
//                }
//            } TODO
        }
    }

    /**
     * IC2 Methods
     */
//    @Override
//    public void invalidate()
//    {
//        super.invalidate();
//        this.unloadTileIC2();
//    }
//
//    @Override
//    public void onChunkUnload()
//    {
//        super.onChunkUnload();
//        this.unloadTileIC2();
//    }

//    protected void initIC()
//    {
//        if (EnergyConfigHandler.isIndustrialCraft2Loaded())
//        {
//            try
//            {
//                Object o = CompatibilityManager.classIC2tileEventLoad.getConstructor(IEnergyTile.class).newInstance(this);
//
//                if (o instanceof Event)
//                {
//                    MinecraftForge.EVENT_BUS.post((Event) o);
//                }
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//
//        this.isAddedToEnergyNet = true;
//    }
//
//    private void unloadTileIC2()
//    {
//        if (this.isAddedToEnergyNet && this.world != null)
//        {
//            if (EnergyConfigHandler.isIndustrialCraft2Loaded() && !this.world.isRemote)
//            {
//                try
//                {
//                    Object o = CompatibilityManager.classIC2tileEventUnload.getConstructor(IEnergyTile.class).newInstance(this);
//
//                    if (o instanceof Event)
//                    {
//                        MinecraftForge.EVENT_BUS.post((Event) o);
//                    }
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//
//            this.isAddedToEnergyNet = false;
//        }
//    }

//    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = CompatibilityManager.modidIC2)
//    public double getDemandedEnergy()
//    {
//        if (EnergyConfigHandler.disableIC2Input)
//        {
//            return 0.0;
//        }
//
//        try
//        {
//            if (this.IC2surplusInGJ < 0.001F)
//            {
//                this.IC2surplusInGJ = 0F;
//                return Math.ceil((this.storage.receiveEnergyGC(Integer.MAX_VALUE, true)) / EnergyConfigHandler.IC2_RATIO);
//            }
//
//            float received = this.storage.receiveEnergyGC(this.IC2surplusInGJ, true);
//            if (received == this.IC2surplusInGJ)
//            {
//                return Math.ceil((this.storage.receiveEnergyGC(Integer.MAX_VALUE, true) - this.IC2surplusInGJ) / EnergyConfigHandler.IC2_RATIO);
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return 0D;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = CompatibilityManager.modidIC2)
//    public double injectEnergy(Direction direction, double amount, double voltage)
//    {
//        //IC2 in 1.8.9 seems to have reversed the sense of direction here, but not in acceptsEnergyFrom.  (Seriously?!)
//        if (!EnergyConfigHandler.disableIC2Input && (direction == null || this.getElectricalInputDirections().contains(direction.getOpposite())))
//        {
//            float convertedEnergy = (float) amount * EnergyConfigHandler.IC2_RATIO;
//            int tierFromIC2 = ((int) voltage > 120) ? (((int) voltage > 256) ? 4 : 2) : 1;
//            float receive = this.receiveElectricity(direction == null ? null : direction.getOpposite(), convertedEnergy, tierFromIC2, true);
//
//            if (convertedEnergy > receive)
//            {
//                this.IC2surplusInGJ = convertedEnergy - receive;
//            }
//            else
//            {
//                this.IC2surplusInGJ = 0F;
//            }
//
//            // injectEnergy returns left over energy but all is used or goes into 'surplus'
//            return 0D;
//        }
//
//        return amount;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = CompatibilityManager.modidIC2)
//    public int getSinkTier()
//    {
//        return 3;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergyAcceptor", modID = CompatibilityManager.modidIC2)
//    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, Direction direction)
//    {
//        if (this.tileEntityInvalid) return false;
//        //Don't add connection to IC2 grid if it's a Galacticraft tile
//        if (emitter instanceof IElectrical || emitter instanceof IConductor || !(emitter instanceof IEnergyTile))
//        {
//            return false;
//        }
//
//        return this.getElectricalInputDirections().contains(direction);
//    }
//
//    //BuildCraft
//    public boolean canReceive()
//    {
//        return !EnergyConfigHandler.disableBuildCraftInput;
//    }
//
//    //Buildcraft 7
//    @RuntimeInterface(clazz = "buildcraft.api.mj.IMjReceiver", modID = CompatibilityManager.modBCraftEnergy)
//    public long getPowerRequested()
//    {
//        if (EnergyConfigHandler.disableBuildCraftInput)
//        {
//            return 0L;
//        }
//
//        // Boost stated demand by factor of 30, otherwise Buildcraft seems to send only a trickle of power
//        return (long) (this.storage.receiveEnergyGC(Integer.MAX_VALUE, true) / EnergyConfigHandler.BC8_INTERNAL_RATIO * 30F);
//    }
//
//    //Buildcraft 7
//    @RuntimeInterface(clazz = "buildcraft.api.mj.IMjReceiver", modID = CompatibilityManager.modBCraftEnergy)
//    public long receivePower(long microJoules, boolean simulate)
//    {
//        if (EnergyConfigHandler.disableBuildCraftInput)
//        {
//            return microJoules;
//        }
//        float receiveGC = microJoules * EnergyConfigHandler.BC8_INTERNAL_RATIO;
//        float sentGC = receiveGC - super.receiveElectricity(null, receiveGC, 1, !simulate);
//        return (long) (sentGC / EnergyConfigHandler.BC8_INTERNAL_RATIO);
//    }
//
//    //Buildcraft 7
//    @RuntimeInterface(clazz = "buildcraft.api.mj.IMjReceiver", modID = CompatibilityManager.modBCraftEnergy)
//    public boolean canConnect(@Nonnull IMjConnector other)
//    {
//        return true;
//    }
//
//    @RuntimeInterface(clazz = "cofh.redstoneflux.api.IEnergyReceiver", modID = "")
//    public int receiveEnergy(Direction from, int maxReceive, boolean simulate)
//    {
//        if (EnergyConfigHandler.disableRFInput)
//        {
//            return 0;
//        }
//
//        if (!this.getElectricalInputDirections().contains(from))
//        {
//            return 0;
//        }
//
//        return MathHelper.floor(super.receiveElectricity(from, maxReceive * EnergyConfigHandler.RF_RATIO, 1, !simulate) / EnergyConfigHandler.RF_RATIO);
//    }
//
//    @RuntimeInterface(clazz = "cofh.redstoneflux.api.IEnergyHandler", modID = "")
//    public boolean canConnectEnergy(Direction from)
//    {
//        return this.getElectricalInputDirections().contains(from) || this.getElectricalOutputDirections().contains(from);
//    }
//
//    @RuntimeInterface(clazz = "cofh.redstoneflux.api.IEnergyHandler", modID = "")
//    public int getEnergyStored(Direction from)
//    {
//        return MathHelper.floor(this.getEnergyStoredGC() / EnergyConfigHandler.RF_RATIO);
//    }
//
//    @RuntimeInterface(clazz = "cofh.redstoneflux.api.IEnergyHandler", modID = "")
//    public int getMaxEnergyStored(Direction from)
//    {
//        return MathHelper.floor(this.getMaxEnergyStoredGC() / EnergyConfigHandler.RF_RATIO);
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = CompatibilityManager.modidMekanism)
//    public double transferEnergyToAcceptor(Direction from, double amount)
//    {
//        if (EnergyConfigHandler.disableMekanismInput)
//        {
//            return 0;
//        }
//
//        if (!this.getElectricalInputDirections().contains(from))
//        {
//            return 0;
//        }
//
//        return this.receiveElectricity(from, (float) amount * EnergyConfigHandler.MEKANISM_RATIO, 1, true) / EnergyConfigHandler.MEKANISM_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = CompatibilityManager.modidMekanism)
//    public boolean canReceiveEnergy(Direction side)
//    {
//        return this.getElectricalInputDirections().contains(LogicalSide);
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = CompatibilityManager.modidMekanism)
//    public double acceptEnergy(Direction side, double amount, boolean simulate)
//    {
//        if (EnergyConfigHandler.disableMekanismInput)
//        {
//            return 0.0;
//        }
//
//        if (!this.getElectricalInputDirections().contains(LogicalSide))
//        {
//            return 0;
//        }
//
//        return this.receiveElectricity(LogicalSide, (float) amount * EnergyConfigHandler.MEKANISM_RATIO, 1, simulate) / EnergyConfigHandler.MEKANISM_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = CompatibilityManager.modidMekanism)
//    public void setEnergy(double energy)
//    {
//        if (EnergyConfigHandler.disableMekanismInput)
//        {
//            return;
//        }
//
//        this.storage.setEnergyStored((float) energy * EnergyConfigHandler.MEKANISM_RATIO);
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = CompatibilityManager.modidMekanism)
//    public double getMaxEnergy()
//    {
//        if (EnergyConfigHandler.disableMekanismInput)
//        {
//            return 0.0;
//        }
//
//        return this.getMaxEnergyStoredGC() / EnergyConfigHandler.MEKANISM_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyOutputter", modID = CompatibilityManager.modidMekanism)
//    public boolean canOutputEnergy(Direction side)
//    {
//        return false;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyOutputter", modID = CompatibilityManager.modidMekanism)
//	public double pullEnergy(Direction side, double amount, boolean simulate)
//	{
//    	return 0D;
//	}
    @Override
    public ReceiverMode getModeFromDirection(Direction direction)
    {
        if (this.getElectricalInputDirections().contains(direction))
        {
            return ReceiverMode.RECEIVE;
        }
        else if (this.getElectricalOutputDirections().contains(direction))
        {
            return ReceiverMode.EXTRACT;
        }

        return null;
    }

    /*
     * Compatibility: call this if the facing metadata is updated
     */
    public void updateFacing()
    {
//        if (EnergyConfigHandler.isIndustrialCraft2Loaded() && !this.world.isRemote)
//        {
//            //This seems the only method to tell IC2 the connection sides have changed
//            //(Maybe there is an internal refresh() method but it's not in the API)
//            this.unloadTileIC2();
//            //This will do an initIC2 on next tick update.
//        }
    }

//    @Override
//    public boolean hasCapability(Capability<?> capability, Direction facing)
//    {
//        if (capability == CapabilityEnergy.ENERGY || (EnergyConfigHandler.isBuildcraftLoaded() && (capability == MjAPI.CAP_RECEIVER || capability == MjAPI.CAP_CONNECTOR)))
//        {
//            return this.getElectricalInputDirections().contains(facing);
//        }
//        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
//    }
//
//    @Override
//    public <T> T getCapability(Capability<T> capability, Direction facing)
//    {
//        if (capability == CapabilityEnergy.ENERGY || (EnergyConfigHandler.isBuildcraftLoaded() && (capability == MjAPI.CAP_RECEIVER || capability == MjAPI.CAP_CONNECTOR)))
//        {
//            return this.getElectricalInputDirections().contains(facing) ? (T) new ForgeReceiver(this) : null;
//        }
//        return super.getCapability(capability, facing);
//    }

//    private static class ForgeReceiver implements net.minecraftforge.energy.IEnergyStorage
//    {
//        private TileBaseUniversalElectrical tile;
//
//        public ForgeReceiver(TileBaseUniversalElectrical tileElectrical)
//        {
//            this.tile = tileElectrical;
//        }
//
//        @Override
//        public int receiveEnergy(int maxReceive, boolean simulate)
//        {
//            if (EnergyConfigHandler.disableFEInput)
//                return 0;
//
//            return MathHelper.floor(tile.receiveElectricity(null, maxReceive * EnergyConfigHandler.RF_RATIO, 1, !simulate) / EnergyConfigHandler.RF_RATIO);
//        }
//
//        @Override
//        public boolean canReceive()
//        {
//            return !EnergyConfigHandler.disableFEInput;
//        }
//
//        @Override
//        public int getEnergyStored()
//        {
//            if (EnergyConfigHandler.disableFEInput)
//                return 0;
//
//            return MathHelper.floor(tile.getEnergyStoredGC() / EnergyConfigHandler.RF_RATIO);
//        }
//
//        @Override
//        public int getMaxEnergyStored()
//        {
//            if (EnergyConfigHandler.disableFEInput)
//                return 0;
//
//            return MathHelper.floor(tile.getMaxEnergyStoredGC() / EnergyConfigHandler.RF_RATIO);
//        }
//
//        @Override
//        public int extractEnergy(int maxExtract, boolean simulate)
//        {
//             return 0;
//        }
//
//        @Override
//        public boolean canExtract()
//        {
//            return false;
//        }
//
//        @RuntimeInterface(clazz = "buildcraft.api.mj.IMjReceiver", modID = CompatibilityManager.modBCraftEnergy)
//        public boolean canConnect(@Nonnull IMjConnector other)
//        {
//            return true;
//        }
//
//        @RuntimeInterface(clazz = "buildcraft.api.mj.IMjReceiver", modID = CompatibilityManager.modBCraftEnergy)
//        public long getPowerRequested()
//        {
//            return tile.getPowerRequested();
//        }
//
//        @RuntimeInterface(clazz = "buildcraft.api.mj.IMjReceiver", modID = CompatibilityManager.modBCraftEnergy)
//        public long receivePower(long microJoules, boolean simulate)
//        {
//            return tile.receivePower(microJoules, simulate);
//        }
//    }
}
