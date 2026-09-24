package com.liametekudasai.aspectofachilles.tether;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

/**
 * Applies the straight-line player launch used by Aspect of Achilles.
 *
 * <p>The effect is refreshed for a short burst of ticks instead of a single frame so the motion
 * feels deliberate and remains close to the specification of a constant-speed pull.</p>
 */
public final class AchillesPullController {

    private AchillesPullController() {
    }

    public static boolean tickPull(ServerPlayer player, Entity target) {
        Vec3 origin = player.position().add(0.0D, TetherConstants.ROPE_VERTICAL_OFFSET, 0.0D);
        Vec3 destination = target.position().add(0.0D, 0.1D, 0.0D);
        Vec3 direction = destination.subtract(origin);
        if (direction.lengthSqr() < 1.0E-4D) {
            return false;
        }

        double distance = direction.length();
        if (distance <= TetherConstants.ACHILLES_PULL_FINISH_DISTANCE) {
            return false;
        }

        Vec3 motion = direction.normalize().scale(TetherConstants.ACHILLES_PULL_SPEED);
        player.setDeltaMovement(motion);
        player.hurtMarked = true;
        return true;
    }
}
