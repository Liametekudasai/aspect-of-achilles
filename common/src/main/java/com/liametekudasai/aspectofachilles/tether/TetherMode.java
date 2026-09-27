package com.liametekudasai.aspectofachilles.tether;

import com.liametekudasai.aspectofachilles.config.AchillesConfig;

public enum TetherMode {
    OBEDIENCE,
    ACHILLES;

    public double getMaxDistance() {
        AchillesConfig config = AchillesConfig.get();
        return this == ACHILLES ? config.achillesMaxDistance : config.obedienceMaxDistance;
    }
}
