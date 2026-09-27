package com.liametekudasai.aspectofachilles.tether;

/**
 * Tunable runtime constants for the current prototype.
 *
 * <p>Values that server owners should be able to tune live in {@code AchillesConfig}.
 * These constants are only fixed mechanics values.</p>
 */
public final class TetherConstants {

    public static final double RECALL_SPEED = 1.20D;
    public static final double RECALL_PICKUP_DISTANCE = 1.80D;
    public static final double OUT_OF_RANGE_RECALL_DAMPING = 0.82D;

    public static final double ACHILLES_PULL_FINISH_DISTANCE = 1.40D;

    public static final double ROPE_VERTICAL_OFFSET = 1.35D;
    public static final int MISSING_TRIDENT_GRACE_TICKS = 10;

    private TetherConstants() {
    }
}
