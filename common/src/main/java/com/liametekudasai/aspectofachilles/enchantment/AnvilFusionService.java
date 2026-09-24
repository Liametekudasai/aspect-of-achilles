package com.liametekudasai.aspectofachilles.enchantment;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;

public final class AnvilFusionService {

    private static final int ACHILLES_FUSION_COST = 8;
    private static final int COMPATIBILITY_ENCHANT_COST = 4;

    private AnvilFusionService() {
    }

    public static boolean canCreateAspect(ItemStack left, ItemStack right, RegistryAccess registryAccess) {
        ItemStack base = getFusionBase(left, right);
        if (base.isEmpty()) {
            return false;
        }

        boolean obedience = TridentEnchantmentHelper.hasObedience(left, registryAccess)
                || TridentEnchantmentHelper.hasObedience(right, registryAccess);
        boolean riptide = TridentEnchantmentHelper.hasRiptide(left, registryAccess)
                || TridentEnchantmentHelper.hasRiptide(right, registryAccess);
        boolean alreadyAspect = TridentEnchantmentHelper.hasAspect(left, registryAccess)
                || TridentEnchantmentHelper.hasAspect(right, registryAccess);

        return obedience && riptide && !alreadyAspect;
    }

    public static ItemStack createAspectResult(ItemStack left, ItemStack right, RegistryAccess registryAccess) {
        ItemStack base = getFusionBase(left, right).copy();
        TridentEnchantmentHelper.replaceWithAspect(base, registryAccess);
        return base;
    }

    public static boolean canApplyCompatibilityEnchant(ItemStack left, ItemStack right, RegistryAccess registryAccess) {
        if (!TridentEnchantmentHelper.isTrident(left) || right.isEmpty()) {
            return false;
        }

        return TridentEnchantmentHelper.hasSharpness(right, registryAccess)
                || TridentEnchantmentHelper.hasPower(right, registryAccess);
    }

    public static ItemStack createCompatibilityResult(ItemStack left, ItemStack right, RegistryAccess registryAccess) {
        ItemStack result = left.copy();

        int sharpness = TridentEnchantmentHelper.getSharpnessLevel(right, registryAccess);
        int power = TridentEnchantmentHelper.getPowerLevel(right, registryAccess);
        if (sharpness > 0) {
            TridentEnchantmentHelper.addOrUpgradeSharpness(result, registryAccess, sharpness);
        }
        if (power > 0) {
            TridentEnchantmentHelper.addOrUpgradePower(result, registryAccess, power);
        }

        return result;
    }

    public static int getAchillesFusionCost() {
        return ACHILLES_FUSION_COST;
    }

    public static int getCompatibilityEnchantCost() {
        return COMPATIBILITY_ENCHANT_COST;
    }

    private static ItemStack getFusionBase(ItemStack left, ItemStack right) {
        if (TridentEnchantmentHelper.isTrident(left)) {
            return left;
        }
        if (TridentEnchantmentHelper.isTrident(right)) {
            return right;
        }
        return ItemStack.EMPTY;
    }
}
