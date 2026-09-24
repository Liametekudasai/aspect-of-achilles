package com.liametekudasai.aspectofachilles.fabric.client;

import com.liametekudasai.aspectofachilles.AspectOfAchilles;
import com.liametekudasai.aspectofachilles.client.AspectOfAchillesClient;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.fabric.FabricLoadContext;
import net.fabricmc.api.ClientModInitializer;

public class AspectOfAchillesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BalmClient.initializeMod(AspectOfAchilles.MOD_ID, FabricLoadContext.INSTANCE, AspectOfAchillesClient::initialize);
    }
}
