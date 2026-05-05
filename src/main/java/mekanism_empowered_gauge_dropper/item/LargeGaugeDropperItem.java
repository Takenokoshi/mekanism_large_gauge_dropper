package mekanism_empowered_gauge_dropper.item;

import java.util.List;

import mekanism.common.capabilities.ItemCapabilityWrapper.ItemCapability;
import mekanism.common.item.ItemGaugeDropper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class LargeGaugeDropperItem extends ItemGaugeDropper {

    public LargeGaugeDropperItem(Properties properties) {
        super(properties);
    }

    public boolean isFoil(ItemStack p_41453_) {
        return true;
    }

    @Override
    protected void gatherCapabilities(List<ItemCapability> capabilities, ItemStack stack, CompoundTag nbt) {
        capabilities.add(LargeGaugeDropperContentsHandler.create());
    }
}
