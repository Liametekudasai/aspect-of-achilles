package com.liametekudasai.aspectofachilles.network;

public enum TetherAction {
    RECALL,
    ACHILLES_PULL;

    public static TetherAction byId(int id) {
        TetherAction[] values = values();
        if (id < 0 || id >= values.length) {
            return RECALL;
        }
        return values[id];
    }
}
