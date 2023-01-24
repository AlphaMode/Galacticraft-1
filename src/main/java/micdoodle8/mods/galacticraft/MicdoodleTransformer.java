/*
 * Copyright (c) 2022 Team Galacticraft
 *
 * Licensed under the MIT license.
 * See LICENSE file in the project root for details.
 */

package micdoodle8.mods.galacticraft;

import net.minecraftforge.coremod.api.ASMAPI;
import net.minecraftforge.fml.ModList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MicdoodleTransformer
{

    HashMap<String, ObfuscationEntry> nodemap = new HashMap<>();
    private boolean deobfuscated = true;
    private boolean optifinePresent;
    private boolean isServer;
    private boolean playerApiActive;

    private String nameForgeHooksClient;
    private String namePlayerList;
    private String namePlayerController;
    private String nameEntityLiving;
    private String nameEntityItem;
    private String nameActiveRenderInfo;
    private String nameLightTexture;
    private String nameEntityRenderer;
    private String nameItemRenderer;
    private String nameGuiSleep;
    private String nameParticleManager;
    private String nameNetHandlerPlay;
    private String nameRenderGlobal;
    private String nameRenderManager;
    private String nameEntity;
    private String nameChunkGeneratorOverworld;
    private String nameChunk;
    private String nameEntityArrow;
    private String nameEntityGolem;
    private String nameWorld;
    private String nameModelBiped;
    private String nameRRCB;
    private String nameRenderChunk;

    private static final String KEY_CLASS_PLAYER_MP = "PlayerMP";
    private static final String KEY_CLASS_WORLD = "worldClass";
    private static final String KEY_CLASS_CLIENT_WORLD = "clientWorldClass";
    private static final String KEY_PLAYER_LIST = "confManagerClass";
    private static final String KEY_CLASS_GAME_PROFILE = "gameProfileClass";
    private static final String KEY_CLASS_INTERACTION_MANAGER = "interactionManagerClass";
    private static final String KEY_CLASS_PLAYER_CONTROLLER = "playerControllerClass";
    private static final String KEY_CLASS_PLAYER_SP = "playerClient";
    private static final String KEY_CLASS_STAT_FILE_WRITER = "statFileWriterClass";
    private static final String KEY_CLASS_RECIPE_BOOK = "recipeBookClass";
    private static final String KEY_CLASS_NET_HANDLER_PLAY = "netHandlerPlayClientClass";
    private static final String KEY_CLASS_ENTITY_LIVING = "entityLivingClass";
    private static final String KEY_CLASS_ENTITY_ITEM = "entityItemClass";
    private static final String KEY_CLASS_ACTIVE_RENDER_INFO = "activeRenderInfoClass";
    private static final String KEY_CLASS_TEXTURE_LIGHT = "textureLightClass";
    private static final String KEY_CLASS_ENTITY_RENDERER = "entityRendererClass";
    private static final String KEY_CLASS_RENDER_GLOBAL = "renderGlobalClass";
    private static final String KEY_CLASS_RENDER_MANAGER = "renderManagerClass";
    private static final String KEY_CLASS_TESSELLATOR = "tessellatorClass";
    private static final String KEY_CLASS_CONTAINER_PLAYER = "containerPlayer";
    private static final String KEY_CLASS_MINECRAFT = "minecraft";
    private static final String KEY_CLASS_SESSION = "session";
    private static final String KEY_CLASS_GUI_SCREEN = "guiScreen";
    private static final String KEY_CLASS_ITEM_RENDERER = "itemRendererClass";
    private static final String KEY_CLASS_VEC3 = "vecClass";
    private static final String KEY_CLASS_ENTITY = "entityClass";
    private static final String KEY_CLASS_TILEENTITY = "tileEntityClass";
    private static final String KEY_CLASS_GUI_SLEEP = "guiSleepClass";
    private static final String KEY_CLASS_PARTICLE_MANAGER = "effectRendererClass";
    private static final String KEY_CLASS_FORGE_HOOKS_CLIENT = "forgeHooks";
    private static final String KEY_CLASS_CUSTOM_PLAYER_MP = "customPlayerMP";
    private static final String KEY_CLASS_CUSTOM_PLAYER_SP = "customPlayerSP";
    private static final String KEY_CLASS_CUSTOM_OTHER_PLAYER = "customEntityOtherPlayer";
    private static final String KEY_CLASS_PACKET_SPAWN_PLAYER = "packetSpawnPlayer";
    private static final String KEY_CLASS_ENTITY_OTHER_PLAYER = "entityOtherPlayer";
    private static final String KEY_CLASS_SERVER = "minecraftServer";
    private static final String KEY_CLASS_WORLD_SERVER = "worldServer";
    private static final String KEY_CLASS_WORLD_CLIENT = "worldClient";
    private static final String KEY_CLASS_CHUNK_PROVIDER_OVERWORLD = "chunkProviderServer";
    private static final String KEY_CLASS_ICHUNKPROVIDER = "IChunkProvider";
    private static final String KEY_CLASS_ICHUNKGENERATOR = "IChunkGenerator";
    private static final String KEY_CLASS_CHUNK = "chunkClass";
    private static final String KEY_NET_HANDLER_LOGIN_SERVER = "netHandlerLoginServer";
    private static final String KEY_CLASS_ENTITY_ARROW = "entityArrow";

    private static final String KEY_CLASS_ENTITYGOLEM = "entityGolem";
    private static final String KEY_CLASS_IBLOCKACCESS = "iBlockAccess";
    private static final String KEY_CLASS_BLOCK = "blockClass";
    private static final String KEY_CLASS_BLOCKPOS = "blockPos";
    private static final String KEY_CLASS_IBLOCKSTATE = "blockState";
    private static final String KEY_CLASS_ICAMERA = "icameraClass";
    private static final String KEY_CLASS_INTCACHE = "intCache";
    private static final String KEY_CLASS_MODEL_BIPED = "modelBiped";
    private static final String KEY_CLASS_RRCB = "rrcb";
    private static final String KEY_CLASS_VERTEX_BUFFER = "vertexBuffer";
    private static final String KEY_CLASS_CCTG = "cctg";
    private static final String KEY_CLASS_RENDER_CHUNK = "renderChunk";
    private static final String KEY_CLASS_DAMAGE_SOURCE = "damageSource";

    private static final String KEY_FIELD_CHUNK_XPOS = "chunkXPos";
    private static final String KEY_FIELD_CHUNK_ZPOS = "chunkZPos";
    private static final String KEY_FIELD_CHUNK_WORLD = "world";
    private static final String KEY_FIELD_ENTITY_RENDERER_RAINCOUNT = "entRenderRaincount";

    private static final String KEY_METHOD_CREATE_PLAYER = "createPlayerMethod";
    private static final String KEY_METHOD_RESPAWN_PLAYER = "respawnPlayerMethod";
    private static final String KEY_METHOD_CREATE_CLIENT_PLAYER = "createClientPlayerMethod";
    private static final String KEY_METHOD_MOVE_ENTITY = "moveEntityMethod";
    private static final String KEY_METHOD_ON_UPDATE = "onUpdateMethod";
    private static final String KEY_METHOD_UPDATE_LIGHTMAP = "updateLightmapMethod";
    private static final String KEY_METHOD_RENDER_OVERLAYS = "renderOverlaysMethod";
    private static final String KEY_METHOD_UPDATE_FOG_COLOR = "updateFogColorMethod";
    private static final String KEY_METHOD_GET_FOG_COLOR = "getFogColorMethod";
    private static final String KEY_METHOD_GET_SKY_COLOR = "getSkyColorMethod";
    private static final String KEY_METHOD_WAKE_ENTITY = "wakeEntityMethod";
    private static final String KEY_METHOD_BED_ORIENT_CAMERA = "orientBedCamera";
    private static final String KEY_METHOD_RENDER_PARTICLES = "renderParticlesMethod";
    private static final String KEY_METHOD_CUSTOM_PLAYER_MP = "customPlayerMPConstructor";
    private static final String KEY_METHOD_CUSTOM_PLAYER_SP = "customPlayerSPConstructor";
    private static final String KEY_METHOD_HANDLE_SPAWN_PLAYER = "handleSpawnPlayerMethod";
    private static final String KEY_METHOD_ORIENT_CAMERA = "orientCamera";
    private static final String KEY_METHOD_UPDATE = "update";
    private static final String KEY_METHOD_ADD_RAIN = "addRainParticles";
    private static final String KEY_METHOD_CAN_RENDER_FIRE = "canRenderOnFire";
    private static final String KEY_METHOD_POPULATE_CHUNK = "populateChunk";
    private static final String KEY_METHOD_RENDER_MODEL = "renderModel";
    private static final String KEY_METHOD_RAIN_STRENGTH = "getRainStrength";
    private static final String KEY_METHOD_REGISTEROF = "registerOF";
    private static final String KEY_METHOD_SETUP_TERRAIN = "setupTerrain";
    private static final String KEY_METHOD_GET_EYE_HEIGHT = "getEyeHeight";
    private static final String KEY_METHOD_ENABLE_ALPHA = "enableAlphaMethod";
    private static final String KEY_METHOD_VALIDATE = "teValidate";
    private static final String KEY_METHOD_BIPED_SET_ROTATION = "bipedSetRotation";
    private static final String KEY_METHOD_RRCB_GET_WORLD_RENDERER = "getWorldRenderer";
    private static final String KEY_METHOD_REBUILD_CHUNK = "rebuildChunk";
    private static final String KEY_METHOD_ATTACK_ENTITY_FROM = "attackEntityFrom";

    private static final String CLASS_RUNTIME_INTERFACE = "micdoodle8/mods/miccore/Annotations$RuntimeInterface";
    private static final String CLASS_MICDOODLE_PLUGIN = "micdoodle8/mods/miccore/MicdoodlePlugin";
    private static final String CLASS_TRANSFORMER_HOOKS = "micdoodle8/mods/galacticraft/core/TransformerHooks";
    private static final String CLASS_INTCACHE_VARIANT = "micdoodle8/mods/miccore/IntCache";
    private static final String CLASS_IENTITYBREATHABLE = "micdoodle8/mods/galacticraft/api/entity/IEntityBreathable";
    private static final String CLASS_RENDERPLAYEROF = "RenderPlayerOF";
    private static final String CLASS_IFORGEARMOR = "net/minecraftforge/common/ISpecialArmor$ArmorProperties";
    private static final String CLASS_MODEL_BIPED_GC = "micdoodle8/mods/galacticraft/core/client/model/ModelBipedGC";

    private static int operationCount = 0;
    private static int injectionCount = 0;

    public MicdoodleTransformer()
    {

            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_PLAYER_MP, new ObfuscationEntry("net/minecraft/entity/player/EntityPlayerMP"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_WORLD, new ObfuscationEntry("net/minecraft/world/World"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_CLIENT_WORLD, new ObfuscationEntry("net/minecraft/client/world/ClientWorld"));
            this.nodemap.put(MicdoodleTransformer.KEY_PLAYER_LIST, new ObfuscationEntry("net/minecraft/server/management/PlayerList"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_GAME_PROFILE, new ObfuscationEntry("com/mojang/authlib/GameProfile"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_INTERACTION_MANAGER, new ObfuscationEntry("net/minecraft/server/management/PlayerInteractionManager"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_PLAYER_CONTROLLER, new ObfuscationEntry("net/minecraft/client/multiplayer/PlayerController"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_PLAYER_SP, new ObfuscationEntry("net/minecraft/client/entity/player/ClientPlayerEntity"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_STAT_FILE_WRITER, new ObfuscationEntry("net/minecraft/stats/StatisticsManager"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_RECIPE_BOOK, new ObfuscationEntry("net/minecraft/client/util/ClientRecipeBook"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_NET_HANDLER_PLAY, new ObfuscationEntry("net/minecraft/client/network/play/ClientPlayNetHandler"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_ENTITY_LIVING, new ObfuscationEntry("net/minecraft/entity/LivingEntity"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_ENTITY_ITEM, new ObfuscationEntry("net/minecraft/entity/item/EntityItem"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_ACTIVE_RENDER_INFO, new ObfuscationEntry("net/minecraft/client/renderer/ActiveRenderInfo"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_TEXTURE_LIGHT, new ObfuscationEntry("net/minecraft/client/renderer/LightTexture"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_ENTITY_RENDERER, new ObfuscationEntry("net/minecraft/client/renderer/EntityRenderer"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_RENDER_GLOBAL, new ObfuscationEntry("net/minecraft/client/renderer/RenderGlobal"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_TESSELLATOR, new ObfuscationEntry("net/minecraft/client/renderer/Tessellator"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_RENDER_MANAGER, new ObfuscationEntry("net/minecraft/client/renderer/entity/RenderManager"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_CONTAINER_PLAYER, new ObfuscationEntry("net/minecraft/inventory/ContainerPlayer"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_MINECRAFT, new ObfuscationEntry("net/minecraft/client/Minecraft"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_SESSION, new ObfuscationEntry("net/minecraft/util/Session"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_GUI_SCREEN, new ObfuscationEntry("net/minecraft/client/gui/GuiScreen"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_ITEM_RENDERER, new ObfuscationEntry("net/minecraft/client/renderer/ItemRenderer"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_VEC3, new ObfuscationEntry("net/minecraft/util/math/Vec3d"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_ENTITY, new ObfuscationEntry("net/minecraft/entity/Entity"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_GUI_SLEEP, new ObfuscationEntry("net/minecraft/client/gui/GuiSleepMP"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_PARTICLE_MANAGER, new ObfuscationEntry("net/minecraft/client/particle/ParticleManager"));

            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_FORGE_HOOKS_CLIENT, new ObfuscationEntry("net/minecraftforge/client/ForgeHooksClient"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_CUSTOM_PLAYER_MP, new ObfuscationEntry("micdoodle8/mods/galacticraft/core/entities/player/GCEntityPlayerMP"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_CUSTOM_PLAYER_SP, new ObfuscationEntry("micdoodle8/mods/galacticraft/core/entities/player/ClientPlayerEntityGC"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_CUSTOM_OTHER_PLAYER, new ObfuscationEntry("micdoodle8/mods/galacticraft/core/entities/player/GCEntityOtherPlayerMP"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_PACKET_SPAWN_PLAYER, new ObfuscationEntry("net/minecraft/network/play/server/SPacketSpawnPlayer"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_ENTITY_OTHER_PLAYER, new ObfuscationEntry("net/minecraft/client/entity/EntityOtherPlayerMP"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_SERVER, new ObfuscationEntry("net/minecraft/server/MinecraftServer"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_WORLD_SERVER, new ObfuscationEntry("net/minecraft/world/WorldServer"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_WORLD_CLIENT, new ObfuscationEntry("net/minecraft/client/multiplayer/WorldClient"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_TILEENTITY, new ObfuscationEntry("net/minecraft/tileentity/TileEntity"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_CHUNK_PROVIDER_OVERWORLD, new ObfuscationEntry("net/minecraft/world/gen/ChunkGeneratorOverworld"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_ICHUNKPROVIDER, new ObfuscationEntry("net/minecraft/world/chunk/IChunkProvider"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_ICHUNKGENERATOR, new ObfuscationEntry("net/minecraft/world/gen/IChunkGenerator"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_CHUNK, new ObfuscationEntry("net/minecraft/world/chunk/Chunk"));
            this.nodemap.put(MicdoodleTransformer.KEY_NET_HANDLER_LOGIN_SERVER, new ObfuscationEntry("net/minecraft/server/network/NetHandlerLoginServer"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_ENTITY_ARROW, new ObfuscationEntry("net/minecraft/entity/projectile/EntityArrow"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_ENTITYGOLEM, new ObfuscationEntry("net/minecraft/entity/monster/EntityGolem"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_IBLOCKACCESS, new ObfuscationEntry("net/minecraft/world/IBlockAccess"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_BLOCK, new ObfuscationEntry("net/minecraft/block/Block"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_BLOCKPOS, new ObfuscationEntry("net/minecraft/util/math/BlockPos"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_IBLOCKSTATE, new ObfuscationEntry("net/minecraft/block/state/IBlockState"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_ICAMERA, new ObfuscationEntry("net/minecraft/client/renderer/culling/ICamera"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_INTCACHE, new ObfuscationEntry("net/minecraft/world/gen/layer/IntCache"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_MODEL_BIPED, new ObfuscationEntry("net/minecraft/client/renderer/entity/model/BipedModel"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_RRCB, new ObfuscationEntry("net/minecraft/client/renderer/RegionRenderCacheBuilder"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_VERTEX_BUFFER, new ObfuscationEntry("net/minecraft/client/renderer/BufferBuilder"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_CCTG, new ObfuscationEntry("net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_RENDER_CHUNK, new ObfuscationEntry("net/minecraft/client/renderer/chunk/RenderChunk"));
            this.nodemap.put(MicdoodleTransformer.KEY_CLASS_DAMAGE_SOURCE, new ObfuscationEntry("net/minecraft/util/DamageSource", "ur"));
            this.nodemap.put(MicdoodleTransformer.KEY_FIELD_CHUNK_XPOS, new FieldObfuscationEntry("x", "b"));
            this.nodemap.put(MicdoodleTransformer.KEY_FIELD_CHUNK_ZPOS, new FieldObfuscationEntry("z", "c"));
            this.nodemap.put(MicdoodleTransformer.KEY_FIELD_CHUNK_WORLD, new FieldObfuscationEntry("world", "k"));
            this.nodemap.put(MicdoodleTransformer.KEY_FIELD_ENTITY_RENDERER_RAINCOUNT, new FieldObfuscationEntry("rendererUpdateCount", "m"));

            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_CREATE_PLAYER, new MethodObfuscationEntry("createPlayerForUser", "g",
                    "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_GAME_PROFILE) + ";)L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_PLAYER_MP) + ";"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_RESPAWN_PLAYER, new MethodObfuscationEntry("recreatePlayerEntity", "a",
                    "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_PLAYER_MP) + ";IZ)L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_PLAYER_MP) + ";"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_CREATE_CLIENT_PLAYER,
                    new MethodObfuscationEntry("createPlayer", "func_199681_a",
                            "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_CLIENT_WORLD) + ";L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_STAT_FILE_WRITER) + ";L"
                                    + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_RECIPE_BOOK) + ";)L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_PLAYER_SP) + ";"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_MOVE_ENTITY, new MethodObfuscationEntry("travel", "a", "(FFF)V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_ON_UPDATE, new MethodObfuscationEntry("onUpdate", "B_", "()V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_UPDATE_LIGHTMAP, new MethodObfuscationEntry("updateLightmap", "func_205106_a", "(F)V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_RENDER_OVERLAYS, new MethodObfuscationEntry("renderOverlays", "b", "(F)V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_UPDATE_FOG_COLOR, new MethodObfuscationEntry("updateFogColor", "func_228371_a_", "(F)V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_GET_FOG_COLOR, new MethodObfuscationEntry("getFogColor", "f", "(F)L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_VEC3) + ";"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_GET_SKY_COLOR, new MethodObfuscationEntry("getSkyColor", "a",
                    "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY) + ";F)L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_VEC3) + ";"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_WAKE_ENTITY, new MethodObfuscationEntry("wakeFromSleep", "a", "()V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_BED_ORIENT_CAMERA,
                    new MethodObfuscationEntry("orientBedCamera",
                            "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_IBLOCKACCESS) + ";L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_BLOCKPOS) + ";L"
                                    + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_IBLOCKSTATE) + ";L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY) + ";)V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_RENDER_PARTICLES,
                    new MethodObfuscationEntry("renderParticles", "a", "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY) + ";F)V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_CUSTOM_PLAYER_MP,
                    new MethodObfuscationEntry("<init>", "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_SERVER) + ";L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_WORLD_SERVER) + ";L"
                            + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_GAME_PROFILE) + ";L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_INTERACTION_MANAGER) + ";)V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_CUSTOM_PLAYER_SP,
                    new MethodObfuscationEntry("<init>",
                            "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_MINECRAFT) + ";L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_CLIENT_WORLD) + ";L"
                                    + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_NET_HANDLER_PLAY) + ";L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_STAT_FILE_WRITER) + ";L"
                                    + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_RECIPE_BOOK) + ";)V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_HANDLE_SPAWN_PLAYER,
                    new MethodObfuscationEntry("handleSpawnPlayer", "a", "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_PACKET_SPAWN_PLAYER) + ";)V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_ORIENT_CAMERA, new MethodObfuscationEntry("orientCamera", "f", "(F)V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_UPDATE, new MethodObfuscationEntry("update", "func_216772_a", "(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/entity/Entity;ZZF)V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_ADD_RAIN, new MethodObfuscationEntry("addRainParticles", "q", "()V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_CAN_RENDER_FIRE, new MethodObfuscationEntry("canRenderOnFire", "bl", "()Z"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_POPULATE_CHUNK,
                    new MethodObfuscationEntry("populate", "a", "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ICHUNKGENERATOR) + ";)V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_RENDER_MODEL,
                    new MethodObfuscationEntry("renderModel", "a", "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY_LIVING) + ";FFFFFF)V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_RAIN_STRENGTH, new MethodObfuscationEntry("getRainStrength", "j", "(F)F"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_REGISTEROF, new MethodObfuscationEntry("register", "register", "()V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_SETUP_TERRAIN, new MethodObfuscationEntry("setupTerrain", "a",
                    "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY) + ";DL" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ICAMERA) + ";IZ)V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_GET_EYE_HEIGHT, new MethodObfuscationEntry("getEyeHeight", "by", "()F"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_ENABLE_ALPHA, new MethodObfuscationEntry("enableAlpha", "e", "()V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_VALIDATE, new MethodObfuscationEntry("validate", "func_145829_t", "()V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_BIPED_SET_ROTATION,
                    new MethodObfuscationEntry("setRotationAngles", "func_225597_a_", "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY_LIVING) + ";FFFFF)V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_RRCB_GET_WORLD_RENDERER,
                    new MethodObfuscationEntry("getWorldRendererByLayerId", "a", "(I)L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_VERTEX_BUFFER) + ";"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_REBUILD_CHUNK,
                    new MethodObfuscationEntry("rebuildChunk", "b", "(FFFL" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_CCTG) + ";)V"));
            this.nodemap.put(MicdoodleTransformer.KEY_METHOD_ATTACK_ENTITY_FROM,
                    new MethodObfuscationEntry("attackEntityFrom", "a", "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_DAMAGE_SOURCE) + ";F)Z"));
    }

    public boolean transform(String name, String transformedName, ClassNode node)
    {
        if (node == null)
        {
            return false;
        }

        if (name.startsWith("micdoodle8"))
        {
//            if (name.equals("micdoodle8.mods.galacticraft.core.energy.tile.TileCableIC2Sealed"))
//            {
//                node = this.transformTileCableIC2Sealed(node);
//            }

            return this.transformCustomAnnotations(node);
        } else
        {
            if (this.nameForgeHooksClient == null)
            {
                this.nameForgeHooksClient = this.getName(MicdoodleTransformer.KEY_CLASS_FORGE_HOOKS_CLIENT);
                if (this.deobfuscated)
                {
                    this.populateNamesDeObf();
                } else
                {
                    this.populateNamesObf();
                }
            }
            String testName = name.replace('.', '/');
            if (testName.equals(this.nameForgeHooksClient))
            {
//                return this.transformForgeHooks(node);
            }

            if (testName.equals(MicdoodleTransformer.CLASS_IFORGEARMOR))
            {
                return this.transformForgeArmor(node);
            }

            if (testName.equals(MicdoodleTransformer.CLASS_RENDERPLAYEROF))
            {
                return this.transformOptifine(node);
            }

            if (testName.equals("appeng/worldgen/MeteoritePlacer"))
            {
                return this.transformAEMeteorite(node);
            }

            node = this.transformRefs(node);

            if (testName.length() <= 3 || this.deobfuscated)
            {
                return this.transformVanilla(testName, node);
            }
        }

        return false;
    }

    private void populateNamesDeObf()
    {
        this.namePlayerList = this.getName(MicdoodleTransformer.KEY_PLAYER_LIST);
        this.namePlayerController = this.getName(MicdoodleTransformer.KEY_CLASS_PLAYER_CONTROLLER);
        this.nameEntityLiving = this.getName(MicdoodleTransformer.KEY_CLASS_ENTITY_LIVING);
        this.nameEntityItem = this.getName(MicdoodleTransformer.KEY_CLASS_ENTITY_ITEM);
        this.nameActiveRenderInfo = this.getName(MicdoodleTransformer.KEY_CLASS_ACTIVE_RENDER_INFO);
        this.nameLightTexture = this.getName(MicdoodleTransformer.KEY_CLASS_TEXTURE_LIGHT);
        this.nameEntityRenderer = this.getName(MicdoodleTransformer.KEY_CLASS_ENTITY_RENDERER);
        this.nameItemRenderer = this.getName(MicdoodleTransformer.KEY_CLASS_ITEM_RENDERER);
        this.nameGuiSleep = this.getName(MicdoodleTransformer.KEY_CLASS_GUI_SLEEP);
        this.nameParticleManager = this.getName(MicdoodleTransformer.KEY_CLASS_PARTICLE_MANAGER);
        this.nameNetHandlerPlay = this.getName(MicdoodleTransformer.KEY_CLASS_NET_HANDLER_PLAY);
        this.nameRenderGlobal = this.getName(MicdoodleTransformer.KEY_CLASS_RENDER_GLOBAL);
        this.nameRenderManager = this.getName(MicdoodleTransformer.KEY_CLASS_RENDER_MANAGER);
        this.nameEntity = this.getName(MicdoodleTransformer.KEY_CLASS_ENTITY);
        this.nameChunkGeneratorOverworld = this.getName(MicdoodleTransformer.KEY_CLASS_CHUNK_PROVIDER_OVERWORLD);
        this.nameChunk = this.getName(MicdoodleTransformer.KEY_CLASS_CHUNK);
        this.nameEntityArrow = this.getName(MicdoodleTransformer.KEY_CLASS_ENTITY_ARROW);
        this.nameEntityGolem = this.getName(MicdoodleTransformer.KEY_CLASS_ENTITYGOLEM);
        this.nameWorld = this.getName(MicdoodleTransformer.KEY_CLASS_WORLD);
        this.nameModelBiped = this.getName(MicdoodleTransformer.KEY_CLASS_MODEL_BIPED);
        this.nameRRCB = this.getName(MicdoodleTransformer.KEY_CLASS_RRCB);
        this.nameRenderChunk = this.getName(MicdoodleTransformer.KEY_CLASS_RENDER_CHUNK);
    }

    private void populateNamesObf()
    {
        this.namePlayerList = this.nodemap.get(MicdoodleTransformer.KEY_PLAYER_LIST).obfuscatedName;
        this.namePlayerController = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_PLAYER_CONTROLLER).obfuscatedName;
        this.nameEntityLiving = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_ENTITY_LIVING).obfuscatedName;
        this.nameEntityItem = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_ENTITY_ITEM).obfuscatedName;
        this.nameEntityRenderer = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_ENTITY_RENDERER).obfuscatedName;
        this.nameItemRenderer = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_ITEM_RENDERER).obfuscatedName;
        this.nameGuiSleep = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_GUI_SLEEP).obfuscatedName;
        this.nameParticleManager = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_PARTICLE_MANAGER).obfuscatedName;
        this.nameNetHandlerPlay = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_NET_HANDLER_PLAY).obfuscatedName;
        this.nameRenderGlobal = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_RENDER_GLOBAL).obfuscatedName;
        this.nameRenderManager = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_RENDER_MANAGER).obfuscatedName;
        this.nameEntity = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_ENTITY).obfuscatedName;
        this.nameChunkGeneratorOverworld = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_CHUNK_PROVIDER_OVERWORLD).obfuscatedName;
        this.nameChunk = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_CHUNK).obfuscatedName;
        this.nameEntityArrow = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_ENTITY_ARROW).obfuscatedName;
        this.nameEntityGolem = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_ENTITYGOLEM).obfuscatedName;
        this.nameWorld = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_WORLD).obfuscatedName;
        this.nameModelBiped = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_MODEL_BIPED).obfuscatedName;
        this.nameRRCB = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_RRCB).obfuscatedName;
        this.nameRenderChunk = this.nodemap.get(MicdoodleTransformer.KEY_CLASS_RENDER_CHUNK).obfuscatedName;
    }

    private boolean transformVanilla(String testName, ClassNode node)
    {
        if (testName.equals(this.namePlayerList))
        {
//            return this.transformPlayerList(node);
        } else if (testName.equals(this.namePlayerController))
        {
            return this.transformPlayerController(node);
        } else if (testName.equals(this.nameEntityLiving))
        {
            return this.transformEntityLiving(node);
        } else if (testName.equals(this.nameEntityItem)) {
            return this.transformEntityItem(node);
        } else if (testName.equals(this.nameActiveRenderInfo)) {
            return this.transformActiveRenderInfo(node);
        } else if (testName.equals(this.nameLightTexture)) {
            return this.transformLightTexture(node);
        } else if (testName.equals(this.nameEntityRenderer))
        {
            return this.transformEntityRenderer(node);
        } else if (testName.equals(this.nameItemRenderer))
        {
            return this.transformItemRenderer(node);
        } else if (testName.equals(this.nameGuiSleep))
        {
            return this.transformGuiSleep(node);
        } else if (testName.equals(this.nameParticleManager))
        {
            return this.transformParticleManager(node);
        } else if (testName.equals(this.nameRenderGlobal))
        {
            return this.transformRenderGlobal(node);
        } else if (testName.equals(this.nameEntity))
        {
            return this.transformEntityClass(node);
        } else if (testName.equals(this.nameChunk))
        {
            return this.transformChunkClass(node);
        } else if (testName.equals(this.nameEntityArrow))
        {
            return this.transformEntityArrow(node);
        } else if (testName.equals(this.nameEntityGolem))
        {
            return this.transformEntityGolem(node);
        } else if (testName.equals(this.nameWorld))
        {
            return this.transformWorld(node);
        } else if (testName.equals(this.nameModelBiped))
        {
            return this.transformModelBiped(node);
        } else if (testName.equals(this.nameRRCB))
        {
            return this.transformRRCB(node);
        } else if (testName.equals(this.nameRenderChunk))
        {
            return this.transformRenderChunk(node);
        }

        return false;
    }

    public boolean transformChunkClass(ClassNode node)
    {
        this.startInjection();

        MicdoodleTransformer.operationCount = 1;

        MethodNode populateMethod = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_POPULATE_CHUNK);

        if (populateMethod != null)
        {
            for (int count = populateMethod.instructions.size() - 1; count > 0; count--)
            {
                final AbstractInsnNode list = populateMethod.instructions.get(count);

                if (list instanceof MethodInsnNode)
                {
                    final MethodInsnNode nodeAt = (MethodInsnNode) list;

                    if (nodeAt.getOpcode() == Opcodes.INVOKESTATIC && nodeAt.owner.contains("GameRegistry"))
                    {
                        final MethodInsnNode hook = new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "otherModGenerate",
                                "(IIL" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_WORLD) + ";L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ICHUNKGENERATOR) + ";L"
                                        + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ICHUNKPROVIDER) + ";)V",
                                false);
                        populateMethod.instructions.set(nodeAt, hook);
                        MicdoodleTransformer.injectionCount++;
                        break;
                    }
                }
            }
        }
        return this.finishInjection(node);
    }

    public boolean transformPlayerController(ClassNode node)
    {
        this.startInjection();

        boolean playerAPI = this.isPlayerApiActive();
        MicdoodleTransformer.operationCount = playerAPI ? 0 : 2;

        MethodNode method = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_CREATE_CLIENT_PLAYER);

        if (method != null)
        {
            for (int count = 0; count < method.instructions.size(); count++)
            {
                final AbstractInsnNode list = method.instructions.get(count);

                if (list instanceof TypeInsnNode)
                {
                    final TypeInsnNode nodeAt = (TypeInsnNode) list;

                    if (nodeAt.desc.contains(this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_PLAYER_SP)))
                    {
                        final TypeInsnNode overwriteNode = new TypeInsnNode(Opcodes.NEW, this.getName(MicdoodleTransformer.KEY_CLASS_CUSTOM_PLAYER_SP));

                        method.instructions.set(nodeAt, overwriteNode);
                        MicdoodleTransformer.injectionCount++;
                    }
                } else if (list instanceof MethodInsnNode)
                {
                    final MethodInsnNode nodeAt = (MethodInsnNode) list;

                    if (nodeAt.name.equals("<init>") && nodeAt.owner.equals(this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_PLAYER_SP)))
                    {
                        method.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, this.getName(MicdoodleTransformer.KEY_CLASS_CUSTOM_PLAYER_SP),
                                this.getName(MicdoodleTransformer.KEY_METHOD_CUSTOM_PLAYER_SP), this.getDescDynamic(MicdoodleTransformer.KEY_METHOD_CUSTOM_PLAYER_SP), false));
                        MicdoodleTransformer.injectionCount++;
                    }
                }
            }
        }

        return this.finishInjection(node);
    }

    public boolean transformEntityLiving(ClassNode node)
    {
        this.startInjection();

        MicdoodleTransformer.operationCount = 1;

        MethodNode method = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_MOVE_ENTITY);

        if (method != null)
        {
            for (int count = 0; count < method.instructions.size(); count++)
            {
                final AbstractInsnNode list = method.instructions.get(count);

                if (list instanceof LdcInsnNode)
                {
                    final LdcInsnNode nodeAt = (LdcInsnNode) list;

                    if (nodeAt.cst.equals(0.08D))
                    {
                        final VarInsnNode beforeNode = new VarInsnNode(Opcodes.ALOAD, 0);
                        final MethodInsnNode overwriteNode = new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "getGravityForEntity",
                                "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY) + ";)D", false);

                        method.instructions.insertBefore(nodeAt, beforeNode);
                        method.instructions.set(nodeAt, overwriteNode);
                        MicdoodleTransformer.injectionCount++;
                    }
                }
            }
        }

        method = this.getMethod(node, KEY_METHOD_ATTACK_ENTITY_FROM);

        if (method != null)
        {
            for (int count = 0; count < method.instructions.size(); count++)
            {
                AbstractInsnNode test = method.instructions.get(count);

                if (test.getOpcode() == Opcodes.FCONST_2)
                {
                    test = method.instructions.get(count + 1);
                    if (test.getOpcode() == Opcodes.FMUL)
                    {
                        InsnList toAdd = new InsnList();
                        toAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        toAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "armorDamageHookF",
                                "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY_LIVING) + ";)F", false));
                        toAdd.add(new InsnNode(Opcodes.FMUL));
                        method.instructions.insertBefore(method.instructions.get(count + 2), toAdd);
                        MicdoodleTransformer.injectionCount++;
                    }
                }

                if (test instanceof LdcInsnNode)
                {
                    if (((LdcInsnNode) test).cst.equals(4.0F))
                    {
                        InsnList toAdd = new InsnList();
                        toAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        toAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "armorDamageHookF",
                                "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY_LIVING) + ";)F", false));
                        toAdd.add(new InsnNode(Opcodes.FMUL));
                        method.instructions.insertBefore(method.instructions.get(count + 2), toAdd);
                        MicdoodleTransformer.injectionCount++;
                    }
                }
            }
        }

        return this.finishInjection(node);
    }

    public boolean transformEntityItem(ClassNode node)
    {
        this.startInjection();

        MicdoodleTransformer.operationCount = 1;

        MethodNode method = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_ON_UPDATE);

        if (method != null)
        {
            for (int count = 0; count < method.instructions.size(); count++)
            {
                final AbstractInsnNode list = method.instructions.get(count);

                if (list instanceof LdcInsnNode)
                {
                    final LdcInsnNode nodeAt = (LdcInsnNode) list;

                    if (nodeAt.cst.equals(0.03999999910593033D))
                    {
                        final VarInsnNode beforeNode = new VarInsnNode(Opcodes.ALOAD, 0);
                        final MethodInsnNode overwriteNode = new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "getItemGravity",
                                "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY_ITEM) + ";)D", false);

                        method.instructions.insertBefore(nodeAt, beforeNode);
                        method.instructions.set(nodeAt, overwriteNode);
                        MicdoodleTransformer.injectionCount++;
                    }
                }
            }
        }

        return this.finishInjection(node);
    }

    public boolean transformActiveRenderInfo(ClassNode classNode) {
        this.startInjection();

        MethodNode updateMethod = this.getMethod(classNode, KEY_METHOD_UPDATE);

        if (updateMethod != null) {
            AbstractInsnNode node = ASMAPI.findFirstInstructionAfter(updateMethod, Opcodes.DNEG, 0);
            updateMethod.instructions.insert(node, ASMAPI.listOf(
                    new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "getCameraZoom", "(D)D", false)
            ));
        }

        return finishInjection(classNode);
    }

    public boolean transformLightTexture(ClassNode node) {
        MethodNode updateLightMapMethod = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_UPDATE_LIGHTMAP);

        if (updateLightMapMethod != null)
        {
            boolean worldBrightnessInjection = false;

            for (int count = 0; count < updateLightMapMethod.instructions.size(); count++)
            {
                final AbstractInsnNode list = updateLightMapMethod.instructions.get(count);

                if (list instanceof MethodInsnNode)
                {
                    MethodInsnNode nodeAt = (MethodInsnNode) list;

                    // Original code: float f1 =
                    // worldclient.getSunBrightness(1.0F) * 0.95F + 0.05F;
                    if (!worldBrightnessInjection && nodeAt.owner.equals(this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_CLIENT_WORLD)))
                    {
                        updateLightMapMethod.instructions.remove(updateLightMapMethod.instructions.get(count - 1));
                        updateLightMapMethod.instructions.set(updateLightMapMethod.instructions.get(count - 1), new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS,
                                "getWorldBrightness", "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_CLIENT_WORLD) + ";)F", false));
                        MicdoodleTransformer.injectionCount++;
                        worldBrightnessInjection = true;

                        continue;
                    }
                }

                if (list instanceof IntInsnNode)
                {
                    final IntInsnNode nodeAt = (IntInsnNode) list;

                    if (nodeAt.operand == 255)
                    {
                        final InsnList nodesToAdd = new InsnList();

                        nodesToAdd.add(new VarInsnNode(Opcodes.FLOAD, 12));
                        nodesToAdd.add(new VarInsnNode(Opcodes.ALOAD, 2));
                        nodesToAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "getColorRed",
                                "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_WORLD) + ";)F", false));
                        nodesToAdd.add(new InsnNode(Opcodes.FMUL));
                        nodesToAdd.add(new VarInsnNode(Opcodes.FSTORE, 12));

                        nodesToAdd.add(new VarInsnNode(Opcodes.FLOAD, 13));
                        nodesToAdd.add(new VarInsnNode(Opcodes.ALOAD, 2));
                        nodesToAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "getColorGreen",
                                "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_WORLD) + ";)F", false));
                        nodesToAdd.add(new InsnNode(Opcodes.FMUL));
                        nodesToAdd.add(new VarInsnNode(Opcodes.FSTORE, 13));

                        nodesToAdd.add(new VarInsnNode(Opcodes.FLOAD, 14));
                        nodesToAdd.add(new VarInsnNode(Opcodes.ALOAD, 2));
                        nodesToAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "getColorBlue",
                                "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_WORLD) + ";)F", false));
                        nodesToAdd.add(new InsnNode(Opcodes.FMUL));
                        nodesToAdd.add(new VarInsnNode(Opcodes.FSTORE, 14));

                        updateLightMapMethod.instructions.insertBefore(nodeAt, nodesToAdd);
                        MicdoodleTransformer.injectionCount++;

                        break;
                    }
                }
            }
        }
        return true;
    }

    public boolean transformEntityRenderer(ClassNode node)
    {
        this.startInjection();

        MicdoodleTransformer.operationCount = this.optifinePresent ? 6 : 7;


        MethodNode orientCameraMethod = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_ORIENT_CAMERA);
        MethodNode addRainMethod = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_ADD_RAIN);

        if (orientCameraMethod != null)
        {
            final InsnList nodesToAdd = new InsnList();

            nodesToAdd.add(new VarInsnNode(Opcodes.FLOAD, 1));
            nodesToAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "orientCamera", "(F)V", false));
            orientCameraMethod.instructions.insertBefore(orientCameraMethod.instructions.get(orientCameraMethod.instructions.size() - 3), nodesToAdd);
            MicdoodleTransformer.injectionCount++;

            for (int count = 0; count < orientCameraMethod.instructions.size(); count++)
            {
                final AbstractInsnNode list = orientCameraMethod.instructions.get(count);

                if (list instanceof VarInsnNode)
                {
                    VarInsnNode varNode = (VarInsnNode) list;

                    if (varNode.getOpcode() == Opcodes.DSTORE && varNode.var == 10)
                    {
                        nodesToAdd.clear();
                        nodesToAdd.add(new VarInsnNode(Opcodes.DLOAD, 10));
                        nodesToAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "getCameraZoom", "(D)D", false));
                        nodesToAdd.add(new VarInsnNode(Opcodes.DSTORE, 10));
                        orientCameraMethod.instructions.insert(list.getNext(), nodesToAdd);
                        MicdoodleTransformer.injectionCount++;
                        break;
                    }
                }
            }
        }



        if (addRainMethod != null)
        {
            for (int count = 0; count < addRainMethod.instructions.size(); count++)
            {
                final AbstractInsnNode listPos = addRainMethod.instructions.get(count);

                if (listPos.getOpcode() == Opcodes.FCMPL && listPos.getPrevious().getOpcode() == Opcodes.FCONST_0)
                {
                    final InsnList nodesToAdd = new InsnList();
                    nodesToAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    nodesToAdd.add(new FieldInsnNode(Opcodes.GETFIELD, this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY_RENDERER),
                            this.getNameDynamic(MicdoodleTransformer.KEY_FIELD_ENTITY_RENDERER_RAINCOUNT), "I"));
                    nodesToAdd.add(new VarInsnNode(Opcodes.FLOAD, 1));
                    nodesToAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "addRainParticles", "(IIF)I", false));
                    addRainMethod.instructions.insert(listPos, nodesToAdd);
                    MicdoodleTransformer.injectionCount++;
                    break;
                }
            }
        }

        return this.finishInjectionWithFrames(node, true);
    }

    private boolean transformModelBiped(ClassNode node)
    {
        this.startInjection();
        MicdoodleTransformer.operationCount = 1;

        MethodNode method = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_BIPED_SET_ROTATION);

        if (method != null)
        {
            // ModelBipedGC.setRotationAngles(this, par1, par2, par3, par4,
            // par5, par6, par7Entity);
            InsnList toAdd = new InsnList();
            toAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
            toAdd.add(new VarInsnNode(Opcodes.ALOAD, 1));
            toAdd.add(new VarInsnNode(Opcodes.FLOAD, 2));
            toAdd.add(new VarInsnNode(Opcodes.FLOAD, 3));
            toAdd.add(new VarInsnNode(Opcodes.FLOAD, 4));
            toAdd.add(new VarInsnNode(Opcodes.FLOAD, 5));
            toAdd.add(new VarInsnNode(Opcodes.FLOAD, 6));

            toAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_MODEL_BIPED_GC, "setRotationAngles",
                    "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_MODEL_BIPED) + ";L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY_LIVING) + ";FFFFF)V", false));
            method.instructions.insertBefore(method.instructions.get(method.instructions.size() - 2), toAdd);
            MicdoodleTransformer.injectionCount++;
        }

        return this.finishInjectionWithFrames(node, true);
    }

    private boolean transformRRCB(ClassNode node)
    {
        this.startInjection();
        MicdoodleTransformer.operationCount = 1;

        MethodNode method = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_RRCB_GET_WORLD_RENDERER);

        if (method != null)
        {
            for (int count = 0; count < method.instructions.size(); count++)
            {
                final AbstractInsnNode test = method.instructions.get(count);
                if (test.getOpcode() == Opcodes.ARETURN)
                {
                    InsnList toAdd = new InsnList();
                    toAdd.add(new InsnNode(Opcodes.DUP));
                    toAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "setCurrentBuffer",
                            "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_VERTEX_BUFFER) + ";)V", false));
                    method.instructions.insertBefore(test, toAdd);
                    MicdoodleTransformer.injectionCount++;
                    break;
                }
            }
        }

        return this.finishInjection(node);
    }

    private boolean transformRenderChunk(ClassNode node)
    {
        this.startInjection();
        MicdoodleTransformer.operationCount = 1;

        MethodNode method = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_REBUILD_CHUNK);

        if (method != null)
        {
            for (int count = 0; count < method.instructions.size(); count++)
            {
                final AbstractInsnNode test = method.instructions.get(count);
                if (test.getOpcode() == Opcodes.IOR)
                {
                    InsnList toAdd = new InsnList();
                    toAdd.add(new VarInsnNode(Opcodes.ALOAD, optifinePresent ? 19 : 16));
                    toAdd.add(
                            new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "isGrating", "(ZL" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_BLOCK) + ";)Z", false));
                    method.instructions.insertBefore(test, toAdd);
                    MicdoodleTransformer.injectionCount++;
                    break;
                }
            }
        }
        return this.finishInjection(node);
    }

    public boolean transformGuiSleep(ClassNode node)
    {
        this.startInjection();

        MicdoodleTransformer.operationCount = 1;

        MethodNode method = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_WAKE_ENTITY);

        if (method != null)
        {
            method.instructions.insertBefore(method.instructions.get(method.instructions.size() - 3),
                    new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_MICDOODLE_PLUGIN, "onSleepCancelled", "()V", false));
            MicdoodleTransformer.injectionCount++;
        }

        return this.finishInjection(node);
    }


    public boolean transformForgeArmor(ClassNode node)
    {
        this.startInjection();
        MicdoodleTransformer.operationCount = 2;
        final Iterator<MethodNode> methods = node.methods.iterator();
        while (methods.hasNext())
        {
            MethodNode method = methods.next();
            if (method.name.equals("applyArmor"))
            {
                int count = 0;
                for (int maxCount = method.instructions.size(); count < maxCount; count++)
                {
                    final AbstractInsnNode test = method.instructions.get(count);
                    // Search for: double absorb = damage * prop.AbsorbRatio;
                    if (test.getOpcode() == Opcodes.GETFIELD && ((FieldInsnNode) test).name.equals("AbsorbRatio"))
                    {
                        final AbstractInsnNode target = method.instructions.get(count + 1);
                        if (target.getOpcode() == Opcodes.DMUL)
                        {
                            InsnList toAdd = new InsnList();
                            toAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            toAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "armorDamageHook",
                                    "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY_LIVING) + ";)D", false));
                            toAdd.add(new InsnNode(Opcodes.DMUL));
                            method.instructions.insert(target, toAdd);
                            MicdoodleTransformer.injectionCount++;
                            break;
                        }
                    }
                }
                for (int maxCount = method.instructions.size(); count < maxCount; count++)
                {
                    final AbstractInsnNode test = method.instructions.get(count);
                    // Search for: double armorDamage = Math.max(1.0F, damage /
                    // 4.0F);
                    if (test.getOpcode() == Opcodes.INVOKESTATIC && ((MethodInsnNode) test).name.equals("max")) // Math.max
                    {
                        final AbstractInsnNode target = method.instructions.get(count + 1);
                        if (target.getOpcode() == Opcodes.DSTORE && ((VarInsnNode) target).var == 10)
                        {
                            InsnList toAdd = new InsnList();
                            toAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            toAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "armorDamageHook",
                                    "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY_LIVING) + ";)D", false));
                            toAdd.add(new InsnNode(Opcodes.DMUL));
                            method.instructions.insertBefore(target, toAdd);
                            MicdoodleTransformer.injectionCount++;
                            break;
                        }
                    }
                }
                break;
            }
        }
        return this.finishInjection(node, true);
    }

    public boolean transformEntityGolem(ClassNode node)
    {
        this.startInjection();
        MicdoodleTransformer.operationCount = 0;
        MicdoodleTransformer.injectionCount = 0;
        String inter = CLASS_IENTITYBREATHABLE;
        try
        {
            Class.forName(inter.replace("/", "."));
            if (!node.interfaces.contains(inter))
            {
                node.interfaces.add(inter);
                MicdoodleTransformer.injectionCount++;
            }
            MethodNode canBreathe = new MethodNode(Opcodes.ACC_PUBLIC, "canBreath", "()Z", null, null);
            canBreathe.instructions.add(new InsnNode(Opcodes.ICONST_1));
            canBreathe.instructions.add(new InsnNode(Opcodes.IRETURN));
            node.methods.add(canBreathe);
        } catch (Exception e)
        {
        }

        return this.finishInjection(node);
    }

    public boolean transformTileCableIC2Sealed(ClassNode node)
    {
        this.startInjection();
        MicdoodleTransformer.operationCount = 1;
        MethodNode method = this.getMethodNoDesc(node, "<init>");
        if (method != null)
        {
            for (int count = 0; count < method.instructions.size(); count++)
            {
                final AbstractInsnNode insn = method.instructions.get(count);

                if (insn instanceof MethodInsnNode)
                {
                    ((MethodInsnNode) insn).owner = "ic2/core/block/wiring/TileEntityCable";
                    MicdoodleTransformer.injectionCount++;
                    break;
                }

            }
        }

        return this.finishInjection(node);
    }

    @SuppressWarnings("unchecked")
    public boolean transformCustomAnnotations(ClassNode node)
    {
        this.startInjection();

        MicdoodleTransformer.operationCount = 0;
        MicdoodleTransformer.injectionCount = 0;

        final Iterator<MethodNode> methods = node.methods.iterator();
        List<String> ignoredMods = new ArrayList<>();

        while (methods.hasNext())
        {
            MethodNode methodnode = methods.next();

            methodLabel: if (methodnode.visibleAnnotations != null && methodnode.visibleAnnotations.size() > 0)
            {
                for (AnnotationNode annotation : methodnode.visibleAnnotations)
                {
                    if (annotation.desc.equals("L" + MicdoodleTransformer.CLASS_RUNTIME_INTERFACE + ";"))
                    {
                        List<String> desiredInterfaces = new ArrayList<>();
                        String modID = "";
                        String deobfName = "";

                        for (int i = 0; i < annotation.values.size(); i += 2)
                        {
                            Object value = annotation.values.get(i);

                            if (value.equals("clazz"))
                            {
                                desiredInterfaces.add(String.valueOf(annotation.values.get(i + 1)));
                            } else if (value.equals("modID"))
                            {
                                modID = String.valueOf(annotation.values.get(i + 1));
                            } else if (value.equals("altClasses"))
                            {
                                desiredInterfaces.addAll((ArrayList<String>) annotation.values.get(i + 1));
                            } else if (value.equals("deobfName"))
                            {
                                deobfName = String.valueOf(annotation.values.get(i + 1));
                            }
                        }

                        if (modID.isEmpty() || !ignoredMods.contains(modID))
                        {
                            boolean modFound = modID.isEmpty() || ModList.get().isLoaded(modID);

                            if (modFound)
                            {
                                for (String inter : desiredInterfaces)
                                {
                                    try
                                    {
                                        Class.forName(inter);
                                    } catch (ClassNotFoundException e)
                                    {

                                        this.printLog("Galacticraft ignored missing interface \"" + inter + "\" from mod \"" + modID + "\".");
                                        continue;
                                    }

                                    inter = inter.replace(".", "/");

                                    if (deobfName.equals("EXTENDS"))
                                    {

                                        this.printLog("Galacticraft added superclass \"" + inter + "\" dynamically from \"" + modID + "\" to class \"" + node.name + "\".");
                                        node.superName = inter;
                                        MicdoodleTransformer.injectionCount++;
                                        continue;
                                    }

                                    if (!node.interfaces.contains(inter))
                                    {

                                        this.printLog("Galacticraft added interface \"" + inter + "\" dynamically from \"" + modID + "\" to class \"" + node.name + "\".");
                                        node.interfaces.add(inter);
                                        MicdoodleTransformer.injectionCount++;

                                        if (!deobfName.isEmpty() && !deobfuscated)
                                        {
                                            String nameBefore = methodnode.name;
                                            methodnode.name = deobfName;

                                            this.printLog("Galacticraft renamed method " + nameBefore + " to " + deobfName + " in class " + node.name);
                                        }
                                    }

                                    break;
                                }
                            } else
                            {
                                ignoredMods.add(modID);

                                this.printLog("Galacticraft ignored dynamic interface insertion since \"" + modID + "\" was not found.");
                            }
                        }

                        break methodLabel;
                    }
                }
            }
        }

        if (MicdoodleTransformer.injectionCount > 0)
        {

            this.printLog("Galacticraft successfully injected bytecode into: " + node.name + " (" + MicdoodleTransformer.injectionCount + ")");
        }

        return this.finishInjection(node, false);
    }

    public boolean transformParticleManager(ClassNode node)
    {
        this.startInjection();

        MicdoodleTransformer.operationCount = 1;

        MethodNode renderParticlesMethod = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_RENDER_PARTICLES);

        if (renderParticlesMethod != null)
        {
            InsnList toAdd = new InsnList();
            toAdd.add(new VarInsnNode(Opcodes.FLOAD, 2));
            toAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "renderFootprints", "(F)V", false));
            renderParticlesMethod.instructions.insert(renderParticlesMethod.instructions.get(0), toAdd);
            MicdoodleTransformer.injectionCount++;
        }

        return this.finishInjection(node);
    }

    public boolean transformItemRenderer(ClassNode node)
    {
        this.startInjection();

        MicdoodleTransformer.operationCount = 1;

        MethodNode renderOverlaysMethod = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_RENDER_OVERLAYS);

        if (renderOverlaysMethod != null)
        {
            for (int count = 0; count < renderOverlaysMethod.instructions.size(); count++)
            {
                final AbstractInsnNode glEnable = renderOverlaysMethod.instructions.get(count);

                if (glEnable instanceof MethodInsnNode && ((MethodInsnNode) glEnable).name.equals(getNameDynamic(MicdoodleTransformer.KEY_METHOD_ENABLE_ALPHA)))
                {
                    InsnList toAdd = new InsnList();

                    toAdd.add(new VarInsnNode(Opcodes.FLOAD, 1));
                    toAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "renderLiquidOverlays", "(F)V", false));

                    renderOverlaysMethod.instructions.insertBefore(glEnable, toAdd);
                    MicdoodleTransformer.injectionCount++;
                    break;
                }
            }
        }

        return this.finishInjection(node);
    }

    public boolean transformRenderGlobal(ClassNode node)
    {
        this.startInjection();

        MicdoodleTransformer.operationCount = 1;

        MethodNode setupTerrainMethod = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_SETUP_TERRAIN);

        if (setupTerrainMethod != null)
        {
            for (int count = 0; count < setupTerrainMethod.instructions.size(); count++)
            {
                final AbstractInsnNode nodeTest = setupTerrainMethod.instructions.get(count);

                if (nodeTest instanceof MethodInsnNode && ((MethodInsnNode) nodeTest).name.equals(this.getNameDynamic(MicdoodleTransformer.KEY_METHOD_GET_EYE_HEIGHT)))
                {
                    /*
                     * The following (hacky) bytecode insertion will always let
                     * entities render above y=256 This is necessary for rockets
                     * since the 1.8 update changed directional frustum culling
                     * See WorldUtil.getRenderPosY
                     */
                    InsnList list = new InsnList();
                    list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    list.add(new VarInsnNode(Opcodes.DLOAD, 15));
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "getRenderPosY",
                            "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY) + ";D)D", false));
                    setupTerrainMethod.instructions.remove(setupTerrainMethod.instructions.get(count - 2));
                    setupTerrainMethod.instructions.remove(setupTerrainMethod.instructions.get(count - 2));
                    setupTerrainMethod.instructions.remove(setupTerrainMethod.instructions.get(count - 2));
                    setupTerrainMethod.instructions.remove(setupTerrainMethod.instructions.get(count - 2));
                    setupTerrainMethod.instructions.remove(setupTerrainMethod.instructions.get(count - 2));
                    setupTerrainMethod.instructions.insertBefore(setupTerrainMethod.instructions.get(count - 2), list);
                    MicdoodleTransformer.injectionCount++;
                    break;
                }
            }
        }

        return this.finishInjection(node);
    }

    public boolean transformEntityClass(ClassNode node)
    {
        if (isServer)
            return false;

        this.startInjection();

        MicdoodleTransformer.operationCount = 1;

        MethodNode method = this.getMethod(node, KEY_METHOD_CAN_RENDER_FIRE);

        if (method != null)
        {
            for (int i = 0; i < method.instructions.size(); i++)
            {
                AbstractInsnNode nodeAt = method.instructions.get(i);

                if (nodeAt instanceof MethodInsnNode && nodeAt.getOpcode() == Opcodes.INVOKEVIRTUAL)
                {
                    MethodInsnNode overwriteNode = new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "shouldRenderFire",
                            "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY) + ";)Z", false);

                    method.instructions.set(nodeAt, overwriteNode);
                    MicdoodleTransformer.injectionCount++;
                    return true;
                }
            }
        }

        return this.finishInjection(node);
    }

    public boolean transformEntityArrow(ClassNode node)
    {
        this.startInjection();

        MicdoodleTransformer.operationCount = 1;

        MethodNode method = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_ON_UPDATE);

        if (method != null)
        {
            for (int count = 0; count < method.instructions.size(); count++)
            {
                final AbstractInsnNode list = method.instructions.get(count);

                if (list instanceof LdcInsnNode)
                {
                    final LdcInsnNode nodeAt = (LdcInsnNode) list;

                    if (nodeAt.cst.equals(0.05F))
                    {
                        final VarInsnNode beforeNode = new VarInsnNode(Opcodes.ALOAD, 0);
                        final MethodInsnNode overwriteNode = new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "getArrowGravity",
                                "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_ENTITY_ARROW) + ";)F", false);

                        method.instructions.insertBefore(nodeAt, beforeNode);
                        method.instructions.set(nodeAt, overwriteNode);
                        MicdoodleTransformer.injectionCount++;
                    }
                }
            }
        }

        return this.finishInjection(node);
    }

    public boolean transformWorld(ClassNode node)
    {
        this.startInjection();

        MicdoodleTransformer.operationCount = 1;

        MethodNode method = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_RAIN_STRENGTH);

        if (method != null)
        {
            for (int count = 0; count < method.instructions.size(); ++count)
            {
                final AbstractInsnNode list = method.instructions.get(count);

                if (list.getOpcode() == Opcodes.ALOAD)
                {
                    if (count + 10 >= method.instructions.size())
                    {
                        // MicdoodlePlugin.showErrorDialog(new Object[]{"Exit",
                        // "Ignore"}, "Are there two copies of MicdoodleCore in
                        // your mods folder? Please remove one!");
                        break;
                    }
                    // Remove ALOAD, GETFIELD, ALOAD, GETFIELD, ALOAD, GETFIELD,
                    // FSUB, FLOAD, FMUL, FADD but keep FRETURN
                    for (int i = 0; i < 10; ++i)
                    {
                        method.instructions.remove(method.instructions.get(count));
                    }

                    InsnList toAdd = new InsnList();
                    toAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    toAdd.add(new VarInsnNode(Opcodes.FLOAD, 1));
                    toAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "getRainStrength",
                            "(L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_WORLD) + ";F)F", false));
                    method.instructions.insertBefore(method.instructions.get(count), toAdd);
                    MicdoodleTransformer.injectionCount++;
                    break;
                }
            }
        }

        return this.finishInjection(node);
    }

    public boolean transformOptifine(ClassNode node)
    {
        this.startInjection();

        MicdoodleTransformer.operationCount = 1;

        MethodNode method = this.getMethod(node, MicdoodleTransformer.KEY_METHOD_REGISTEROF);

        if (method != null)
        {
            AbstractInsnNode toAdd = new InsnNode(Opcodes.RETURN);
            method.instructions.insertBefore(method.instructions.get(0), toAdd);
            MicdoodleTransformer.injectionCount++;
        }

        return this.finishInjection(node);
    }

    public boolean transformAEMeteorite(ClassNode node)
    {
        this.startInjection();
        MethodNode method = this.getMethodNoDesc(node, "<init>");
        MicdoodleTransformer.operationCount = 1;

        if (method != null)
        {
            for (int count = 0; count < method.instructions.size(); ++count)
            {
                final AbstractInsnNode list = method.instructions.get(count);
                if (list.getOpcode() == Opcodes.GETFIELD && ((FieldInsnNode) list).name.equals("skyStoneDefinition"))
                {
                    AbstractInsnNode toAdd = new MethodInsnNode(Opcodes.INVOKESTATIC, MicdoodleTransformer.CLASS_TRANSFORMER_HOOKS, "addAE2MeteorSpawn",
                            "(Ljava/lang/Object;L" + this.getNameDynamic(MicdoodleTransformer.KEY_CLASS_BLOCK) + ";)Z", false);
                    method.instructions.set(method.instructions.get(count - 5), toAdd);
                    MicdoodleTransformer.injectionCount++;
                    break;
                }
            }
        }

        return this.finishInjection(node);
    }

    public ClassNode transformRefs(ClassNode node)
    {
        this.startInjection();
        String intCache1 = this.getName(KEY_CLASS_INTCACHE);
        String intCache2 = this.getObfName(KEY_CLASS_INTCACHE);
        int invokeStatic = Opcodes.INVOKESTATIC;

        for (MethodNode m : node.methods)
        {
            int listsize = m.instructions.size();
            for (int count = 0; count < listsize; count++)
            {
                if (m.instructions.get(count).getOpcode() == invokeStatic)
                {
                    MethodInsnNode mn = (MethodInsnNode) m.instructions.get(count);
                    if (mn.owner.equals(intCache1) || mn.owner.equals(intCache2))
                    {
                        mn.owner = CLASS_INTCACHE_VARIANT;
                    }
                }
            }
        }

        this.finishInjection(node, false);
        return node;
    }

    public static class ObfuscationEntry
    {

        public String name;
        public String obfuscatedName;

        public ObfuscationEntry(String name, String obfuscatedName)
        {
            this.name = name;
            this.obfuscatedName = obfuscatedName;
        }

        public ObfuscationEntry(String name)
        {
            this(name, name);
        }
    }

    public static class MethodObfuscationEntry extends ObfuscationEntry
    {

        public String methodDesc;

        public MethodObfuscationEntry(String name, String obfuscatedName, String methodDesc)
        {
            super(name, obfuscatedName);
            this.methodDesc = methodDesc;
        }

        public MethodObfuscationEntry(String commonName, String methodDesc)
        {
            this(commonName, commonName, methodDesc);
        }
    }

    public static class FieldObfuscationEntry extends ObfuscationEntry
    {

        public FieldObfuscationEntry(String name, String obfuscatedName)
        {
            super(name, obfuscatedName);
        }
    }

    public static Logger miccoreLogger = LogManager.getLogger("MicdoodleCore");

    private void printResultsAndReset(String nodeName)
    {
        if (MicdoodleTransformer.operationCount > 0)
        {
            if (MicdoodleTransformer.injectionCount >= MicdoodleTransformer.operationCount)
            {

                this.printLog("Galacticraft successfully injected bytecode into: " + nodeName + " (" + MicdoodleTransformer.injectionCount + " / " + MicdoodleTransformer.operationCount + ")");
            } else
            {
                miccoreLogger.error("Potential problem: Galacticraft did not complete injection of bytecode into: " + nodeName + " (" + MicdoodleTransformer.injectionCount + " / "
                        + MicdoodleTransformer.operationCount + ")");
            }
        }
    }

    private MethodNode getMethod(ClassNode node, String keyName)
    {
        for (MethodNode methodNode : node.methods)
        {
            if (this.methodMatches(keyName, methodNode))
            {
                return methodNode;
            }
        }

        return null;
    }

    private MethodNode getMethodNoDesc(ClassNode node, String methodName)
    {
        for (MethodNode methodNode : node.methods)
        {
            if (methodNode.name.equals(methodName))
            {
                return methodNode;
            }
        }

        return null;
    }

    private boolean methodMatches(String keyName, MethodInsnNode node)
    {
        return node.name.equals(this.getNameDynamic(keyName)) && node.desc.equals(this.getDescDynamic(keyName));
    }

    private boolean methodMatches(String keyName, MethodNode node)
    {
        return node.name.equals(this.getNameDynamic(keyName)) && node.desc.equals(this.getDescDynamic(keyName));
    }

    public String getName(String keyName)
    {
        return this.nodemap.get(keyName).name;
    }

    public String getObfName(String keyName)
    {
        return this.nodemap.get(keyName).obfuscatedName;
    }

    public String getNameDynamic(String keyName)
    {
        try
        {
            if (this.deobfuscated)
            {
                return this.getName(keyName);
            } else
            {
                return this.getObfName(keyName);
            }
        } catch (NullPointerException e)
        {
            this.printLog("Could not find key: " + keyName);
            throw e;
        }
    }

    public String getDescDynamic(String keyName)
    {
        return ((MethodObfuscationEntry) this.nodemap.get(keyName)).methodDesc;
    }

    private void printLog(String message)
    {
        miccoreLogger.info(message);
    }

    private void startInjection()
    {
        MicdoodleTransformer.injectionCount = 0;
        MicdoodleTransformer.operationCount = 0;
    }

    private boolean finishInjection(ClassNode node)
    {
        return this.finishInjection(node, true);
    }

    private boolean finishInjection(ClassNode node, boolean printToLog)
    {
        if (printToLog)
        {
            this.printResultsAndReset(node.name);
        }

        return true;
    }

    private boolean finishInjectionWithFrames(ClassNode node, boolean printToLog)
    {

        if (printToLog)
        {
            this.printResultsAndReset(node.name);
        }

        return true;
    }

    private boolean isPlayerApiActive()
    {
        return false;
    }
}
