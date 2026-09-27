package com.liametekudasai.aspectofachilles.tether;

import com.liametekudasai.aspectofachilles.config.AchillesConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

/**
 * Applies one Aspect of Achilles activation. It always chooses exactly one movement mode:
 * teleportation or a single physical impulse.
 */
public final class AchillesPullController {

    private AchillesPullController() {
    }

    public static void pullOnce(ServerPlayer player, Entity target) {
        Vec3 origin = player.position().add(0.0D, TetherConstants.ROPE_VERTICAL_OFFSET, 0.0D);
        Vec3 destination = target.position().add(0.0D, 0.1D, 0.0D);
        Vec3 direction = destination.subtract(origin);
        if (direction.lengthSqr() < 1.0E-4D) {
            return;
        }

        double distance = direction.length();
        if (distance <= TetherConstants.ACHILLES_PULL_FINISH_DISTANCE) {
            return;
        }

        AchillesConfig config = AchillesConfig.get();
        if (config.teleportToTrident) {
            Vec3 landing = target.position().add(0.0D, 0.1D, 0.0D);
            player.teleportTo(landing.x, landing.y, landing.z);
            player.setDeltaMovement(Vec3.ZERO);
            player.hurtMarked = true;
            return;
        }

        Vec3 motion = direction.normalize().scale(config.achillesPullImpulse);
        player.setDeltaMovement(motion);
        player.hurtMarked = true;
    }
}
