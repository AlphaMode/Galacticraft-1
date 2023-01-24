var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');

function initializeCoreMod() {
    return {
        "customPlayer": {
            "target": {
                "type": "METHOD",
                "class": "net.minecraft.client.network.play.ClientPlayNetHandler",
                "methodName": "func_147237_a",
                "methodDesc": "(Lnet/minecraft/network/play/server/SSpawnPlayerPacket;)V"
            },
            "transformer": function (handleNamedSpawnMethod) {
                for (count = 0; count < handleNamedSpawnMethod.instructions.size(); count++) {
                    var list = handleNamedSpawnMethod.instructions.get(count);

                    if (list instanceof TypeInsnNode) {
                        var nodeAt = list;

                        if (nodeAt.desc.contains("RemoteClientPlayerEntity")) {
                            var overwriteNode = new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/entities/player/GCEntityOtherPlayerMP");

                            handleNamedSpawnMethod.instructions.set(nodeAt, overwriteNode);
                        }
                    } else if (list instanceof MethodInsnNode) {
                        var nodeAt = list;

                        if (nodeAt.name.equals("<init>") && nodeAt.owner.equals("net/minecraft/client/entity/player/RemoteClientPlayerEntity")) {
                            handleNamedSpawnMethod.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/entities/player/GCEntityOtherPlayerMP", "<init>",
                                "(Lnet/minecraft/client/world/ClientWorld;Lcom/mojang/authlib/GameProfile;)V", false));
                        }
                    }
                }
                return handleNamedSpawnMethod;
            }
        }
    }
}