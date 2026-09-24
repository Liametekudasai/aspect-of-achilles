package com.liametekudasai.aspectofachilles.neoforge.client;

import com.liametekudasai.aspectofachilles.AspectOfAchilles;
import com.liametekudasai.aspectofachilles.client.AspectOfAchillesClient;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = AspectOfAchilles.MOD_ID, dist = Dist.CLIENT)
public class AspectOfAchillesNeoForgeClient {

    public AspectOfAchillesNeoForgeClient(IEventBus modEventBus) {
        BalmClient.initializeMod(AspectOfAchilles.MOD_ID, new NeoForgeLoadContext(modEventBus), AspectOfAchillesClient::initialize);
    }
}
