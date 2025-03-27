package com.example.AutoSpecialAttack;

import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.VarPlayer;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import com.example.InteractionApi.InteractionHelper;

import java.time.Instant;

@PluginDescriptor(
        name = "Auto Special Attack",
        description = "Automatically uses special attack when at 100% energy",
        tags = {"combat", "special", "attack"}
)
@Slf4j
public class AutoSpecialAttackPlugin extends Plugin {
    @Inject
    private Client client;

    private Instant lastSpecialAttempt = Instant.EPOCH;
    private static final int RETRY_DELAY_MS = 500;

    @Provides
    AutoSpecialAttackConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(AutoSpecialAttackConfig.class);
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (client.getGameState() != GameState.LOGGED_IN) {
            return;
        }

        int specialEnergy = client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT);
        
        // Check if special is at 100% (1000 = 100.0%)
        if (specialEnergy >= 1000) {
            Instant now = Instant.now();
            // Only attempt to use special if enough time has passed since last attempt
            if (now.toEpochMilli() - lastSpecialAttempt.toEpochMilli() >= RETRY_DELAY_MS) {
                InteractionHelper.toggleSpecial();
                lastSpecialAttempt = now;
                log.info("Auto Special Attack triggered at " + now);
            }
        }
    }
} 