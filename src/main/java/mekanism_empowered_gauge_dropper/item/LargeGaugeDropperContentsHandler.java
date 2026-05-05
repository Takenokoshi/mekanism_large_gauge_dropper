package mekanism_empowered_gauge_dropper.item;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mekanism.api.NBTConstants;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.fluid.IMekanismFluidHandler;
import mekanism.common.capabilities.DynamicHandler.InteractPredicate;
import mekanism.common.capabilities.chemical.dynamic.DynamicChemicalHandler.DynamicGasHandler;
import mekanism.common.capabilities.chemical.dynamic.DynamicChemicalHandler.DynamicInfusionHandler;
import mekanism.common.capabilities.chemical.dynamic.DynamicChemicalHandler.DynamicPigmentHandler;
import mekanism.common.capabilities.chemical.dynamic.DynamicChemicalHandler.DynamicSlurryHandler;
import mekanism.common.capabilities.chemical.variable.RateLimitChemicalTank.RateLimitGasTank;
import mekanism.common.capabilities.chemical.variable.RateLimitChemicalTank.RateLimitInfusionTank;
import mekanism.common.capabilities.chemical.variable.RateLimitChemicalTank.RateLimitPigmentTank;
import mekanism.common.capabilities.chemical.variable.RateLimitChemicalTank.RateLimitSlurryTank;
import mekanism.common.capabilities.fluid.item.RateLimitFluidHandler.RateLimitFluidTank;
import mekanism.common.capabilities.merged.MergedTank;
import mekanism.common.capabilities.merged.MergedTankContentsHandler;
import mekanism.common.capabilities.resolver.BasicCapabilityResolver;
import mekanism.common.capabilities.resolver.ICapabilityResolver;
import mekanism.common.util.ItemDataUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class LargeGaugeDropperContentsHandler extends MergedTankContentsHandler<MergedTank>
        implements IMekanismFluidHandler, IFluidHandlerItem {

    private static final IntSupplier INT_MAX_SUP = () -> 0x7fffffff;
    private static final LongSupplier LONG_MAX_SUP = () -> 0x7fffffffffffffffL;

    public static LargeGaugeDropperContentsHandler create() {
        return new LargeGaugeDropperContentsHandler();
    }

    protected final List<IExtendedFluidTank> fluidTanks;

    private LargeGaugeDropperContentsHandler() {
        mergedTank = MergedTank.create(
                new RateLimitFluidTank(INT_MAX_SUP, INT_MAX_SUP, this),
                new RateLimitGasTank(LONG_MAX_SUP, LONG_MAX_SUP,
                        ChemicalTankBuilder.GAS.alwaysTrueBi,
                        ChemicalTankBuilder.GAS.alwaysTrueBi,
                        ChemicalTankBuilder.GAS.alwaysTrue, ChemicalAttributeValidator.ALWAYS_ALLOW,
                        gasHandler = new DynamicGasHandler(side -> gasTanks, InteractPredicate.ALWAYS_TRUE,
                                InteractPredicate.ALWAYS_TRUE,
                                () -> onContentsChanged(NBTConstants.GAS_TANKS, gasTanks))),
                new RateLimitInfusionTank(LONG_MAX_SUP, LONG_MAX_SUP,
                        ChemicalTankBuilder.INFUSION.alwaysTrueBi,
                        ChemicalTankBuilder.INFUSION.alwaysTrueBi,
                        ChemicalTankBuilder.INFUSION.alwaysTrue,
                        infusionHandler = new DynamicInfusionHandler(side -> infusionTanks,
                                InteractPredicate.ALWAYS_TRUE,
                                InteractPredicate.ALWAYS_TRUE,
                                () -> onContentsChanged(NBTConstants.INFUSION_TANKS, infusionTanks))),
                new RateLimitPigmentTank(LONG_MAX_SUP, LONG_MAX_SUP,
                        ChemicalTankBuilder.PIGMENT.alwaysTrueBi,
                        ChemicalTankBuilder.PIGMENT.alwaysTrueBi,
                        ChemicalTankBuilder.PIGMENT.alwaysTrue,
                        pigmentHandler = new DynamicPigmentHandler(side -> pigmentTanks, InteractPredicate.ALWAYS_TRUE,
                                InteractPredicate.ALWAYS_TRUE,
                                () -> onContentsChanged(NBTConstants.PIGMENT_TANKS, pigmentTanks))),
                new RateLimitSlurryTank(LONG_MAX_SUP, LONG_MAX_SUP,
                        ChemicalTankBuilder.SLURRY.alwaysTrueBi,
                        ChemicalTankBuilder.SLURRY.alwaysTrueBi,
                        ChemicalTankBuilder.SLURRY.alwaysTrue,
                        slurryHandler = new DynamicSlurryHandler(side -> slurryTanks, InteractPredicate.ALWAYS_TRUE,
                                InteractPredicate.ALWAYS_TRUE,
                                () -> onContentsChanged(NBTConstants.SLURRY_TANKS, slurryTanks))));
        this.fluidTanks = Collections.singletonList(mergedTank.getFluidTank());
    }

    @Override
    protected void load() {
        super.load();
        ItemDataUtils.readContainers(getStack(), NBTConstants.FLUID_TANKS, getFluidTanks(null));
    }

    @NotNull
    @Override
    public List<IExtendedFluidTank> getFluidTanks(@Nullable Direction side) {
        return fluidTanks;
    }

    @Override
    public void onContentsChanged() {
        onContentsChanged(NBTConstants.FLUID_TANKS, fluidTanks);
    }

    @NotNull
    @Override
    public ItemStack getContainer() {
        return getStack();
    }

    @Override
    protected void gatherCapabilityResolvers(Consumer<ICapabilityResolver> consumer) {
        super.gatherCapabilityResolvers(consumer);
        consumer.accept(BasicCapabilityResolver.constant(ForgeCapabilities.FLUID_HANDLER_ITEM, this));
    }
}