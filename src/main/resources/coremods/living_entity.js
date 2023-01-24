var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

function initializeCoreMod() {
    return {
        "gravity": {
            "target": {
                "type":"METHOD","class":"net.minecraft.entity.LivingEntity","methodName":"travel","methodDesc":"(Lnet/minecraft/util/math/Vec3d;)V"
            },
            "transformer": function (method) {
                method.instructions.insert(ASM.listOf(
                    new VarInsnNode(Opcodes.ALOAD, 0),
                    new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/TransformerHooks", "updateEntityGravity",
                        "(Lnet/minecraft/entity/LivingEntity;)V", false)
                ));
                return method;
            }
        }
    }
}