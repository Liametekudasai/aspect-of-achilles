package com.liametekudasai.aspectofachilles.util;

import com.liametekudasai.aspectofachilles.AspectOfAchilles;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public final class ModEnchantmentKeys {

    public static final ResourceKey<Enchantment> OBEDIENCE = key("obedience");
    public static final ResourceKey<Enchantment> ASPECT_OF_ACHILLES = key("aspect_of_achilles");

    private ModEnchantmentKeys() {
    }

    private static ResourceKey<Enchantment> key(String path) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(AspectOfAchilles.MOD_ID, path));
    }
}
