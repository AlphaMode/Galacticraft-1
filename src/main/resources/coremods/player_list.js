var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');

function initializeCoreMod() {
    return {
        "fromUser": {
            "target": {
                "type": "METHOD",
                "class": "net.minecraft.server.management.PlayerList",
                "methodName": "createPlayerForUser",
                "methodDesc": "(Lcom/mojang/authlib/GameProfile;)Lnet/minecraft/entity/player/ServerPlayerEntity;"
            },
            "transformer": function (methodNode) {
                for (count = 0; count < methodNode.instructions.size(); count++)
                {
                    var list = methodNode.instructions.get(count);

                    if (list instanceof TypeInsnNode) {
                        var nodeAt = list;

                        if (nodeAt.getOpcode() != Opcodes.CHECKCAST && nodeAt.desc.contains("ServerPlayerEntity"))
                        {
                            var overwriteNode = new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/entities/player/GCEntityPlayerMP");

                            methodNode.instructions.set(nodeAt, overwriteNode);
                        }
                    } else if (list instanceof MethodInsnNode)
                    {
                        var nodeAt = list;

                        if (nodeAt.owner.contains("ServerPlayerEntity") && nodeAt.getOpcode() == Opcodes.INVOKESPECIAL)
                        {
                            methodNode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/entities/player/GCEntityPlayerMP",
                                "<init>", "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/server/ServerWorld;Lcom/mojang/authlib/GameProfile;Lnet/minecraft/server/management/PlayerInteractionManager;)V", false));
                        }
                    }
                }
                return methodNode;
            }
        },
        "respawn": {
            "target": {
                "type": "METHOD",
                "class": "net.minecraft.server.management.PlayerList",
                "methodName": "recreatePlayerEntity",
                "methodDesc": "(Lnet/minecraft/entity/player/ServerPlayerEntity;Lnet/minecraft/world/dimension/DimensionType;Z)Lnet/minecraft/entity/player/ServerPlayerEntity;"
            },
            "transformer": function (respawnPlayerMethod) {
                for (count = 0; count < respawnPlayerMethod.instructions.size(); count++)
                {
                    var list = respawnPlayerMethod.instructions.get(count);

                    if (list instanceof TypeInsnNode)
                    {
                        var nodeAt = list;

                        if (nodeAt.getOpcode() != Opcodes.CHECKCAST && nodeAt.desc.contains("ServerPlayerEntity"))
                        {
                            var overwriteNode = new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/entities/player/GCEntityPlayerMP");

                            respawnPlayerMethod.instructions.set(nodeAt, overwriteNode);
                        }
                    } else if (list instanceof MethodInsnNode)
                    {
                        var nodeAt = list;

                        if (nodeAt.owner.contains("ServerPlayerEntity") && nodeAt.getOpcode() == Opcodes.INVOKESPECIAL)
                        {
                            respawnPlayerMethod.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/entities/player/GCEntityPlayerMP",
                                "<init>", "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/server/ServerWorld;Lcom/mojang/authlib/GameProfile;Lnet/minecraft/server/management/PlayerInteractionManager;)V", false));
                        }
                    }
                }
                return respawnPlayerMethod;
            }
        }
    };
}