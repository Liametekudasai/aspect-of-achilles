package com.liametekudasai.aspectofachilles.enchantment;

import com.liametekudasai.aspectofachilles.util.ModEnchantmentKeys;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public final class TridentEnchantmentHelper {

    private TridentEnchantmentHelper() {
    }

    public static boolean isTrident(ItemStack stack) {
        return stack.is(Items.TRIDENT);
    }

    public static int getObedienceLevel(ItemStack stack, RegistryAccess registryAccess) {
        return getLevel(stack, registryAccess, ModEnchantmentKeys.OBEDIENCE);
    }

    public static int getAspectLevel(ItemStack stack, RegistryAccess registryAccess) {
        return getLevel(stack, registryAccess, ModEnchantmentKeys.ASPECT_OF_ACHILLES);
    }

    public static int getRiptideLevel(ItemStack stack, RegistryAccess registryAccess) {
        return getVanillaLevel(stack, registryAccess, Enchantments.RIPTIDE);
    }

    public static int getImpalingLevel(ItemStack stack, RegistryAccess registryAccess) {
        return getVanillaLevel(stack, registryAccess, Enchantments.IMPALING);
    }

    public static boolean hasObedience(ItemStack stack, RegistryAccess registryAccess) {
        return getObedienceLevel(stack, registryAccess) > 0;
    }

    public static boolean hasAspect(ItemStack stack, RegistryAccess registryAccess) {
        return getAspectLevel(stack, registryAccess) > 0;
    }

    public static boolean hasRiptide(ItemStack stack, RegistryAccess registryAccess) {
        return getRiptideLevel(stack, registryAccess) > 0;
    }

    public static int getFireAspectLevel(ItemStack stack, RegistryAccess registryAccess) {
        return getVanillaLevel(stack, registryAccess, Enchantments.FIRE_ASPECT);
    }

    public static boolean hasFireAspect(ItemStack stack, RegistryAccess registryAccess) {
        return getFireAspectLevel(stack, registryAccess) > 0;
    }

    public static boolean stackWouldCreateAspect(ItemStack stack, RegistryAccess registryAccess) {
        if (!isTrident(stack)) {
            return false;
        }
        return hasObedience(stack, registryAccess) && hasRiptide(stack, registryAccess);
    }

    public static int getLevel(ItemStack stack, RegistryAccess registryAccess, ResourceKey<Enchantment> key) {
        Optional<Holder.Reference<Enchantment>> holder = resolve(registryAccess, key);
        return holder.map(reference -> stack.getEnchantments().getLevel(reference)).orElse(0);
    }

    public static int getVanillaLevel(ItemStack stack, RegistryAccess registryAccess, ResourceKey<Enchantment> key) {
        Optional<Holder.Reference<Enchantment>> holder = resolve(registryAccess, key);
        return holder.map(reference -> stack.getEnchantments().getLevel(reference)).orElse(0);
    }

    public static void replaceWithAspect(ItemStack stack, RegistryAccess registryAccess) {
        Holder<Enchantment> obedience = resolveOrThrow(registryAccess, ModEnchantmentKeys.OBEDIENCE);
        Holder<Enchantment> riptide = resolveOrThrow(registryAccess, Enchantments.RIPTIDE);
        Holder<Enchantment> aspect = resolveOrThrow(registryAccess, ModEnchantmentKeys.ASPECT_OF_ACHILLES);

        EnchantmentHelper.updateEnchantments(stack, mutable -> {
            mutable.set(obedience, 0);
            mutable.set(riptide, 0);
            mutable.set(aspect, 1);
        });
    }

    public static void addOrUpgradeFireAspect(ItemStack stack, RegistryAccess registryAccess, int level) {
        Holder<Enchantment> enchantment = resolveOrThrow(registryAccess, Enchantments.FIRE_ASPECT);
        int targetLevel = Math.max(level, stack.getEnchantments().getLevel(enchantment));
        EnchantmentHelper.updateEnchantments(stack, mutable -> mutable.set(enchantment, targetLevel));
    }

    public static Optional<Holder.Reference<Enchantment>> resolve(RegistryAccess registryAccess, ResourceKey<Enchantment> key) {
        return registryAccess.lookupOrThrow(Registries.ENCHANTMENT).get(key);
    }

    public static Holder<Enchantment> resolveOrThrow(RegistryAccess registryAccess, ResourceKey<Enchantment> key) {
        return registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
    }
}
