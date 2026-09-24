package com.liametekudasai.aspectofachilles.neoforge.registry;

import com.liametekudasai.aspectofachilles.AspectOfAchilles;
import com.liametekudasai.aspectofachilles.entity.TetherEntity;
import com.liametekudasai.aspectofachilles.util.ModResourceLocations;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class NeoForgeModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, AspectOfAchilles.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<TetherEntity>> TETHER = ENTITY_TYPES.register("tether",
            () -> EntityType.Builder.<TetherEntity>of(TetherEntity::new, MobCategory.MISC)
                    .sized(0.1F, 0.1F)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build(ModResourceLocations.id("tether").toString()));

    private NeoForgeModEntities() {
    }

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }
}
