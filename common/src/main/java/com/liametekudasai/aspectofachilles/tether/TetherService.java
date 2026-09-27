package com.liametekudasai.aspectofachilles.tether;

import com.liametekudasai.aspectofachilles.AspectOfAchilles;
import com.liametekudasai.aspectofachilles.entity.TetherEntity;
import java.util.Locale;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/**
 * Central runtime service for the tether gameplay loop.
 */
public final class TetherService {

    private TetherService() {
    }

    public static void begin(ServerPlayer player, ThrownTrident trident, TetherMode mode, int lockedSlot, TetherEntity tetherEntity) {
        cleanup(player);

        TetherPlayerState state = TetherRuntime.getOrCreate(player.getUUID());
        state.setMode(mode);
        state.setTridentUuid(trident.getUUID());
        state.setLockedSlot(lockedSlot);
        state.setOriginDimension(player.level().dimension());
        state.setRecalling(false);
        state.setAchillesPullQueued(false);
        state.stopAchillesPull();
        state.setTetherEntityUuid(tetherEntity.getUUID());
        state.setReservedTrident(trident.getWeaponItem().copy());
        state.resetMissingTridentTicks();
        state.setLastKnownDistance(0.0D);
        state.setBoundaryLatched(false);
        enforceLockedSlot(player, state);
        syncInventory(player);
    }

    public static void queueRecall(ServerPlayer player) {
        TetherPlayerState state = TetherRuntime.get(player.getUUID());
        if (state != null && state.isActive()) {
            state.setRecalling(true);
            state.stopAchillesPull();
        }
    }

    public static void queueAchillesPull(ServerPlayer player) {
        TetherPlayerState state = TetherRuntime.get(player.getUUID());
        if (state != null && state.isActive() && state.getMode() == TetherMode.ACHILLES) {
            state.setAchillesPullQueued(true);
        }
    }

    /** Right click recalls an Obedience trident and activates an Achilles trident. */
    public static void queuePrimaryAction(ServerPlayer player) {
        TetherPlayerState state = TetherRuntime.get(player.getUUID());
        if (state == null || !state.isActive()) {
            return;
        }

        if (state.getMode() == TetherMode.ACHILLES) {
            queueAchillesPull(player);
        } else {
            queueRecall(player);
        }
    }

    public static void tick(ServerPlayer player) {
        TetherPlayerState state = TetherRuntime.get(player.getUUID());
        if (state == null || !state.isActive()) {
            return;
        }

        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        state.tickLifetime();
        enforceLockedSlot(player, state);

        Entity tridentEntity = serverLevel.getEntity(state.getTridentUuid());
        if (!(tridentEntity instanceof ThrownTrident thrownTrident) || !thrownTrident.isAlive()) {
            if (state.incrementMissingTridentTicks() >= TetherConstants.MISSING_TRIDENT_GRACE_TICKS
                    || state.getOriginDimension() != player.level().dimension()) {
                forceReturnToInventory(player, state, null);
            }
            return;
        }
        state.resetMissingTridentTicks();

        if (state.getOriginDimension() != player.level().dimension()) {
            forceReturnToInventory(player, state, thrownTrident);
            return;
        }

        Vec3 playerAnchor = player.position().add(0.0D, TetherConstants.ROPE_VERTICAL_OFFSET, 0.0D);
        Vec3 tridentAnchor = thrownTrident.position();
        double distance = playerAnchor.distanceTo(tridentAnchor);
        state.setLastKnownDistance(distance);

        boolean clampedToBoundary = false;
        if (distance > state.getMode().getMaxDistance()) {
            if (isEmbedded(thrownTrident)) {
                clampPlayerToRange(player, tridentAnchor, state.getMode().getMaxDistance());
            } else {
                stopTridentAtRange(playerAnchor, thrownTrident, state.getMode().getMaxDistance());
                clampPlayerToRange(player, thrownTrident.position(), state.getMode().getMaxDistance());
            }
            clampedToBoundary = true;
            state.setLastKnownDistance(state.getMode().getMaxDistance());
            if (state.isRecalling()) {
                state.setBoundaryLatched(false);
            } else {
                state.setBoundaryLatched(true);
            }
        } else if (distance < state.getMode().getMaxDistance() - 0.5D) {
            state.setBoundaryLatched(false);
        }

        if (state.isAchillesPullQueued()) {
            state.setAchillesPullQueued(false);
            state.setRecalling(false);
            AchillesPullController.pullOnce(player, thrownTrident);
        }

        if (state.isRecalling()) {
            if (clampedToBoundary) {
                // Refresh the anchor after the clamp so the recall vector begins from the real tether edge.
                playerAnchor = player.position().add(0.0D, TetherConstants.ROPE_VERTICAL_OFFSET, 0.0D);
            }
            stepRecall(player, thrownTrident);
            if (playerAnchor.distanceTo(thrownTrident.position()) < TetherConstants.RECALL_PICKUP_DISTANCE) {
                forceReturnToInventory(player, state, thrownTrident);
            }
        } else if (thrownTrident.isNoPhysics()) {
            // Once recall is no longer active, restore normal projectile collision.
            thrownTrident.setNoPhysics(false);
        }
    }

