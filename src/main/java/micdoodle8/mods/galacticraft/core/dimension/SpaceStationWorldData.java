package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.UUID;

public class SpaceStationWorldData extends WorldSavedData
{
    private String spaceStationName = "NoName";
    private String owner = "NoOwner";
    private final ArrayList<UUID> allowedPlayers;
    private boolean allowAllPlayers;
    private DimensionType homePlanet;
    //    private DimensionType dimensionIdDynamic;
//    private DimensionType dimensionIdStatic;
    private DimensionType dimensionType;
    private CompoundNBT dataCompound;

    public SpaceStationWorldData(String dataName)
    {
        super(dataName);

        this.allowedPlayers = new ArrayList<UUID>();
    }

    public ArrayList<UUID> getAllowedPlayers()
    {
        return this.allowedPlayers;
    }

    public boolean getAllowedAll()
    {
        return this.allowAllPlayers;
    }

    public void putAllowedAll(boolean b)
    {
        this.allowAllPlayers = b;
        this.markDirty();
    }

    public String getOwner()
    {
        return this.owner;
    }

    public void putOwner(String name)
    {
        this.owner = name.replace(".", "");
        this.markDirty();
    }

    public String getSpaceStationName()
    {
        return this.spaceStationName;
    }

    public DimensionType getHomePlanet()
    {
        return homePlanet;
    }

    public void putSpaceStationName(String string)
    {
        this.spaceStationName = string;
    }

//    public DimensionType getDimensionIdStatic()
//    {
//        return dimensionIdStatic;
//    }
//
//    public void putDimensionIdStatic(DimensionType dimensionIdStatic)
//    {
//        this.dimensionIdStatic = dimensionIdStatic;
//    }
//
//    public DimensionType getDimensionIdDynamic()
//    {
//        return dimensionIdDynamic;
//    }
//
//    public void putDimensionIdDynamic(DimensionType dimensionIdDynamic)
//    {
//        this.dimensionIdDynamic = dimensionIdDynamic;
//    }


    public DimensionType getDimensionType()
    {
        return dimensionType;
    }

