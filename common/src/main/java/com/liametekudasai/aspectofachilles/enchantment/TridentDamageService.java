package com.liametekudasai.aspectofachilles.enchantment;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;

/**
 * Centralized damage math for trident enchantments that need behavior beyond vanilla's
 * item restrictions.
 */
public final class TridentDamageService {

    private TridentDamageService() {
    }

    public static float applyUniversalImpaling(float baseDamage, ItemStack stack, RegistryAccess registryAccess) {
        int level = TridentEnchantmentHelper.getImpalingLevel(stack, registryAccess);
        if (level <= 0) {
            return baseDamage;
        }

        // This is the vanilla Impaling bonus (2.5 damage per level), now granted to every
        // target instead of only targets for which vanilla's wet-target condition succeeds.
        return baseDamage + 2.5F * level;
    }
}
