package com.liametekudasai.aspectofachilles.neoforge;

import com.liametekudasai.aspectofachilles.AspectOfAchilles;
import com.liametekudasai.aspectofachilles.neoforge.registry.NeoForgeModEntities;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(AspectOfAchilles.MOD_ID)
public class AspectOfAchillesNeoForge {

    public AspectOfAchillesNeoForge(IEventBus modEventBus) {
        NeoForgeModEntities.register(modEventBus);
        Balm.initializeMod(AspectOfAchilles.MOD_ID, new NeoForgeLoadContext(modEventBus), AspectOfAchilles::initialize);
    }
}
