var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');

function initializeCoreMod() {
    return {
        "fogColor": {
            "target": {
                "type": "METHOD",
                "class": "net.minecraft.client.renderer.FogRenderer",
                "methodName": "updateFogColor",
                "methodDesc": "(Lnet/minecraft/client/renderer/ActiveRenderInfo;FLnet/minecraft/client/world/ClientWorld;IF)V"
            },
            "transformer": function (updateFogColorMethod) {
                for (count = 0; count < updateFogColorMethod.instructions.size(); count++)
                {
                    var list = updateFogColorMethod.instructions.get(count);

                    if (list instanceof MethodInsnNode)
                    {
                        var nodeAt = list;

                        if (nodeAt.name.equals(ASM.mapMethod("func_228329_i_")) && nodeAt.desc.equals("(F)Lnet/minecraft/util/math/Vec3d;"))
                        {
                            var toAdd = new InsnList();

                            toAdd.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            toAdd.add(new VarInsnNode(Opcodes.FLOAD, 1));
                            toAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/TransformerHooks", "getFogColorHook",
                                "(Lnet/minecraft/client/world/ClientWorld;F)Lnet/minecraft/util/math/Vec3d;", false));
                            toAdd.add(new VarInsnNode(Opcodes.ASTORE, 11));

                            updateFogColorMethod.instructions.insertBefore(updateFogColorMethod.instructions.get(count + 2), toAdd);
                        }
                    }
                }
                return updateFogColorMethod;
            }
        },
        "skyColor": {
            "target": {
                "type": "METHOD",
                "class": "net.minecraft.client.renderer.FogRenderer",
                "methodName": "updateFogColor",
                "methodDesc": "(Lnet/minecraft/client/renderer/ActiveRenderInfo;FLnet/minecraft/client/world/ClientWorld;IF)V"
            },
            "transformer": function (updateFogColorMethod) {
                for (count = 0; count < updateFogColorMethod.instructions.size(); count++) {
                    var list = updateFogColorMethod.instructions.get(count);

                    if (list instanceof MethodInsnNode) {
                        var nodeAt = list;

                        if (nodeAt.name.equals(ASM.mapMethod("func_228318_a_")) && nodeAt.desc.equals("(Lnet/minecraft/util/math/BlockPos;F)Lnet/minecraft/util/math/Vec3d;")) {
                            var toAdd = new InsnList();

                            toAdd.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            toAdd.add(new VarInsnNode(Opcodes.FLOAD, 1));
                            toAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/TransformerHooks", "getSkyColorHook",
                                "(Lnet/minecraft/client/world/ClientWorld;F)Lnet/minecraft/util/math/Vec3d;", false));
                            toAdd.add(new VarInsnNode(Opcodes.ASTORE, 7));

                            updateFogColorMethod.instructions.insertBefore(updateFogColorMethod.instructions.get(count + 2), toAdd);
                        }
                    }
                }
            }
        }
    }
}