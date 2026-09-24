package com.liametekudasai.aspectofachilles.util;

import com.liametekudasai.aspectofachilles.AspectOfAchilles;
import net.minecraft.resources.ResourceLocation;

public final class ModResourceLocations {

    private ModResourceLocations() {
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(AspectOfAchilles.MOD_ID, path);
    }
}
