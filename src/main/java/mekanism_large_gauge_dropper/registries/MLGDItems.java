package mekanism_large_gauge_dropper.registries;

import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism_large_gauge_dropper.MLGaugeDropper;
import mekanism_large_gauge_dropper.item.LargeGaugeDropperItem;

public class MLGDItems {

    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(MLGaugeDropper.MODID);

    public static final ItemRegistryObject<LargeGaugeDropperItem> LARGE_GAUGE_DROPPER = ITEMS
            .register("large_gauge_dropper", LargeGaugeDropperItem::new);
}
