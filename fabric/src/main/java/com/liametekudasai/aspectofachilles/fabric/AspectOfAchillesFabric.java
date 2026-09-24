package com.liametekudasai.aspectofachilles.fabric;

import com.liametekudasai.aspectofachilles.AspectOfAchilles;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.fabric.FabricLoadContext;
import net.fabricmc.api.ModInitializer;

public class AspectOfAchillesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Balm.initializeMod(AspectOfAchilles.MOD_ID, FabricLoadContext.INSTANCE, AspectOfAchilles::initialize);
    }
}
