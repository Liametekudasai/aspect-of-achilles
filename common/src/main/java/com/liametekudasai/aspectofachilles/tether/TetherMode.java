package com.liametekudasai.aspectofachilles.tether;

public enum TetherMode {
    OBEDIENCE,
    ACHILLES;

    public double getMaxDistance() {
        return this == ACHILLES ? TetherConstants.ACHILLES_MAX_DISTANCE : TetherConstants.OBEDIENCE_MAX_DISTANCE;
    }
}
