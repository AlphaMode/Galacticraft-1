package micdoodle8.mods.galacticraft.core.client.jei.circuitfabricator;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CircuitFabricatorRecipeWrapper
{
    @Nonnull
    private final List<Object> input;
    @Nonnull
    public final ItemStack output;

    public CircuitFabricatorRecipeWrapper(@Nonnull List<Object> objects, @Nonnull ItemStack output)
    {
        this.input = objects;
        this.output = output;
    }

    @Nonnull
    public List<ItemStack> getInput() {
        List<ItemStack> displayInput = new ArrayList<>();
        for (Object input : this.input) {
            if (input instanceof List<?>) {
                for (ItemStack stack : (List<ItemStack>) input) {
                    displayInput.add(stack);
                }
            } else {
                displayInput.add((ItemStack) input);
            }
        }
        return displayInput;
    }

    public boolean equals(Object o)
    {
        if (o instanceof CircuitFabricatorRecipeWrapper)
        {
            CircuitFabricatorRecipeWrapper match = (CircuitFabricatorRecipeWrapper)o;
            if (!ItemStack.areItemStacksEqual(match.output, this.output))
                return false;
            for (int i = 0; i < this.input.size(); i++)
            {
                Object a = this.input.get(i);
                Object b = match.input.get(i);
                if (a == null && b == null)
                    continue;

                if (a instanceof ItemStack)
                {
                    if (!(b instanceof ItemStack))
                        return false;
                    if (!ItemStack.areItemStacksEqual((ItemStack) a, (ItemStack) b))
                        return false;
                }
                else if (a instanceof List<?>)
                {
                    if (!(b instanceof List<?>))
                        return false;
                    List aa = ((List)a);
                    List bb = ((List)b);
                    if (aa.size() != bb.size())
                        return false;
                    for (int j = 0; j < aa.size(); j++)
                    {
                        ItemStack c = (ItemStack) aa.get(j);
                        ItemStack d = (ItemStack) bb.get(j);
                        if (!ItemStack.areItemStacksEqual((ItemStack) c, (ItemStack) d))
                            return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
