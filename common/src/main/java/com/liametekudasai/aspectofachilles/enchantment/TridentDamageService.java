package com.liametekudasai.aspectofachilles.enchantment;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;

/**
 * Centralized damage math for the compatibility enchantments.
 *
 * <p>Power still needs explicit help because vanilla's Power enchantment only applies its
 * damage component when the direct attacker is tagged as an arrow. Sharpness does not need
 * a custom runtime hook once tridents are added to the relevant vanilla enchantable item tags:
 * the built-in minecraft:damage enchantment effect already handles melee damage.</p>
 */
public final class TridentDamageService {

    private TridentDamageService() {
    }

    public static float applyPowerToThrownTrident(float baseDamage, ItemStack stack, RegistryAccess registryAccess) {
        int level = TridentEnchantmentHelper.getPowerLevel(stack, registryAccess);
        if (level <= 0) {
            return baseDamage;
        }

        float scaled = (float) (baseDamage * (1.0D + 0.25D * (level + 1)));
        return roundToNearestHalf(scaled);
    }

    private static float roundToNearestHalf(float value) {
        return Math.round(value * 2.0F) / 2.0F;
    }
}
