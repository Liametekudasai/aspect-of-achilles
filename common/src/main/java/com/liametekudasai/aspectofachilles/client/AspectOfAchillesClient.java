package com.liametekudasai.aspectofachilles.client;

import com.liametekudasai.aspectofachilles.AspectOfAchilles;

/**
 * Shared client bootstrap.
 */
public final class AspectOfAchillesClient {

    private static boolean initialized;

    private AspectOfAchillesClient() {
    }

    public static void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        AspectOfAchilles.LOGGER.info("Initializing {} client module", AspectOfAchilles.MOD_NAME);
    }
}
