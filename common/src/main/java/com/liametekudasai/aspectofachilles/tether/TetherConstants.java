package com.liametekudasai.aspectofachilles.tether;

/**
 * Tunable runtime constants for the current prototype.
 *
 * <p>These are kept in code for now so the NeoForge build can be stabilized first.
 * Once the project compiles cleanly in a real workspace, these should move into a Balm
 * configuration class so servers can change ranges and speeds without a rebuild.</p>
 */
public final class TetherConstants {

    public static final double OBEDIENCE_MAX_DISTANCE = 20.0D;
    public static final double ACHILLES_MAX_DISTANCE = 40.0D;

    public static final double RECALL_SPEED = 1.20D;
    public static final double RECALL_PICKUP_DISTANCE = 1.80D;
    public static final double OUT_OF_RANGE_RECALL_DAMPING = 0.82D;

    public static final double ACHILLES_PULL_SPEED = 1.35D;
    public static final double ACHILLES_PULL_FINISH_DISTANCE = 1.40D;
    public static final int ACHILLES_PULL_MAX_TICKS = 14;

    public static final double ROPE_VERTICAL_OFFSET = 1.35D;
    public static final int MISSING_TRIDENT_GRACE_TICKS = 10;

    private TetherConstants() {
    }
}