    public static void cleanup(ServerPlayer player) {
        TetherPlayerState state = TetherRuntime.get(player.getUUID());
        if (state == null || !state.isActive() || !(player.level() instanceof ServerLevel serverLevel)) {
            TetherRuntime.clear(player.getUUID());
            return;
        }

        if (state.getTetherEntityUuid() != null) {
            Entity rope = serverLevel.getEntity(state.getTetherEntityUuid());
            if (rope != null) {
                rope.discard();
            }
        }
        TetherRuntime.clear(player.getUUID());
    }

    public static void forceReturnToInventory(ServerPlayer player, TetherPlayerState state, ThrownTrident thrownTrident) {
        ItemStack returnedStack = ItemStack.EMPTY;
        if (thrownTrident != null) {
            returnedStack = thrownTrident.getWeaponItem().copy();
            thrownTrident.discard();
        } else if (!state.getReservedTrident().isEmpty()) {
            returnedStack = state.getReservedTrident().copy();
        }

        if (!returnedStack.isEmpty()) {
            giveBackToLockedSlot(player, state, returnedStack);
        }

        syncInventory(player);
        cleanup(player);
    }

    public static void forceReturnToInventory(ServerPlayer player) {
        TetherPlayerState state = TetherRuntime.get(player.getUUID());
        if (state != null && state.isActive()) {
            forceReturnToInventory(player, state, resolveTrackedTrident(player, state));
        }
    }

    public static boolean isLockedInventorySlot(Player player, int inventorySlot) {
        TetherPlayerState state = TetherRuntime.get(player.getUUID());
        return state != null && state.isActive() && inventorySlot == state.getLockedSlot();
    }

    public static boolean isLockedMenuSlot(Player player, Slot slot) {
        if (slot == null || slot.container != player.getInventory()) {
            return false;
        }
        return isLockedInventorySlot(player, slot.getContainerSlot());
    }


    private static ThrownTrident resolveTrackedTrident(ServerPlayer player, TetherPlayerState state) {
        UUID tridentUuid = state.getTridentUuid();
        if (tridentUuid == null) {
            return null;
        }

        for (ServerLevel level : player.serverLevel().getServer().getAllLevels()) {
            Entity entity = level.getEntity(tridentUuid);
            if (entity instanceof ThrownTrident thrownTrident && thrownTrident.isAlive()) {
                return thrownTrident;
            }
        }

        return null;
    }

    private static void stepRecall(ServerPlayer player, ThrownTrident trident) {
        Vec3 target = player.position().add(0.0D, TetherConstants.ROPE_VERTICAL_OFFSET, 0.0D);
        Vec3 direction = target.subtract(trident.position());
        if (direction.lengthSqr() < 1.0E-4D) {
            return;
        }

        // A grounded trident stays embedded until recall explicitly switches it into no-physics mode,
        // similar to vanilla Loyalty behavior.
        trident.setNoPhysics(true);

        Vec3 normalized = direction.normalize();
        Vec3 velocity = normalized.scale(TetherConstants.RECALL_SPEED);
        trident.setDeltaMovement(velocity);

        // Nudge the trident slightly toward the owner so the first recall tick can pull it free from blocks.
        Vec3 nudgedPosition = trident.position().add(normalized.scale(0.15D));
        trident.setPos(nudgedPosition.x, nudgedPosition.y, nudgedPosition.z);
        trident.hurtMarked = true;
    }

    private static boolean isEmbedded(ThrownTrident trident) {
        // A trident embedded in a block has no meaningful remaining velocity. This avoids
        // moving a lodged projectile just because its owner reaches the rope boundary.
        return trident.getDeltaMovement().lengthSqr() < 1.0E-6D;
    }

    private static void stopTridentAtRange(Vec3 playerAnchor, ThrownTrident trident, double maxDistance) {
        Vec3 fromPlayer = trident.position().subtract(playerAnchor);
        if (fromPlayer.lengthSqr() < 1.0E-6D) {
            return;
        }

        Vec3 boundaryDirection = fromPlayer.normalize();
        Vec3 clampedPosition = playerAnchor.add(boundaryDirection.scale(maxDistance));
        trident.setPos(clampedPosition.x, clampedPosition.y, clampedPosition.z);

        trident.setDeltaMovement(Vec3.ZERO);
        trident.hurtMarked = true;
    }

