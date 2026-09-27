package com.liametekudasai.aspectofachilles.enchantment;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;

public final class AnvilFusionService {

    private static final int ACHILLES_FUSION_COST = 8;
    private static final int FIRE_ASPECT_ENCHANT_COST = 4;

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

    public static boolean canApplyFireAspect(ItemStack left, ItemStack right, RegistryAccess registryAccess) {
        if (!TridentEnchantmentHelper.isTrident(left) || right.isEmpty()) {
            return false;
        }

        return TridentEnchantmentHelper.hasFireAspect(right, registryAccess);
    }

    public static ItemStack createFireAspectResult(ItemStack left, ItemStack right, RegistryAccess registryAccess) {
        ItemStack result = left.copy();

        int fireAspect = TridentEnchantmentHelper.getFireAspectLevel(right, registryAccess);
        if (fireAspect > 0) {
            TridentEnchantmentHelper.addOrUpgradeFireAspect(result, registryAccess, fireAspect);
        }

        return result;
    }

    public static int getAchillesFusionCost() {
        return ACHILLES_FUSION_COST;
    }

    public static int getFireAspectEnchantCost() {
        return FIRE_ASPECT_ENCHANT_COST;
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
