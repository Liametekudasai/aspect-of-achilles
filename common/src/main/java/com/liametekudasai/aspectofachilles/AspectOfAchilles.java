package com.liametekudasai.aspectofachilles;

import com.liametekudasai.aspectofachilles.tether.TetherRuntime;
import com.liametekudasai.aspectofachilles.config.AchillesConfig;
import net.blay09.mods.balm.api.Balm;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

/**
 * Shared entrypoint for all loaders.
 *
 * <p>The real gameplay logic is kept in the common module so it can be reused by
 * NeoForge and Fabric. Platform-specific event wiring still lives in the loader
 * modules.</p>
 */
public final class AspectOfAchilles {

    public static final String MOD_ID = "aspectofachilles";
    public static final String MOD_NAME = "Aspect of Achilles";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static boolean initialized;

    private AspectOfAchilles() {
    }

    public static void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;

        LOGGER.info("Initializing {} common module", MOD_NAME);
        Balm.getConfig().registerConfig(AchillesConfig.class);
        TetherRuntime.initialize();
        ModRegistries.initialize();
    }
}
