package micdoodle8.mods.galacticraft;

import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import net.minecraftforge.coremod.api.ASMAPI;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Glue implements ILaunchPluginService {
    @Override
    public String name() {
        return "GalacticraftGlue";
    }

    private static final EnumSet<Phase> YAY = EnumSet.of(Phase.AFTER);
    private static final EnumSet<Phase> NAY = EnumSet.noneOf(Phase.class);

    private final MicdoodleTransformer transformer = new MicdoodleTransformer();

    public EnumSet<ILaunchPluginService.Phase> handlesClass(Type classType, boolean isEmpty) {
        return isEmpty ? NAY : YAY;
    }

    public static void write(String name, byte[] bittys, String outputFolder) {
        Path path = Paths.get(outputFolder + "/" + name.replace('.', '/') + ".class");
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, bittys);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> classes = new ArrayList<>();

    static {
        classes.add("net/minecraft/client/network/play/ClientPlayNetHandler");
        classes.add("net/minecraft/client/renderer/FogRenderer");
        classes.add("net/minecraft/client/world/ClientWorld");
    }

    @Override
    public boolean processClass(Phase phase, ClassNode classNode, Type type) {
        boolean result = transformer.transform(classNode.name, classNode.name, classNode);
        if (result)
            classes.add(classNode.name);
        return result;
    }

    public static boolean shouldOutputClass(String name) {
        return classes.contains(name.replace('.', '/'));
    }

    public static void transformSkyColor(MethodNode node) {
        LabelNode labelNode = new LabelNode();
        node.instructions.insert(ASMAPI.listOf(
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.FLOAD, 2),
                new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/TransformerHooks", "getSkyColorHook",
                        "(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/math/BlockPos;F)Lnet/minecraft/util/math/Vec3d", false),
                new JumpInsnNode(Opcodes.IFNULL, labelNode),
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.FLOAD, 2),
                new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/TransformerHooks", "getSkyColorHook",
                        "(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/math/BlockPos;F)Lnet/minecraft/util/math/Vec3d;", false),
                new InsnNode(Opcodes.ARETURN),
                labelNode
        ));
    }

    public static void tranformWeatherRenderer(MethodNode node) {
        LabelNode labelNode = new LabelNode();
        ASMAPI.listOf(
                new JumpInsnNode(Opcodes.IFEQ, labelNode),
                new VarInsnNode(Opcodes.ALOAD, 9),
                new TypeInsnNode(Opcodes.INSTANCEOF, "micdoodle8/mods/galacticraft/api/client/GCWeatherRenderHandler"),
                new VarInsnNode(Opcodes.ALOAD, 9),
                new VarInsnNode(Opcodes.ALOAD, 0),
                new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/WorldRenderer", ASMAPI.mapField("field_72773_u"), "I"),
                new VarInsnNode(Opcodes.FLOAD, 2),
                new VarInsnNode(Opcodes.ALOAD, 0),
                new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/WorldRenderer", ASMAPI.mapField("field_72777_q"), "Lnet/minecraft/client/Minecraft;"),
                new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", ASMAPI.mapField("field_71441_e"), "Lnet/minecraft/client/world/ClientWorld;"),
                new VarInsnNode(Opcodes.ALOAD, 0),
                new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/WorldRenderer", ASMAPI.mapField("field_72777_q"), "Lnet/minecraft/client/Minecraft;"),
                new MethodInsnNode(Opcodes.INVOKEINTERFACE, "micdoodle8/mods/galacticraft/api/client/GCWeatherRenderHandler", "render", "(IFLnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/Minecraft;)V", true)
        );
    }
}
