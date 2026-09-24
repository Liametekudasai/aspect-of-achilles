package com.liametekudasai.aspectofachilles.neoforge.event;

import com.liametekudasai.aspectofachilles.AspectOfAchilles;
import com.liametekudasai.aspectofachilles.tether.TetherService;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;

/**
 * Extra inventory guardrails for the reserved trident slot while a tether is active.
 */
@EventBusSubscriber(modid = AspectOfAchilles.MOD_ID)
public final class AchillesInventorySafetyEvents {

    private AchillesInventorySafetyEvents() {
    }

    @SubscribeEvent
    public static void onItemStackedOnOther(ItemStackedOnOtherEvent event) {
        Player player = event.getPlayer();
        Slot slot = event.getSlot();
        if (!TetherService.isLockedMenuSlot(player, slot)) {
            return;
        }

        event.setCanceled(true);
    }
}
