package com.example.PrayerFlicker;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.InteractionApi.InteractionHelper;
import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.HotkeyListener;
import com.example.PacketUtils.WidgetInfoExtended;

@PluginDescriptor(
        name = "Prayer Flicker",
        description = "Flicks quick prayers automatically",
        tags = {"prayer", "flicker"}
)
@Slf4j
public class PrayerFlickerPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Inject
    private KeyManager keyManager;

    @Inject
    private PrayerFlickerConfig config;

    private boolean toggle = false;

    @Provides
    PrayerFlickerConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(PrayerFlickerConfig.class);
    }

    @Override
    protected void startUp() {
        keyManager.registerKeyListener(prayerToggle);
    }

    @Override
    protected void shutDown() {
        keyManager.unregisterKeyListener(prayerToggle);
        toggle = false;
        if (client.getGameState() == GameState.LOGGED_IN && EthanApiPlugin.isQuickPrayerEnabled()) {
            clientThread.invoke(InteractionHelper::togglePrayer);
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (client.getGameState() != GameState.LOGGED_IN || !toggle) {
            return;
        }

        if (client.getBoostedSkillLevel(Skill.PRAYER) < 1) {
            return;
        }

        if (EthanApiPlugin.isQuickPrayerEnabled()) {
            InteractionHelper.togglePrayer();
        }
        InteractionHelper.togglePrayer();
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        if (config.minimapToggle() && 
            event.getId() == 1 && 
            event.getParam1() == WidgetInfoExtended.MINIMAP_QUICK_PRAYER_ORB.getPackedId()) {
            toggleFlicker();
            event.consume();
        }
    }

    private final HotkeyListener prayerToggle = new HotkeyListener(() -> config.toggle()) {
        @Override
        public void hotkeyPressed() {
            toggleFlicker();
        }
    };

    public void toggleFlicker() {
        toggle = !toggle;
        if (client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        if (!toggle) {
            clientThread.invoke(() -> {
                if (EthanApiPlugin.isQuickPrayerEnabled()) {
                    InteractionHelper.togglePrayer();
                }
            });
        }
    }
} 