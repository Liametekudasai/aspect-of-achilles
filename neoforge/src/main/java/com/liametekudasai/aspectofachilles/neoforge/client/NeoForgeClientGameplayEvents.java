package com.liametekudasai.aspectofachilles.neoforge.client;

import com.liametekudasai.aspectofachilles.AspectOfAchilles;
import com.liametekudasai.aspectofachilles.network.TetherActionPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = AspectOfAchilles.MOD_ID, value = Dist.CLIENT)
public final class NeoForgeClientGameplayEvents {

    private NeoForgeClientGameplayEvents() {
    }

    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        PacketDistributor.sendToServer(TetherActionPayload.recall());
    }

}
