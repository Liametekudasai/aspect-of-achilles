package com.liametekudasai.aspectofachilles.network;

import com.liametekudasai.aspectofachilles.util.ModResourceLocations;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record TetherActionPayload(int actionId) implements CustomPacketPayload {

    public static final Type<TetherActionPayload> TYPE = new Type<>(ModResourceLocations.id("tether_action"));
    public static final StreamCodec<RegistryFriendlyByteBuf, TetherActionPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            TetherActionPayload::actionId,
            TetherActionPayload::new
    );

    public static TetherActionPayload recall() {
        return new TetherActionPayload(TetherAction.RECALL.ordinal());
    }

    public static TetherActionPayload achillesPull() {
        return new TetherActionPayload(TetherAction.ACHILLES_PULL.ordinal());
    }

    public TetherAction action() {
        return TetherAction.byId(actionId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
