package micdoodle8.mods.galacticraft.core.data;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.TickTrigger;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Consumer;

public class GCAdvancements implements Consumer<Consumer<Advancement>> {
    @Override
    public void accept(Consumer<Advancement> advancementConsumer) {
        Advancement.Builder.builder()
                .withDisplay(
                        GCItems.rocketTierOne,
                        new TranslationTextComponent("advancement.galacticraft.root.title").applyTextStyles(TextFormatting.BOLD, TextFormatting.GRAY),
                        new TranslationTextComponent("advancement.galacticraft.root.description"),
                        GalacticraftCore.rl("textures/gui/galacticraft_background.png"),
                        FrameType.TASK,
                        false, false, false
                ).withCriterion("tick", new TickTrigger.Instance()).register(advancementConsumer, Constants.MOD_ID_CORE + ":root");
    }
}
