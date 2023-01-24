var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');

function initializeCoreMod() {
    return {
        "skyColor": {
            "target": {
                "type": "METHOD",
                "class": "net.minecraft.client.world.ClientWorld",
                "methodName": "func_228318_a_",
                "methodDesc": "(Lnet/minecraft/util/math/BlockPos;F)Lnet/minecraft/util/math/Vec3d;"
            },
            "transformer": function (node) {
                var labelNode = new LabelNode();
                node.instructions.insert(ASM.listOf(
                    new VarInsnNode(Opcodes.ALOAD, 0),
                    new VarInsnNode(Opcodes.ALOAD, 1),
                    new VarInsnNode(Opcodes.FLOAD, 2),
                    new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/TransformerHooks", "getSkyColorDimension",
                        "(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/math/BlockPos;F)Lnet/minecraft/util/math/Vec3d;", false),
                    new JumpInsnNode(Opcodes.IFNULL, labelNode),
                    new VarInsnNode(Opcodes.ALOAD, 0),
                    new VarInsnNode(Opcodes.ALOAD, 1),
                    new VarInsnNode(Opcodes.FLOAD, 2),
                    new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/TransformerHooks", "getSkyColorDimension",
                        "(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/math/BlockPos;F)Lnet/minecraft/util/math/Vec3d;", false),
                    new InsnNode(Opcodes.ARETURN),
                    labelNode
                ));
                return node;
            }
        }
    }
}