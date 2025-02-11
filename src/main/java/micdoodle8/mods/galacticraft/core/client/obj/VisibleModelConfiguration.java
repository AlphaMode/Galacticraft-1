package micdoodle8.mods.galacticraft.core.client.obj;

import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometryPart;

import javax.annotation.Nonnull;
import java.util.List;

public class VisibleModelConfiguration extends WrapperModelConfiguration {

    private final List<String> visibleGroups;

    public VisibleModelConfiguration(IModelConfiguration internal, List<String> visibleGroups) {
        super(internal);
        this.visibleGroups = visibleGroups;
    }

    @Override
    public boolean getPartVisibility(@Nonnull IModelGeometryPart part, boolean fallback) {
        //Ignore fallback as we always have a true or false answer
        return getPartVisibility(part);
    }

    @Override
    public boolean getPartVisibility(@Nonnull IModelGeometryPart part) {
        if (visibleGroups.isEmpty()) return true;
        return visibleGroups.contains(part.name());
    }
}