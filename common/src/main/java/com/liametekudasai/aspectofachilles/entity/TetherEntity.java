package com.liametekudasai.aspectofachilles.entity;

import com.liametekudasai.aspectofachilles.tether.TetherMode;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Lightweight entity used only to sync owner/trident references and give the client
 * something concrete to render as a rope tether.
 */
public class TetherEntity extends Entity {

    private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(TetherEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TRIDENT_ID = SynchedEntityData.defineId(TetherEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MODE = SynchedEntityData.defineId(TetherEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> MAX_DISTANCE = SynchedEntityData.defineId(TetherEntity.class, EntityDataSerializers.FLOAT);

    public TetherEntity(EntityType<? extends TetherEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.noCulling = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(OWNER_ID, -1);
        builder.define(TRIDENT_ID, -1);
        builder.define(MODE, 0);
        builder.define(MAX_DISTANCE, 20.0F);
    }

    public void configure(Entity owner, Entity trident, TetherMode mode) {
        entityData.set(OWNER_ID, owner.getId());
        entityData.set(TRIDENT_ID, trident.getId());
        entityData.set(MODE, mode.ordinal());
        entityData.set(MAX_DISTANCE, (float) mode.getMaxDistance());
    }

    public int getOwnerId() {
        return entityData.get(OWNER_ID);
    }

    public int getTridentId() {
        return entityData.get(TRIDENT_ID);
    }

    public TetherMode getMode() {
        int index = Math.max(0, Math.min(TetherMode.values().length - 1, entityData.get(MODE)));
        return TetherMode.values()[index];
    }

    public float getMaxDistance() {
        return entityData.get(MAX_DISTANCE);
    }

    public Entity resolveOwner() {
        return getOwnerId() >= 0 ? level().getEntity(getOwnerId()) : null;
    }

    public Entity resolveTrident() {
        return getTridentId() >= 0 ? level().getEntity(getTridentId()) : null;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        entityData.set(OWNER_ID, tag.getInt("owner_id"));
        entityData.set(TRIDENT_ID, tag.getInt("trident_id"));
        entityData.set(MODE, tag.getInt("mode"));
        entityData.set(MAX_DISTANCE, tag.contains("max_distance") ? tag.getFloat("max_distance") : 20.0F);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("owner_id", entityData.get(OWNER_ID));
        tag.putInt("trident_id", entityData.get(TRIDENT_ID));
        tag.putInt("mode", entityData.get(MODE));
        tag.putFloat("max_distance", entityData.get(MAX_DISTANCE));
    }

    @Override
    public void tick() {
        super.tick();
        Entity owner = resolveOwner();
        Entity trident = resolveTrident();
        if (owner == null || trident == null || !owner.isAlive() || !trident.isAlive()) {
            if (!level().isClientSide()) {
                discard();
            }
            return;
        }

        if (owner.level() != trident.level()) {
            if (!level().isClientSide()) {
                discard();
            }
            return;
        }

        setPos(owner.position().lerp(trident.position(), 0.5D));
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }
}
