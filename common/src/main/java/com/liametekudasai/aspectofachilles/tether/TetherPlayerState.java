package com.liametekudasai.aspectofachilles.tether;

import java.util.UUID;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TetherPlayerState {

    private final UUID playerUuid;
    private UUID tridentUuid;
    private UUID tetherEntityUuid;
    private TetherMode mode;
    private int lockedSlot = -1;
    private boolean recalling;
    private boolean boundaryLatched;
    private boolean achillesPullQueued;
    private int achillesPullTicksRemaining;
    private ResourceKey<Level> originDimension;
    private ItemStack reservedTrident = ItemStack.EMPTY;
    private int missingTridentTicks;
    private int ticksSinceStart;
    private double lastKnownDistance;

    public TetherPlayerState(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public UUID getTridentUuid() {
        return tridentUuid;
    }

    public void setTridentUuid(UUID tridentUuid) {
        this.tridentUuid = tridentUuid;
    }

    public UUID getTetherEntityUuid() {
        return tetherEntityUuid;
    }

    public void setTetherEntityUuid(UUID tetherEntityUuid) {
        this.tetherEntityUuid = tetherEntityUuid;
    }

    public TetherMode getMode() {
        return mode;
    }

    public void setMode(TetherMode mode) {
        this.mode = mode;
    }

    public int getLockedSlot() {
        return lockedSlot;
    }

    public void setLockedSlot(int lockedSlot) {
        this.lockedSlot = lockedSlot;
    }

    public boolean isRecalling() {
        return recalling;
    }

    public void setRecalling(boolean recalling) {
        this.recalling = recalling;
    }

    public boolean isBoundaryLatched() {
        return boundaryLatched;
    }

    public void setBoundaryLatched(boolean boundaryLatched) {
        this.boundaryLatched = boundaryLatched;
    }

    public boolean isAchillesPullQueued() {
        return achillesPullQueued;
    }

    public void setAchillesPullQueued(boolean achillesPullQueued) {
        this.achillesPullQueued = achillesPullQueued;
    }

    public int getAchillesPullTicksRemaining() {
        return achillesPullTicksRemaining;
    }

    public void beginAchillesPull() {
        this.achillesPullTicksRemaining = TetherConstants.ACHILLES_PULL_MAX_TICKS;
    }

    public boolean isAchillesPullActive() {
        return achillesPullTicksRemaining > 0;
    }

    public int tickAchillesPull() {
        if (achillesPullTicksRemaining > 0) {
            achillesPullTicksRemaining--;
        }
        return achillesPullTicksRemaining;
    }

    public void stopAchillesPull() {
        achillesPullTicksRemaining = 0;
        achillesPullQueued = false;
    }

    public ResourceKey<Level> getOriginDimension() {
        return originDimension;
    }

    public void setOriginDimension(ResourceKey<Level> originDimension) {
        this.originDimension = originDimension;
    }

    public ItemStack getReservedTrident() {
        return reservedTrident;
    }

    public void setReservedTrident(ItemStack reservedTrident) {
        this.reservedTrident = reservedTrident.copy();
    }

    public int getMissingTridentTicks() {
        return missingTridentTicks;
    }

    public void resetMissingTridentTicks() {
        this.missingTridentTicks = 0;
    }

    public int incrementMissingTridentTicks() {
        return ++missingTridentTicks;
    }

    public int getTicksSinceStart() {
        return ticksSinceStart;
    }

    public int tickLifetime() {
        return ++ticksSinceStart;
    }

    public double getLastKnownDistance() {
        return lastKnownDistance;
    }

    public void setLastKnownDistance(double lastKnownDistance) {
        this.lastKnownDistance = lastKnownDistance;
    }

    public boolean isActive() {
        return tridentUuid != null && mode != null;
    }

    public void clear() {
        tridentUuid = null;
        tetherEntityUuid = null;
        mode = null;
        lockedSlot = -1;
        recalling = false;
        boundaryLatched = false;
        achillesPullQueued = false;
        achillesPullTicksRemaining = 0;
        originDimension = null;
        reservedTrident = ItemStack.EMPTY;
        missingTridentTicks = 0;
        ticksSinceStart = 0;
        lastKnownDistance = 0.0D;
    }
}