    private static void clampPlayerToRange(ServerPlayer player, Vec3 tridentAnchor, double maxDistance) {
        Vec3 playerAnchor = player.position().add(0.0D, TetherConstants.ROPE_VERTICAL_OFFSET, 0.0D);
        Vec3 fromTrident = playerAnchor.subtract(tridentAnchor);
        if (fromTrident.lengthSqr() < 1.0E-6D) {
            return;
        }

        Vec3 clampedAnchor = tridentAnchor.add(fromTrident.normalize().scale(maxDistance));
        Vec3 clampedPosition = clampedAnchor.subtract(0.0D, TetherConstants.ROPE_VERTICAL_OFFSET, 0.0D);
        player.teleportTo(clampedPosition.x, clampedPosition.y, clampedPosition.z);
        player.setDeltaMovement(Vec3.ZERO);
        player.resetFallDistance();
        player.hurtMarked = true;
    }

    private static void enforceLockedSlot(ServerPlayer player, TetherPlayerState state) {
        int slot = state.getLockedSlot();
        if (slot < 0 || slot >= player.getInventory().items.size()) {
            return;
        }

        ItemStack stackInSlot = player.getInventory().getItem(slot);
        if (stackInSlot.isEmpty()) {
            return;
        }

        if (!stackInSlot.is(state.getReservedTrident().getItem())) {
            ItemStack moved = stackInSlot.copy();
            player.getInventory().setItem(slot, ItemStack.EMPTY);
            moveIntruderElsewhere(player, slot, moved);
            return;
        }

        // Even if the same item type somehow lands back in the slot before recall, the slot must stay reserved.
        player.getInventory().setItem(slot, ItemStack.EMPTY);
        syncInventory(player);
    }

    private static void moveIntruderElsewhere(ServerPlayer player, int lockedSlot, ItemStack moved) {
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            if (i == lockedSlot) {
                continue;
            }

            ItemStack existing = player.getInventory().getItem(i);
            if (existing.isEmpty()) {
                player.getInventory().setItem(i, moved);
                syncInventory(player);
                return;
            }
        }

        if (!player.getInventory().add(moved)) {
            player.drop(moved, false);
        }
        syncInventory(player);
    }

    private static void giveBackToLockedSlot(ServerPlayer player, TetherPlayerState state, ItemStack returnedStack) {
        int slot = state.getLockedSlot();
        if (slot >= 0 && slot < player.getInventory().items.size()) {
            ItemStack existing = player.getInventory().getItem(slot);
            if (!existing.isEmpty()) {
                ItemStack moved = existing.copy();
                player.getInventory().setItem(slot, ItemStack.EMPTY);
                moveIntruderElsewhere(player, slot, moved);
            }

            if (player.getInventory().getItem(slot).isEmpty()) {
                player.getInventory().setItem(slot, returnedStack);
                syncInventory(player);
                return;
            }
        }

        if (!player.getInventory().add(returnedStack)) {
            player.drop(returnedStack, false);
        }
        syncInventory(player);
    }

    private static void syncInventory(ServerPlayer player) {
        player.getInventory().setChanged();
        player.inventoryMenu.broadcastChanges();
        player.containerMenu.broadcastChanges();
    }

    public static boolean isTracked(ServerPlayer player, UUID tridentUuid) {
        TetherPlayerState state = TetherRuntime.get(player.getUUID());
        return state != null && state.isActive() && tridentUuid.equals(state.getTridentUuid());
    }

    public static boolean hasActiveTether(ServerPlayer player) {
        TetherPlayerState state = TetherRuntime.get(player.getUUID());
        return state != null && state.isActive();
    }

    public static String describe(ServerPlayer player) {
        TetherPlayerState state = TetherRuntime.get(player.getUUID());
        if (state == null || !state.isActive()) {
            return "inactive";
        }

        return "mode=" + state.getMode()
                + ", slot=" + state.getLockedSlot()
                + ", recalling=" + state.isRecalling()
                + ", boundaryLatched=" + state.isBoundaryLatched()
                + ", distance=" + String.format(Locale.ROOT, "%.2f", state.getLastKnownDistance())
                + ", life=" + state.getTicksSinceStart();
    }

    public static void debugLogStart(ServerPlayer player, TetherMode mode, int slot) {
        AspectOfAchilles.LOGGER.debug("Started {} tether for {} on slot {}", mode, player.getScoreboardName(), slot);
    }
}
