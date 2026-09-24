package com.liametekudasai.aspectofachilles.neoforge.network;

import com.liametekudasai.aspectofachilles.AspectOfAchilles;
import com.liametekudasai.aspectofachilles.network.TetherAction;
import com.liametekudasai.aspectofachilles.network.TetherActionPayload;
import com.liametekudasai.aspectofachilles.tether.TetherService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber(modid = AspectOfAchilles.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class AchillesNetwork {

    private AchillesNetwork() {
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playToServer(TetherActionPayload.TYPE, TetherActionPayload.STREAM_CODEC, AchillesNetwork::handleTetherAction);
    }

    private static void handleTetherAction(TetherActionPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (!(player instanceof ServerPlayer serverPlayer)) {
                return;
            }

            if (!TetherService.hasActiveTether(serverPlayer)) {
                return;
            }

            if (payload.action() == TetherAction.ACHILLES_PULL) {
                TetherService.queueAchillesPull(serverPlayer);
            } else {
                TetherService.queueRecall(serverPlayer);
            }
        });
    }
}
