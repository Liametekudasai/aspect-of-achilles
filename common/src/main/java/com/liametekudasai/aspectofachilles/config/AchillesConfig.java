package com.liametekudasai.aspectofachilles.config;

import com.liametekudasai.aspectofachilles.AspectOfAchilles;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.reflection.Comment;
import net.blay09.mods.balm.api.config.reflection.Config;
import net.blay09.mods.balm.api.config.reflection.Synced;

/** Server-authoritative gameplay settings, synchronized to connected clients by Balm. */
@Config(value = AspectOfAchilles.MOD_ID, type = "server")
public class AchillesConfig {

    @Synced
    @Comment("Maximum rope length, in blocks, for a trident enchanted with Obedience.")
    public double obedienceMaxDistance = 20.0D;

    @Synced
    @Comment("Maximum rope length, in blocks, for a trident enchanted with Aspect of Achilles.")
    public double achillesMaxDistance = 40.0D;

    @Synced
    @Comment("Strength of one Aspect of Achilles pull impulse. Ignored when teleportToTrident is enabled.")
    public double achillesPullImpulse = 1.35D;

    @Synced
    @Comment("When true, an Aspect of Achilles activation teleports the player to the trident instead of applying an impulse.")
    public boolean teleportToTrident = false;

    public static AchillesConfig get() {
        return Balm.getConfig().getActiveConfig(AchillesConfig.class);
    }
}
