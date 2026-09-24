package com.liametekudasai.aspectofachilles.tether;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TetherRuntime {

    private static final Map<UUID, TetherPlayerState> STATES = new ConcurrentHashMap<>();

    private TetherRuntime() {
    }

    public static void initialize() {
        STATES.clear();
    }

    public static TetherPlayerState getOrCreate(UUID playerUuid) {
        return STATES.computeIfAbsent(playerUuid, TetherPlayerState::new);
    }

    public static TetherPlayerState get(UUID playerUuid) {
        return STATES.get(playerUuid);
    }

    public static void clear(UUID playerUuid) {
        TetherPlayerState state = STATES.remove(playerUuid);
        if (state != null) {
            state.clear();
        }
    }
}