    public void setDimensionType(DimensionType dimensionType)
    {
        this.dimensionType = dimensionType;
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        this.owner = nbt.getString("owner").replace(".", "");
        this.spaceStationName = nbt.getString("spaceStationName");

        if (nbt.contains("dataCompound"))
        {
            this.dataCompound = nbt.getCompound("dataCompound");
        }
        else
        {
            this.dataCompound = new CompoundNBT();
        }

        if (nbt.contains("homePlanetRes"))
        {
            this.homePlanet = DimensionType.byName(new ResourceLocation(nbt.getString("homePlanetRes")));
        }
        else
        {
            GCLog.info("Home planet data not found in space station save file for \"" + this.spaceStationName + "\". Using default overworld.");
            this.homePlanet = DimensionType.OVERWORLD; // Overworld dimension ID
        }

//        if (nbt.contains("dimensionIdStatic"))
//        {
//            this.dimensionIdStatic = nbt.getInt("dimensionIdStatic");
//        }
//        else
//        {
//            GCLog.info("Static dimension ID not found in space station save file for \"" + this.spaceStationName + "\". Using default overworld.");
//            this.dimensionIdStatic = ConfigManagerCore.idDimensionOverworldOrbitStatic.get();
//        }
//
//        if (nbt.contains("dimensionIdDynamic"))
//        {
//            this.dimensionIdDynamic = nbt.getInt("dimensionIdDynamic");
//        }
//        else
//        {
//            GCLog.info("Dynamic dimension ID not found in space station save file for \"" + this.spaceStationName + "\". Using default overworld.");
//            this.dimensionIdDynamic = ConfigManagerCore.idDimensionOverworldOrbit.get();
//        }

        this.allowAllPlayers = nbt.getBoolean("allowedAll");

        ListNBT nbtList = nbt.getList("allowedPlayers", 10);
        this.allowedPlayers.clear();

        for (int i = 0; i < nbtList.size(); ++i)
        {
            CompoundNBT compound = nbtList.getCompound(i);
            UUID uid = compound.getUniqueId("allowedPlayer");

            if (!this.allowedPlayers.contains(uid))
            {
                this.allowedPlayers.add(uid);
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        nbt.putString("owner", this.owner);
        nbt.putString("spaceStationName", this.spaceStationName);
        if (this.homePlanet != null)
            nbt.putString("homePlanetRes", this.homePlanet.getRegistryName().toString());
//        nbt.putInt("dimensionIdDynamic", this.dimensionIdDynamic);
//        nbt.putInt("dimensionIdStatic", this.dimensionIdStatic); TODO
        nbt.put("dataCompound", this.dataCompound);
        nbt.putBoolean("allowedAll", this.allowAllPlayers);

        ListNBT nbtList = new ListNBT();

        for (int i = 0; i < this.allowedPlayers.size(); ++i)
        {
            UUID player = this.allowedPlayers.get(i);

            if (player != null)
            {
                CompoundNBT compound = new CompoundNBT();
                compound.putUniqueId("allowedPlayer", player);
                nbtList.add(compound);
            }
        }

        nbt.put("allowedPlayers", nbtList);
        return nbt;
    }

    /**
     * Retrieve an already created space station date entry
     */
    public static SpaceStationWorldData getStationData(ServerWorld world, ResourceLocation stationID, PlayerEntity player)
    {
        return getStationData(world, stationID, DimensionType.OVERWORLD, player);
    }

    /**
     * Retrieve a space station data entry, creating if necessary (with provided data)
     */
    public static SpaceStationWorldData getStationData(ServerWorld world, ResourceLocation stationID, DimensionType homeType/*, int providerIdDynamic, int providerIdStatic*/, PlayerEntity owner)
    {
        DimensionType providerType = DimensionType.byName(stationID);

//        boolean foundMatch = false;
//
        // Loop through all registered satellites, checking for a dimension ID match. If none is found, this method is
        // being called on an incorrect
//        for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
//        {
//            if (satellite.getDimensionIdStatic() == providerType.getId() || satellite.getDimensionType() == providerType)
//            {
//                foundMatch = true;
//                break;
//            }
//        }

        if (providerType == null)
        {
            return null;
        }
        else
        {
//            final String stationIdentifier = SpaceStationWorldData.getSpaceStationID(stationID);
            String id = stationID.toString();
            return world.getSavedData().getOrCreate(() -> {
                SpaceStationWorldData stationData = new SpaceStationWorldData(id);
                stationData.dataCompound = new CompoundNBT();

                if (owner != null)
                {
                    stationData.owner = PlayerUtil.getName(owner).replace(".", "");
                }

                stationData.spaceStationName = "Station: " + stationData.owner;

                if (owner != null)
                {
                    stationData.allowedPlayers.add(owner.getUniqueID());
                }

                if (homeType == null)
                {
                    throw new RuntimeException("Space station being created on bad home planet ID!");
                }
                else
                {
                    stationData.homePlanet = homeType;
                }

                stationData.markDirty();
                world.getServer().save(true, false, false);
                return stationData;
            }, id);



//            if (providerIdDynamic == -1 || providerIdStatic == -1)
//            {
//                throw new RuntimeException("Space station being created on bad dimension IDs!");
//            }
//            else
//            {
//                stationData.dimensionIdDynamic = providerIdDynamic;
//                stationData.dimensionIdStatic = providerIdStatic;
//            }
        }

//        if (stationData.getSpaceStationName().replace(" ", "").isEmpty())
//        {
//            stationData.putSpaceStationName("Station: " + stationData.owner);
//            stationData.markDirty();
//        }

//        return stationData;
    }

    public static SpaceStationWorldData getMPSpaceStationData(ServerWorld world, DimensionType stationId, PlayerEntity player)
    {
        final String id = stationId.getRegistryName().toString();
        if (world == null)
        {
            world = WorldUtil.getWorldForDimensionServer(stationId);
        }
        SpaceStationWorldData var3 = null;

        if (world != null)
        {
            var3 = world.getSavedData().getOrCreate(() -> {
                SpaceStationWorldData data = new SpaceStationWorldData(id);
                data.dataCompound = new CompoundNBT();

                if (player != null)
                {
                    data.owner = PlayerUtil.getName(player).replace(".", "");
                }

                data.spaceStationName = "Station: " + data.owner;

                if (player != null)
                {
                    data.allowedPlayers.add(player.getUniqueID());
                }

                data.markDirty();
                return data;
            }, id);
        }
        else
        {
            GCLog.severe("No world for dimension 0?  That should be unpossible!  Please report at https://github.com/micdoodle8/Galacticraft/issues/2617");
        }

        if (var3.getSpaceStationName().replace(" ", "").isEmpty())
        {
            var3.putSpaceStationName("Station: " + var3.owner);
            var3.markDirty();
        }

        return var3;
    }

    public static String getSpaceStationID(DimensionType dimID)
    {
        return "spacestation_" + dimID.getRegistryName();
    }

    public static void updateSSOwnership(ServerPlayerEntity player, String playerName, GCPlayerStats stats, DimensionType stationID, SpaceStationWorldData stationData)
    {
        if (stationData == null)
        {
            stationData = SpaceStationWorldData.getMPSpaceStationData(null, stationID, null);
        }

        if (stationData.owner.equals(playerName))
        {
            //This player is the owner of the station - ensure stats data matches
            if (!(stats.getSpaceStationDimensionData().values().contains(stationID)))
            {
                GCLog.debug("Player owns station: " + stationData.getSpaceStationName() + " with home planet " + stationData.getHomePlanet());
                stats.getSpaceStationDimensionData().put(stationData.getHomePlanet(), stationID);
            }
        }
        else
        {
            //This player is the owner of the station - remove from stats data
            DimensionType savedOwned = stats.getSpaceStationDimensionData().get(stationData.getHomePlanet());
            if (savedOwned != null && savedOwned == stationID)
            {
                GCLog.debug("Player does not own station: " + stationData.getSpaceStationName() + " with home planet " + stationData.getHomePlanet());
                stats.getSpaceStationDimensionData().remove(savedOwned);
            }
        }
    }

    public static void checkAllStations(ServerPlayerEntity thePlayer, GCPlayerStats stats)
    {
        String name = PlayerUtil.getName(thePlayer).replace(".", "");
        for (DimensionType stationType : WorldUtil.registeredSpaceStations)
        {
            SpaceStationWorldData.updateSSOwnership(thePlayer, name, stats, stationType, null);
        }
    }
}
