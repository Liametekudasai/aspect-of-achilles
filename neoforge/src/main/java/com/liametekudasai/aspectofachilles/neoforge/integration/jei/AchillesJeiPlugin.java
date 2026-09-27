package com.liametekudasai.aspectofachilles.neoforge.integration.jei;

import com.liametekudasai.aspectofachilles.AspectOfAchilles;
import com.liametekudasai.aspectofachilles.enchantment.TridentEnchantmentHelper;
import com.liametekudasai.aspectofachilles.util.ModEnchantmentKeys;
import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

/** JEI-only documentation for the non-vanilla anvil fusion. */
@JeiPlugin
public final class AchillesJeiPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(
            AspectOfAchilles.MOD_ID, "jei_plugin"
    );

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ItemStack aspectBook = createAspectBook();
        if (aspectBook.isEmpty()) {
            return;
        }

        registration.addItemStackInfo(
                List.of(aspectBook),
                Component.translatable("jei.aspectofachilles.aspect_of_achilles.description")
        );
    }

    private static ItemStack createAspectBook() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return ItemStack.EMPTY;
        }

        Holder<Enchantment> aspect = TridentEnchantmentHelper.resolveOrThrow(
                minecraft.level.registryAccess(), ModEnchantmentKeys.ASPECT_OF_ACHILLES
        );
        return EnchantedBookItem.createForEnchantment(new EnchantmentInstance(aspect, 1));
    }
}
