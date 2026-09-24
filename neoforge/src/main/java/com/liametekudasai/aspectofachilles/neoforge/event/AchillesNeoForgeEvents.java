package com.liametekudasai.aspectofachilles.neoforge.event;

import com.liametekudasai.aspectofachilles.AspectOfAchilles;
import com.liametekudasai.aspectofachilles.enchantment.AnvilFusionService;
import com.liametekudasai.aspectofachilles.enchantment.TridentDamageService;
import com.liametekudasai.aspectofachilles.enchantment.TridentEnchantmentHelper;
import com.liametekudasai.aspectofachilles.entity.TetherEntity;
import com.liametekudasai.aspectofachilles.neoforge.registry.NeoForgeModEntities;
import com.liametekudasai.aspectofachilles.tether.TetherMode;
import com.liametekudasai.aspectofachilles.tether.TetherRuntime;
import com.liametekudasai.aspectofachilles.tether.TetherService;
import com.liametekudasai.aspectofachilles.util.ModResourceLocations;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = AspectOfAchilles.MOD_ID)
public final class AchillesNeoForgeEvents {

    private AchillesNeoForgeEvents() {
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        if (event.getPlayer() == null) {
            return;
        }

        if (AnvilFusionService.canCreateAspect(event.getLeft(), event.getRight(), event.getPlayer().level().registryAccess())) {
            event.setOutput(AnvilFusionService.createAspectResult(event.getLeft(), event.getRight(), event.getPlayer().level().registryAccess()));
            event.setCost(AnvilFusionService.getAchillesFusionCost());
            event.setMaterialCost(1);
            return;
        }

        if (AnvilFusionService.canApplyCompatibilityEnchant(event.getLeft(), event.getRight(), event.getPlayer().level().registryAccess())) {
            event.setOutput(AnvilFusionService.createCompatibilityResult(event.getLeft(), event.getRight(), event.getPlayer().level().registryAccess()));
            event.setCost(AnvilFusionService.getCompatibilityEnchantCost());
            event.setMaterialCost(1);
        }
    }

    @SubscribeEvent
    public static void onTridentJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        Entity entity = event.getEntity();
        if (!(entity instanceof ThrownTrident trident)) {
            return;
        }

        Entity owner = trident.getOwner();
        if (!(owner instanceof ServerPlayer player)) {
            return;
        }

        ItemStack stack = trident.getWeaponItem().copy();
        if (TetherService.isTracked(player, trident.getUUID())) {
            return;
        }

        boolean hasAspect = TridentEnchantmentHelper.hasAspect(stack, player.level().registryAccess());
        boolean hasObedience = TridentEnchantmentHelper.hasObedience(stack, player.level().registryAccess());
        if (!hasAspect && !hasObedience) {
            return;
        }

        TetherMode mode = hasAspect ? TetherMode.ACHILLES : TetherMode.OBEDIENCE;
        TetherEntity tetherEntity = NeoForgeModEntities.TETHER.get().create(player.level());
        if (tetherEntity == null) {
            return;
        }

        tetherEntity.configure(player, trident, mode);
        tetherEntity.setPos(player.getX(), player.getY(), player.getZ());
        player.level().addFreshEntity(tetherEntity);

        TetherService.begin(player, trident, mode, player.getInventory().selected, tetherEntity);
        TetherService.debugLogStart(player, mode, player.getInventory().selected);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer) || serverPlayer.level().isClientSide()) {
            return;
        }

        TetherService.tick(serverPlayer);
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            TetherService.queueRecall(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            TetherService.queueRecall(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            TetherService.queueAchillesPull(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onItemAttributeModifiers(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (!TridentEnchantmentHelper.isTrident(stack)) {
            return;
        }

        int sharpnessLevel = 0;
        for (var entry : stack.getEnchantments().entrySet()) {
            if (entry.getKey().is(Enchantments.SHARPNESS)) {
                sharpnessLevel = Math.max(sharpnessLevel, entry.getIntValue());
            }
        }

        if (sharpnessLevel <= 0) {
            return;
        }

        double bonusDamage = 0.5D * sharpnessLevel + 0.5D;
        AttributeModifier modifier = new AttributeModifier(
                ModResourceLocations.id("trident_sharpness_melee_bonus"),
                bonusDamage,
                AttributeModifier.Operation.ADD_VALUE
        );
        event.replaceModifier(Attributes.ATTACK_DAMAGE, modifier, EquipmentSlotGroup.MAINHAND);
    }

    @SubscribeEvent
    public static void onThrownTridentDamage(LivingDamageEvent.Pre event) {
        Entity directEntity = event.getSource().getDirectEntity();
        if (!(directEntity instanceof ThrownTrident trident)) {
            return;
        }

        ItemStack stack = trident.getWeaponItem().copy();
        if (!TridentEnchantmentHelper.hasPower(stack, event.getEntity().level().registryAccess())) {
            return;
        }

        float boosted = TridentDamageService.applyPowerToThrownTrident(
                event.getNewDamage(),
                stack,
                event.getEntity().level().registryAccess()
        );
        event.setNewDamage(boosted);
    }


    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer && !serverPlayer.level().isClientSide()) {
            TetherService.forceReturnToInventory(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onTravelDimension(EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            TetherService.forceReturnToInventory(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            TetherService.forceReturnToInventory(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            TetherService.forceReturnToInventory(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            TetherRuntime.clear(serverPlayer.getUUID());
        }
    }
}
