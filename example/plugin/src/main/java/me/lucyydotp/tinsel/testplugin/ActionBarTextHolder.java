package me.lucyydotp.tinsel.testplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Repeatedly sends action bar text to players so that it stays on their screen.
 */
@NullMarked
public class ActionBarTextHolder implements Listener {
    public ActionBarTextHolder(Plugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, this::send, 20, 20);
    }

    private final Map<UUID, Component> text = new HashMap<>();

    private void send() {
        for (final var player: Bukkit.getOnlinePlayers()) {
            final var playerText = text.get(player.getUniqueId());
            if (playerText != null) {
                player.sendActionBar(playerText);
            }
        }
    }

    public void setText(Player player, @Nullable Component component) {
        if (component == null) {
            text.remove(player.getUniqueId());
            player.sendActionBar(Component.empty());
        } else {
            text.put(player.getUniqueId(), component);
            player.sendActionBar(component);
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        text.remove(event.getPlayer().getUniqueId());
    }
}
